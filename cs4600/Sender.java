package cs4600;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Sender {
	public static void main(String[] args) throws IOException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException {
	    
		String encrypted_payload = "";
		
		// Retrieve message to be sent from sender's computer
		String message = FileReaderWriter.readFile("senderComputer(Alice)/message.txt");
//		String message = "I LOVE \n DRAKE \n SO MUCH!!";
		System.out.println("PLAIN TEXT: \n" +message);
		System.out.println("----------------------------------------------------");
		
		// Retrieve AES Key from sender's computer
		String AES_Key = FileReaderWriter.readFile("senderComputer(Alice)/AES_Key");
		System.out.println("AES KEY: \n" +AES_Key);
		System.out.println("----------------------------------------------------");
		
		// Retrieve MAC Key from sender's computer
		String MAC_Key = FileReaderWriter.readFile("senderComputer(Alice)/MAC_Key");
		System.out.println("MAC KEY: \n" +MAC_Key);
		System.out.println("----------------------------------------------------");
	    
	    // Encrypt message with AES 
	    String encryptMsg = AES.encrypt(message, AES_Key);
	    System.out.println("ENCRYPTED MESSAGE: \n" +encryptMsg);
		System.out.println("----------------------------------------------------");
		
        // Encrypt AES Key with Receiver's RSA Public Key
	    String Receiver_PubKey = FileReaderWriter.readFile("senderComputer(Alice)/BobPublicKey");
		String encryptAES_Key = RSA.encrypt(AES_Key, Receiver_PubKey);
		System.out.println("ENCRYPTED AES KEY: " +encryptAES_Key);
		System.out.println("----------------------------------------------------");
		
		/*
		 *  Since we are just partially simulating a communication for this project, 
		 *  I will just use the phrase "seperator" as our delimeter so the receiver 
		 *  can tokenize the encrypted data more efficiently.
		 */
		
		// MAC(E_AESKey(m1) || E_RSAPubKVBob(AES_key))
		MAC mac = new MAC(encryptMsg + encryptAES_Key, MAC_Key);
		System.out.println("MAC RESULT: " +mac.getMACResult());
		System.out.println("----------------------------------------------------");
		
		// encrypted_data = E_AESKey(m1) || E_RSAPubKVBob(AES_key) || MAC(E_AESKey(m1) || E_RSAPubKVBob(AES_key))
		encrypted_payload = encryptMsg + "seperator" + encryptAES_Key;
		encrypted_payload += ("seperator" + mac.getMACResult());
		System.out.println("ENCRYPTED PAYLOAD: " +encrypted_payload); 
		System.out.println("----------------------------------------------------");
		
		// Simulating receiver (Bob) finally receiving the encrypted file on his computer
		FileReaderWriter.writeToFile("receiverComputer(Bob)/encryptedPayload", encrypted_payload);
		
		
	}
	
}