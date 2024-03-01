package com.npst.evok.api.evok_apis.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
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

    public TxnStatusEntity() {
        super();
        // TODO Auto-generated constructor stub
    }

    public TxnStatusEntity(int id, String source, String channel, String extTransactionId, String terminalId,
                           String checksum, String encKey, String headerKey, Date date) {
        super();
        this.id = id;
        this.source = source;
        this.channel = channel;
        this.extTransactionId = extTransactionId;
        this.terminalId = terminalId;
        this.checksum = checksum;
        this.encKey = encKey;
        this.headerKey = headerKey;
        this.date = date;
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

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTxnType() {
        return txnType;
    }

    public void setTxnType(String txnType) {
        this.txnType = txnType;
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

    @Override
    public String toString() {
        return "TxnStatusEntity [id=" + id + ", source=" + source + ", channel=" + channel + ", extTransactionId="
                + extTransactionId + ", terminalId=" + terminalId + ", checksum=" + checksum + ", encKey=" + encKey
                + ", headerKey=" + headerKey + ", date=" + date + ", status=" + status + ", txnType=" + txnType
                + ", respCode=" + respCode + ", respMessge=" + respMessge + "]";
    }

}
