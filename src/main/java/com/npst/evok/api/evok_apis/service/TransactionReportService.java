package com.npst.evok.api.evok_apis.service;

import com.npst.evok.api.evok_apis.pojo.TransactionReport;

public interface TransactionReportService {

    String report(TransactionReport transactionReport);

    String decryptResponse(String dcrypt);

}
