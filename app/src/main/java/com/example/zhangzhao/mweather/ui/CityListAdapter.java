package com.example.zhangzhao.mweather.ui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zhangzhao.mweather.BR;
import com.example.zhangzhao.mweather.R;
import com.example.zhangzhao.mweather.databinding.CityListItemBinding;
import com.example.zhangzhao.mweather.model.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangzhao on 2015/8/26.
 */
public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.MyHolder> {
    private Context context;
    private List<Location> locationList = new ArrayList<Location>();
    private MyItemClickListener itemListener;

    public interface MyItemClickListener {
        public void onItemClick(int postion);
    }

    public CityListAdapter(Context context) {
        this.context = context;
    }

    public void setLocationList(List<Location> locationList) {
        this.locationList = locationList;
    }

    public List<Location> getLocationList() {
        return locationList;
    }

    public void setItemListener(MyItemClickListener itemListener) {
        this.itemListener = itemListener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CityListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.city_list_item, parent, false);
        MyHolder myHolder = new MyHolder(binding.getRoot());
        myHolder.setBinding(binding);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        Location city = locationList.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemListener != null) {
                    itemListener.onItemClick(position);
                }
            }
        });

        holder.binding.setVariable(BR.city, city);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }


    public static class MyHolder extends RecyclerView.ViewHolder {
        private CityListItemBinding binding;

        public void setBinding(CityListItemBinding binding) {
            this.binding = binding;
        }

        public MyHolder(View itemView) {
            super(itemView);
        }
    }
}
