package com.example.android.lifecycleweather;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Calendar;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.request.target.CustomTarget;

public class ForecastDetailActivity extends AppCompatActivity {
    public static final String EXTRA_URL = "ForecastDetailActivity.ForecastData";
    public static final String EXTRA_THUMB_URL = "ForecastDetailActivity.ForecastCity";
    public static final String EXTRA_EXPLANATION = "ForecastDetailActivity.ForecastExplanation";
    public static final String EXTRA_TITLE = "ForecastDetailActivity.ForecastTitle";
    private static final String TAG = MainActivity.class.getSimpleName();

    private ForecastData forecastData = null;
    private ForecastCity forecastCity = null;

    private String url;
    private String thumbUrl;
    private String explanation;
    private String title;
    private Bitmap bitmap;
    private ImageView forecastIconIV;
    private String correctUrl;
    private FileOutputStream outStream;
    private SharedPreferences sharedPreferences;
    private String savedUnits;
    private String units;

    @SuppressLint("WrongThread")
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
            forecastIconIV = findViewById(R.id.iv_picture);
//            Glide.with(this)
//                    .load(this.thumbUrl)
//                    .into(forecastIconIV);
            correctUrl = thumbUrl;

        } else {
            forecastIconIV = findViewById(R.id.iv_picture);
//            Glide.with(this)
//                    .load(this.url)
//                    .into(forecastIconIV);
            correctUrl = url;

        }
        Log.d(TAG, "correctURL: " + correctUrl);
        Glide.with(this).asBitmap().load(correctUrl).into(new CustomTarget<Bitmap>() {
        @Override
        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
            bitmap = resource;
            forecastIconIV.setImageBitmap(resource);
            int w = resource.getWidth();
            int h = resource.getHeight();
            Log.d(TAG, "bitmap w and h : " + w + ", " + h);
            try {
                if (resource == null){
                    Log.d(TAG, "null bitmap");
                }
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File(sdCard.getAbsolutePath() + "/Download");
                dir.mkdirs();
//                Log.d(TAG, "Debug3");
                String fileName = String.format("%d.jpg", System.currentTimeMillis());

                File outFile = new File(dir, fileName);
//                Log.d(TAG, "Adding to filename: " + fileName);

                outStream = new FileOutputStream(outFile);
//                Log.d(TAG, "Debug5");

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                outStream.flush();
                outStream.close();

                Log.d(TAG, "Picture is being written to " + outFile.getAbsolutePath());


            } catch (FileNotFoundException e) {
                Log.d(TAG,"FNF");
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

            }
        }
        @Override
        public void onLoadCleared(@Nullable Drawable placeholder) {
        }
    });
//            int w = bitmap.getWidth();
//            int h = bitmap.getHeight();
//        Log.d(TAG, "bitmap w and h : " + w + ", " + h);
//        try {
//            if (bitmap == null){
//                Log.d(TAG, "null bitmap");
//            }
//            File sdCard = Environment.getExternalStorageDirectory();
//            File dir = new File(sdCard.getAbsolutePath() + "/Download");
//            dir.mkdirs();
//            Log.d(TAG, "Debug3");
//            String fileName = String.format("%d.jpg", System.currentTimeMillis());
//
//            File outFile = new File(dir, fileName);
//            Log.d(TAG, "Debug4, filename: " + fileName);
//
//            outStream = new FileOutputStream(outFile);
//            Log.d(TAG, "Debug5");
//
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
//            outStream.flush();
//            outStream.close();
//
//            Log.d(TAG, "onPictureTaken - wrote to " + outFile.getAbsolutePath());
//
////                                    refreshGallery(outFile);
//
//        } catch (FileNotFoundException e) {
//            Log.d(TAG,"FNF");
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//
//        }
    }


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