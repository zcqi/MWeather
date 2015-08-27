package com.example.zhangzhao.mweather.model;

import java.util.ArrayList;

/**
 * Created by zhangzhao on 2015/8/26.
 */
public class WeatherForecastListDataEnvelope {
    public int cod;
    public Location city;
    public ArrayList<ForecastDataEnvelope> list;
}
