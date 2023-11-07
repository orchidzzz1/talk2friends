package com.example.frontend.ClientAPI;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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

            urlBuilder.addQueryParameter(key, params.get(key).toString());
        }
        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", this.AUTH_TOKEN)
                .build();
        CompletableFuture<JSONObject> f = new CompletableFuture<>();
        try {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println("Request failed: " + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    // Check if the response is successful (HTTP code 200-299)
                    if (response.isSuccessful()) {
                        // Handle the response data here
                        f.complete(processJsonResponse(response.body().string()));
                    } else {
                        System.out.println("Request failed with code: " + response.code());
                    }
                }
            });
            return f.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
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
                .addHeader("Authorization", this.AUTH_TOKEN)
                .build();
        CompletableFuture<JSONObject> f = new CompletableFuture<>();
        try {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println("Request failed: " + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    // Check if the response is successful (HTTP code 200-299)
                    if (response.isSuccessful()) {
                        // Handle the response data here
                        f.complete(processJsonResponse(response.body().string()));
                    } else {
                        System.out.println("Request failed with code: " + response.code());
                    }
                }
            });
            return f.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
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
