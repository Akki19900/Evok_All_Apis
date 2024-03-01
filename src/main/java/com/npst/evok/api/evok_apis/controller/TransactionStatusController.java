package com.npst.evok.api.evok_apis.controller;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.npst.evok.api.evok_apis.entity.TxnStatusEntity;
import com.npst.evok.api.evok_apis.okhttp.OkHttpTxnStatus;
import com.npst.evok.api.evok_apis.pojo.Constants;
import com.npst.evok.api.evok_apis.pojo.TransactionStatus;
import com.npst.evok.api.evok_apis.repository.TxnStatusRepository;
import com.npst.evok.api.evok_apis.service.TransactionStatusService;
import com.npst.evok.api.evok_apis.serviceimpl.TransactionStatusServiceImpl;

@RestController
public class TransactionStatusController {

    @Autowired
    private TransactionStatusService transactionStatusService;

    @Autowired
    private TxnStatusRepository txnStatusRepository;

    @Autowired
    private OkHttpTxnStatus okHttpTxnStatus;

    @PostMapping("/transactionStatusEnc")
    public ResponseEntity<Object> transactionStatus(@RequestBody TransactionStatus transactionStatus) {
        Object response = null;
        try {
            response = transactionStatusService.transactionStatus(transactionStatus);
            String encResp = okHttpTxnStatus.httpTxnStatus(response.toString());
            String decResp = TransactionStatusServiceImpl.decryptResponse(encResp, transactionStatus.getEncKey());

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> dataMap = objectMapper.readValue(decResp, new TypeReference<Map<String, Object>>() {
            });

            String extId = (String) dataMap.get("extTransactionId");
            String txnType = (String) dataMap.get("txnType");
            String status = (String) dataMap.get("status");

            JSONObject jsonObject = new JSONObject(decResp);
            JSONArray dataArray = jsonObject.getJSONArray("data");

            String respCode = "";
            String respMessage = "";
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject dataObject = dataArray.getJSONObject(i);

                respCode = dataObject.getString("respCode");
                respMessage = dataObject.getString("respMessge");

            }

            TxnStatusEntity txn = new TxnStatusEntity();
            txn.setSource(transactionStatus.getSource());
            txn.setChannel(transactionStatus.getChannel());
            txn.setExtTransactionId(extId);
            txn.setTerminalId(transactionStatus.getTerminalId());
            txn.setChecksum(transactionStatus.getChecksum());
            txn.setEncKey(transactionStatus.getEncKey());
            txn.setHeaderKey(Constants.cid);
            txn.setRespCode(respCode);
            txn.setStatus(status);
            txn.setRespMessge(respMessage);
            txn.setTxnType(txnType);
            txnStatusRepository.save(txn);

            return new ResponseEntity<>(decResp, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/transactionStatusDec")
    public ResponseEntity<Object> decryptedData(@RequestBody String decrypted) {

        String dcrypt = null;
        try {
            dcrypt = transactionStatusService.decryptResponse(decrypted);
            return new ResponseEntity<Object>(dcrypt, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Object>(dcrypt, HttpStatus.BAD_REQUEST);
        }
    }

}
