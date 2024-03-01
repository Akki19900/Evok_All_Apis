package com.npst.evok.api.evok_apis.controller;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.npst.evok.api.evok_apis.pojo.Callback;

@RestController
public class AESDecryptor {

    @PostMapping("/callback")
    public static String decryptCode(@RequestBody Callback callback) {
        try {
            String plainText = AESDecrypt(callback.getCipherText(), callback.getEncryptionKey());
            return "Decrypted Text: " + plainText;
        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
            return "NO DATA FOUND";
        }
    }

    public static String AESDecrypt(String cipherText, String encryptionKey) throws Exception {
        String iv = "NetworkPeopleVec";
        byte[] keyBytes = encryptionKey.getBytes("UTF-8");
        byte[] ivBytes = iv.getBytes("UTF-8");
        byte[] cipherTextBytes = Base64.getDecoder().decode(cipherText);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING"); // AES/ECB/PKCS5PADDING
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] decryptedBytes = cipher.doFinal(cipherTextBytes);
        String plainText = new String(decryptedBytes, "UTF-8");
        int openingBraceIndex = plainText.indexOf('"');
        String result = openingBraceIndex != -1 ? plainText.substring(openingBraceIndex) : "Opening brace not found";
        // Remove the first 16 characters if needed
//		if (plainText.length() > 14) {
//			plainText = plainText.substring(14);
//		}
        return "{" + result;
    }

//	public static String extractJsonSubstring(String input) {
//		int openingBraceIndex = input.indexOf('"');
//		System.out.println("This is the index of " + openingBraceIndex);
//		return openingBraceIndex != -1 ? input.substring(openingBraceIndex) : "Opening brace not found";
//	}

//	@PostMapping("/callback")
//	public static String decryption(@RequestBody Callback callback) {
//		try {
//			String plainText = AESDecrypt(callback.getCipherText(), callback.getEncryptionKey());
//			return "Decrypted Text: " + plainText;
//		} catch (Exception e) {
//			System.out.println("Error: " + e.getMessage());
//			return "No Data Found";
//		}
//	}
//
//	public static String AESDecrypt(String cipherText, String encKey) {
//		try {
//			String initializationVector = "NetworkPeopleVec";
//			byte[] keyBytes = encKey.getBytes(StandardCharsets.UTF_8);
//			byte[] ivBytes = initializationVector.getBytes(StandardCharsets.UTF_8);
//
//			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//			SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
//			IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);
//
//			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
//
//			byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(cipherText));
//
//			// Remove the first 16 bytes, which might be the initialization vector (IV)
//			if (decryptedBytes.length > 16) {
//				byte[] trimmedBytes = new byte[decryptedBytes.length - 16];
//				System.arraycopy(decryptedBytes, 16, trimmedBytes, 0, trimmedBytes.length);
//				decryptedBytes = trimmedBytes;
//			}
//
//			return new String(decryptedBytes, StandardCharsets.UTF_8);
//		} catch (Exception ex) {
//			return ex.getMessage();
//		}
//	}

}
