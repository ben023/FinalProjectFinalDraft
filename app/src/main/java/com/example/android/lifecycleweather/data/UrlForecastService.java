package com.example.android.lifecycleweather.data;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UrlForecastService {
    @GET("planetary/apod")
//    @GET("/data/2.5/forecast?appid=9a48a75cd427d57d80ffbd74e9147d07")
    Call<UrlFiveDayForecast> getUrlForecast(@Query("api_key") String OPENWEATHER_APPID, @Query("thumbs") String thumbs, @Query("date") String date);
}
