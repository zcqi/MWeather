package com.example.zhangzhao.mweather.model;

import java.util.ArrayList;

/**
 * Created by zhangzhao on 2015/8/26.
 */
public class CurrentWeatherDataEnvelope {
    public int cod;
    public String name;
    public long dt;
    public ArrayList<Weather> weather;
    public Main main;

    public class Main {
        public float temp;
        public float temp_min;
        public float temp_max;
    }
}
