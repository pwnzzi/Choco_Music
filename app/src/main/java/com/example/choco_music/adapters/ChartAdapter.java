package com.example.choco_music.adapters;

import android.app.Activity;
import android.content.Intent;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.choco_music.Holder.Chart_ViewHolder;
import com.example.choco_music.R;
import com.example.choco_music.activities.MusicPlay_activity;
import com.example.choco_music.model.ChartData;
import com.example.choco_music.model.VerticalData;

import java.util.ArrayList;

public class ChartAdapter extends RecyclerView.Adapter<Chart_ViewHolder>  {

    private ArrayList<ChartData> chartDatas;
    public void setData(ArrayList<ChartData> list){
        chartDatas = list;
    }

    @NonNull
    @Override
    public Chart_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //사용할 아이템 뷰를 생성해준다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chart_recycler_items,parent,false);
        Chart_ViewHolder holder = new Chart_ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull Chart_ViewHolder holder, int position) {
        ChartData data = chartDatas.get(position);

        holder.vocal.setText(data.getVocal());
        holder.title.setText(data.getTitle());
        holder.title.setTextSize(TypedValue.COMPLEX_UNIT_SP,14f);
        holder.vocal.setTextSize(TypedValue.COMPLEX_UNIT_SP,12f);
        try{ Glide.with(holder.itemView.getContext()).load(data.getImg_path()).into(holder.img);}
        catch(Exception e){}

    }

    @Override
    public int getItemCount() {
        return chartDatas.size();
    }
}


