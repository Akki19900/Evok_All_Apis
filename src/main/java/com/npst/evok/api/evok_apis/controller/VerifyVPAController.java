package com.npst.evok.api.evok_apis.controller;

import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.npst.evok.api.evok_apis.entity.VerifyVpaEntity;
import com.npst.evok.api.evok_apis.okhttp.OKHttpConfig;
import com.npst.evok.api.evok_apis.pojo.AllEntity;
import com.npst.evok.api.evok_apis.pojo.Constants;
import com.npst.evok.api.evok_apis.repository.VerifyVpaRepository;
import com.npst.evok.api.evok_apis.service.VerifyVPAService;
import com.npst.evok.api.evok_apis.serviceimpl.GenerateQrServiceImpl;
import com.npst.evok.api.evok_apis.serviceimpl.VerifyVpaServiceImpl;

@RestController
public class VerifyVPAController {

    @Autowired
    private VerifyVPAService verifyVPAService;
    @Autowired
    private OKHttpConfig okHttpConfig;
    @Autowired
    private VerifyVpaRepository verifyVpaRepository;

    @PostMapping("/verifyVpaEnc")
    public ResponseEntity<Object> verifyVpa(@RequestBody AllEntity allEntity) throws TimeoutException {
        Object response = null;
        try {

            response = verifyVPAService.verifyVpa(allEntity);

            System.out.println("This is response for codebase ===>" + response);
            String encResp = okHttpConfig.httpConfig(response.toString());
            System.out.println("This is response for Switch =====> " + encResp);
            String decresp = VerifyVpaServiceImpl.decryptResponse(encResp, allEntity.getEncKey());

            String decResp = GenerateQrServiceImpl.decryptResponse(encResp, allEntity.getEncKey());
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> dataMap = objectMapper.readValue(decResp, new TypeReference<Map<String, Object>>() {
            });
            String extId = (String) dataMap.get("extTransactionId");
            JSONObject jsonObject = new JSONObject(decResp);
            JSONArray dataArray = jsonObject.getJSONArray("data");
            String customerName = "";
            String respCode = "";
            String respMessage = "";
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject dataObject = dataArray.getJSONObject(i);

                customerName = dataObject.getString("customerName");
                respCode = dataObject.getString("respCode");
                respMessage = dataObject.getString("respMessage");

            }

            VerifyVpaEntity vpa = new VerifyVpaEntity();
            vpa.setSource(allEntity.getSource());
            vpa.setChannel(allEntity.getChannel());
            vpa.setUpiId(allEntity.getUpiId());
            vpa.setTerminalId(allEntity.getTerminalId());
            vpa.setSid(allEntity.getSid());
            vpa.setEncKey(allEntity.getEncKey());
            vpa.setChecksum(allEntity.getChecksum());
            vpa.setExtTransactionId(extId);
            vpa.setHeaderKey(Constants.cid);
            vpa.setCustomerName(customerName);
            vpa.setRespCode(respCode);
            vpa.setRespMessage(respMessage);
            verifyVpaRepository.save(vpa);

            return new ResponseEntity<>(decresp, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/verifyVpaDec")
    public ResponseEntity<Object> decryptedData(@RequestBody AllEntity decrypted) {
        String dcrypt = null;
        try {
            dcrypt = verifyVPAService.dresponse(decrypted);
            return new ResponseEntity<Object>(dcrypt, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Object>(dcrypt, HttpStatus.BAD_REQUEST);
        }
    }
}