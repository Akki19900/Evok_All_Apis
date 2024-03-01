package com.npst.evok.api.evok_apis.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionReport {

    private String source;
    private String channel;
    private String extTransactionId;
    private String upiId;
    private String terminalId;
    private String sid;
    private String customerName;
    private String amount;
    private String statusKYC;
    private String startDate;
    private String pageSize;
    private String pageNo;
    private String endDate;
    private String type;
    private String remark;
    private String requestTime;
    private String minAmount;
    private String receipt;
    private String checksum;
    private String encKey;
    private String messageDec;
    private String messageKey;

}
