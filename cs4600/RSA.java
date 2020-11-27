package cs4600;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSA {
    
	PrivateKey prKey;
	PublicKey pubKey;
	static String publicKey;
	static String privateKey;
    public static void main(String[] args) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, IOException {
    	
    	/* 	
    	 * Initial RSA keypair generation, not used because
    	 * 	we are told to assume that both clients have
    	 * 	already established the keys before hand
    	 */
    	RSA keyPairGenerator = new RSA();
    	FileReaderWriter.writeToFile("publicKey", Base64.getMimeEncoder().encodeToString(keyPairGenerator.getPublicKey().getEncoded()));
    	FileReaderWriter.writeToFile("privateKey", Base64.getMimeEncoder().encodeToString(keyPairGenerator.getPrivateKey().getEncoded()));
    	String publicKey = Base64.getEncoder().encodeToString(keyPairGenerator.getPublicKey().getEncoded());
    	String privateKey = Base64.getEncoder().encodeToString(keyPairGenerator.getPrivateKey().getEncoded());
    	System.out.println("Public Key: " +publicKey);
    	System.out.println("Private Key: " +privateKey);


    	publicKey = FileReaderWriter.readFile("publicKey");
    	privateKey = FileReaderWriter.readFile("privateKey");
    	String message = FileReaderWriter.readFile("message.txt");
    	
        String cipherText = encrypt(message, publicKey);
        System.out.println("Ciphertext: " +cipherText);
        String plainText = decrypt(cipherText, privateKey);
        System.out.println("Plaintext: " +plainText);
    	
	}
    
    public RSA(){
    	
        KeyPairGenerator keyGen = null;
		try {
			keyGen = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Failed to instantiate key pair generator");
			e.printStackTrace();
		}
        keyGen.initialize(1024);
        KeyPair pair = keyGen.generateKeyPair();
        // Private key is generated in PKCS#8 format
        this.prKey = pair.getPrivate();
        // Public key is generated in X.509 format
        this.pubKey = pair.getPublic();
        
    }

    public static String encrypt(String data, String publicKey) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, UnsupportedEncodingException {
    	Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    	cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey)); // getPublicKey returns a PublicKey object
    	return new String(Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes())));
    }
    
    public static PublicKey getPublicKey(String base64PublicKey) throws UnsupportedEncodingException{
    	
        PublicKey publicKey = null;
        try{
        	/*  
        	 * base64PublicKey requires X509EncodedKeySpec to be able to convert it again to an RSA public key,
        	 * but we must first decode because is it currently base64encoded
        	 */
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getMimeDecoder().decode(base64PublicKey.getBytes()));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return publicKey;
        
    }
    
    public static String decrypt(String base64data, String privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
    	Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    	cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(privateKey));
    	// The data we are decrypting is base64 encoded, so we have to decode it first!
    	return new String(cipher.doFinal(Base64.getMimeDecoder().decode(base64data.getBytes())));
    }
    
    public static PrivateKey getPrivateKey(String base64PrivateKey) throws UnsupportedEncodingException{
    	
        PrivateKey privateKey = null;
        try {
        	/*
        	 *  base64PrivateKey requires PKCS8EncodedKeySpec to be able to convert it again to an RSA private key,
        	 *  but we must first decode because is it currently base64encoded
        	 */
        	PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getMimeDecoder().decode(base64PrivateKey.getBytes()));
        	KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            privateKey = keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return privateKey;
    }
    
    public PrivateKey getPrivateKey() {
        return prKey;
    }

    public PublicKey getPublicKey() {
        return pubKey;
    }
    
}