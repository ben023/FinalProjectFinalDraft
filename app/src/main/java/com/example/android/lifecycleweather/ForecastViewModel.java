package com.example.android.lifecycleweather;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.lifecycleweather.data.FiveDayForecast;
import com.example.android.lifecycleweather.data.ForecastRepository;
import com.example.android.lifecycleweather.data.LoadingStatus;

public class ForecastViewModel extends ViewModel {
    private ForecastRepository repository;
    private LiveData<FiveDayForecast> forecastResults;
    private LiveData<LoadingStatus> loadingStatus;

    public ForecastViewModel(){
        this.repository = new ForecastRepository();
        this.forecastResults = this.repository.getForecastResults();
        this.loadingStatus = this.repository.getLoadingStatus();
    }

    public LiveData<FiveDayForecast> getForecastResults() {
        return this.forecastResults;
    }

    public LiveData<LoadingStatus> getLoadingStatus() {
        return this.loadingStatus;
    }

    public void loadForecastResults(String OPENWEATHER_APPID, String thumbs, String date){
        this.repository.loadForecastResults(OPENWEATHER_APPID, thumbs, date);
    }
}
