package com.npst.evok.api.evok_apis.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.npst.evok.api.evok_apis.entity.GenerateQrEntity;
import com.npst.evok.api.evok_apis.okhttp.OkHttpQR;
import com.npst.evok.api.evok_apis.pojo.Constants;
import com.npst.evok.api.evok_apis.pojo.GenerateQr;
import com.npst.evok.api.evok_apis.repository.GenerateQrRepository;
import com.npst.evok.api.evok_apis.service.GenerateQrService;
import com.npst.evok.api.evok_apis.serviceimpl.GenerateQrServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;
import java.util.Map;

@RestController
public class GenerateQrController {

    @Autowired
    private GenerateQrService generateQrService;

    @Autowired
    private OkHttpQR genQR;
    @Autowired
    private GenerateQrRepository generateQrRepository;

    @PostMapping("/generateQrEnc")
    public ResponseEntity<Object> genQr(@RequestBody GenerateQr generateQr) {
        Object response = null;
        try {

            response = generateQrService.qrGenerate(generateQr);

            String encResp = genQR.qr(response.toString());

            String decResp = GenerateQrServiceImpl.decryptResponse(encResp, generateQr.getEncKey());
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> dataMap = objectMapper.readValue(decResp, new TypeReference<Map<String, Object>>() {
            });
            String qrIntent = (String) dataMap.get("qrString");
            String extId = (String) dataMap.get("extTransactionId");
            String s = URLDecoder.decode(qrIntent);

            GenerateQrEntity dqr = new GenerateQrEntity();
            dqr.setAmount(generateQr.getAmount());
            dqr.setChannel(generateQr.getChannel());
            dqr.setChecksum(generateQr.getChecksum());
            dqr.setEncKey(generateQr.getEncKey());
            dqr.setExtTransactionId(extId);
            dqr.setMinAmount(generateQr.getMinAmount());
            dqr.setReceipt(generateQr.getReciept());
            dqr.setRemark(generateQr.getRemark());
            dqr.setRequestTime(generateQr.getRequestTime());
            dqr.setSid(generateQr.getSid());
            dqr.setSource(generateQr.getSource());
            dqr.setTerminalId(generateQr.getTerminalId());
            dqr.setType(generateQr.getType().toUpperCase());
            dqr.setHeaderKey(Constants.cid);
            dqr.setQrString(s);
            generateQrRepository.save(dqr);

            String toScreen = "";

            if (extId != null && !extId.isEmpty()) {

                toScreen = "QR ===> " + s + "\nextTxn ID ===>  " + extId;

            } else {

                toScreen = "QR ===> " + s + "\nextTxn ID ===>  Not Required for Static QR";

            }

            System.out.println("This is decrypted final response ===== " + decResp);

            return new ResponseEntity<>(toScreen, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/generateQrDec")
    public ResponseEntity<Object> decryptedData(@RequestBody String decrypted) {

        String dcrypt = null;
        try {
            dcrypt = generateQrService.decryptResponse(decrypted);
            return new ResponseEntity<Object>(dcrypt, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Object>(dcrypt, HttpStatus.BAD_REQUEST);
        }
    }

}
