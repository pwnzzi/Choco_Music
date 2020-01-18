package com.example.choco_music.Holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.choco_music.R;

public class TodaySong_ViewHolder extends RecyclerView.ViewHolder {

    public TextView title;
    public TextView vocal;
    public TextView lyrics;
    public ImageView img;



    public TodaySong_ViewHolder(@NonNull View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.title_todaysong);
        vocal = itemView.findViewById(R.id.vocal_todaysong);
        lyrics = itemView.findViewById(R.id.lylics_todaysonog);
        img= itemView.findViewById(R.id.album_img_todaysong);



    }

}
