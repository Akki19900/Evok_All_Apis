package com.npst.evok.api.evok_apis.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CallbackReciver {

    private String plainText;

    public String getPlainText() {
        return plainText;
    }

    public void setPlainText(String plainText) {
        this.plainText = plainText;
    }

    @PostMapping("/getCallback")
    public void recieveCallback(@RequestBody CallbackReciver callbackReciver) {

        callbackReciver.getPlainText();
        System.out.println(callbackReciver.toString());

    }

}
