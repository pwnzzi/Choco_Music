package com.example.choco_music.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.choco_music.R;
import com.example.choco_music.activities.MusicPlay_activity;
import com.example.choco_music.adapters.ChartAdapter;
import com.example.choco_music.adapters.Playlist_Apdapter;
import com.example.choco_music.model.Music_Playlist_Data;
import com.example.choco_music.model.Playlist_Database_OpenHelper;
import com.example.choco_music.model.RecyclerItemClickListener;
import java.util.List;

public class Playlist_Fragment extends Fragment {

    private RecyclerView Play_list_View;
    private List<Music_Playlist_Data> playlist_data;
    Playlist_Database_OpenHelper openHelper;
    private Playlist_Apdapter playlist_apdapter;
    private LinearLayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.playlist_fragment, null, false);
        init_recyclerview(view);

        return view;
    }


    public void init_recyclerview(View view){

        Play_list_View = view.findViewById(R.id.play_list);
        layoutManager = new LinearLayoutManager(getContext());
      //  playlist_data = new ArrayList<Music_Playlist_Data>();
        openHelper = new Playlist_Database_OpenHelper(getActivity());
        playlist_data =  openHelper.get_Music_list();
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        Play_list_View.setLayoutManager(layoutManager);
        playlist_apdapter = new Playlist_Apdapter();
        playlist_apdapter.setData(playlist_data);
        Play_list_View.setAdapter(playlist_apdapter);

        Play_list_View.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), Play_list_View,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getActivity(), MusicPlay_activity.class);
                //        intent.putExtra("list", playlist_data);
                        intent.putExtra("position", position);
                        startActivity(intent);
                    }
                    @Override public void onLongItemClick(View view, int position) { }
                })
        );
    }
}
