package com.npst.evok.api.evok_apis.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
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

    public GenerateQrEntity() {
        super();
        // TODO Auto-generated constructor stub
    }

    public GenerateQrEntity(int id, String source, String channel, String sid, String terminalId, String amount,
                            String type, String remark, String requestTime, String minAmount, String receipt, String checksum,
                            String encKey, String extTransactionId, String qrString) {
        super();
        this.id = id;
        this.source = source;
        this.channel = channel;
        this.sid = sid;
        this.terminalId = terminalId;
        this.amount = amount;
        this.type = type;
        this.remark = remark;
        this.requestTime = requestTime;
        this.minAmount = minAmount;
        this.receipt = receipt;
        this.checksum = checksum;
        this.encKey = encKey;
        this.extTransactionId = extTransactionId;
        this.qrString = qrString;
    }

    public String getHeaderKey() {
        return headerKey;
    }

    public void setHeaderKey(String headerKey) {
        this.headerKey = headerKey;
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

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(String minAmount) {
        this.minAmount = minAmount;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
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

    public String getExtTransactionId() {
        return extTransactionId;
    }

    public void setExtTransactionId(String extTransactionId) {
        this.extTransactionId = extTransactionId;
    }

    public String getQrString() {
        return qrString;
    }

    public void setQrString(String qrString) {
        this.qrString = qrString;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "GenerateQrEntity [id=" + id + ", source=" + source + ", channel=" + channel + ", sid=" + sid
                + ", terminalId=" + terminalId + ", amount=" + amount + ", type=" + type + ", remark=" + remark
                + ", requestTime=" + requestTime + ", minAmount=" + minAmount + ", receipt=" + receipt + ", checksum="
                + checksum + ", encKey=" + encKey + ", extTransactionId=" + extTransactionId + ", qrString=" + qrString
                + "]";
    }

}
