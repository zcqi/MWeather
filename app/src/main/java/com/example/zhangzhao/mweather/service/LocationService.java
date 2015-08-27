package com.example.zhangzhao.mweather.service;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;

import rx.Observable;
import rx.Subscriber;

/**
 * Implement an Rx-style location service by wrapping the Android
 * LocationManager and providing the location result as an Observable.
 */
public class LocationService {

    private LocationManagerProxy mLocationManager;

    public LocationService(LocationManagerProxy locationManager) {
        mLocationManager = locationManager;
        mLocationManager.setGpsEnable(false);// 禁用GPS定位
    }

    public Observable<AMapLocation> getLocation() {
        return Observable.create(new Observable.OnSubscribe<AMapLocation>() {
            @Override
            public void call(final Subscriber<? super AMapLocation> subscriber) {

                final AMapLocationListener locationListener = new AMapLocationListener() {

                    @Override
                    public void onLocationChanged(Location location) {

                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }

                    @Override
                    public void onLocationChanged(AMapLocation location) {
                        Log.e("location",
                                String.format("location lat=>%f, lng=>%f", location.getLatitude(),
                                        location.getLongitude()));
                        subscriber.onNext(location);
                        subscriber.onCompleted();
                        stopLocation();
                    }
                };

                mLocationManager
                        .requestLocationData(LocationProviderProxy.AMapNetwork, -1, 15, locationListener);

            }
        });
    }

    private void stopLocation() {
        if (mLocationManager != null) {
            mLocationManager.destroy();
            mLocationManager = null;
        }
    }
}

