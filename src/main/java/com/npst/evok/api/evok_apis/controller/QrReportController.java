package com.npst.evok.api.evok_apis.controller;

import com.npst.evok.api.evok_apis.okhttp.OkHttpQRReport;
import com.npst.evok.api.evok_apis.pojo.QrReport;
import com.npst.evok.api.evok_apis.service.QrReportService;
import com.npst.evok.api.evok_apis.serviceimpl.QrReportServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QrReportController {

    @Autowired
    private QrReportService qrReportService;
    @Autowired
    private OkHttpQRReport report;

    @PostMapping("/qrReportEnc")
    public ResponseEntity<Object> qrRpt(@RequestBody QrReport qrRe) {
        Object response = null;
        try {
            response = qrReportService.qrRpt(qrRe);
            String qrResp = report.qrReport(response.toString());
            String decresp = QrReportServiceImpl.decryptResponse(qrResp, qrRe.getEncKey());
            return new ResponseEntity<>(decresp, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/qrReportDec")
    public ResponseEntity<Object> decryptedData(@RequestBody String decrypted) {

        String dcrypt = null;
        try {
            dcrypt = qrReportService.decryptResponse(decrypted);
            return new ResponseEntity<Object>(dcrypt, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Object>(dcrypt, HttpStatus.BAD_REQUEST);
        }
    }

}
