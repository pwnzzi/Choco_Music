package com.example.choco_music.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.choco_music.Holder.TodaySong_ViewHolder;
import com.example.choco_music.R;
import com.example.choco_music.model.TodaySongData;
import java.util.ArrayList;

public class TodaySongAdapter extends RecyclerView.Adapter<TodaySong_ViewHolder> {

    private ArrayList<TodaySongData> todaySongDatas;
    public void setData(ArrayList<TodaySongData> list){ todaySongDatas = list;}
    @NonNull
    @Override
    public TodaySong_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todaysong_items,parent,false);
        TodaySong_ViewHolder holder = new TodaySong_ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TodaySong_ViewHolder holder, int position) {

        TodaySongData data = todaySongDatas.get(position);
       // holder.vocal.setText(data.getVocal());
       // holder.title.setText(data.getTitle());
    }

    @Override
    public int getItemCount() {
        return todaySongDatas.size();
    }
}
