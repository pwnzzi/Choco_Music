package com.example.choco_music.adapters;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.choco_music.Holder.VerticalViewHolder;
import com.example.choco_music.R;
import com.example.choco_music.model.ChartData;
import com.example.choco_music.model.VerticalData;

import java.util.ArrayList;

import static com.example.choco_music.R.id.genre_layout;

public class VerticalAdapter extends RecyclerView.Adapter<VerticalViewHolder> {
    private ArrayList<ChartData> verticalDatas;
    private View view;
    private int id;


    public void setData(ArrayList<ChartData> list){
        verticalDatas = list;
    }
    @NonNull
    @Override
    public VerticalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //사용할 아이템 뷰를 생성해준다.

        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.vertical_recycler_items,parent,false);
        VerticalViewHolder holder = new VerticalViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull VerticalViewHolder holder, final int position) {

        ChartData data = verticalDatas.get(position);

        holder.vocal.setText(data.getVocal());
        holder.title.setText(data.getTitle());
        if(data.getType()) {
            holder.genre.setBackgroundResource(R.drawable.round_songtype_orig);
            holder.genre.setText("자작곡");
            holder.genre.setTextColor(Color.rgb(255,255,255));
            holder.border.setBackgroundResource(R.drawable.border_cover_orig);
            holder.tri.setImageResource(R.drawable.ic_triangle_original);
        }
    }
// https://android-blog.dev/19?category=677940 출처
    @Override
    public int getItemCount() {
        return verticalDatas.size();
    }

    public int getItemPosition() {
        return id;
    }

}
