package com.npst.evok.api.evok_apis.serviceimpl;

import com.npst.evok.api.evok_apis.pojo.CommonForAllPayout;
import com.npst.evok.api.evok_apis.service.IRefundService;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

@Service
public class RefundServiceImpl implements IRefundService {
    @Override
    public String refund(CommonForAllPayout commonForAllPayout) {
        JSONObject obj = new JSONObject();
        ENC_KEY = commonForAllPayout.getEncKey();
        String extTransactionId = commonForAllPayout.getExtTransactionId();
        System.out.println(extTransactionId);
        obj.put("source", commonForAllPayout.getSource());
        obj.put("sid", commonForAllPayout.getSid());
        obj.put("extTransactionId", commonForAllPayout.getSource() + Math.abs(new Random().nextInt()));
        obj.put("orgTxnId", commonForAllPayout.getOrgTxnId());
        obj.put("orgRrn", commonForAllPayout.getOrgRrn());
        obj.put("payeeAddr", commonForAllPayout.getPayeeAddr());
        obj.put("amount", commonForAllPayout.getAmount());
        obj.put("remarks", commonForAllPayout.getRemarks());
        String checksum = generateVerifyVpaChecksum(obj, commonForAllPayout.getCheckSumKey());
        obj.put("checksum", checksum);
        return encryptRequest(obj.toString(), commonForAllPayout.getEncKey());
    }

    public static String ENC_KEY = "";

    private static String generateVerifyVpaChecksum(JSONObject qrObject, String checkSumKey) {
        StringBuilder concatenatedString = new StringBuilder();

        try {
            concatenatedString.append(qrObject.get("source"));
            concatenatedString.append(qrObject.get("sid"));
            concatenatedString.append(qrObject.get("extTransactionId"));
            concatenatedString.append(qrObject.get("orgTxnId"));
            concatenatedString.append(qrObject.get("orgRrn"));
            concatenatedString.append(qrObject.get("payeeAddr"));
            concatenatedString.append(qrObject.get("amount"));
            concatenatedString.append(qrObject.get("remarks"));

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
            byte[] byteData = md.digest();
            sb = new StringBuffer();

//            for (int i = 0; i < byteData.length; i++) {
//                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
//            }
            for (byte byteDatum : byteData) {
                sb.append(Integer.toString((byteDatum & 0xff) + 0x100, 16).substring(1));
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
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static SecretKeySpec setMerchantKey(String myKey) {

        SecretKeySpec merchantSecretKey_ = null;
        try {
            MessageDigest sha = null;
            byte[] key_ = myKey.getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance("SHA-256");
            key_ = sha.digest(key_);
            key_ = Arrays.copyOf(key_, 16);
            merchantSecretKey_ = new SecretKeySpec(key_, "AES");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return merchantSecretKey_;
    }

    public static String decryptResponse(String responseString, String encryptKey) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, setMerchantKey(encryptKey));
            return new String(cipher.doFinal(Base64.getDecoder().decode(responseString)), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
