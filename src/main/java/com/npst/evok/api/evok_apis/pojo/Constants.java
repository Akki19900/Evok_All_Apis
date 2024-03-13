package com.npst.evok.api.evok_apis.pojo;

public interface Constants {

    final String cid = "6a6bb7f094e0f84f81ed2b6437c2adb8";
    final String verifyVPA = "https://merchantuat.timepayonline.com/evok/cm/v2/verifyVPA";
    final String transfer = "https://merchantuat.timepayonline.com/evok/cm/v2/transfer";
    final String txnStatus = "https://merchantuat.timepayonline.com/evok/qr/v1/qrStatus";
    final String txnReport = "https://merchantuat.timepayonline.com/evok/cm/v2/report";
    final String qr = "https://merchantuat.timepayonline.com/evok/qr/v1/dqr";
    final String qrRRN = "https://merchantuat.timepayonline.com/evok/qr/v1/qrStatusRRN";
    final String qrReport = "https://merchantuat.timepayonline.com/evok/qr/v1/qrreport";
    final String qrStatus = "https://merchantuat.timepayonline.com/evok/qr/v1/qrStatus";
    final String payout = "https://merchantuat.timepayonline.com/evok/cm/merchantpayout/v1/payout";
    final String ACCOUNT_VERIFICATION = "https://merchantuat.timepayonline.com/evok/cm/v1/tpv";
    final String ACCOUNT_ENQUIRY = "https://merchantuat.timepayonline.com/evok/cm/v1/tpvEnquiry";
    final String TPV_UPI_ID = "https://merchantuat.timepayonline.com/evok/cm/v1/tpvUPIID";
    final String REFUND = "https://merchantuat.timepayonline.com/evok/cm/v1/refund";
    final String REFUND_STATUS = "https://merchantuat.timepayonline.com/evok/cm/v1/refundStatus";
    // final String BULK_ENQUIRY = "https://merchantuat.timepayonline.com/evok/cm/v1/payout/bulkEnquiry";
    final String PAYOUT_ENQUIRY = "https://merchantuat.timepayonline.com/evok/cm/merchantpayout/v1/payout";
    //  production apis for payout
    final String BULK_ENQUIRY = "https://merchantuat.timepayonline.com/evok/cm/merchantpayout/v1/payout/bulkEnquiry";
}