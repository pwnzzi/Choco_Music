package com.example.choco_music.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.choco_music.Holder.VerticalViewHolder;
import com.example.choco_music.R;
import com.example.choco_music.model.VerticalData;

import java.util.ArrayList;

public class VerticalAdapter extends RecyclerView.Adapter<VerticalViewHolder> {
    private ArrayList<VerticalData> verticalDatas;


    public void setData(ArrayList<VerticalData> list){
        verticalDatas = list;
    }
    @NonNull
    @Override
    public VerticalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //사용할 아이템 뷰를 생성해준다.

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.vertical_recycler_items,parent,false);
        VerticalViewHolder holder = new VerticalViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull VerticalViewHolder holder, int position) {

        VerticalData data = verticalDatas.get(position);

        holder.description.setText(data.getText());
        holder.icon.setImageResource(data.getImg());
    }
// https://android-blog.dev/19?category=677940 출처
    @Override
    public int getItemCount() {
        return verticalDatas.size();
    }
}
