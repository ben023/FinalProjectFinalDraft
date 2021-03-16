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

    public UrlFiveDayForecast(String url) {

        this.url = url;
    }

    public String getUrl(){
        return url;
    }

    public static class JsonDeserializer implements com.google.gson.JsonDeserializer<UrlFiveDayForecast> {
        @Override
        public UrlFiveDayForecast deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject cityObj = json.getAsJsonObject();
//            JsonObject coordObj = cityObj.getAsJsonObject("coord");
            return new UrlFiveDayForecast(
//                    cityObj.getAsJsonPrimitive("url").getAsString()
                    cityObj.getAsJsonPrimitive("url").getAsString()
//                    coordObj.getAsJsonPrimitive("lon").getAsDouble(),
//                    cityObj.getAsJsonPrimitive("timezone").getAsInt()
            );
        }
    }
}
