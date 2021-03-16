package com.example.android.lifecycleweather.data;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class FiveDayForecast implements Serializable, Comparable<FiveDayForecast> {
    @SerializedName("thumbnail_url")
    private String thumbnailUrl;

//    @SerializedName("url")
    private String url;

    private Double timeStamp;

    public FiveDayForecast() {
        this.thumbnailUrl = null;
        this.timeStamp = null;
    }
//    public FiveDayForecast(Double timestamp, String url, String thumbnailUrl) {
////        Log.d("forecast", "this is thumbnail" + thumbnailUrl);
////        Log.d("forecast", "this is url"+url);
//        this.thumbnailUrl = thumbnailUrl;
//        this.timeStamp = timestamp;
//    }

    public FiveDayForecast(Double timestamp, String url, String thumbnailUrl) {
//        Log.d("forecast", "this is thumbnail" + thumbnailUrl);
//        Log.d("forecast", "this is url"+url);
        this.thumbnailUrl = thumbnailUrl;
        this.timeStamp = timestamp;
        this.url = url;
    }

    public void setTimestamp(Double timestamp){
        this.timeStamp=timestamp;
    }

    public String getUrl(){return this.url;}
    public String getThumbnailUrl() {
        return this.thumbnailUrl;
    }

    public Double getTimeStamp() {
        return timeStamp;
    }
//    public void addUrl(){
//
//    }
    @Override
    public int compareTo(FiveDayForecast o){
        return Double.compare(this.getTimeStamp(), o.getTimeStamp());
    }

    public static class JsonDeserializer implements com.google.gson.JsonDeserializer<FiveDayForecast> {
        @Override
        public FiveDayForecast deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject cityObj = json.getAsJsonObject();
//            JsonObject coordObj = cityObj.getAsJsonObject("coord");
            return new FiveDayForecast(0.0,
                    cityObj.getAsJsonPrimitive("thumb_url").getAsString(), null
//                    cityObj.getAsJsonPrimitive("thumb_url").getAsString()
//                    coordObj.getAsJsonPrimitive("lon").getAsDouble(),
//                    cityObj.getAsJsonPrimitive("timezone").getAsInt()
            );
        }
    }
}
