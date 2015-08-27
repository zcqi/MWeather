package com.example.zhangzhao.mweather.ui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zhangzhao.mweather.BR;
import com.example.zhangzhao.mweather.R;
import com.example.zhangzhao.mweather.databinding.WeatherForecastListItemBinding;
import com.example.zhangzhao.mweather.model.ForecastDataEnvelope;
import com.example.zhangzhao.mweather.util.DayFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangzhao on 2015/8/26.
 */
public class ForecastListAdapter extends RecyclerView.Adapter<ForecastListAdapter.MyHolder> {
    private List<ForecastDataEnvelope> forecastDataEnvelopeList=new ArrayList<ForecastDataEnvelope>();
    private Context context;

    public ForecastListAdapter(Context context) {
        this.context = context;
    }

    public void setForecastDataEnvelopeList(List<ForecastDataEnvelope> forecastDataEnvelopeList) {
        this.forecastDataEnvelopeList = forecastDataEnvelopeList;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        WeatherForecastListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.weather_forecast_list_item, parent, false);
        MyHolder myHolder = new MyHolder(binding.getRoot());
        myHolder.setBinding(binding);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        ForecastDataEnvelope forecast = forecastDataEnvelopeList.get(position);
        forecast.dtString=new DayFormatter(context).format(forecast.dt);
        holder.binding.setVariable(BR.forecast, forecast);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return forecastDataEnvelopeList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private WeatherForecastListItemBinding binding;

        public MyHolder(View itemView) {
            super(itemView);
        }

        public WeatherForecastListItemBinding getBinding() {
            return binding;
        }

        public void setBinding(WeatherForecastListItemBinding binding) {
            this.binding = binding;
        }
    }
}
