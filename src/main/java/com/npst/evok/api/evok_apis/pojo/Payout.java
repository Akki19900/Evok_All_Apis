package com.npst.evok.api.evok_apis.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class Payout {

    private String source;
    private String channel;
    private String sid;
    private String terminalId;
    private String encKey;
    private String checkSum;
    private String requestDate;
    private List<Payee> payee;

}
