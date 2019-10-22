package com.coolweather.android.util;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {

    /**
     * OkHttp GET 加载百度网页
     * 注意：9.0以上HTTP请求网页不能用明文。解决：android:usesCleartextTraffic="true"
     * 重点：okHttp3和HttpURLConnection的网络请求和回调都在子线程
     * 说明：enqueue会自动创建子线程。因为线程中无法返回服务器的响应数据，所以使用okhttp3.Callback的回调机制
     * 搭建web服务器：Apache
     */
    public void sendRequestWithOkHttp(final String address, final okhttp3.Callback callback) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(callback);
    }

    public static void sendOkHttpRequest(String address,okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }

    //提交homebrach分支到master


}

