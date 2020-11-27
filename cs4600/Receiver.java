package cs4600;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Receiver {
	public static void main(String[] args) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, IOException {
		
		// encrypted_payload = E_AESKey(m1)||E_RSAPubKVBob(AES_key)||MAC(E_AESKey(m1)||E_RSAPubKVBob(AES_key))
		String encrypted_payload = FileReaderWriter.readFile("receiverComputer(Bob)/encryptedPayload");
		
		/*
		 *  Retrieve MAC Key from receiver's computer, we are told 
		 *  to assume both parties know the MAC key already
		 */
		String MAC_Key = FileReaderWriter.readFile("receiverComputer(Bob)/MAC_Key");
		
		// Retrieve Receiver's RSA Private Key
		String RSA_PrivKey = FileReaderWriter.readFile("receiverComputer(Bob)/BobPrivateKey");
		
		// Tokenize the encrypted data so we can properly decrypt each part
		String[] tokens = encrypted_payload.split("seperator");
		String Encrypted_Message = tokens[0];
		String Encrypted_AESKey = tokens[1];
 		String MAC_Result = tokens[2];
 		
 		/*
 		 * There was an issue when reading MAC_Result from a file because some 
 		 * characters was not properly decoded. To resolve this, I would compute
 		 * the MAC of the encrypted payload but also write it to a file, and read 
 		 * both of the MAC results (one from the sender and one from receiver's 
 		 * own computation). This solution was to guarantee that I was checking
 		 * properly by having both MAC computations be done in the same conditions.
 		 */
		MAC mac1 = new MAC(Encrypted_Message + Encrypted_AESKey, MAC_Key);
		FileReaderWriter.writeToFile("receiverComputer(Bob)/ComputedMac", mac1.getMACResult());
		String computed_MAC = FileReaderWriter.readFile("receiverComputer(Bob)/ComputedMac");
		
		// Authenticating MAC by comparing both hash result
		if(MAC_Result.equals(computed_MAC)) {
			System.out.println("No tampering detected during data transmission");
			System.out.println("----------------------------------------------------");
		}
		else {
			System.out.println("TAMPERING DETECTED! Possible message hijacking..exiting ");
			System.exit(0);
		}
		
		// Decrypt AES Key by using RSA Receiver's Private Key
		String AES_Key = RSA.decrypt(Encrypted_AESKey, RSA_PrivKey);
		System.out.println("AES Key: " +AES_Key);
		System.out.println("----------------------------------------------------");
		
		// Decrypt message with the AES key 
		String message = AES.decrypt(Encrypted_Message, AES_Key);
		System.out.println("PLAINTEXT: " +message);
		System.out.println("----------------------------------------------------");
		
		// Write decrypted message to computer
		try {
			FileReaderWriter.writeToFile("receiverComputer(Bob)/decryptedPlaintext.txt", message);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
