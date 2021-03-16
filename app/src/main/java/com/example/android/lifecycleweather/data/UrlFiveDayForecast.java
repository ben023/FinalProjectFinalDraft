package com.example.android.lifecycleweather.data;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;

public class UrlFiveDayForecast {
    @SerializedName("url")
    public String url;

    @SerializedName("explanation")
    private String explanation;

    @SerializedName("title")
    private String title;


    public UrlFiveDayForecast(String url,  String explanation, String title) {
        this.explanation = explanation;
        this.title = title;
        this.url = url;
    }

    public String getUrl(){
        return url;
    }
    public String getExplanation(){return this.explanation;}
    public String getTitle() {
        return this.title;
    }
    public static class JsonDeserializer implements com.google.gson.JsonDeserializer<UrlFiveDayForecast> {
        @Override
        public UrlFiveDayForecast deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject cityObj = json.getAsJsonObject();
//            JsonObject coordObj = cityObj.getAsJsonObject("coord");
            return new UrlFiveDayForecast(
//                    cityObj.getAsJsonPrimitive("url").getAsString()
                    cityObj.getAsJsonPrimitive("url").getAsString(),
                    cityObj.getAsJsonPrimitive("explanation").getAsString(),
                    cityObj.getAsJsonPrimitive("title").getAsString()
//                    coordObj.getAsJsonPrimitive("lon").getAsDouble(),
//                    cityObj.getAsJsonPrimitive("timezone").getAsInt()
            );
        }
    }
}
