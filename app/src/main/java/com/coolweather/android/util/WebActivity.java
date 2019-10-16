package com.coolweather.android.util;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.coolweather.android.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WebActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "WebActivity";

    private TextView scroll_view;//scroll_view以滚动的形式

    private Button btn_sendRequest;
    private WebView web_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        initView();
//        initWebView();
//        initHttpURLConnection();


    }


    /**
     * OkHttp加载百度网页
     * 注意：9.0以上HTTP请求网页不能用明文。解决：android:usesCleartextTraffic="true"
     */
    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient mOkHttpClient = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://www.baidu.com")
                            .build();
                    Response mResponse = mOkHttpClient.newCall(request).execute();
                    String str = mResponse.body().string();
                    showResponse(str);
                } catch (
                        IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }


    /**
     * HttpURLConnection详解
     */
    private void initHttpURLConnection() {
//        android原生支持HTTP协议的两种方式：HttpURLConnection(建议)和HttpClient(废弃)
//        这里介绍HttpURLConnection:网络请求都是在子线程的，不允许在主线程做耗时操作
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;//BufferedReader是缓冲字符输入流
                try {
                    URL url = new URL("https://www.baidu.com");
                    connection = (HttpURLConnection) url.openConnection();//得到HttpURLConnection实例
                    connection.setRequestMethod("GET");//HTTP请求所用的方法：GET和POST。GET表示希望从服务器那里获取数据，而POST则表示希望提交数据给服务器。
                    connection.setConnectTimeout(8000);//连接超时时间
                    connection.setReadTimeout(8000);//读取超时的毫秒数
                    InputStream in = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));//将外部的数据写入内存
                    StringBuilder reponse = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        reponse.append(line);
                    }
                    showResponse(reponse.toString());

                    //号外
//                    如果使用POST方法，在获取输入流之前把要提交的数据写出即可，注意每条数据都要以键值对的形式存在，数据与数据之间用“&”符号隔开
//                    DataOutputStream数据输出流允许应用程序将java基本数据类型写入到底层
//                    connection.setRequestMethod("POST");
//                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
//                    out.writeBytes("username=admin&password=123456");
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
                    if (connection != null) {
                        connection.disconnect();//关闭HTTP连接
                    }
                }
            }

        }).start();
    }


    //    必须在主线程更新UI，这里要进行线程切换
    private void showResponse(final String toString) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                scroll_view.setText(toString);
            }
        });
    }


    /**
     * WebView加载百度网页
     */
    private void initWebView() {
        web_view.getSettings().setJavaScriptEnabled(true);  //设置WebView属性，能够支持Javascript脚本
        //如果没有设置WebViewClient实例，那么就会由系统默认浏览器来处理url，就是跳转到百度的首页
        //如果设置了WebViewClient，就让应用来处理这个url，即在应用的界面加载百度首页
        web_view.setWebViewClient(new WebViewClient());
        web_view.loadUrl("http://baidu.com");//加载需要显示的网页
    }

    private void initView() {
        btn_sendRequest = (Button) findViewById(R.id.btn_sendRequest);
        btn_sendRequest.setOnClickListener(this);

        //web_view使用的协议：HTTP
        //webView已经在后台帮我们处理好了发送HTTP请求，接收服务器响应，解析返回数据，最终界面显示。
        web_view = (WebView) findViewById(R.id.web_view);

//        ScrollView的子元素只能有一个，本文是textview
        scroll_view = (TextView) findViewById(R.id.scrollview_text);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sendRequest:
//                sendRequestWithOkHttp();
                HttpUtil mHttpUtil = new HttpUtil();
                mHttpUtil.sendRequestWithOkHttp("http://www.baidu.com", new okhttp3.Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e(TAG, "onFailure: " + e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();
                        Log.i(TAG, "onResponse: " + responseData);
                        showResponse(responseData);
                    }
                });
                break;
        }
    }

    public void onDestroy() {
        super.onDestroy();
    }


}
