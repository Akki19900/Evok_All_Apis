package com.npst.evok.api.evok_apis.pojo;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonForAllPayout {

    private String source;
    private String sid;
    private String customerAcc;
    private String customerIFSC;
    private String customerMobileNo;
    private String checkSumKey;
    private String encKey;
    private String extTransactionId;
    private String customerName;
    private String upiId;
    private String orgTxnId;
    private String orgRrn;
    private String payeeAddr;
    private String amount;
    private String remarks;
    private String enquiryId;

}
