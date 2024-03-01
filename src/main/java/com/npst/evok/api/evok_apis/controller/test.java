package com.npst.evok.api.evok_apis.controller;

import org.springframework.scheduling.annotation.Scheduled;

public class test {

    @Scheduled(fixedRate = 5000)

    public void doThis() {

        System.out.println("This method is working");
    }

}
