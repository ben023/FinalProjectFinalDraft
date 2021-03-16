package com.example.android.lifecycleweather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.lifecycleweather.data.ForecastCity;
import com.example.android.lifecycleweather.data.ForecastData;
import com.example.android.lifecycleweather.utils.OpenWeatherUtils;

import java.util.Calendar;

public class ForecastDetailActivity extends AppCompatActivity {
    public static final String EXTRA_URL = "ForecastDetailActivity.ForecastData";
    public static final String EXTRA_THUMB_URL = "ForecastDetailActivity.ForecastCity";
    public static final String EXTRA_EXPLANATION = "ForecastDetailActivity.ForecastExplanation";
    public static final String EXTRA_TITLE = "ForecastDetailActivity.ForecastTitle";

    private ForecastData forecastData = null;
    private ForecastCity forecastCity = null;

    private String url;
    private String thumbUrl;
    private String explanation;
    private String title;

    private SharedPreferences sharedPreferences;
    private String savedUnits;
    private String units;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        unit = sharedPreferences.

        setContentView(R.layout.activity_forecast_detail);

        Intent intent = getIntent();


//
        if (EXTRA_THUMB_URL != null) {
            if (intent != null && intent.hasExtra(EXTRA_URL)) {
                this.url = intent.getStringExtra(EXTRA_URL);
//                TextView forecastCityTV = findViewById(R.id.tv_forecast_city);
//            forecastCity = EXTRA
//                forecastCityTV.setText(this.forecastCity.getName());
            }
        } else {
            if (intent != null && intent.hasExtra(EXTRA_THUMB_URL)) {
                if (intent != null && intent.hasExtra(EXTRA_THUMB_URL)) {
                    this.thumbUrl = intent.getStringExtra(EXTRA_THUMB_URL);
//                    TextView forecastCityTV = findViewById(R.id.tv_forecast_city);
    //            forecastCity = EXTRA
//                    forecastCityTV.setText(this.forecastCity.getName());
                }
            }
        }


        if (intent != null && intent.hasExtra(EXTRA_EXPLANATION)) {
            this.explanation = intent.getStringExtra(EXTRA_EXPLANATION);
            TextView expTV = findViewById(R.id.tv_explanation);
//            forecastCity = EXTRA
            Log.d("Forecast Detail", "Explanation: " + this.explanation);


            expTV.setText(this.explanation);
//            expTV.setMovementMethod(new ScrollingMovementMethod());

        }

        if (intent != null && intent.hasExtra(EXTRA_TITLE)) {
            this.title = intent.getStringExtra(EXTRA_TITLE);
            TextView titleTV = findViewById(R.id.tv_title);
            Log.d("Forecast Detail", "Title: " + this.title);

//            forecastCity = EXTRA
            titleTV.setText(this.title);
//            titleTV.setMovementMethod(new ScrollingMovementMethod());

        }


            /*
             * Load forecast icon into ImageView using Glide: https://bumptech.github.io/glide/
             */
        if (this.thumbUrl != null ) {
            ImageView forecastIconIV = findViewById(R.id.iv_picture);
            Glide.with(this)
                    .load(this.thumbUrl)
                    .into(forecastIconIV);
        } else {
            ImageView forecastIconIV = findViewById(R.id.iv_picture);
            Glide.with(this)
                    .load(this.url)
                    .into(forecastIconIV);
        }
//            TextView forecastDateTV = findViewById(R.id.tv_forecast_date);
//            Calendar date = OpenWeatherUtils.dateFromEpochAndTZOffset(
//                    forecastData.getEpoch(),
//                    forecastCity.getTimezoneOffsetSeconds()
//            );
//            forecastDateTV.setText(getString(
//                    R.string.forecast_date_time,
//                    getString(R.string.forecast_date, date),
//                    getString(R.string.forecast_time, date)
//            ));

//            TextView lowTempTV = findViewById(R.id.tv_low_temp);
//            lowTempTV.setText(getString(R.string.forecast_temp, forecastData.getLowTemp(), this.units));
//
//            TextView highTempTV = findViewById(R.id.tv_high_temp);
//            highTempTV.setText(getString(R.string.forecast_temp, forecastData.getHighTemp(), this.units));
//
//            TextView popTV = findViewById(R.id.tv_pop);
//            popTV.setText(getString(R.string.forecast_pop, forecastData.getPop()));
//
//            TextView cloudsTV = findViewById(R.id.tv_clouds);
//            cloudsTV.setText(getString(R.string.forecast_clouds, forecastData.getCloudCoverage()));
//
//            TextView windTV = findViewById(R.id.tv_wind);
//            windTV.setText(getString(R.string.forecast_wind, forecastData.getWindSpeed(), "mph"));
//
//            ImageView windDirIV = findViewById(R.id.iv_wind_dir);
//            windDirIV.setRotation(forecastData.getWindDirDeg());
//
//            TextView forecastDescriptionTV = findViewById(R.id.tv_forecast_description);
//            forecastDescriptionTV.setText(forecastData.getShortDescription());
        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_forecast_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                shareForecastText();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This method uses an implicit intent to launch the Android Sharesheet to allow the user to
     * share the current forecast.
     */
    private void shareForecastText() {
        if (this.explanation != null && this.title != null) {
//            Calendar date = OpenWeatherUtils.dateFromEpochAndTZOffset(
//                    forecastData.getEpoch(),
//                    forecastCity.getTimezoneOffsetSeconds()
//            );
            String shareText = getString(
                    R.string.share_forecast_text,
                    this.title,
                    this.url
            );

            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            sendIntent.setType("text/plain");

            Intent chooserIntent = Intent.createChooser(sendIntent, null);
            startActivity(chooserIntent);
        }
    }
}