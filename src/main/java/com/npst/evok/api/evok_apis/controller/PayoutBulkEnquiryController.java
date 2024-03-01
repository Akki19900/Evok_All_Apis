package com.npst.evok.api.evok_apis.controller;

import com.npst.evok.api.evok_apis.okhttp.OKHttpBulkEnquiry;
import com.npst.evok.api.evok_apis.pojo.CommonForAllPayout;
import com.npst.evok.api.evok_apis.service.IPayoutBulkEnquiry;
import com.npst.evok.api.evok_apis.serviceimpl.PayoutServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeoutException;

@RestController
public class PayoutBulkEnquiryController {

    @Autowired
    private IPayoutBulkEnquiry iPayoutBulkEnquiry;

    @Autowired
    private OKHttpBulkEnquiry okHttpBulkEnquiry;
    ;

    @PostMapping("/payoutBulkEnquiry")
    public ResponseEntity<Object> payOut(@RequestBody CommonForAllPayout payout) throws TimeoutException {
//        Object response = null;
        try {
            String payoutRequest = iPayoutBulkEnquiry.bulkEnquiry(payout);
            String rawResp = okHttpBulkEnquiry.httpConfig(payoutRequest);
            System.out.println(rawResp);
            String decryptResponse = PayoutServiceImpl.decryptResponse(rawResp, payout.getEncKey());
            return new ResponseEntity<>(decryptResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
