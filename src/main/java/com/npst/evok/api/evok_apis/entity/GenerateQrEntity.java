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
public class GenerateQrEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String source;
    private String channel;
    private String sid;
    private String terminalId;
    private String amount;
    private String type;
    private String remark;
    private String requestTime;
    private String minAmount;
    private String receipt;
    private String checksum;
    private String encKey;
    private String extTransactionId;
    private String qrString;
    private String headerKey;
    private Date date = new Date();

}
