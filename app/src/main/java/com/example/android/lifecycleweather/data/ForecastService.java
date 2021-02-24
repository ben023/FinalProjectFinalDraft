package com.example.android.lifecycleweather.data;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ForecastService {
    @GET("/data/2.5/forecast?appid=9a48a75cd427d57d80ffbd74e9147d07")
    Call<FiveDayForecast> getForecast(@Query("q") String query, @Query("units") String units);
}
