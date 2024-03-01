package com.npst.evok.api.evok_apis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.npst.evok.api.evok_apis.okhttp.OkHttpQRStatusRRN;
import com.npst.evok.api.evok_apis.pojo.QrTxnStatusRrn;
import com.npst.evok.api.evok_apis.service.QrTxnStatusRrnService;
import com.npst.evok.api.evok_apis.serviceimpl.QrTxnStatusRrnServiceImpl;

@RestController
public class QrTxnStatusRrnController {

    @Autowired
    private QrTxnStatusRrnService qrTxnStatusRrnService;

    @Autowired
    private OkHttpQRStatusRRN httprespQr;

    @PostMapping("/qrTxnStatusRrnEnc")
    public ResponseEntity<Object> qrTxn(@RequestBody QrTxnStatusRrn txnStatus) {
        Object response = null;
        try {
            response = qrTxnStatusRrnService.qrStatusRrn(txnStatus);
            String encResp = httprespQr.qrTxnRRN(response.toString());
            String decResp = QrTxnStatusRrnServiceImpl.decryptResponse(encResp, txnStatus.getEncKey());

            return new ResponseEntity<>(decResp, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/qrTxnStatusRrnDec")
    public ResponseEntity<Object> decryptedData(@RequestBody String decrypted) {

        String dcrypt = null;
        try {
            dcrypt = qrTxnStatusRrnService.decryptResponse(decrypted);
            return new ResponseEntity<Object>(dcrypt, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Object>(dcrypt, HttpStatus.BAD_REQUEST);
        }
    }

}
