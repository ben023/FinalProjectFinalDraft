package com.example.android.lifecycleweather;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.lifecycleweather.data.FiveDayForecast;
import com.example.android.lifecycleweather.data.ForecastData;
import com.example.android.lifecycleweather.utils.OpenWeatherUtils;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastItemViewHolder> {
    private ArrayList<FiveDayForecast> fiveDayForecast = new ArrayList<FiveDayForecast>();
    private OnForecastItemClickListener onForecastItemClickListener;
    private String units;
    private static final String TAG = MainActivity.class.getSimpleName();

    public interface OnForecastItemClickListener {
        void onForecastItemClick(FiveDayForecast fiveDayForecast);
    }

    public ForecastAdapter(OnForecastItemClickListener onForecastItemClickListener) {
        this.onForecastItemClickListener = onForecastItemClickListener;
    }

    @NonNull
    @Override
    public ForecastItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.forecast_list_item, parent, false);
        return new ForecastItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastItemViewHolder holder, int position) {
        holder.bind(this.fiveDayForecast.get(position), this.units);
    }

    public void updateForecastData(FiveDayForecast fiveDayForecast, String units) {
        if (this.fiveDayForecast!=null) {
            fiveDayForecast.setTimestamp((double) System.currentTimeMillis());
            Log.d(TAG, "Five day forecast title and explanation: " + fiveDayForecast.getTitle() + ", " + fiveDayForecast.getExplanation());
            this.fiveDayForecast.add(fiveDayForecast);
            this.units = units;
            //Note need to implement after click
            Collections.sort(this.fiveDayForecast, Collections.reverseOrder());
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        if (this.fiveDayForecast == null) {
            return 0;
        } else {
            return this.fiveDayForecast.size();
        }
    }

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }
    class ForecastItemViewHolder extends RecyclerView.ViewHolder {
//        final private TextView dateTV;
//        final private TextView timeTV;
//        final private TextView highTempTV;
//        final private TextView lowTempTV;
//        final private TextView popTV;
        final private ImageView thumbnailTV;

        public ForecastItemViewHolder(@NonNull View itemView) {
            super(itemView);
//            dateTV = itemView.findViewById(R.id.tv_date);
//            timeTV = itemView.findViewById(R.id.tv_time);
//            highTempTV = itemView.findViewById(R.id.tv_high_temp);
//            lowTempTV = itemView.findViewById(R.id.tv_low_temp);
//            popTV = itemView.findViewById(R.id.tv_pop);
            thumbnailTV = itemView.findViewById(R.id.thumbnail);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onForecastItemClickListener.onForecastItemClick(
                            fiveDayForecast.get(getAdapterPosition())

                    );
                    Log.d(TAG, "Adapter position: " + getAdapterPosition());
                }
            });
        }

        public void bind(FiveDayForecast fiveDayForecast, String units) {
            Context ctx = this.itemView.getContext();
//            Calendar date = OpenWeatherUtils.dateFromEpochAndTZOffset(
//                    forecastData.getEpoch(),
//                    fiveDayForecast.getForecastCity().getTimezoneOffsetSeconds()
//            );
//            dateTV.setText(ctx.getString(R.string.forecast_date, date));
//            timeTV.setText(ctx.getString(R.string.forecast_time, date));
//            highTempTV.setText(ctx.getString(R.string.forecast_temp, forecastData.getHighTemp(), units));
//            lowTempTV.setText(ctx.getString(R.string.forecast_temp, forecastData.getLowTemp(), units));
//            popTV.setText(ctx.getString(R.string.forecast_pop, forecastData.getPop()));
            /*
             * Load forecast icon into ImageView using Glide: https://bumptech.github.io/glide/
             */
            if(fiveDayForecast.getThumbnailUrl() != null){
                Glide.with(ctx)
                        .load(fiveDayForecast.getThumbnailUrl())
                        .into(thumbnailTV);
            }
            else{
                Glide.with(ctx)
                        .load(fiveDayForecast.getUrl())
                        .into(thumbnailTV);
            }

        }

    }
}
