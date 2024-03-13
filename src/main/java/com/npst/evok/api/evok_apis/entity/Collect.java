package com.npst.evok.api.evok_apis.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class Collect {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String source;
    private String channel;
    private String extTransactionId;
    private String upiId;
    private String terminalId;
    private String amount;
    private String customerName;
    private String statusKYC;
    private String sid;
    private String checksum;
    private String encKey;
    private String headerKey;
    private Date date = new Date();
    private String respCode;
    private String respMessage;
    private String txnTime;

}
