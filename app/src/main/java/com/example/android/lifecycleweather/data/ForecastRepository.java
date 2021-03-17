package com.example.android.lifecycleweather.data;

import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForecastRepository {
//    private MutableLiveData<List<ForecastData>> forecastResults;

    private MutableLiveData<FiveDayForecast> forecastResults;
    private MutableLiveData<LoadingStatus> loadingStatus;
    private MutableLiveData<UrlFiveDayForecast> urlForecastResults;
//    private MutableLiveData<List<FiveDayForecast>> forecastList;
    private MutableLiveData<List<FiveDayForecast>> forecastList;


    private String currentDate;
    private String currentUnits;

    private static final String TAG = ForecastRepository.class.getSimpleName();
    private ForecastService forecastService;
    private UrlForecastService urlForecastService;

    private static final String BASE_URL = "https://api.nasa.gov";
    public Gson gson;

    public ForecastRepository() {
        this.forecastResults = new MutableLiveData<>();
        this.forecastList = new MutableLiveData<>();

        this.forecastList.setValue(null);
        this.forecastResults.setValue(null);
        this.urlForecastResults = new MutableLiveData<>();
        this.urlForecastResults.setValue(null);
//        this.currentQuery = null;
        this.loadingStatus = new MutableLiveData<>();
        this.loadingStatus.setValue(LoadingStatus.SUCCESS);

        this.gson = new GsonBuilder()
                .registerTypeAdapter(FiveDayForecast.class, new FiveDayForecast.JsonDeserializer())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        this.forecastService = retrofit.create(ForecastService.class);
        this.urlForecastService = retrofit.create(UrlForecastService.class);
    }

    public LiveData<FiveDayForecast> getForecastResults(){

        return this.forecastResults;
    }

    public LiveData<List<FiveDayForecast>> getForecastList(){

        return this.forecastList;
    }


    public LiveData<LoadingStatus> getLoadingStatus() {
        return this.loadingStatus;
    }

    public void loadForecastResults(String OPENWEATHER_APPID, String thumbs, String date) {
//        if (shouldExecuteForecast(date)) {
            Log.d(TAG, "getting new results for this date: ");
//            this.currentDate = date;
//            this.currentUnits = units;
            this.forecastResults.setValue(null);
            this.urlForecastResults.setValue(null);
            this.loadingStatus.setValue(LoadingStatus.LOADING);
//            Log.d(TAG, "running new forecast search for this query" + query + " and units " + units);
            Call<FiveDayForecast> thumbFiveDayForecastResults = this.forecastService.getForecast(OPENWEATHER_APPID, thumbs, date);
        Log.d(TAG, "this is date"+date);
            thumbFiveDayForecastResults.enqueue(new Callback<FiveDayForecast>() {
                @Override
                public void onResponse(Call<FiveDayForecast> call, Response<FiveDayForecast> response) {
                    if (response.code() == 200){
//
                        Log.d(TAG, "Response code 200");
//                        ArrayList<ForecastData> forecastDataList = new ArrayList<>();
//                        for (ForecastData data_item : response.body().getForecastDataList()) {
//                            forecastDataList.add(data_item);
//                        }

                        FiveDayForecast fiveDayForecast = new FiveDayForecast((double) System.currentTimeMillis(),null, response.body().getThumbnailUrl(), response.body().getExplanation(), response.body().getTitle());
//                        forecastResults.addUrl(response)
                        forecastResults.setValue(fiveDayForecast);
//                        forecastList.add(forecastResults);

//                    forecastResults.setValue(response.body().getForecastDataList());
//                        forecastResults.setValue(fiveDayForecast);
                        loadingStatus.setValue(LoadingStatus.SUCCESS);
//                    Log.d(TAG, "CITY: " + forecastResults.forecast)
                    } else {
                        loadingStatus.setValue(LoadingStatus.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<FiveDayForecast> call, Throwable t) {
                    Log.d("forecast", "using just url now");
                    Call<UrlFiveDayForecast> urlFiveDayForecastResults = urlForecastService.getUrlForecast(OPENWEATHER_APPID, thumbs, date);
                    urlFiveDayForecastResults.enqueue(new Callback<UrlFiveDayForecast>() {
                        @Override
                        public void onResponse(Call<UrlFiveDayForecast> call, Response<UrlFiveDayForecast> response) {
                            if(response.code() == 200){
//                                Log.d("forecast", String.valueOf(response.body()));
                                String url = response.body().getUrl();
                                FiveDayForecast fiveDayForecast = new FiveDayForecast((double) System.currentTimeMillis(), url, null, response.body().getExplanation(), response.body().getTitle());
                                forecastResults.setValue(fiveDayForecast);

                                loadingStatus.setValue(LoadingStatus.SUCCESS);
                            }
                        }

                        @Override
                        public void onFailure(Call<UrlFiveDayForecast> call, Throwable t) {
                            Log.d("forecast", "failed");
                        }
                    });

//                    Log.d(TAG, "Response code failed");
//                    loadingStatus.setValue(LoadingStatus.ERROR);
//                    t.printStackTrace();
                }
            });
//        } else {
//            Log.d(TAG, "using cached results for this query: " + query);
//        }

    }

//    private boolean shouldExecuteForecast(String query, String units){
//        return !TextUtils.equals(query, this.currentQuery) || this.loadingStatus.getValue() == LoadingStatus.ERROR || !TextUtils.equals(units, this.currentUnits);
//    }
}
