package com.coolweather.android.Interface;


import com.coolweather.android.GSON.Weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WeatherServices {
//    String requestWeaWeb = "https://free-api.heweather.net/s6/weather/" + weathertype + "?location=" + city_name.getText() + "&key=" + web_api_key;

    @GET("s6/weather/{weathertype}")
    //其中写的是Url中主机名后面的地址
    Call<Weather> getWeather(@Path("weathertype") String weathertype,
                             @Query("location") String location,
                             @Query("key") String key);

}