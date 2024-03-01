package com.npst.evok.api.evok_apis.service;

import com.npst.evok.api.evok_apis.pojo.TransactionStatus;

public interface TransactionStatusService {

    String transactionStatus(TransactionStatus transactionStatus);

    String decryptResponse(String dcrypt);

}
