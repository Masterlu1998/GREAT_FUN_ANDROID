package com.example.great_fun_http.httpHelper;

import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpApiHelper {
    // 获取http接口函数
    public static String getApiData(String params, String apiUrl) {
        Log.d("接口入参", params);
        String jsonResult = "";
        URL url = null;
        BufferedReader reader = null;
        try {

            url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
//            connection.setRequestProperty("Content-Length", String.valueOf(params.length()));
            connection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
            connection.setRequestProperty("accept","application/json");

            OutputStream out = connection.getOutputStream();
            if (params != null) {
                out.write(params.getBytes());
            }
            out.flush();
            out.close();

            if (connection.getResponseCode()==HttpURLConnection.HTTP_OK){

                reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                jsonResult = reader.readLine();

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d("活动接口返回结果",jsonResult);
        return jsonResult;
    }

}

