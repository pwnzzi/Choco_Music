package com.example.choco_music.adapters;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.choco_music.Holder.Playlist_ViewHolder;
import com.example.choco_music.Holder.Recent_ViewHolder;
import com.example.choco_music.R;
import com.example.choco_music.model.ChartData;

import java.util.List;

public class Recent_Music_Adpater extends RecyclerView.Adapter<Recent_ViewHolder> {

    private List<ChartData> recent_music_playlist;

    public void setData(List<ChartData> list){
        recent_music_playlist = list;
    }

    @NonNull
    @Override
    public Recent_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_music_items,parent,false);
        return new Recent_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Recent_ViewHolder holder, int position) {
        ChartData data = recent_music_playlist.get(position);

        holder.title.setText(data.getTitle());
        holder.title.setTextSize(TypedValue.COMPLEX_UNIT_SP,14f);
        try{ Glide.with(holder.itemView.getContext()).load(data.getImg_path()).into(holder.img);}
        catch(Exception e){}

    }

    @Override
    public int getItemCount() {
            return recent_music_playlist.size();
    }
}

