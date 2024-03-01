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
import com.npst.evok.api.evok_apis.service.IAccountEnquiry;
import com.npst.evok.api.evok_apis.service.IAccountVerificationViaAccountNoAndIFSC;

@Service
public class AccountEnquiryImpl implements IAccountEnquiry {

//	public static String generateRandomString(String baseString, int length) {
//		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
//		StringBuilder randomString = new StringBuilder(length);
//		Random random = new Random();
//
//		for (int i = 0; i < length; i++) {
//			int randomIndex = random.nextInt(characters.length());
//			char randomChar = characters.charAt(randomIndex);
//			randomString.append(randomChar);
//		}
//
//		// System.out.println("Random Function Output ---> " + baseString +
//		// randomString.toString());
//
//		return baseString + randomString.toString();
//	}

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

    private static String generateVerifyVpaChecksum(JSONObject qrObject, String checkSumKey) {
        StringBuilder concatenatedString = new StringBuilder();

        try {

            concatenatedString.append(qrObject.get("source"));
            concatenatedString.append(qrObject.get("sid"));
            concatenatedString.append(qrObject.get("extTransactionId"));
            System.out.println("String is " + concatenatedString.toString());
        } catch (Exception e) {
            e.printStackTrace();

        }

//		System.out.println("Generate Checksum OUtput ----> "
//				+ generateChecksumMerchant(concatenatedString.toString(), checkSumKey));
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

//		String keyString = Arrays.toString(merchantSecretKey_.getEncoded());

//		System.out.println("Merchant Key Output ----> " + keyString);

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
    public String accountEnquiry(CommonForAllPayout commonForAllPayout) {
        JSONObject obj = getJsonRequest();

        ENC_KEY = commonForAllPayout.getEncKey();

        String extTransactionId = commonForAllPayout.getExtTransactionId();
        System.out.println(extTransactionId);

        // String source = commonForAllPayout.getSource();

        // String exTxnId =
        // generateRandomString(commonForAllPayout.getExtTransactionId(), 15);

        obj.put("source", commonForAllPayout.getSource());
        obj.put("extTransactionId", commonForAllPayout.getExtTransactionId());
        System.out
                .println(commonForAllPayout.getExtTransactionId() + "sdhfksjdhfkjsdfhkjshfkjsdfhjkdsfhksjdfjskfbksdjf");
        obj.put("sid", commonForAllPayout.getSid());
        String checksum = generateVerifyVpaChecksum(obj, commonForAllPayout.getCheckSumKey());
        System.out.println("Checksum is " + checksum);
        obj.put("checksum", checksum);
        String encryptedReq = encryptRequest(obj.toString(), commonForAllPayout.getEncKey());
        System.out.println("This is source " + ENC_KEY);
        return encryptedReq;
    }

    // public static void main(String[] args) {
    // VerifyVpaServiceImpl verifyVpaServiceImpl = new VerifyVpaServiceImpl();
    // String enqReq = verifyVpaServiceImpl.verifyVpa();
    // System.out.println("Encrypted request to check " + enqReq);

    // System.out.println("Decrypted request to cross verify " + decryptResponse(
    // "36yDqzRDKHL7m1LNcxW9mQHuvcNoQKk2gblK1IanXJeJ10uIlG6GvzpGAL+pecaRgKkuf26yJu3ZbECs6he3gz7sis3HM/WK+9VnmwIX4P6sYHP75h1FzFIp4b3XXkgGK7Et5GcYLHib4HS9qfPAgqwqaxzXm3aKIvuogYp/ba0iSuxzfRRokZVkftOUi8fBPDcNq6OeFUGlL1QpUknWcdcQmBezxPL2rZvc0oxf83UQ3h/FrrphKZq0r4knPFnhDZnbOWL6Jjn+tUGXF0Ky4fHzvtcFP5C4NqmGsr9X9A/dyWvrvZC2ItKUHuPYWlPMVeJOPY3D/ivkTjUJ+l4Vffg8UZ2G3BsDpVCiyDbpJ6lq6DRY7H1SNq6R9ZdBZvDbJcDtlD6Vtzkn5hhUVnNOWljorr4hBZRQmY4x5V4DHpewrwxxpOdslTq2T3ADKGOTt5fG0uRxUyZ558NhHVFl5szrjrd82f8Xzb6bFSenSfE=",
    // ENC_KEY));
    // }

}
