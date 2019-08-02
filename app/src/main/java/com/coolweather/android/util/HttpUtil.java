package com.coolweather.android.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback) {
        //创建OkHttpClient对象
        OkHttpClient client = new OkHttpClient();
        //创建Request对象
        Request request = new Request.Builder().url(address).build();
        //创建call对象  enqueue请求网络之后返回的callback
        client.newCall(request).enqueue(callback);
    }
}




