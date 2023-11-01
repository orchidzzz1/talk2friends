package com.example.frontend.ClientAPI;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;
import java.util.Objects;

public class API {
    private String BASE_URL = "";
    private String AUTH_TOKEN = "";
    public API(String base_url, String auth_token){
        BASE_URL = base_url;
        AUTH_TOKEN = auth_token;
    }

    public JSONObject makeGetRequest(String endpoint, JSONObject params) throws JSONException {
        OkHttpClient client = new OkHttpClient();
        String apiUrl = this.BASE_URL + endpoint;
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(apiUrl)).newBuilder();
        for (Iterator<String> it = params.keys(); it.hasNext(); ) {
            String key = it.next();
            urlBuilder.addQueryParameter(key, (String) params.get(key));
        }
        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + this.AUTH_TOKEN)
                .build();
        try {
            Response response = client.newCall(request).execute();
//            if(response.code == 401){
//                // unauthorized and need to authorize again
//            }
            assert response.body() != null;
            return processJsonResponse(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }


    public JSONObject makePostRequest(String endpoint, JSONObject params) {
        OkHttpClient client = new OkHttpClient();
        String apiUrl = this.BASE_URL + endpoint;
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody requestBody = RequestBody.create((String) params.toString(), JSON);
        Request request = new Request.Builder()
                .url(apiUrl)
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + this.AUTH_TOKEN)
                .build();
        try {
            Response response = client.newCall(request).execute();
//            if(response.code == 401){
//                // unauthorized and need to authorize again
//            }
            assert response.body() != null;
            return processJsonResponse(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    private static JSONObject processJsonResponse(String jsonResponse) {
        try {
            // Parse JSON response
            JSONObject jsonRes = new JSONObject(jsonResponse);
            return jsonRes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
