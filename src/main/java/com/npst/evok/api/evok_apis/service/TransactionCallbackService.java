package com.npst.evok.api.evok_apis.service;

import com.npst.evok.api.evok_apis.pojo.TransactionCallback;

public interface TransactionCallbackService {

    String callback(TransactionCallback transactionCallback);

    String decryptResponse(String dcrypt);

}
