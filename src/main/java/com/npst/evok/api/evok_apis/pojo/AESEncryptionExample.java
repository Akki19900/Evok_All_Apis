package com.npst.evok.api.evok_apis.pojo;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;

public class AESEncryptionExample {

    public static void main(String[] args) throws Exception {
        // Generate a secret key for AES
        SecretKey secretKey = generateAESKey();

        // Sample data to encrypt
        String originalData = "Hello, AES Encryption!";

        // Encrypt the data
        byte[] encryptedData = encryptAES(originalData, secretKey);

        System.out.println("Original Data: " + originalData);
        System.out.println("Encrypted Data: " + bytesToHexString(encryptedData));

        // Decrypt the data
        String decryptedData = decryptAES(encryptedData, secretKey);
        System.out.println("Decrypted Data: " + decryptedData);
    }

    // Generate a secret key for AES
    private static SecretKey generateAESKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256); // You can also use 128 or 192 bits
        return keyGenerator.generateKey();
    }

    // Encrypt data using AES
    private static byte[] encryptAES(String data, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data.getBytes());
    }

    // Decrypt data using AES
    private static String decryptAES(byte[] encryptedData, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedBytes = cipher.doFinal(encryptedData);
        return new String(decryptedBytes);
    }

    // Convert byte array to hexadecimal string for display
    private static String bytesToHexString(byte[] bytes) {
        StringBuilder hexStringBuilder = new StringBuilder();
        for (byte b : bytes) {
            hexStringBuilder.append(String.format("%02X", b));
        }
        return hexStringBuilder.toString();
    }
}
