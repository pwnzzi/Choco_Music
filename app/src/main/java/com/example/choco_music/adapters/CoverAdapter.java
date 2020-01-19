package com.example.choco_music.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.choco_music.Holder.Chart_ViewHolder;
import com.example.choco_music.Holder.Cover_ViewHolder;
import com.example.choco_music.R;
import com.example.choco_music.model.CoverData;
import com.example.choco_music.model.HomeData;
import com.example.choco_music.model.VerticalData;

import java.util.ArrayList;

public class CoverAdapter extends RecyclerView.Adapter<Cover_ViewHolder>{

    private ArrayList<HomeData> homeDatas ;
    public void setData(ArrayList<HomeData> list){
        homeDatas = list;
    }

    @NonNull
    @Override
    public Cover_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //사용할 아이템 뷰를 생성해준다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chart_recycler_items,parent,false);
        Cover_ViewHolder holder = new Cover_ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull Cover_ViewHolder holder, int position) {

        HomeData data = homeDatas.get(position);

        holder.vocal.setText(data.getVocal());
        holder.title.setText(data.getTitle());
        Glide.with(holder.itemView.getContext()).load(data.getImg_path()).into(holder.img);


    }

    @Override
    public int getItemCount() {
        return homeDatas.size();
    }
}

