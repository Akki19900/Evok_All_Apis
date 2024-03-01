package com.npst.evok.api.evok_apis.service;

import com.npst.evok.api.evok_apis.pojo.QrTxnStatusRrn;

public interface QrTxnStatusRrnService {

    String qrStatusRrn(QrTxnStatusRrn qrTxnStatusRrn);

    String decryptResponse(String dcrypt);
}
