package com.npst.evok.api.evok_apis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.npst.evok.api.evok_apis.okhttp.OkHttpTxnReport;
import com.npst.evok.api.evok_apis.pojo.TransactionReport;
import com.npst.evok.api.evok_apis.service.TransactionReportService;
import com.npst.evok.api.evok_apis.serviceimpl.TransactionReportServiceImpl;

@RestController
public class TransactionReportController {

    @Autowired
    private TransactionReportService transactionReportService;

    @Autowired
    private OkHttpTxnReport httpTxnReport;

    @PostMapping("/transactionReportEnc")
    public ResponseEntity<Object> transactionReport(@RequestBody TransactionReport transactionReport) {
        Object response = null;
        try {
            response = transactionReportService.report(transactionReport);
            String encRec = httpTxnReport.txnReport(response.toString());
            String decResp = TransactionReportServiceImpl.decryptResponse(encRec, transactionReport.getEncKey());
            return new ResponseEntity<>(decResp, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/transactionReportDec")
    public ResponseEntity<Object> decryptedData(@RequestBody String decrypted) {

        String dcrypt = null;
        try {
            dcrypt = transactionReportService.decryptResponse(decrypted);
            return new ResponseEntity<Object>(dcrypt, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Object>(dcrypt, HttpStatus.BAD_REQUEST);
        }
    }
}
