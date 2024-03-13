package com.npst.evok.api.evok_apis.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.npst.evok.api.evok_apis.entity.Collect;
import com.npst.evok.api.evok_apis.okhttp.OkHttpTransfer;
import com.npst.evok.api.evok_apis.pojo.Constants;
import com.npst.evok.api.evok_apis.pojo.MerchantTransfer;
import com.npst.evok.api.evok_apis.repository.CollectRepository;
import com.npst.evok.api.evok_apis.service.MerchantTransferService;
import com.npst.evok.api.evok_apis.serviceimpl.MerchantTransferImpl;
import lombok.AllArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@AllArgsConstructor
public class MerchantTransferController {


    private MerchantTransferService merchantTransferService;
    private OkHttpTransfer okHttpTransfer;
    CollectRepository collectRepository;

    @PostMapping("/transferEnc")
    public ResponseEntity<Object> transferMerchant(@RequestBody MerchantTransfer merchantTransfer) {
        Object response = null;
        try {
            response = merchantTransferService.collectByPayee(merchantTransfer);
            String encResp = okHttpTransfer.httpTransfer(response.toString());
            String decResp = MerchantTransferImpl.decryptResponse(encResp, merchantTransfer.getEncKey());

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> dataMap = objectMapper.readValue(decResp, new TypeReference<Map<String, Object>>() {
            });

            String extId = (String) dataMap.get("extTransactionId");

            JSONObject jsonObject = new JSONObject(decResp);
            JSONArray dataArray = jsonObject.getJSONArray("data");

            String txnTime = "";
            String respCode = "";
            String respMessage = "";

            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject dataObject = dataArray.getJSONObject(i);

                txnTime = dataObject.getString("txnTime");
                respCode = dataObject.getString("respCode");
                respMessage = dataObject.getString("respMessge");

            }

            Collect collect = new Collect();
            collect.setSource(merchantTransfer.getSource());
            collect.setChannel(merchantTransfer.getChannel());
            collect.setExtTransactionId(extId);
            collect.setUpiId(merchantTransfer.getUpiId());
            collect.setTerminalId(merchantTransfer.getTerminalId());
            collect.setAmount(merchantTransfer.getAmount());
            collect.setCustomerName(merchantTransfer.getCustomerName());
            collect.setStatusKYC(merchantTransfer.getStatusKYC());
            collect.setSid(merchantTransfer.getSid());
            collect.setChecksum(merchantTransfer.getChecksum());
            collect.setHeaderKey(Constants.cid);
            collect.setEncKey(merchantTransfer.getEncKey());
            collect.setRespCode(respCode);
            collect.setRespMessage(respMessage);
            collect.setTxnTime(txnTime);
            collectRepository.save(collect);

            return new ResponseEntity<>(decResp, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/transferDec")
    public ResponseEntity<Object> decryptedData(@RequestBody String decrypted) {

        String dcrypt = null;
        try {
            dcrypt = merchantTransferService.decryptResponse(decrypted);
            return new ResponseEntity<Object>(dcrypt, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Object>(dcrypt, HttpStatus.BAD_REQUEST);
        }
    }

}
