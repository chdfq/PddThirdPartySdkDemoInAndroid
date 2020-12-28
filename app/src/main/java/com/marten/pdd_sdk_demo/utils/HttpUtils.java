package com.marten.pdd_sdk_demo.utils;

import com.marten.pdd_sdk_demo.domain.ResultData;
import com.marten.pdd_sdk_demo.tools.JsonTool;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtils {

    private static OkHttpClient okHttpClient;

    private HttpUtils() {
        okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .callTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    private static HttpUtils instance;

    public static void getInstance() {
        if (instance == null) {
            instance = new HttpUtils();
        }
    }

    public static void get(String url, Map<String, Object> map, HttpCallBack callback) {
        getInstance();
        StringBuilder builder = new StringBuilder(url).append("?");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            builder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }

        Request request = new Request.Builder()
                .get()
                .url(builder.toString().substring(0, builder.length() - 1)) //去掉最后多的一个&符号
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.onFail(e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                callback.onSuccess(JsonTool.jsonToObject(response.body().string(), ResultData.class));
            }
        });
    }

    public static void post(String url, Map<String, Object> map, HttpCallBack callback) {
        getInstance();

        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            builder.add(entry.getKey(), String.valueOf(entry.getValue()));
        }
        FormBody formBody = builder.build();

        Request request = new Request.Builder()
                .post(formBody)
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.onFail(e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                callback.onSuccess(JsonTool.jsonToObject(response.body().string(), ResultData.class));
            }
        });
    }

    public interface HttpCallBack {
        void onSuccess(ResultData resultData);

        void onFail(String error);
    }
}
