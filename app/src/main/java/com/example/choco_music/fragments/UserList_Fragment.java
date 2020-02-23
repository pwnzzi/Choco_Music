package com.example.choco_music.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.choco_music.R;
import com.example.choco_music.adapters.Recent_Music_Adpater;
import com.example.choco_music.model.ChartData;
import com.example.choco_music.model.RecentPlaySongs_OpenHelper;
import java.util.ArrayList;

public class UserList_Fragment extends androidx.fragment.app.Fragment {


    private RecyclerView recent_View;
    private ArrayList<ChartData> chartData;
    private RecentPlaySongs_OpenHelper recent_openHelper;
    private Recent_Music_Adpater recent_music_apdapter;
    private LinearLayoutManager layoutManager;
    private Button likelist_fragment_btn, following_fragment_btn, belongings_fragment,playlist_fragment_btn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.userlist_fragment, container, false);
        init_btn(view);
        init_recyclerview(view);
        return view;
    }
    private void init_btn(View view){
        playlist_fragment_btn = view.findViewById(R.id.play_list);
        likelist_fragment_btn = view.findViewById(R.id.like_list);
        following_fragment_btn = view.findViewById(R.id.following_follower_list);
        belongings_fragment = view.findViewById(R.id.belongings_list);
        likelist_fragment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.anim_slide_in_right,R.anim.anim_slide_out_left).replace(R.id.user_fragment_layout,new LikeList_Fragment()).commit();
            }
        });
        following_fragment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.anim_slide_in_right,R.anim.anim_slide_out_left).replace(R.id.user_fragment_layout,new Following_Fragment()).commit();
            }
        });
        belongings_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.anim_slide_in_right,R.anim.anim_slide_out_left).replace(R.id.user_fragment_layout,new Belongings_Fragment()).commit();
            }
        });
        playlist_fragment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.anim_slide_in_right,R.anim.anim_slide_out_left).replace(R.id.user_fragment_layout,new Playlist_Fragment()).commit();
            }
        });
    }
    private void init_recyclerview(View view){
        recent_View = view.findViewById(R.id.recent_music_list);
        layoutManager = new LinearLayoutManager(getContext());
        recent_openHelper = new RecentPlaySongs_OpenHelper(getActivity());
        chartData = recent_openHelper.get_recent_music();
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recent_View.setLayoutManager(layoutManager);
        recent_music_apdapter = new Recent_Music_Adpater();
        recent_music_apdapter.setData(chartData);
        recent_View.setAdapter(recent_music_apdapter);
    }
}
