package cs4600;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AES {
 
    private static SecretKeySpec secretKey;
    private static byte[] keyData;
    public static void setKey(String myKey)
    {
        try {
	        keyData = myKey.getBytes("UTF-8");
	        
	        // Creating a MessageDigest Instance using SHA-256 algorithm
	        MessageDigest keyDigest = MessageDigest.getInstance("SHA-256");
	        
	        /*
	         *	Our key will actually be the message digest to detect any modifications,
	       	 *	the receiver will still have to enter a string password which will be 
			 *	used to calculate a message digest then compared with this "key"
	         */
	        
	        byte[] key = keyDigest.digest(keyData);
	        key = Arrays.copyOf(key, 16); 
	        secretKey = new SecretKeySpec(key, "AES");
            
        } 
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
		}
    }
 
    public static String encrypt(String strToEncrypt, String secret) 
    {
        try {
            setKey(secret);
            
            // Creating a cipher using AES in ECB mode with PKCS5Padding
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            
            // Initializing the cipher to encrypt secretKey
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            
            // Return a String
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
            
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }
        
        return null;
    }
 
    public static String decrypt(String strToDecrypt, String secret) 
    {
        try {
            
        	setKey(secret);
	        // Creating a cipher using AES in ECB mode with PCKS5Padding
	        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
	        
	        // Initializing the cipher to decrypt secretKey
	        cipher.init(Cipher.DECRYPT_MODE, secretKey);
	        
	        // Return a String 
	        return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }
}