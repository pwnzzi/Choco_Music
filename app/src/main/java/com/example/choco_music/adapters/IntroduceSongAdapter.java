package com.example.choco_music.adapters;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.choco_music.Holder.TodaySong_ViewHolder;
import com.example.choco_music.R;
import com.example.choco_music.model.IntroduceData;
import java.util.ArrayList;

public class IntroduceSongAdapter extends RecyclerView.Adapter<TodaySong_ViewHolder>  {

    private ArrayList<IntroduceData> Introcude_TodaySongDatas;
    public void setData(ArrayList<IntroduceData> list){Introcude_TodaySongDatas = list;}

    @NonNull
    @Override
    public TodaySong_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todaysong_items,parent,false);
        TodaySong_ViewHolder holder = new TodaySong_ViewHolder(view);

        return holder;
    }
    @Override
    public void onBindViewHolder(@NonNull TodaySong_ViewHolder holder, int position) {
        //데이터에 받아온 값을 각각의 아이템에 넣어준다.
        IntroduceData data = Introcude_TodaySongDatas.get(position);
        holder.vocal.setText(data.getVocal());
        holder.title.setText(data.getTitle());
        holder.lyrics.setText(data.getLyrics());

        //이미지를 글라이드를 이용해서 넣어준다.
        Glide.with(holder.itemView.getContext()).load(data.getImg_path()).into(holder.img);

    }
    @Override
    public int getItemCount() {
        return  Introcude_TodaySongDatas.size();
    }
}
