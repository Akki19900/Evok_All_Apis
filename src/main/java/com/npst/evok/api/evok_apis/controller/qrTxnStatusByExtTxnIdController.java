package com.npst.evok.api.evok_apis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.npst.evok.api.evok_apis.entity.TxnStatusEntity;
import com.npst.evok.api.evok_apis.okhttp.OkHttpQRStatusExTxnId;
import com.npst.evok.api.evok_apis.pojo.Constants;
import com.npst.evok.api.evok_apis.pojo.QrTxnStatusByExtTxnId;
import com.npst.evok.api.evok_apis.repository.TxnStatusRepository;
import com.npst.evok.api.evok_apis.service.QrTxnStatusByExtTxnIdService;
import com.npst.evok.api.evok_apis.serviceimpl.QrTxnStatusByExtTxnIdServiceImpl;

@RestController
public class qrTxnStatusByExtTxnIdController {

    @Autowired
    private QrTxnStatusByExtTxnIdService qrTxnStatusByExtTxnIdService;

    @Autowired
    private OkHttpQRStatusExTxnId exTxnId;

    @PostMapping("/qrTxnStatusExtTcnIdEnc")
    public ResponseEntity<Object> txnRrn(@RequestBody QrTxnStatusByExtTxnId txnStatusRrn) {
        Object response = null;
        try {
            response = qrTxnStatusByExtTxnIdService.statusByRrn(txnStatusRrn);
            String encResp = exTxnId.qrExTxnId(response.toString());
            String decResp = QrTxnStatusByExtTxnIdServiceImpl.decryptResponse(encResp, txnStatusRrn.getEncKey());

            return new ResponseEntity<>(decResp, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/qrTxnStatusExtTcnIdDec")
    public ResponseEntity<Object> decryptedData(@RequestBody String decrypted) {

        String dcrypt = null;
        try {
            dcrypt = qrTxnStatusByExtTxnIdService.decryptResponse(decrypted);
            return new ResponseEntity<Object>(dcrypt, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Object>(dcrypt, HttpStatus.BAD_REQUEST);
        }
    }
}
