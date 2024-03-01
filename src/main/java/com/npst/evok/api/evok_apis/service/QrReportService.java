package com.npst.evok.api.evok_apis.service;

import com.npst.evok.api.evok_apis.pojo.QrReport;

public interface QrReportService {

    String qrRpt(QrReport qrReport);

    String decryptResponse(String dcrypt);

}
