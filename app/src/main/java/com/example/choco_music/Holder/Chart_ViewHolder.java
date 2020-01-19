package com.example.choco_music.Holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.choco_music.R;

public class Chart_ViewHolder extends RecyclerView.ViewHolder{
    public TextView title;
    public TextView vocal;
    public ImageView img;

    public Chart_ViewHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.chart_title);
        vocal = itemView.findViewById(R.id.chart_vocal);
        img = itemView.findViewById(R.id.music_icon);
    }
}
