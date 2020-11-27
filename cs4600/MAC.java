package cs4600;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class MAC {
//	private final static String Key = "suran123";
	
	private static byte[] macResult;
	public MAC(String message, String MAC_Key) throws UnsupportedEncodingException {
		
		SecretKeySpec secretKey = null;
		byte[] keyData;
		Mac mac;
		
		try {
			keyData = MAC_Key.getBytes("UTF-8");
	
			// Creating a MessageDigest Instance using SHA-256 algorithm
			MessageDigest keyDigest = MessageDigest.getInstance("SHA-256");
	
			/***
				Our key will actually be the message digest to detect any modifications,
				the receiver will still have to enter a string password which will be 
				used to calculate a message digest then compared with this "key"
			***/
	
			byte[] key = keyDigest.digest(keyData);
			key = Arrays.copyOf(key, 16); 
			secretKey = new SecretKeySpec(key, "AES");
	    
		} 
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		// Creating a Mac object
		mac = null;
		try {
			mac = Mac.getInstance("HmacSHA256");
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}
		// Initializing the Mac object
		try {
			mac.init(secretKey);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}

		//Computing the Mac
		byte[] bytes = message.getBytes("UTF-8");      
		macResult = mac.doFinal(bytes);

	}
	public String getMACResult() {
		return new String(macResult);
	}
	public static void main(String[] args) throws UnsupportedEncodingException {
//		MAC m = new MAC("Hi how are you", Key);
//	     System.out.println("MAC RESULT: \n" +m.getMACResult());
	}
}