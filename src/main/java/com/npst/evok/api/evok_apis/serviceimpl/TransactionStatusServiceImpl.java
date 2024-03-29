package com.npst.evok.api.evok_apis.serviceimpl;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.npst.evok.api.evok_apis.pojo.TransactionStatus;
import com.npst.evok.api.evok_apis.service.TransactionStatusService;

@Service
public class TransactionStatusServiceImpl implements TransactionStatusService {


    public static String ENC_KEY = "";

    private static JSONObject getJsonRequest() {
        JSONObject obj = new JSONObject();
        // obj.put("source", SOURCE);
        // obj.put("channel", CHANNEL);
        // obj.put("extTransactionId", EXTERNAL_TRANS_ID);
        // obj.put("upiId", UPI_ID);
        // obj.put("terminalId", TERMINAL_ID);
        // obj.put("sid", SID);
        return obj;
    }

    private static String generateTransactionStatusChecksum(JSONObject qrObject, String checkSumKey) {
        StringBuilder concatenatedString = new StringBuilder();
        try {

            concatenatedString.append(qrObject.get("source"));
            concatenatedString.append(qrObject.get("channel"));
            concatenatedString.append(qrObject.get("terminalId"));
            concatenatedString.append(qrObject.get("extTransactionId"));

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
    public String transactionStatus(TransactionStatus transactionStatus) {
        JSONObject obj = getJsonRequest();

        ENC_KEY = transactionStatus.getEncKey();


        obj.put("source", transactionStatus.getSource());
        obj.put("channel", transactionStatus.getChannel());
        obj.put("extTransactionId", transactionStatus.getExtTransactionId());
        obj.put("terminalId", transactionStatus.getTerminalId());
        System.out.println("Raw Request" + obj.toString());
        String checksum = generateTransactionStatusChecksum(obj, transactionStatus.getChecksum());
        System.out.println("Checksum is " + checksum);
        obj.put("checksum", checksum);
        System.out.println("Final string to encrypt is " + obj.toString());
        String encryptedReq = encryptRequest(obj.toString(), transactionStatus.getEncKey());
        System.out.println("Final encrypted request " + encryptedReq);
        return encryptedReq;
    }

    @Override
    public String decryptResponse(String dcrypt) {
        return "Decrypted request to cross verify" + decryptResponse(dcrypt, ENC_KEY);
    }

}
