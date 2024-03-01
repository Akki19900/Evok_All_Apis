package com.npst.evok.api.evok_apis.controller;

import com.npst.evok.api.evok_apis.okhttp.OKHttpAccountVerificationUPIID;
import com.npst.evok.api.evok_apis.pojo.CommonForAllPayout;
import com.npst.evok.api.evok_apis.service.AccountVerificationUpiIDService;
import com.npst.evok.api.evok_apis.serviceimpl.AccountVerificationUpiIdServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeoutException;

@RestController
public class AccountVerificationUpiIDController {

    @Autowired
    private AccountVerificationUpiIDService verificationUpiIDService;

    @Autowired
    private OKHttpAccountVerificationUPIID accountVerificationUPIID;

    @PostMapping("/accVerificationUpiId")
    public ResponseEntity<Object> payOut(@RequestBody CommonForAllPayout commonForAllPayout) throws TimeoutException {
        Object response = null;
        try {
            String incResp = verificationUpiIDService.accountVerUpiId(commonForAllPayout);
            String rawResp = accountVerificationUPIID.httpConfig(incResp);
            String decryptResponse = AccountVerificationUpiIdServiceImpl.decryptResponse(rawResp, commonForAllPayout.getEncKey());
            return new ResponseEntity<>(decryptResponse, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("This comes in catch block ");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

}
