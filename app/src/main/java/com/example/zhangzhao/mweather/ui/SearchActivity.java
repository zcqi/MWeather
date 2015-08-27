package com.example.zhangzhao.mweather.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;

import com.example.zhangzhao.mweather.R;
import com.example.zhangzhao.mweather.service.WeatherService;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.android.widget.OnTextChangeEvent;
import rx.android.widget.WidgetObservable;


/**
 * Created by zhangzhao on 2015/8/26.
 */
public class SearchActivity extends AppCompatActivity {
    private EditText editText;
    private RecyclerView recyclerView;
    private CityListAdapter adapter;
    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        editText = (EditText) findViewById(R.id.edittext);
        recyclerView = (RecyclerView) findViewById(R.id.city_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CityListAdapter(this);
        adapter.setItemListener(new CityListAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(int postion) {
                Intent intent=new Intent();
                intent.putExtra("location",adapter.getLocationList().get(postion));
                setResult(1,intent );
                finish();
            }
        });
        recyclerView.setAdapter(adapter);

        Observable<OnTextChangeEvent> textChangeObservable = WidgetObservable.text(editText);
        subscription = AppObservable.bindActivity(this, textChangeObservable)
                .map(event->event.text().toString())
                .filter(s->s.length()>2)
                .concatMap(s-> WeatherService.getWeatherService().searchLocation(s))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        city->{
                            Log.i("SearchActivity",city.cod+"");
                            adapter.setLocationList(city.list);
                            adapter.notifyDataSetChanged();
                        }
                );
    }
}
