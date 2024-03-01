package com.npst.evok.api.evok_apis.serviceimpl;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.naming.java.javaURLContextFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import com.npst.evok.api.evok_apis.pojo.AllEntity;
import com.npst.evok.api.evok_apis.pojo.Payee;
import com.npst.evok.api.evok_apis.pojo.Payout;
import com.npst.evok.api.evok_apis.service.IPayoutService;

@Service
public class PayoutServiceImpl implements IPayoutService {

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
            concatenatedString.append(payoutObj.get("source"));
            concatenatedString.append(payoutObj.get("sid"));
            concatenatedString.append("CH_$0Ma_K(yN@!D@n$T"); // CH_$0Ma_K(yN@!D@n$T
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
    public String payoutRequest(Payout payout) {

        JSONObject mainObj = new JSONObject();

        JSONArray payees = new JSONArray();
        JSONObject p1 = new JSONObject();
        JSONObject p2 = new JSONObject();
        JSONObject p3 = new JSONObject();
        JSONObject p4 = new JSONObject();
        JSONObject p5 = new JSONObject();
        JSONObject p6 = new JSONObject();
        JSONObject p7 = new JSONObject();
        JSONObject p8 = new JSONObject();
        JSONObject p9 = new JSONObject();
        JSONObject p10 = new JSONObject();

        List<Payee> list = payout.getPayee();

        Map<String, List<Payee>> payeesNO = list.stream().collect(Collectors.groupingBy(Payee::getPayeeNO));

        List<Payee> payee1 = payeesNO.get("payee1");
        List<Payee> payee2 = payeesNO.get("payee2");
        List<Payee> payee3 = payeesNO.get("payee3");
        List<Payee> payee4 = payeesNO.get("payee4");
        List<Payee> payee5 = payeesNO.get("payee5");
        List<Payee> payee6 = payeesNO.get("payee6");
        List<Payee> payee7 = payeesNO.get("payee7");
        List<Payee> payee8 = payeesNO.get("payee8");
        List<Payee> payee9 = payeesNO.get("payee9");
        List<Payee> payee10 = payeesNO.get("payee10");
        try {

            for (Payee payee : payee1) {

                p1.put("extTransactionId",
                        payout.getSource() + UUID.randomUUID().toString().replaceAll("-", "").toUpperCase());
                p1.put("payeeAcNum", payee.getPayeeAcNum());
                p1.put("payeeIfsc", payee.getPayeeIfsc());
                p1.put("payeeName", payee.getPayeeName());
                p1.put("payeeMobile", payee.getPayeeMobile());
                p1.put("payeeAmount", payee.getPayeeAmount());
                p1.put("remarks", payee.getRemarks());
                payees.add(p1);

            }

            for (Payee payee : payee2) {

                p2.put("extTransactionId",
                        payout.getSource() + UUID.randomUUID().toString().replaceAll("-", "").toUpperCase());
                p2.put("payeeAddr", payee.getPayeeAddr());
                p2.put("payeeName", payee.getPayeeName());
                p2.put("payeeMobile", payee.getPayeeMobile());
                p2.put("payeeAmount", payee.getPayeeAmount());
                p2.put("remarks", payee.getRemarks());
                payees.add(p2);

            }
            for (Payee payee : payee3) {

                p3.put("extTransactionId",
                        payout.getSource() + UUID.randomUUID().toString().replaceAll("-", "").toUpperCase());
                p3.put("payeeAddr", payee.getPayeeAddr());
                p3.put("payeeName", payee.getPayeeName());
                p3.put("payeeMobile", payee.getPayeeMobile());
                p3.put("payeeAmount", payee.getPayeeAmount());
                p3.put("remarks", payee.getRemarks());
                payees.add(p3);

            }
            for (Payee payee : payee4) {

                p4.put("extTransactionId",
                        payout.getSource() + UUID.randomUUID().toString().replaceAll("-", "").toUpperCase());
                p4.put("payeeAddr", payee.getPayeeAddr());
                p4.put("payeeName", payee.getPayeeName());
                p4.put("payeeMobile", payee.getPayeeMobile());
                p4.put("payeeAmount", payee.getPayeeAmount());
                p4.put("remarks", payee.getRemarks());
                payees.add(p4);
            }

//			for (Payee payee : payee5) {
//
//				p5.put("extTransactionId",
//						payout.getSource() + UUID.randomUUID().toString().replaceAll("-", "").toUpperCase());
//				p5.put("payeeAddr", payee.getPayeeAddr());
//				p5.put("payeeName", payee.getPayeeName());
//				p5.put("payeeMobile", payee.getPayeeMobile());
//				p5.put("payeeAmount", payee.getPayeeAmount());
//				p5.put("remarks", payee.getRemarks());
//				payees.add(p5);
//
//			}
        } catch (Exception e) {
            e.printStackTrace();
        }

        String requestDate = new Date(new java.util.Date().getTime()).toString();

        mainObj.put("source", payout.getSource());
        mainObj.put("sid", payout.getSid());
        mainObj.put("requestDate", requestDate);
        mainObj.put("payees", payees);
        String checksum = generatePayoutChecksum(mainObj, payout.getCheckSum());
        mainObj.put("checksum", checksum);

        String encryptedReq = encryptRequest(mainObj.toString(), payout.getEncKey());

        return encryptedReq;
    }

//	@Override
//	public String dresponse(AllEntity verifyVPAEntity) {
//
//		// VerifyVpaServiceImpl verifyVpaServiceImpl = new VerifyVpaServiceImpl(); //
//		// 89a081e3760aa8fb2de1665177e4abbe
//		// String enqReq = verifyVpaServiceImpl.verifyVpa();
//		return "Decrypted request to cross verify"
//				+ decryptResponse(verifyVPAEntity.getMessageDec(), verifyVPAEntity.getMessageKey());
//	}

}
