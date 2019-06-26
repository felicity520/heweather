package com.coolweather.android.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.coolweather.android.GSON.Weather;
import com.coolweather.android.R;
import com.coolweather.android.activity.MainActivity;
import com.coolweather.android.adapter.RecyclerViewAdapter;
import com.coolweather.android.util.HttpUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * onAttach方法：Fragment和Activity建立关联的时候调用。
 * onCreateView方法：为Fragment加载布局时调用。
 * onActivityCreated方法：当Activity中的onCreate方法执行完后调用。
 * onDestroyView方法：Fragment中的布局被移除时调用。
 * onDetach方法：Fragment和Activity解除关联的时候调用。
 */
public class WeatherFragment extends Fragment {

    String JSONWea;//从网络返回的数据，定义成全局变量，方便多次调用

    private List<String> list;

    public final String web_api_key = "b8e486f16f4d4973ae19f46872184afc";
    String weathertype = "forecast";//now:实况天气  forecast:3-10天预报 hourly:逐小时预报  lifestyle	:生活指数

    // 常规天气数据:https://free-api.heweather.net/s6/weather/{weather-type}?{parameters}   forecast是指3-10天预报  location和key是必选项
    // basic，update和status是基本参数

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_recycler_view, container, false);
        initData();

        //从服务器获取数据
        TextView city_name = view.findViewById(R.id.city_name);
        Log.i("yyy", "city_name.toString()-----------: " + city_name.getText());
        String requestWeaWeb = "https://free-api.heweather.net/s6/weather/" + weathertype + "?location=" + city_name.getText() + "&key=" + web_api_key;
        getWeaFromSer(requestWeaWeb);

        //将获取到的数据加载出来
        initView(view);
        return view;
    }

    private void initView(View view) {

        RecyclerView recyclerView = view.findViewById(R.id.rv);
        // 设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        // 设置Item添加和移除的动画
        DefaultItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setItemAnimator(itemAnimator);
        itemAnimator.setSupportsChangeAnimations(false);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), list);
        recyclerView.setAdapter(adapter);
    }


    private void initData() {
        list = new ArrayList<>();
//        演示getWeaFromSer还没有收到数据，view就已经加载出来了
//        for (int i = 0; i <= 20; i++) {
//            list.add("Item " + i);
//        }
    }


    public void getWeaFromSer(String host) {
        HttpUtil.sendOkHttpRequest(host, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JSONWea = response.body().string();
                Log.i("yyy", "从服务器获取天气成功-----------: ");
                Log.i("yyy", "JSONWea--------: " + JSONWea + "\n");
                parseJSONWithGSON(JSONWea);


            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("yyy", "从服务器获取天气失败-----------: ");
                e.printStackTrace();
            }
        });
    }


    //方法二：使用GSON
    public void parseJSONWithGSON(String JsonData) {
        Gson gson = new Gson();
        Weather wetherBean = gson.fromJson(JsonData, Weather.class);
        Log.i("yyy", "parseJSONWithGSON---------------: " + wetherBean.getHeWeather6());
        List<Weather.HeWeather6Bean> HeWeather6Bean = wetherBean.getHeWeather6();
        for (Weather.HeWeather6Bean heWealist : HeWeather6Bean) {
            Log.i("yyy", "status------------: " + heWealist.getStatus() + "\n");
            List<Weather.HeWeather6Bean.DailyForecastBean> mdaily_forecast = heWealist.getDaily_forecast();
            for (int i = 0; i <= 2; i++) {
                mdaily_forecast.get(i);
                list.add("预报第" + (i + 1) + "天的数据：" + "\n" + "时间：" + mdaily_forecast.get(i).getDate() + "\n" +
                        "最高温：" + mdaily_forecast.get(i).getTmp_max() + "\n" +
                        "最低温：" + mdaily_forecast.get(i).getTmp_min() + "\n" +
                        "白天天气状况：" + mdaily_forecast.get(i).getCond_txt_d() + "\n" +
                        "晚上天气状况：" + mdaily_forecast.get(i).getCond_txt_n() + "\n");
                Log.i("yyy", "list----------: " + list.get(i));

            }
        }
    }







}



