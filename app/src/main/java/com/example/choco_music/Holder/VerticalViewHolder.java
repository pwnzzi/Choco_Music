package com.example.choco_music.Holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.choco_music.R;

public class VerticalViewHolder extends RecyclerView.ViewHolder {
    public ImageView icon;
    public TextView title;
    public TextView vocal;

    public VerticalViewHolder(@NonNull View itemView) {
        super(itemView);


        title = (TextView) itemView.findViewById(R.id.title);
        vocal = (TextView) itemView.findViewById(R.id.vocal);

    }
}
