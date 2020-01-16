package com.example.choco_music.Holder;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.choco_music.R;
import com.example.choco_music.adapters.VerticalAdapter;
import com.example.choco_music.fragments.Home_Fragment;
import com.example.choco_music.model.VerticalData;

import java.util.ArrayList;

public class VerticalViewHolder extends RecyclerView.ViewHolder {
    public Button icon;
    public TextView title;
    public TextView vocal;
    public TextView genre;
    public ImageView border;
    public ImageView tri;
    public boolean love = false;
    public boolean layout=false;
    public LinearLayout genre_layout;

    public VerticalViewHolder(@NonNull View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.title);
        vocal = itemView.findViewById(R.id.vocal);

        //리사이클러뷰 아이템
        icon = itemView.findViewById(R.id.love_icon);
        genre = itemView.findViewById(R.id.vertical_genre);
        border = itemView.findViewById(R.id.vertical_icon);
        tri = itemView.findViewById(R.id.vertical_tri);
        genre_layout= itemView.findViewById(R.id.genre_layout);

        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(love == false) {
                    icon.setBackgroundResource(R.drawable.heart_selected);
                }
                else{
                    icon.setBackgroundResource(R.drawable.heart_unselected_album);
                }
                love = !love;
            }
        });

        border.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(layout == false) {
                    icon.setVisibility(View.GONE);
                    genre_layout.setVisibility(View.VISIBLE);
                }
                else{
                    icon.setVisibility(View.VISIBLE);
                    genre_layout.setVisibility(View.GONE);
                }
                layout = !layout;
            }
        });



    }
}
