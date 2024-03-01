package com.npst.evok.api.evok_apis.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TxnStatusEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String source;
    private String channel;
    private String extTransactionId;
    private String terminalId;
    private String checksum;
    private String encKey;
    private String headerKey;
    private Date date = new Date();
    private String status;
    private String txnType;
    private String respCode;
    private String respMessge;

}
