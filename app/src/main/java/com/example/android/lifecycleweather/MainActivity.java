package com.example.android.lifecycleweather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.lifecycleweather.data.FiveDayForecast;
import com.example.android.lifecycleweather.data.ForecastCity;
import com.example.android.lifecycleweather.data.ForecastData;
import com.example.android.lifecycleweather.data.LoadingStatus;
import com.example.android.lifecycleweather.utils.OpenWeatherUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements ForecastAdapter.OnForecastItemClickListener, SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    /*
     * To use your own OpenWeather API key, create a file called `gradle.properties` in your
     * GRADLE_USER_HOME directory (this will usually be `$HOME/.gradle/` in MacOS/Linux and
     * `$USER_HOME/.gradle/` in Windows), and add the following line:
     *
     *   OPENWEATHER_API_KEY="<put_your_own_OpenWeather_API_key_here>"
     *
     * The Gradle build for this project is configured to automatically grab that value and store
     * it in the field `BuildConfig.OPENWEATHER_API_KEY` that's used below.  You can read more
     * about this setup on the following pages:
     *
     *   https://developer.android.com/studio/build/gradle-tips#share-custom-fields-and-resource-values-with-your-app-code
     *
     *   https://docs.gradle.org/current/userguide/build_environment.html#sec:gradle_configuration_properties
     *
     * Alternatively, you can just hard-code your API key below 🤷‍.  If you do hard code your API
     * key below, make sure to get rid of the following line (line 18) in build.gradle:
     *
     *   buildConfigField("String", "OPENWEATHER_API_KEY", OPENWEATHER_API_KEY)
     */
    private static final String OPENWEATHER_APPID = "jqfTKLy7wfm6ANMOWLx4Gt0bFhQDZ28n9rqHfJxz";
    private static final String FORECAST_RESULTS_KEY = "MainActivity.forecastResults";
    private ForecastAdapter forecastAdapter;
    private ForecastViewModel forecastViewModel;
    private SharedPreferences sharedPreferences;
    private int test = 0;
    private RequestQueue requestQueue;

    private ForecastCity forecastCity;

    private RecyclerView forecastListRV;
    private ProgressBar loadingIndicatorPB;
    private TextView errorMessageTV;

    private Toast errorToast;

    private FiveDayForecast forecast;

    private String savedUnits;
    private String savedQuery;
    private URL imageUrl;
    private Bitmap bitmap;
    private String correctUrl;

//    private String date = "2020-01-01";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.loadingIndicatorPB = findViewById(R.id.pb_loading_indicator);
        this.errorMessageTV = findViewById(R.id.tv_error_message);
        this.forecastListRV = findViewById(R.id.rv_forecast_list);
        this.forecastListRV.setLayoutManager(new LinearLayoutManager(this));
        this.forecastListRV.setHasFixedSize(true);
        if (test == 0) {
            this.forecastAdapter = new ForecastAdapter(this);
            this.forecastListRV.setAdapter(this.forecastAdapter);
        }
        this.requestQueue = Volley.newRequestQueue(this);

        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.sharedPreferences.registerOnSharedPreferenceChangeListener(this);
//        savedUnits = sharedPreferences.getString("pref_units", "Metric");
        savedQuery = sharedPreferences.getString("pref_city", "2020-10-10");

        this.forecastViewModel = new ViewModelProvider(this)
                .get(ForecastViewModel.class);

        this.forecastViewModel.getForecastResults().observe(this,
                new Observer<FiveDayForecast>() {
                    @Override
                    public void onChanged(FiveDayForecast fiveDayForecast) {
                        if (fiveDayForecast != null) {
                            String units="F";

                            if(fiveDayForecast != null) {
                                forecastAdapter.updateForecastData(fiveDayForecast, units);
                                if (fiveDayForecast.getThumbnailUrl() != null) {
//                                        imageUrl = new URL(fiveDayForecast.getThumbnailUrl());
                                    Log.d(TAG, "debugging: " + fiveDayForecast.getThumbnailUrl());
                                    } else{
//                                        imageUrl = new URL(fiveDayForecast.getUrl());
                                    Log.d(TAG, "debugging: " + fiveDayForecast.getUrl());
                                    }
//                                URL imageUrl = null;
//                                String correctUrl;

                                try {
                                    if (fiveDayForecast.getThumbnailUrl() != null) {
                                        imageUrl = new URL(fiveDayForecast.getThumbnailUrl());
                                        correctUrl = fiveDayForecast.getThumbnailUrl();
                                    } else {
                                        imageUrl = new URL(fiveDayForecast.getUrl());
                                        correctUrl = fiveDayForecast.getUrl();

                                    }
                                } catch(MalformedURLException ex){
//
                                }
                                GetImageBitMap getImageBitMap = new GetImageBitMap();
//                                Bitmap bitmap;
//                                if (imageUrl != null) {
//                                try{
//                                   bitmap = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
                                    getImageBitMap.execute(correctUrl);
                                    bitmap = getImageBitMap.getBitmap();
                                    Log.d(TAG, "bitmapURL  " + correctUrl);

//                                } catch (IOException ioE){
//
//                                }
                                FileOutputStream outStream = null;
//
//// Write to SD Card
                                Log.d(TAG,"Debug2" + Environment.getExternalStorageState());

                                try {
                                    File sdCard = Environment.getExternalStorageDirectory();
                                    File dir = new File(sdCard.getAbsolutePath() + "/camtest");
                                    dir.mkdirs();

                                    String fileName = String.format("%d.jpg", System.currentTimeMillis());
                                    File outFile = new File(dir, fileName);

                                    outStream = new FileOutputStream(outFile);
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                                    outStream.flush();
                                    outStream.close();

                                    Log.d(TAG, "onPictureTaken - wrote to " + outFile.getAbsolutePath());

//                                    refreshGallery(outFile);

                                } catch (FileNotFoundException e) {
                                    Log.d(TAG,"FNF");
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } finally {

                                }
                                test = 1;
                            }
                        }
                    }
                });
        this.forecastViewModel.getLoadingStatus().observe(this, new Observer<LoadingStatus>() {
            @Override
            public void onChanged(LoadingStatus loadingStatus) {
                if (loadingStatus == LoadingStatus.LOADING){
                    loadingIndicatorPB.setVisibility(View.VISIBLE);
                } else if (loadingStatus == LoadingStatus.SUCCESS){
                    loadingIndicatorPB.setVisibility(View.INVISIBLE);
                    forecastListRV.setVisibility(View.VISIBLE);
                    errorMessageTV.setVisibility(View.INVISIBLE);
                    ActionBar actionBar = getSupportActionBar();
//                    actionBar.setTitle(forecastCity.getName());
                } else {
                    loadingIndicatorPB.setVisibility(View.INVISIBLE);
                    forecastListRV.setVisibility(View.INVISIBLE);
                    errorMessageTV.setVisibility(View.VISIBLE);

                }

    //
    //                    ActionBar actionBar = getSupportActionBar();
    //                    actionBar.setTitle(forecastCity.getName());
            }
        });

        forecastViewModel.loadForecastResults(OPENWEATHER_APPID, "True", this.sharedPreferences.getString("pref_city", "2020-10-10"));

//        this.fetchFiveDayForecast("Corvallis,OR,US", "imperial");

//        forecastViewModel.loadForecastResults(OPENWEATHER_APPID, "True");
//        this.forecastViewModel.getForecastResults().observe(this,
//                new Observer<FiveDayForecast>() {
//                    @Override
//                    public void onChanged(FiveDayForecast fiveDayForecast) {
//                        if (fiveDayForecast != null) {
//                            String units;
//                            if (savedUnits.equals("Imperial")){
//                                units = "F";
//                            } else if (savedUnits.equals("Metric")){
//                                units = "C";
//                            } else {
//                                units = "K";
//                            }
////                            forecastViewModel.getForecastResults();
//                            Log.d(TAG, "adding one picture");
//                            if(fiveDayForecast!=null) {
//                                forecastAdapter.updateForecastData(fiveDayForecast, units);
//                            }
//                        }
////                        forecastCity = forecast.getForecastCity();
//                    }
//                });

        //        if (savedInstanceState != null && savedInstanceState.containsKey(FORECAST_RESULTS_KEY)){
//            this.forecast = (FiveDayForecast) savedInstanceState.getSerializable(FORECAST_RESULTS_KEY);
//            this.forecastAdapter.updateForecastData(this.forecast);
//        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d(TAG, "shared preference changed, key: " + key + ", value: " + sharedPreferences.getString(key, ""));
        forecastViewModel.loadForecastResults(OPENWEATHER_APPID, "True", this.sharedPreferences.getString("pref_city", "2020-10-10"));

    }

//    public static Bitmap getBitmapFromURL(String src) {
//        try {
//            URL url = new URL(src);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setDoInput(true);
//            connection.connect();
//            InputStream input = connection.getInputStream();
//            GetImageBitMap getImageBitMap
//            Bitmap myBitmap = getImageBitMap.
//            return myBitmap;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
    @Override
    public void onForecastItemClick(FiveDayForecast fiveDayForecast) {
        Intent intent = new Intent(this, ForecastDetailActivity.class);
        if (fiveDayForecast.getThumbnailUrl() == null) {
            intent.putExtra(ForecastDetailActivity.EXTRA_URL, fiveDayForecast.getUrl());
        } else {
            intent.putExtra(ForecastDetailActivity.EXTRA_THUMB_URL, fiveDayForecast.getThumbnailUrl());
        }
        intent.putExtra(ForecastDetailActivity.EXTRA_EXPLANATION, fiveDayForecast.getExplanation());
        intent.putExtra(ForecastDetailActivity.EXTRA_TITLE, fiveDayForecast.getTitle());
        startActivity(intent);
        Log.d(TAG, "clicked on tag, printing explanation and title from main: " + fiveDayForecast.getTitle() + " , " + fiveDayForecast.getExplanation());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this,SettingsActivity.class);
                startActivity(intent);
                return true;
//            case R.id.action_map:
//                viewForecastCityInMap();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This function uses Volley to fetch the 5 day/3 hour forecast from the OpenWeather API for
     * a specified city.  Updates this activity's UI to reflect the status of the request to the
     * OpenWeather API.  For more information about Volley, see the documentation here:
     *
     *   https://developer.android.com/training/volley
     *
     * @param city The name of the city for which to fetch the forecast, e.g. "Corvallis,OR,US".
     * @param units String specifying the type of units of measurement to fetch.  Can be
     *              "imperial", "metric", or "standard".
     */
//    private void fetchFiveDayForecast(String city, String units) {
//        String forecastUrl = OpenWeatherUtils.buildFiveDayForecastUrl(city, units, OPENWEATHER_APPID);
//        Log.d(TAG, "Request URL: " + forecastUrl);
//
//        StringRequest req = new StringRequest(
//                Request.Method.GET,
//                forecastUrl,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        forecast =
//                                OpenWeatherUtils.parseFiveDayForecastResponse(response);
//                        forecastAdapter.updateForecastData(forecast);
//                        forecastCity = forecast.getForecastCity();
//
//                        forecastListRV.setVisibility(View.VISIBLE);
//                        loadingIndicatorPB.setVisibility(View.INVISIBLE);
//                        errorMessageTV.setVisibility(View.INVISIBLE);
//
//                        ActionBar actionBar = getSupportActionBar();
//                        actionBar.setTitle(forecastCity.getName());
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        error.printStackTrace();
//                        errorMessageTV.setText(getString(R.string.loading_error, error.getMessage()));
//                        errorMessageTV.setVisibility(View.VISIBLE);
//                        loadingIndicatorPB.setVisibility(View.INVISIBLE);
//                        forecastListRV.setVisibility(View.INVISIBLE);
//                    }
//                }
//        );
//        loadingIndicatorPB.setVisibility(View.VISIBLE);
//        this.requestQueue.add(req);
//    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
    }

//    @Override
//    protected void onSaveInstanceState(@NonNull Bundle outState) {
//        Log.d(TAG, "onSaveInstance");
//        if (this.forecast != null){
//            outState.putSerializable(FORECAST_RESULTS_KEY, this.forecast);
//        }
//        super.onSaveInstanceState(outState);
//    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        savedUnits = sharedPreferences.getString("pref_units", "Metric");
        savedQuery = sharedPreferences.getString("pref_city", "Corvallis,OR,US");
//        forecastViewModel.loadForecastResults(OPENWEATHER_APPID, "True");

        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        this.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        Log.d(TAG, "onDestroy");

        super.onDestroy();
    }


    // How to get bitmap in another thread - https://stackoverflow.com/questions/37060782/cannot-convert-url-to-bitmap-in-android
    class GetImageBitMap extends AsyncTask<String, Void, Bitmap> {

        private Bitmap bitmap;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        public Bitmap doInBackground(String... urls) {
            Bitmap map = null;
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection =(HttpURLConnection)url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                map= BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.bitmap = map;

            return map;
        }

        public Bitmap getBitmap(){
            return this.bitmap;
        }
        protected void onPostExecute(Bitmap bMap) {
            try {
                if (!isCancelled()) {
                    if (bMap != null) {
                        //set your image view here.
                    }
                }
            }catch (Exception exception){
                exception.printStackTrace();
            }
        }

    }

    /**
     * This function uses an implicit intent to view the forecast city in a map.
     */
//
//    private void viewForecastCityInMap() {
//        if (this.forecastCity != null) {
//            Uri forecastCityGeoUri = Uri.parse(getString(
//                    R.string.geo_uri,
//                    this.forecastCity.getLatitude(),
//                    this.forecastCity.getLongitude(),
//                    12
//            ));
//            Intent intent = new Intent(Intent.ACTION_VIEW, forecastCityGeoUri);
//            try {
//                startActivity(intent);
//            } catch (ActivityNotFoundException e) {
//                if (this.errorToast != null) {
//                    this.errorToast.cancel();
//                }
//                this.errorToast = Toast.makeText(
//                        this,
//                        getString(R.string.action_map_error),
//                        Toast.LENGTH_LONG
//                );
//                this.errorToast.show();
//            }
//        }
//    }
}