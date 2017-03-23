package com.mrd.shopsup.ui;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mrd.shopsup.model.Weather;

import java.util.List;
import com.mrd.shopsup.R;



public class WeatherListAdapter extends RecyclerView.Adapter<WeatherListAdapter.ViewHolder> {

    private final List<Weather> mValues;

    public WeatherListAdapter(List<Weather> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_weather, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bind(mValues.get(position));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtHumidity;
        public TextView txtPressure;
        public TextView txtDescription;
        public TextView txtDate;
        public TextView txtTemp;


        public ViewHolder(View view) {
            super(view);
            txtHumidity = (TextView) view.findViewById(R.id.txt_humidity);
            txtPressure = (TextView) view.findViewById(R.id.txt_pressure);
            txtDescription = (TextView) view.findViewById(R.id.txt_description);
            txtDate = (TextView) view.findViewById(R.id.txt_date);
            txtTemp = (TextView) view.findViewById(R.id.txt_temp);

        }

        public void bind(Weather weather) {
            txtHumidity.setText(weather.getHumidity());
            txtPressure.setText(weather.getPressure());
            txtDate.setText(weather.getDt());
            if(weather.getTemp() != null) {
                txtTemp.setText("Max:" + weather.getTemp().getMax() + " C " + " Min:" + weather.getTemp().getMin() + " C");
            } else {
                txtTemp.setText("Data not available");
            }
            if (weather.getWeather() != null && weather.getWeather().length > 0
                    && !TextUtils.isEmpty(weather.getWeather()[0].getDescription())) {
                txtDescription.setText(weather.getWeather()[0].getDescription());
            }
        }
    }
}
