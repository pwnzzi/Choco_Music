package com.example.choco_music.Holder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.choco_music.R;

public class VerticalViewHolder extends RecyclerView.ViewHolder {
    public Button icon;
    public TextView title;
    public TextView vocal;
    public TextView genre;
    public ImageView border;
    public ImageView tri;
    public boolean love = false;

    public VerticalViewHolder(@NonNull View itemView) {
        super(itemView);

        title = (TextView) itemView.findViewById(R.id.title);
        vocal = (TextView) itemView.findViewById(R.id.vocal);

        //리사이클러뷰 아이템
        icon = itemView.findViewById(R.id.love_icon);
        genre = itemView.findViewById(R.id.vertical_genre);
        border = itemView.findViewById(R.id.vertical_icon);
        tri = itemView.findViewById(R.id.vertical_tri);

        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(love == false)
                    icon.setBackgroundResource(R.drawable.heart_selected);
                else
                    icon.setBackgroundResource(R.drawable.heart_unselected_album);

                love = !love;
            }
        });

    }
}
