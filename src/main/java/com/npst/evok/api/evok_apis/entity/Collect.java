package com.npst.evok.api.evok_apis.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;

@Entity
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
    private String respMessge;
    private String txnTime;

    public Collect() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String getRespCode() {
        return respCode;
    }
    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespMessge() {
        return respMessge;
    }

    public void setRespMessge(String respMessge) {
        this.respMessge = respMessge;
    }

    public String getTxnTime() {
        return txnTime;
    }

    public void setTxnTime(String txnTime) {
        this.txnTime = txnTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getExtTransactionId() {
        return extTransactionId;
    }

    public void setExtTransactionId(String extTransactionId) {
        this.extTransactionId = extTransactionId;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getStatusKYC() {
        return statusKYC;
    }

    public void setStatusKYC(String statusKYC) {
        this.statusKYC = statusKYC;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public String getEncKey() {
        return encKey;
    }

    public void setEncKey(String encKey) {
        this.encKey = encKey;
    }

    public String getHeaderKey() {
        return headerKey;
    }

    public void setHeaderKey(String headerKey) {
        this.headerKey = headerKey;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Collect [id=" + id + ", source=" + source + ", channel=" + channel + ", extTransactionId="
                + extTransactionId + ", upiId=" + upiId + ", terminalId=" + terminalId + ", amount=" + amount
                + ", customerName=" + customerName + ", statusKYC=" + statusKYC + ", sid=" + sid + ", checksum="
                + checksum + ", encKey=" + encKey + ", headerKey=" + headerKey + ", date=" + date + ", respCode="
                + respCode + ", respMessge=" + respMessge + ", txnTime=" + txnTime + "]";
    }

}
