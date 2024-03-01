package com.npst.evok.api.evok_apis.controller;

import com.npst.evok.api.evok_apis.okhttp.OKHttpRefundApi;
import com.npst.evok.api.evok_apis.pojo.CommonForAllPayout;
import com.npst.evok.api.evok_apis.service.IRefundService;
import com.npst.evok.api.evok_apis.serviceimpl.RefundServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeoutException;

@RestController
public class RefundController {

    @Autowired
    private IRefundService iRefundService;

    @Autowired
    private OKHttpRefundApi httpConfig;

    @PostMapping("/refund")
    public ResponseEntity<Object> payOut(@RequestBody CommonForAllPayout commonForAllPayout) throws TimeoutException {
        Object response = null;
        try {
            String incResp = iRefundService.refund(commonForAllPayout);
            String rawResp = httpConfig.httpConfig(incResp);
            System.out.println("This is raw request "+rawResp);
            String decryptResponse = RefundServiceImpl.decryptResponse(rawResp, commonForAllPayout.getEncKey());
            return new ResponseEntity<>(decryptResponse, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("This comes in catch block ");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
