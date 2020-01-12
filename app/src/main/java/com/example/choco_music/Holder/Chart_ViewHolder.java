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

    public Chart_ViewHolder(@NonNull View itemView) {
        super(itemView);

        title = (TextView) itemView.findViewById(R.id.chart_title);
        vocal = (TextView) itemView.findViewById(R.id.chart_vocal);
    }
}
