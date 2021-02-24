package com.example.android.lifecycleweather.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class FiveDayForecast implements Serializable {
    @SerializedName("list")
    private ArrayList<ForecastData> forecastDataList;

    @SerializedName("city")
    private ForecastCity forecastCity;

    public FiveDayForecast() {
        this.forecastDataList = null;
        this.forecastCity = null;
    }
    public FiveDayForecast(ArrayList<ForecastData> forecastDataList, ForecastCity forecastCity){
        this.forecastCity = forecastCity;
        this.forecastDataList = forecastDataList;
    }

    public ArrayList<ForecastData> getForecastDataList() {
        return forecastDataList;
    }

    public ForecastCity getForecastCity() {
        return forecastCity;
    }
}
