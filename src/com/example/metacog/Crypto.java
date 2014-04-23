package com.example.metacog;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import android.util.Log;

public class Crypto {
	
	private static final String TAG = "CryptoDebug";
	
	KeyPairGenerator kpg;
    KeyPair kp;
    PublicKey publicKey;
    PrivateKey privateKey;
    //byte [] encryptedBytes,decryptedBytes;
    Cipher cipher,cipher1;
    String encrypted,decrypted;
	
	public Crypto() throws NoSuchAlgorithmException{
		kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(1024);
        kp = kpg.genKeyPair();
        
        publicKey = kp.getPublic();
        Log.d(TAG, publicKey.toString());
        
        privateKey = kp.getPrivate();
        Log.d(TAG, privateKey.toString());
	}

    public byte[] RSAEncrypt (String plain) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException 
    {
    	
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte [] encryptedBytes = cipher.doFinal(plain.getBytes());
        System.out.println("EEncrypted?????"+new String(encryptedBytes.toString()));
        return encryptedBytes;

    }

    public String RSADecrypt (byte[] result) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException 
    {
    	
        Cipher cipher1=Cipher.getInstance("RSA");
        cipher1.init(Cipher.DECRYPT_MODE, privateKey);
        byte [] decryptedBytes = cipher1.doFinal(result);
        String decrypted = new String(decryptedBytes);
        System.out.println("DDecrypted?????"+decrypted);
        return decrypted;

    }
    
    public void testCrypto(String toBeEncoded) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException
    {
    	Log.d(TAG, toBeEncoded);
    	byte[] encryptedData = RSAEncrypt(toBeEncoded);
    	String result = RSADecrypt(encryptedData);
    	Log.d(TAG, result);
    }
    
}
