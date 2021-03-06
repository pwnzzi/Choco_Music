package com.example.choco_music.Holder;

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
import com.example.choco_music.fragments.Home_Fragment;
import com.example.choco_music.model.HomeData;
import com.example.choco_music.model.Playlist_Database_OpenHelper;
import com.example.choco_music.model.RecyclerItemClickListener;
import com.example.choco_music.model.VerticalData;

import java.util.ArrayList;
import java.util.EventListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    public ImageView img;
    public Home_Fragment fragment;
    public int pos;

    public VerticalViewHolder(@NonNull final View itemView, final Home_Fragment fragment) {
        super(itemView);

        this.fragment = fragment;

        title = itemView.findViewById(R.id.title);
        vocal = itemView.findViewById(R.id.vocal);
        img= itemView.findViewById(R.id.vertical_icon);
        icon=itemView.findViewById(R.id.love_icon);

        genre = itemView.findViewById(R.id.vertical_genre);
        border = itemView.findViewById(R.id.vertical_icon);
        tri = itemView.findViewById(R.id.vertical_tri);
        genre_layout= itemView.findViewById(R.id.genre_layout);

        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(love == false) {
                    icon.setBackgroundResource(R.drawable.heart_selected);
                    pos = getAdapterPosition();
                    Log.e("현재 위치",""+ pos);
                    fragment.add_playlist(pos,itemView);

                }
                else{
                    icon.setBackgroundResource(R.drawable.heart_unselected_album);
                }
                love = !love;
            }
        });
    }
}
