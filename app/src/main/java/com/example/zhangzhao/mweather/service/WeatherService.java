package com.example.zhangzhao.mweather.service;


import com.example.zhangzhao.mweather.model.CurrentWeatherDataEnvelope;
import com.example.zhangzhao.mweather.model.LocationEnvelope;
import com.example.zhangzhao.mweather.model.WeatherForecastListDataEnvelope;

import java.util.ArrayList;

import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by zhangzhao on 2015/8/25.
 */
public class WeatherService {
    private static final String WEB_SERVICE_BASE_URL = "http://api.openweathermap.org/data/2.5";

    public static OpenWeatherMapWebService getWeatherService() {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(WEB_SERVICE_BASE_URL).build();
        return restAdapter.create(OpenWeatherMapWebService.class);
    }

    public interface OpenWeatherMapWebService {
        @GET("/weather?units=metric")
        Observable<CurrentWeatherDataEnvelope> fetchCurrentWeather(@Query("lon") double longitude,
                                                                   @Query("lat") double latitude);

        @GET("/forecast/daily?units=metric&cnt=7")
        Observable<WeatherForecastListDataEnvelope> fetchWeatherForecasts(
                @Query("lon") double longitude, @Query("lat") double latitude);

        @GET("/find?type=like")
        Observable<LocationEnvelope> searchLocation(@Query("q") String q);
    }


}
