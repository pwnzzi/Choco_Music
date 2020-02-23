package com.example.choco_music.Holder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.choco_music.R;
import com.example.choco_music.model.ChartData;

import java.util.List;

public class Playlist_ViewHolder extends RecyclerView.ViewHolder {

    public TextView title;
    public TextView vocal;
    public ImageButton icon;
    public ImageView img;

    public Playlist_ViewHolder(@NonNull final View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.chart_title);
        vocal = itemView.findViewById(R.id.chart_vocal);
        icon = itemView.findViewById(R.id.love_icon_playlist);
        img= itemView.findViewById(R.id.cover_img);
    }
}
