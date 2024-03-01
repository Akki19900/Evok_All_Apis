package com.npst.evok.api.evok_apis.serviceimpl;

import com.npst.evok.api.evok_apis.pojo.MerchantTransfer;
import com.npst.evok.api.evok_apis.repository.CollectRepository;
import com.npst.evok.api.evok_apis.service.MerchantTransferService;
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
public class MerchantTransferImpl implements MerchantTransferService {
    public static String ENC_KEY = "";
    @Autowired
    private CollectRepository collectRepository;
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
    private static String generateMerchantTransferChecksum(JSONObject qrObject, String checkSumKey) {
        StringBuilder concatenatedString = new StringBuilder();
        try {
            concatenatedString.append(qrObject.get("source"));
            concatenatedString.append(qrObject.get("channel"));
            concatenatedString.append(qrObject.get("extTransactionId"));
            concatenatedString.append(qrObject.get("upiId"));
            concatenatedString.append(qrObject.get("terminalId"));
            concatenatedString.append(qrObject.get("amount"));
            concatenatedString.append(qrObject.get("customerName"));
            concatenatedString.append(qrObject.get("statusKYC"));
            concatenatedString.append(qrObject.get("sid"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String generateChecksumMerchant = generateChecksumMerchant(concatenatedString.toString(), checkSumKey);
        return generateChecksumMerchant;
    }

    public static String generateChecksumMerchant(String concatenatedString, String checksumkey) {
        String inputString = concatenatedString + checksumkey;
        System.out.println("This is checksum sequence " + inputString);
        StringBuffer sb = null;
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(inputString.getBytes());
            byte[] byteData = md.digest();
            sb = new StringBuffer();
            for (byte data : byteData) {
                sb.append(Integer.toString((data & 0xff) + 0x100, 16).substring(1));
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
    public String collectByPayee(MerchantTransfer merchantTransfer) {
        JSONObject obj = getJsonRequest();
        ENC_KEY = merchantTransfer.getEncKey();
        String src = merchantTransfer.getSource();
        String exTxnId = generateRandomString(merchantTransfer.getExtTransactionId(), 20);
//		Collect collect = new Collect();
//		collect.setSource(merchantTransfer.getSource());
//		collect.setChannel(merchantTransfer.getChannel());
//		collect.setExtTransactionId(src + exTxnId);
//		collect.setUpiId(merchantTransfer.getUpiId());
//		collect.setTerminalId(merchantTransfer.getTerminalId());
//		collect.setAmount(merchantTransfer.getAmount());
//		collect.setCustomerName(merchantTransfer.getCustomerName());
//		collect.setStatusKYC(merchantTransfer.getStatusKYC());
//		collect.setSid(merchantTransfer.getSid());
//		collect.setChecksum(merchantTransfer.getChecksum());
//		collect.setHeaderKey(Constants.cid);
//		collect.setEncKey(merchantTransfer.getEncKey());
//		collectRepository.save(collect);

        // src + exTxnId
        obj.put("source", merchantTransfer.getSource());
        obj.put("channel", merchantTransfer.getChannel());
        obj.put("extTransactionId",src+exTxnId);
        obj.put("upiId", merchantTransfer.getUpiId());
        obj.put("terminalId", merchantTransfer.getTerminalId());
        obj.put("amount", merchantTransfer.getAmount());
        obj.put("customerName", merchantTransfer.getCustomerName());
        obj.put("statusKYC", merchantTransfer.getStatusKYC());
        obj.put("sid", merchantTransfer.getSid());

        System.out.println("Raw Request" + obj.toString());
        String checksum = generateMerchantTransferChecksum(obj, merchantTransfer.getChecksum());
        System.out.println("Checksum is " + checksum);
        obj.put("checksum", checksum);
        System.out.println("Final string to encrypt is " + obj.toString());
        String encryptedReq = encryptRequest(obj.toString(), merchantTransfer.getEncKey());
        return encryptedReq;
    }

    @Override
    public String decryptResponse(String dcrypt) {
        return "Decrypted request to cross verify" + decryptResponse(dcrypt, ENC_KEY);
    }

}