package com.example.android.lifecycleweather.data;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class FiveDayForecast implements Serializable {
    @SerializedName("thumbnail_url")
    private String thumbnailUrl;

    private Double timeStamp;

    public FiveDayForecast() {
        this.thumbnailUrl = null;
        this.timeStamp = null;
    }
    public FiveDayForecast(Double timestamp, String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
        this.timeStamp = timestamp;
    }


    public String getThumbnailUrl() {
        return this.thumbnailUrl;
    }

    public Double getTimeStamp() {
        return timeStamp;
    }
//    public void addUrl(){
//
//    }


    public static class JsonDeserializer implements com.google.gson.JsonDeserializer<FiveDayForecast> {
        @Override
        public FiveDayForecast deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject cityObj = json.getAsJsonObject();
//            JsonObject coordObj = cityObj.getAsJsonObject("coord");
            return new FiveDayForecast(0.0,
                    cityObj.getAsJsonPrimitive("thumbnail_url").getAsString()
//                    coordObj.getAsJsonPrimitive("lat").getAsDouble(),
//                    coordObj.getAsJsonPrimitive("lon").getAsDouble(),
//                    cityObj.getAsJsonPrimitive("timezone").getAsInt()
            );
        }
    }
}
