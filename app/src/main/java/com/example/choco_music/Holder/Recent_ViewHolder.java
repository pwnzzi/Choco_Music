package com.example.choco_music.Holder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.choco_music.R;

public class Recent_ViewHolder extends RecyclerView.ViewHolder {

    public TextView title;
    public ImageView img;

    public Recent_ViewHolder(@NonNull View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.title_recent_music);
        img = itemView.findViewById(R.id.img_recent_music);
    }
}
