package com.npst.evok.api.evok_apis.controller;

import com.npst.evok.api.evok_apis.okhttp.OKHttpPayout;
import com.npst.evok.api.evok_apis.pojo.Payout;
import com.npst.evok.api.evok_apis.service.IPayoutService;
import com.npst.evok.api.evok_apis.serviceimpl.PayoutServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeoutException;

@RestController
public class PayoutController {

    @Autowired
    private IPayoutService payoutService;

    @Autowired
    private OKHttpPayout httpPayout;

    @PostMapping("/payoutRefund")
    public ResponseEntity<Object> payOut(@RequestBody Payout payout) throws TimeoutException {
//        Object response = null;
        try {
            String payoutRequest = payoutService.payoutRequest(payout);
            String rawResp = httpPayout.httpConfig(payoutRequest);
            String decryptResponse = PayoutServiceImpl.decryptResponse(rawResp, payout.getEncKey());
            return new ResponseEntity<>(decryptResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
