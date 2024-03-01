package com.npst.evok.api.evok_apis.service;

import com.npst.evok.api.evok_apis.pojo.CommonForAllPayout;
import com.npst.evok.api.evok_apis.pojo.GenerateQr;

public interface AccountVerificationUpiIDService {
    String accountVerUpiId(CommonForAllPayout commonForAllPayout);

}
