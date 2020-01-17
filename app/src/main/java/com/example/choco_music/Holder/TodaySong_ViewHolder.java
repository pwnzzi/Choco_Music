package com.example.choco_music.Holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.choco_music.R;

public class TodaySong_ViewHolder extends RecyclerView.ViewHolder {

    public TextView title;
    public TextView vocal;

    public TodaySong_ViewHolder(@NonNull View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.title_todaysong);
        vocal = itemView.findViewById(R.id.vocal_todaysong);
    }

}
