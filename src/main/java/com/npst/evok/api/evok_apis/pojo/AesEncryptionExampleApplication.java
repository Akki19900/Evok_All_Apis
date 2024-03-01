package com.npst.evok.api.evok_apis.pojo;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AesEncryptionExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(AesEncryptionExampleApplication.class, args);

        // Your JSON data
        String jsonData = "{\"source\":\"MER00021-SB\",\"sid\":\"SID9944-SID\",\"requestDate\":\"2023-04-13\",\"payees\":[{\"extTransactionId\":\"BGD867HJGDHVHDV55454JHVHV\",\"payeeAcNum\":\"01234567890\",\"payeeIfsc\":\"COSB000000012\",\"payeeName\":\"Vinay Kumar\",\"payeeMobile\":\"919555151119\",\"payeeAmount\":\"10.00\",\"remarks\":\"Payout amount\"}]}";

        // Your secret key (should be securely managed in production)
        String secretKey = "48747932473294732749832467239894";

        // Encrypt the JSON data
        String encryptedData = encrypt(jsonData, secretKey);
        System.out.println("Encrypted Data: " + encryptedData);
    }

    private static String encrypt(String data, String secretKey) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKey key = new SecretKeySpec(secretKey.getBytes(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting data: " + e.getMessage(), e);
        }
    }
}

