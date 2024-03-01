package com.npst.evok.api.evok_apis.service;

import com.npst.evok.api.evok_apis.pojo.QrTxnStatusByExtTxnId;

public interface QrTxnStatusByExtTxnIdService {

    String statusByRrn(QrTxnStatusByExtTxnId txnStatusRrn);

    String decryptResponse(String dcrypt);

}
