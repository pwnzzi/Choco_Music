package com.example.choco_music.adapters;

import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.choco_music.Holder.VerticalViewHolder;
import com.example.choco_music.R;
import com.example.choco_music.fragments.Home_Fragment;
import com.example.choco_music.model.ChartData;

import java.util.ArrayList;

public class HomeSongAdapter extends RecyclerView.Adapter<VerticalViewHolder> {

    private ArrayList<ChartData> homeDatas;
    private Home_Fragment fragment;

    public HomeSongAdapter(Home_Fragment fragment){
        this.fragment = fragment;
    }

    public void setData(ArrayList<ChartData> list){homeDatas = list;}

    @NonNull
    @Override
    public VerticalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vertical_recycler_items,parent,false);
        VerticalViewHolder holder = new VerticalViewHolder(view, fragment);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull VerticalViewHolder holder, int position) {

        //데이터에 받아온 값을 각각의 아이템에 넣어준다.
        ChartData data = homeDatas.get(position);
        holder.vocal.setText(data.getVocal());
        holder.title.setText(data.getTitle());
        holder.title.setTextSize(TypedValue.COMPLEX_UNIT_SP,14f);
        holder.vocal.setTextSize(TypedValue.COMPLEX_UNIT_SP,12f);

        //이미지를 글라이드를 이용해서 넣어준다.


        //Log.e("잘넘어오나?",data.getImg_path());
        try{ Glide.with(holder.itemView.getContext()).load(data.getImg_path()).into(holder.img);}
        catch(Exception e){}

        if(data.getType()) {
            holder.genre.setBackgroundResource(R.drawable.round_songtype_orig);
            holder.genre.setText("자작곡");
            holder.genre.setTextColor(Color.rgb(255,255,255));
            holder.border.setBackgroundResource(R.drawable.border_cover_orig);
            holder.tri.setImageResource(R.drawable.ic_triangle_original);

        }else{
            holder.genre.setBackgroundResource(R.drawable.round_songtype);
            holder.genre.setText("커버곡");
            holder.border.setBackgroundResource(R.drawable.border_cover);
            holder.tri.setImageResource(R.drawable.ic_triangle_cover);
        }

    }

    @Override
    public int getItemCount() {
        return homeDatas.size();
    }
}
