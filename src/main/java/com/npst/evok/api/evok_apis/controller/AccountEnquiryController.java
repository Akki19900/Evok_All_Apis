package com.npst.evok.api.evok_apis.controller;

import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.npst.evok.api.evok_apis.okhttp.OKHttpAccVerAnoIf;
import com.npst.evok.api.evok_apis.okhttp.OKHttpAccountEnquiry;
import com.npst.evok.api.evok_apis.pojo.CommonForAllPayout;
import com.npst.evok.api.evok_apis.pojo.Payout;
import com.npst.evok.api.evok_apis.service.IAccountEnquiry;
import com.npst.evok.api.evok_apis.service.IAccountVerificationViaAccountNoAndIFSC;
import com.npst.evok.api.evok_apis.serviceimpl.AccountEnquiryImpl;
import com.npst.evok.api.evok_apis.serviceimpl.AccountVerificationViaAccountNoAndIFSCImpl;
import com.npst.evok.api.evok_apis.serviceimpl.PayoutServiceImpl;

@RestController
public class AccountEnquiryController {

    @Autowired
    private IAccountEnquiry accEnquiry;

    @Autowired
    private OKHttpAccountEnquiry accountEnquiry;

    @PostMapping("/accEnquiry")
    public ResponseEntity<Object> payOut(@RequestBody CommonForAllPayout commonForAllPayout) throws TimeoutException {
        Object response = null;
        try {

            String incResp = accEnquiry.accountEnquiry(commonForAllPayout);

            String rawResp = accountEnquiry.httpConfig(incResp);

            String decryptResponse = AccountEnquiryImpl.decryptResponse(rawResp, commonForAllPayout.getEncKey());

            return new ResponseEntity<>(decryptResponse, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

}
