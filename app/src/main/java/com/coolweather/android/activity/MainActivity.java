package com.coolweather.android.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.coolweather.android.GSON.Weather;
import com.coolweather.android.R;
import com.coolweather.android.util.HttpUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private final String web_api_key = "b8e486f16f4d4973ae19f46872184afc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initDate();

        //getWeaFromSer();

    }

    private void initDate() {

    }

    private void initView() {

    }


    public void getWeaFromSer() {
        String requestWeaWeb = "https://free-api.heweather.net/s6/weather/forecast?location=beijing&key=b8e486f16f4d4973ae19f46872184afc";
        // 常规天气数据:https://free-api.heweather.net/s6/weather/{weather-type}?{parameters}   forecast是指3-10天预报  location和key是必选项
        HttpUtil.sendOkHttpRequest(requestWeaWeb, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String JSONWea = response.body().string();
                Log.i("getWeaFromSer", "从服务器获取天气成功-----------: ");
                Log.i("getWeaFromSer", "bingPic--------: " + JSONWea + "\n");
                parseJSONWithGSON(JSONWea);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("getWeaFromSer", "从服务器获取天气失败-----------: ");
                e.printStackTrace();
            }
        });
    }


    //方法二：使用GSON
    private void parseJSONWithGSON(String JsonData) {
        Gson gson = new Gson();
        Weather wetherBean = gson.fromJson(JsonData, Weather.class);
        Log.i("yyy", "parseJSONWithGSON---------------: " + wetherBean.getHeWeather6());
        List<Weather.HeWeather6Bean> HeWeather6Bean = wetherBean.getHeWeather6();
        for (Weather.HeWeather6Bean heWealist : HeWeather6Bean) {
            Log.i("yyy", "status------------: " + heWealist.getStatus() + "\n");
            Log.i("yyy", "basic------------: " + heWealist.getBasic() + "\n");
            Log.i("yyy", "update------------: " + heWealist.getUpdate() + "\n");
            Log.i("yyy", "daily_forecast------------: " + heWealist.getDaily_forecast() + "\n");
        }
    }


}


