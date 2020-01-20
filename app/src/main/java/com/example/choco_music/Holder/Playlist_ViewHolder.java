package com.example.choco_music.Holder;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.choco_music.R;
import com.example.choco_music.activities.MainActivity;
import com.example.choco_music.adapters.Playlist_Apdapter;
import com.example.choco_music.fragments.Playlist_Fragment;
import com.example.choco_music.model.Music_Playlist_Data;
import com.example.choco_music.model.Playlist_Database_OpenHelper;

import java.util.ArrayList;
import java.util.List;

public class Playlist_ViewHolder extends RecyclerView.ViewHolder {

    public TextView title;
    public TextView vocal;
    public ImageView icon;
    public ImageView img;
    private List<Music_Playlist_Data> playlist_data;
    private Playlist_Fragment playlist_fragment= new Playlist_Fragment();


    public Playlist_ViewHolder(@NonNull final View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.chart_title);
        vocal = itemView.findViewById(R.id.chart_vocal);
        icon = itemView.findViewById(R.id.love_icon_playlist);
        img= itemView.findViewById(R.id.cover_img);


        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Toast myToast = Toast.makeText(itemView.getContext(),"좋아요를 취소하였습니다.",Toast.LENGTH_SHORT);
                    myToast.setGravity(Gravity.CENTER,0,0);
                    myToast.show();
                int pos = getAdapterPosition();
                Playlist_Database_OpenHelper playlist_database_openHelper = new Playlist_Database_OpenHelper(itemView.getContext());
                playlist_data = playlist_database_openHelper.get_Music_list();
                playlist_database_openHelper.deleteData(playlist_data.get(pos).getTitle());


            }
        });

    }
}
