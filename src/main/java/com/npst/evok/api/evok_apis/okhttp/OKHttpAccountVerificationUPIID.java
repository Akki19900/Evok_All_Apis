package com.npst.evok.api.evok_apis.okhttp;

import com.npst.evok.api.evok_apis.pojo.Constants;
import com.squareup.okhttp.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OKHttpAccountVerificationUPIID {

    public String httpConfig(String req) throws IOException {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, req);
        Request request = new Request.Builder().url(Constants.TPV_UPI_ID).method("POST", body)
                .addHeader("cid", Constants.cid).addHeader("Content-Type", "text/plain").build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

}
