package com.example.zhangzhao.mweather.model;

import com.example.zhangzhao.mweather.util.TemperatureFormatter;

import java.util.ArrayList;

/**
 * Created by zhangzhao on 2015/8/26.
 */
public class ForecastDataEnvelope {
    public long dt;
    public String dtString;
    public ArrayList<Weather> weather;
    public Temp temp;
    public class Temp {
        public float min;
        public float max;
        public String minString;
        public String maxString;


    }
}
