package com.npst.evok.api.evok_apis.service;

import com.npst.evok.api.evok_apis.pojo.MerchantTransfer;

public interface MerchantTransferService {

    String collectByPayee(MerchantTransfer merchantTransfer);
    String decryptResponse(String dcrypt);

}
