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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.npst.evok.api.evok_apis.pojo.GenerateQr;
import com.npst.evok.api.evok_apis.repository.GenerateQrRepository;
import com.npst.evok.api.evok_apis.service.GenerateQrService;

@Service
public class GenerateQrServiceImpl implements GenerateQrService {

    public static String ENC_KEY = "";
    @Autowired
    private GenerateQrRepository generateQrRepository;

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

    private static String generateQrChecksum(JSONObject qrObject, String checkSumKey) {
        StringBuilder concatenatedString = new StringBuilder();

        try {

            concatenatedString.append(qrObject.get("source"));
            concatenatedString.append(qrObject.get("channel"));
            concatenatedString.append(qrObject.get("extTransactionId"));
            concatenatedString.append(qrObject.get("sid"));
            concatenatedString.append(qrObject.get("terminalId"));
            concatenatedString.append(qrObject.get("amount"));
            concatenatedString.append(qrObject.get("type"));
            concatenatedString.append(qrObject.get("remark"));
            concatenatedString.append(qrObject.get("requestTime"));
            concatenatedString.append(qrObject.get("minAmount"));
            concatenatedString.append(qrObject.get("reciept"));
            // concatenatedString.append(qrObject.get("param1"));
            // concatenatedString.append(qrObject.get("param2"));
            // concatenatedString.append(qrObject.get("param3"));
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
    public String qrGenerate(GenerateQr generateQr) {

        JSONObject obj = getJsonRequest();
        ENC_KEY = generateQr.getEncKey();

        String source = generateQr.getSource();

        String exTxnId = generateRandomString(generateQr.getExtTransactionId(), 20);

//		GenerateQrEntity dqr = new GenerateQrEntity();
//		dqr.setAmount(generateQr.getAmount());
//		dqr.setChannel(generateQr.getChannel());
//		dqr.setChecksum(generateQr.getChecksum());
//		dqr.setEncKey(generateQr.getEncKey());
//		dqr.setExtTransactionId(source + exTxnId);
//		dqr.setMinAmount(generateQr.getMinAmount());
//		dqr.setReceipt(generateQr.getReciept());
//		dqr.setRemark(generateQr.getRemark());
//		dqr.setRequestTime(generateQr.getRequestTime());
//		dqr.setSid(generateQr.getSid());
//		dqr.setSource(generateQr.getSource());
//		dqr.setTerminalId(generateQr.getTerminalId());
//		dqr.setType(generateQr.getType());
//		dqr.setHeaderKey(Constants.cid);
//		generateQrRepository.save(dqr);

        obj.put("source", generateQr.getSource());
        obj.put("channel", generateQr.getChannel());
//		obj.put("extTransactionId", generateQr.getType().equalsIgnoreCase("D") ? source + exTxnId : "");
        obj.put("extTransactionId", generateQr.getType().equalsIgnoreCase("D") ? source + exTxnId : "");
        obj.put("sid", generateQr.getSid());
        obj.put("terminalId", generateQr.getTerminalId());
        obj.put("amount", generateQr.getAmount());
        obj.put("type", generateQr.getType().toUpperCase());
        obj.put("remark", generateQr.getRemark());
        obj.put("requestTime", generateQr.getRequestTime());
        obj.put("minAmount", generateQr.getMinAmount());
        obj.put("reciept", generateQr.getReciept());
        String checksum = generateQrChecksum(obj, generateQr.getChecksum());
        obj.put("checksum", checksum);
        String encryptedReq = encryptRequest(obj.toString(), generateQr.getEncKey());

        return encryptedReq;
    }

    @Override
    public String decryptResponse(String dcrypt) {
        return "Decrypted request to cross verify" + decryptResponse(dcrypt, ENC_KEY);
    }
}
