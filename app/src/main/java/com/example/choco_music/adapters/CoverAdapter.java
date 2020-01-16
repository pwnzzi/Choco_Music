package com.example.choco_music.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.choco_music.Holder.Chart_ViewHolder;
import com.example.choco_music.Holder.Cover_ViewHolder;
import com.example.choco_music.R;
import com.example.choco_music.model.CoverData;
import com.example.choco_music.model.VerticalData;

import java.util.ArrayList;

public class CoverAdapter extends RecyclerView.Adapter<Cover_ViewHolder>{

    private ArrayList<CoverData> CoverDatas ;
    public void setData(ArrayList<CoverData> list){
        CoverDatas = list;
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

        CoverData data = CoverDatas.get(position);

        holder.vocal.setText(data.getVocal());
        holder.title.setText(data.getTitle());

    }

    @Override
    public int getItemCount() {
        return CoverDatas.size();
    }
}

