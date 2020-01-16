package com.example.choco_music.Holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.choco_music.R;

public class Playlist_ViewHolder extends RecyclerView.ViewHolder {

    public TextView title;
    public TextView vocal;

    public Playlist_ViewHolder(@NonNull View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.chart_title);
        vocal = itemView.findViewById(R.id.chart_vocal);
    }
}
