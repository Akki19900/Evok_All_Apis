package com.npst.evok.api.evok_apis.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.npst.evok.api.evok_apis.pojo.TransactionCallback;
import com.npst.evok.api.evok_apis.service.TransactionCallbackService;

@RestController
public class TransactionCallbackController {

    @Autowired
    private TransactionCallbackService transactionCallbackService;

    @GetMapping("")
    public ResponseEntity<Object> transactionCallback(@RequestBody TransactionCallback transactionCallback) {
        Object response = null;
        try {
            response = transactionCallbackService.callback(transactionCallback);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/transactionCallbackDec")
    public ResponseEntity<Object> decryptedData(@RequestBody String decrypted) {

        String dcrypt = null;
        try {
            dcrypt = transactionCallbackService.decryptResponse(decrypted);
            return new ResponseEntity<Object>(dcrypt, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Object>(dcrypt, HttpStatus.BAD_REQUEST);
        }
    }
}
