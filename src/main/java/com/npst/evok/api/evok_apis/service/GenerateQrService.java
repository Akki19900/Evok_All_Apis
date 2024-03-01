package com.npst.evok.api.evok_apis.service;

import com.npst.evok.api.evok_apis.pojo.GenerateQr;

public interface GenerateQrService {

    String qrGenerate(GenerateQr generateQr);

    String decryptResponse(String dcrypt);

}
