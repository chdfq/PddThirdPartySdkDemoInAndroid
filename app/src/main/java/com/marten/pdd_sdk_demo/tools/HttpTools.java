package com.marten.pdd_sdk_demo.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpTools {
    //OkHttp
    //xUtils
    //HttpURLConnection *post *get put delete
    public static void getData(String path, HttpBackListener backListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                StringBuilder sb = new StringBuilder();
                try {
                    //创建URL
                    URL url = new URL(path);
                    //获取HttpURLConnection 对象
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    //connection 配置
                    connection.setConnectTimeout(5000); //超时时间 毫秒
                    connection.setReadTimeout(5000); //读超时时 毫秒

                    //connection 配置请求数据结构
                    connection.setRequestProperty("Content-type", "application/x-java-serialized-object");

                    //connection 配置请求方式
                    connection.setRequestMethod("GET");

                    //HttpURLConnection 发起连接
                    connection.connect();

                    //HttpURLConnection 发起请求
                    //OutputStream outputStream = connection.getOutputStream(); //到输出流，getOutputStream会隐含的进行链接

                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream = connection.getInputStream();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        String temp;
                        while ((temp = bufferedReader.readLine()) != null) {
                            sb.append(temp);
                        }
                        bufferedReader.close();
                        backListener.onSuccess(sb.toString(), connection.getResponseCode());
                    } else {
                        //失败
                        backListener.onError(connection.getResponseMessage(), connection.getResponseCode());
                    }
                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void postData(String path, HttpBackListener backListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                StringBuilder sb = new StringBuilder();
                try {
                    //创建URL
                    URL url = new URL(path);
                    //获取HttpURLConnection 对象
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    //connection 配置
                    connection.setConnectTimeout(5000); //超时时间 毫秒
                    connection.setReadTimeout(5000); //读超时时 毫秒

                    //*post
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setUseCaches(false);

                    //connection 配置请求方式 *post
                    connection.setRequestMethod("POST");

                    //HttpURLConnection 发起连接
                    connection.connect();

                    String body = "type=guonei&key=c60ab75f8ce6994bc6a06ea5adb617db";
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8)
                    );
                    writer.write(body);
                    writer.close();

                    //HttpURLConnection 发起请求
                    //OutputStream outputStream = connection.getOutputStream(); //到输出流，getOutputStream会隐含的进行链接

                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream = connection.getInputStream();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        String temp;
                        while ((temp = bufferedReader.readLine()) != null) {
                            sb.append(temp);
                        }
                        bufferedReader.close();
                        backListener.onSuccess(sb.toString(), connection.getResponseCode());
                    } else {
                        //失败
                        backListener.onError(connection.getResponseMessage(), connection.getResponseCode());
                    }
                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public interface HttpBackListener {
        void onSuccess(String data, int code);

        void onError(String errorMes, int code);
    }

}
