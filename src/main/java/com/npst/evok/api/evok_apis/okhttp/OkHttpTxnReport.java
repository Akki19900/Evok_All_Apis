package com.npst.evok.api.evok_apis.okhttp;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.npst.evok.api.evok_apis.pojo.Constants;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

@Component
public class OkHttpTxnReport {

    public String txnReport(String req) throws IOException {

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, req);
        Request request = new Request.Builder().url(Constants.txnReport).method("POST", body)
                .addHeader("cid", Constants.cid).addHeader("Content-Type", "text/plain").build();
        Response response = client.newCall(request).execute();
//		System.out.println(response.body().string());
        return response.body().string();
    }

}
