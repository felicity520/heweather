package com.coolweather.android.fragment;


import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;

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

import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.Date;

//此处之前有个导包出错误，Call、Callback、Response导入了okhttp的包
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.widget.TextView;
import com.coolweather.android.GSON.Weather;
import com.coolweather.android.Interface.WeatherServices;
import com.coolweather.android.R;
import com.coolweather.android.adapter.RecyclerViewAdapter;
import java.util.ArrayList;
import java.util.List;


/**
 * onAttach方法：Fragment和Activity建立关联的时候调用。
 * onCreateView方法：为Fragment加载布局时调用。
 * onActivityCreated方法：当Activity中的onCreate方法执行完后调用。
 * onDestroyView方法：Fragment中的布局被移除时调用。
 * onDetach方法：Fragment和Activity解除关联的时候调用。
 */
public class WeatherFragment extends Fragment {


    private ImageView glideImage;
    private View mView;

    private List<String> forecastList;

    TextView city_name;

    private List<String> list;


    public final String web_api_key = "b8e486f16f4d4973ae19f46872184afc";
    String weathertype = "forecast";//now:实况天气  forecast:3-10天预报 hourly:逐小时预报  lifestyle	:生活指数

    // 常规天气数据:https://free-api.heweather.net/s6/weather/{weather-type}?{parameters}   forecast是指3-10天预报  location和key是必选项
    // basic，update和status是基本参数

//    String requestWeaWeb = "https://free-api.heweather.net/s6/weather/" + weathertype + "?location=" + city_name.getText() + "&key=" + web_api_key;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.activity_recycler_view, container, false);
        city_name = mView.findViewById(R.id.city_name);
        Log.i("yyy", "city_name.toString()-----------: " + city_name.getText());

        glideImage = mView.findViewById(R.id.glideImage);
        getDailyImage();

        getDataFromServer();

        return mView;
    }

    private void getDailyImage() {
        Log.i("yyy", "loading...  image");
        String url = "http://cn.bing.com/az/hprichbg/rb/Dongdaemun_ZH-CN10736487148_1920x1080.jpg";
        Glide.with(this).load(url).into(glideImage);
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


//    private void initView(View view) {
//        View view = inflater.inflate(R.layout.activity_recycler_view, container, false);
//        initData();
//
//        //从服务器获取数据
//        TextView city_name = view.findViewById(R.id.city_name);
//        Log.i("yyy", "city_name.toString()-----------: " + city_name.getText());
//        String requestWeaWeb = "https://free-api.heweather.net/s6/weather/" + weathertype + "?location=" + city_name.getText() + "&key=" + web_api_key;
//        getWeaFromSer(requestWeaWeb);
//
//        //将获取到的数据加载出来
//        initView(view);
//        return view;
//    }

    private void initViewAfter(View view) {

        RecyclerView recyclerView = view.findViewById(R.id.rv);
        // 设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        // 设置Item添加和移除的动画
        DefaultItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setItemAnimator(itemAnimator);
        itemAnimator.setSupportsChangeAnimations(false);

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), forecastList);
        recyclerView.setAdapter(adapter);
        Log.i("yyy", "view加载结束");
    }


    /**
     * 从和风天气服务端获取数据，并显示在界面上
     * 问题：数据还未加载完但布局已经加载完毕
     * 解决：将view作为全局变量，等数据加载完再去加载布局，Retrofit的回调会直接回到主线程，所以没必要用handler来做线程切换
     */
    private void getDataFromServer() {
        forecastList = new ArrayList<>();//别忘了创建
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://free-api.heweather.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherServices service = retrofit.create(WeatherServices.class);
        Call<Weather> call = service.getWeather(weathertype, city_name.getText().toString(), web_api_key);
        call.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                Log.i("yyy", "从服务器获取天气成功-----------: ");
                List<Weather.HeWeather6Bean> HeWeather6Bean = response.body().getHeWeather6();
                for (Weather.HeWeather6Bean heWealist : HeWeather6Bean) {
                    Log.i("yyy", "status------------: " + heWealist.getStatus() + "\n");
                    List<Weather.HeWeather6Bean.DailyForecastBean> mdaily_forecast = heWealist.getDaily_forecast();
                    for (int i = 0; i <= 2; i++) {
                        mdaily_forecast.get(i);
                        forecastList.add("预报第" + (i + 1) + "天的数据：" + "\n" + "时间：" + mdaily_forecast.get(i).getDate() + "\n" +
                                "最高温：" + mdaily_forecast.get(i).getTmp_max() + "\n" +
                                "最低温：" + mdaily_forecast.get(i).getTmp_min() + "\n" +
                                "白天天气状况：" + mdaily_forecast.get(i).getCond_txt_d() + "\n" +
                                "晚上天气状况：" + mdaily_forecast.get(i).getCond_txt_n() + "\n");
                        Log.i("yyy", "list----------: " + forecastList.get(i));
                    }
                }
                initViewAfter(mView);
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                Log.e("yyy", "从服务器获取天气失败-----------: ");
                t.printStackTrace();
            }
        });


    }


//    之前用OkHttp从网络上获取数据
//    public void getWeaFromSer(String host) {
//        HttpUtil.sendOkHttpRequest(host, new Callback() {
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                JSONWea = response.body().string();
//                Log.i("yyy", "从服务器获取天气成功-----------: ");
//                Log.i("yyy", "JSONWea--------: " + JSONWea + "\n");
//                parseJSONWithGSON(JSONWea);
//            }
//
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.i("yyy", "从服务器获取天气失败-----------: ");
//                e.printStackTrace();
//            }
//        });
//    }

    //方法二：使用GSON
//    public void parseJSONWithGSON(String JsonData) {
//        Gson gson = new Gson();
//        Weather wetherBean = gson.fromJson(JsonData, Weather.class);
//        Log.i("yyy", "parseJSONWithGSON---------------: " + wetherBean.getHeWeather6());
//        List<Weather.HeWeather6Bean> HeWeather6Bean = wetherBean.getHeWeather6();
//        for (Weather.HeWeather6Bean heWealist : HeWeather6Bean) {
//            Log.i("yyy", "status------------: " + heWealist.getStatus() + "\n");
//            List<Weather.HeWeather6Bean.DailyForecastBean> mdaily_forecast = heWealist.getDaily_forecast();
//            for (int i = 0; i <= 2; i++) {
//                mdaily_forecast.get(i);
//                list.add("预报第" + (i + 1) + "天的数据：" + "\n" + "时间：" + mdaily_forecast.get(i).getDate() + "\n" +
//                        "最高温：" + mdaily_forecast.get(i).getTmp_max() + "\n" +
//                        "最低温：" + mdaily_forecast.get(i).getTmp_min() + "\n" +
//                        "白天天气状况：" + mdaily_forecast.get(i).getCond_txt_d() + "\n" +
//                        "晚上天气状况：" + mdaily_forecast.get(i).getCond_txt_n() + "\n");
//                Log.i("yyy", "list----------: " + list.get(i));
//
//            }
//        }
//    }


//        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), list);
//        recyclerView.setAdapter(adapter);


//    private void initData() {
//        list = new ArrayList<>();
////        for (int i = 0; i <= 20; i++) {
////            list.add("Item " + i);
////        }
//    }






}



