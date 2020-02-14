package com.example.choco_music.adapters;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.choco_music.Holder.Playlist_ViewHolder;
import com.example.choco_music.R;
import com.example.choco_music.model.ChartData;
import java.util.List;

public class Playlist_Apdapter extends RecyclerView.Adapter<Playlist_ViewHolder> {

   // private List<Music_Playlist_Data> music_playlist;
    private List<ChartData> music_playlist;

    public void setData(List<ChartData> list){
        music_playlist = list;
    }


    @NonNull
    @Override
    public Playlist_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_fragment_items,parent,false);
        return new Playlist_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Playlist_ViewHolder holder, int position) {

        ChartData data = music_playlist.get(position);

        holder.vocal.setText(data.getVocal());
        holder.title.setText(data.getTitle());
        holder.title.setTextSize(TypedValue.COMPLEX_UNIT_SP,14f);
        holder.vocal.setTextSize(TypedValue.COMPLEX_UNIT_SP,12f);
        try{ Glide.with(holder.itemView.getContext()).load(data.getImg_path()).into(holder.img);}
        catch(Exception e){}

    }

    @Override
    public int getItemCount() {
        return music_playlist.size();
    }
}
