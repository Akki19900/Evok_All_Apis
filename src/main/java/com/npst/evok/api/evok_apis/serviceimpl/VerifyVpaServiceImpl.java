package com.npst.evok.api.evok_apis.serviceimpl;

import com.npst.evok.api.evok_apis.pojo.AllEntity;
import com.npst.evok.api.evok_apis.repository.VerifyVpaRepository;
import com.npst.evok.api.evok_apis.service.VerifyVPAService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

@Service
public class VerifyVpaServiceImpl implements VerifyVPAService {

    public static String ENC_KEY = "";
    @Autowired
    private VerifyVpaRepository verifyVpaRepository;

    public static String generateRandomString(String baseString, int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder randomString = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            randomString.append(randomChar);
        }
        return baseString + randomString.toString();
    }

    private static String generateVerifyVpaChecksum(JSONObject qrObject, String checkSumKey) {
        StringBuilder concatenatedString = new StringBuilder();

        try {

            concatenatedString.append(qrObject.get("source"));
            concatenatedString.append(qrObject.get("channel"));
            concatenatedString.append(qrObject.get("extTransactionId"));
            concatenatedString.append(qrObject.get("upiId"));
            concatenatedString.append(qrObject.get("terminalId"));
            concatenatedString.append(qrObject.get("sid"));

            System.out.println("String is " + concatenatedString.toString());
        } catch (Exception e) {
            e.printStackTrace();

        }
        return generateChecksumMerchant(concatenatedString.toString(), checkSumKey);
    }

    public static String generateChecksumMerchant(String concatenatedString, String checksumkey) {
        String inputString = concatenatedString + checksumkey;
        StringBuffer sb = null;
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(inputString.getBytes());
            byte byteData[] = md.digest();
            sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private static String encryptRequest(String strToEncrypt, String encryptKey) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, setMerchantKey(encryptKey));
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static SecretKeySpec setMerchantKey(String myKey) {

        SecretKeySpec merchantSecretKey_ = null;
        try {
            MessageDigest sha = null;
            byte[] key_ = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-256");
            key_ = sha.digest(key_);
            key_ = Arrays.copyOf(key_, 16);
            merchantSecretKey_ = new SecretKeySpec(key_, "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return merchantSecretKey_;
    }

    public static String decryptResponse(String responseString, String encryptKey) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, setMerchantKey(encryptKey));
            return new String(cipher.doFinal(Base64.getDecoder().decode(responseString)), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String verifyVpa(AllEntity verifyVPAEntity) {
        JSONObject obj = new JSONObject();
        ENC_KEY = verifyVPAEntity.getEncKey();
        String source = verifyVPAEntity.getSource();
        String exTxnId = generateRandomString(verifyVPAEntity.getExtTransactionId(), 15);
        obj.put("source", verifyVPAEntity.getSource());
        obj.put("channel", verifyVPAEntity.getChannel());
        obj.put("extTransactionId", source + exTxnId);
        obj.put("upiId", verifyVPAEntity.getUpiId());
        obj.put("terminalId", verifyVPAEntity.getTerminalId());
        obj.put("sid", verifyVPAEntity.getSid());
        String checksum = generateVerifyVpaChecksum(obj, verifyVPAEntity.getChecksum());
        System.out.println("Checksum is " + checksum);
        obj.put("checksum", checksum);
        String encryptedReq = encryptRequest(obj.toString(), verifyVPAEntity.getEncKey());
        return encryptedReq;
    }

    @Override
    public String dresponse(AllEntity verifyVPAEntity) {
        return "Decrypted request to cross verify"
                + decryptResponse(verifyVPAEntity.getMessageDec(), verifyVPAEntity.getMessageKey());
    }
}