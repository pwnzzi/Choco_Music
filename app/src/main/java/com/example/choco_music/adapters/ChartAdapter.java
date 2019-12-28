package com.example.choco_music.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.choco_music.Holder.Chart_ViewHolder;
import com.example.choco_music.R;
import com.example.choco_music.model.ChartData;
import com.example.choco_music.model.VerticalData;

import java.util.ArrayList;

public class ChartAdapter extends RecyclerView.Adapter<Chart_ViewHolder>  {

    private ArrayList<ChartData> chartDatas ;

    public void setData(ArrayList<ChartData> list){
        chartDatas = list;
    }


    @NonNull
    @Override
    public Chart_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //사용할 아이템 뷰를 생성해준다.

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.chart_recycler_items,parent,false);
        Chart_ViewHolder holder = new Chart_ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull Chart_ViewHolder holder, int position) {

        ChartData data = chartDatas.get(position);

        holder.description.setText(data.getText());
        holder.icon.setImageResource(data.getImg());

    }

    @Override
    public int getItemCount() {
        return chartDatas.size();
    }
}
