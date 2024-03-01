package com.npst.evok.api.evok_apis.serviceimpl;

import com.npst.evok.api.evok_apis.pojo.CommonForAllPayout;
import com.npst.evok.api.evok_apis.service.IPayoutBulkEnquiry;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

@Service
public class PayoutBulkEnquiryImpl implements IPayoutBulkEnquiry {

    public static String encryptRequest(String strToEncrypt, String encryptKey) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, setMerchantKey(encryptKey));
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static SecretKeySpec setMerchantKey(String myKey) {
        SecretKeySpec merchantSecretKey_ = null;
        try {
            MessageDigest sha = null;
            byte[] key_ = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-256");
            key_ = sha.digest(key_);
            key_ = Arrays.copyOf(key_, 16);
            merchantSecretKey_ = new SecretKeySpec(key_, "AES");
        } catch (NoSuchAlgorithmException e) {

        } catch (UnsupportedEncodingException e) {

        }

        return merchantSecretKey_;
    }

    static String generatePayoutChecksum(JSONObject payoutObj, String checkSumKey) {
        StringBuilder concatenatedString = new StringBuilder();
        try {
            concatenatedString.append(payoutObj.get("enquiryId"));
            concatenatedString.append(payoutObj.get("source"));
            concatenatedString.append(payoutObj.get("sid"));
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
    public String bulkEnquiry(CommonForAllPayout payout) {
        JSONObject mainObj = new JSONObject();
        mainObj.put("enquiryId", payout.getEnquiryId());
        mainObj.put("source", payout.getSource());
        mainObj.put("sid", payout.getSid());
        String checksum = generatePayoutChecksum(mainObj, payout.getCheckSumKey());
        mainObj.put("checksum", checksum);
        String encryptedReq = encryptRequest(mainObj.toString(), payout.getEncKey());
        System.out.println("Encrypted" + encryptedReq);
        return encryptedReq;
    }
}
