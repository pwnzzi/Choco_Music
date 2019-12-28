package com.example.choco_music.Holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.choco_music.R;

public class Chart_ViewHolder extends RecyclerView.ViewHolder{
    public ImageView icon;
    public TextView description;

    public Chart_ViewHolder(@NonNull View itemView) {
        super(itemView);

        icon = (ImageView)itemView.findViewById(R.id.music_icon);
        description = (TextView) itemView.findViewById(R.id.music_description);
    }
}
