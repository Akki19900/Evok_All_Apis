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
public class OKHttpAccountEnquiry {

    public String httpConfig(String req) throws IOException {

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, req);
        Request request = new Request.Builder().url(Constants.ACCOUNT_ENQUIRY).method("POST", body)
                .addHeader("cid", Constants.cid).addHeader("Content-Type", "text/plain").build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

}
