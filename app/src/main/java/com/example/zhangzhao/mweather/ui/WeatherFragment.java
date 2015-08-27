package com.example.zhangzhao.mweather.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.example.zhangzhao.mweather.BR;

import com.amap.api.location.LocationManagerProxy;
import com.example.zhangzhao.mweather.R;
import com.example.zhangzhao.mweather.databinding.FragmentWeatherBinding;
import com.example.zhangzhao.mweather.model.Coord;
import com.example.zhangzhao.mweather.model.CurrentWeatherDataEnvelope;
import com.example.zhangzhao.mweather.model.Location;
import com.example.zhangzhao.mweather.model.WeatherForecastListDataEnvelope;
import com.example.zhangzhao.mweather.service.LocationService;
import com.example.zhangzhao.mweather.service.WeatherService;

import org.apache.http.HttpException;

import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import retrofit.RetrofitError;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by zhangzhao on 2015/8/26.
 */
public class WeatherFragment extends Fragment {
    private static final String KEY_CURRENT_WEATHER = "key_current_weather";
    private static final String KEY_WEATHER_FORECASTS = "key_weather_forecasts";
    private static final long LOCATION_TIMEOUT_SECONDS = 20;
    private static final String TAG = WeatherFragment.class.getCanonicalName();

    private CompositeSubscription mCompositeSubscription;
    private FragmentWeatherBinding binding;
    private ForecastListAdapter adapter;
    private Location location;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mCompositeSubscription = new CompositeSubscription();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_weather, container, false);
        binding.weatherForecastList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.weatherForecastList.setLayoutManager(layoutManager);
        adapter = new ForecastListAdapter(getActivity());
        binding.weatherForecastList.setAdapter(adapter);
        binding.locationName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        updateWeather();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        mCompositeSubscription.unsubscribe();
        super.onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1&&resultCode==1){
            location= (Location) data.getSerializableExtra("location");
            updateWeather();
        }
    }

    private void updateWeather() {
        Observable<Location> locationObservable;
        if (location!=null){
             locationObservable=Observable.just(location);
        }else {
            LocationManagerProxy locationManager = LocationManagerProxy.getInstance(getActivity());
            LocationService locationService = new LocationService(locationManager);
            locationObservable=locationService.getLocation().map(aMapLocation -> {
                Log.i("WeatherFragment", aMapLocation.getLatitude() + "，" + aMapLocation.getLongitude());
                Location location = new Location();
                Coord coord=new Coord();
                coord.lat = aMapLocation.getLatitude();
                coord.lon = aMapLocation.getLongitude();
                location.coord=coord;
                return location;
            });
        }
        Observable<HashMap<String, Object>> fetchDataObservable = locationObservable
                .flatMap(location -> {
                    double longitude = location.coord.lon;
                    double latitude = location.coord.lat;
                    Log.i("Tag", "经纬度：" + latitude + "," + longitude);
                    return Observable.zip(
                            // Fetch current and 7 day forecasts for the
                            // location.

                            WeatherService.getWeatherService().fetchCurrentWeather(longitude, latitude),
                            WeatherService.getWeatherService().fetchWeatherForecasts(longitude, latitude),

                            // Only handle the fetched results when both
                            // sets are available.
                            (currentWeather, weatherForecasts) -> {
                                Log.i(TAG, "哈哈");
                                Log.i(TAG, weatherForecasts.city.name);
                                HashMap<String, Object> weatherData = new HashMap<String, Object>();
                                weatherData.put(KEY_CURRENT_WEATHER, currentWeather);
                                weatherData.put(KEY_WEATHER_FORECASTS, weatherForecasts);
                                return weatherData;
                            });

                });
        mCompositeSubscription.add(fetchDataObservable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(weatherData -> {

                            // Update UI with current weather.
                            CurrentWeatherDataEnvelope currentWeatherDataEnvelope = (CurrentWeatherDataEnvelope) weatherData.get(KEY_CURRENT_WEATHER);
                            binding.setVariable(BR.current, currentWeatherDataEnvelope);
                            // Update weather forecast list.
                            WeatherForecastListDataEnvelope weatherForecastListDataEnvelope = (WeatherForecastListDataEnvelope) (weatherData.get(KEY_WEATHER_FORECASTS));
                            adapter.setForecastDataEnvelopeList(weatherForecastListDataEnvelope.list);
                            adapter.notifyDataSetChanged();
                        },


                        error -> {

                            if (error instanceof TimeoutException) {
                                Toast.makeText(getActivity().getApplicationContext(), "获取位置失败",
                                        Toast.LENGTH_SHORT).show();
                            } else if (error instanceof RetrofitError
                                    || error instanceof HttpException) {
                                Toast.makeText(getActivity().getApplicationContext(), "获取天气失败",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e(TAG, error.getMessage());
                                error.printStackTrace();
                                throw new RuntimeException("See inner exception");
                            }
                        }
                ));


    }
}
