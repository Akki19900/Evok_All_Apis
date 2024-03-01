package com.npst.evok.api.evok_apis.serviceimpl;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.npst.evok.api.evok_apis.pojo.CommonForAllPayout;
import com.npst.evok.api.evok_apis.service.IAccountVerificationViaAccountNoAndIFSC;

@Service
public class AccountVerificationViaAccountNoAndIFSCImpl implements IAccountVerificationViaAccountNoAndIFSC {

    public static String ENC_KEY = "";

    public static String generateRandomString(String baseString, int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
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
            concatenatedString.append(qrObject.get("extTransactionId"));
            concatenatedString.append(qrObject.get("sid"));
            concatenatedString.append(qrObject.get("customerName"));
            concatenatedString.append(qrObject.get("customerAcc"));
            concatenatedString.append(qrObject.get("customerIFSC"));
            concatenatedString.append(qrObject.get("customerMobileNo"));

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
    public String accountVerification(CommonForAllPayout commonForAllPayout) {
        JSONObject obj = new JSONObject();
        ENC_KEY = commonForAllPayout.getEncKey();
        String source = commonForAllPayout.getSource();
        String exTxnId = generateRandomString(commonForAllPayout.getExtTransactionId(), 15);
        obj.put("source", commonForAllPayout.getSource());
        obj.put("extTransactionId", source + exTxnId);
        obj.put("sid", commonForAllPayout.getSid());
        obj.put("customerName", commonForAllPayout.getCustomerName());
        obj.put("customerAcc", commonForAllPayout.getCustomerAcc());
        obj.put("customerIFSC", commonForAllPayout.getCustomerIFSC());
        obj.put("customerMobileNo", commonForAllPayout.getCustomerMobileNo());

        String checksum = generateVerifyVpaChecksum(obj, commonForAllPayout.getCheckSumKey());
        System.out.println("Checksum is " + checksum);
        obj.put("checksum", checksum);
        String encryptedReq = encryptRequest(obj.toString(), commonForAllPayout.getEncKey());
        return encryptedReq;
    }

}
