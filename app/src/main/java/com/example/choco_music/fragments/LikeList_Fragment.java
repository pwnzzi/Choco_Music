package com.example.choco_music.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.choco_music.Audio.AudioApplication;
import com.example.choco_music.R;
import com.example.choco_music.adapters.Playlist_Apdapter;
import com.example.choco_music.model.ChartData;
import com.example.choco_music.model.Playlist_Database_OpenHelper;
import com.example.choco_music.model.RecyclerItemClickListener;

import java.util.ArrayList;

public class LikeList_Fragment extends androidx.fragment.app.Fragment {

    private RecyclerView Play_list_View;
    private ArrayList<ChartData> chartData;
    Playlist_Database_OpenHelper openHelper;
    private Playlist_Apdapter playlist_apdapter;
    private LinearLayoutManager layoutManager;
    private Button back_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.likelist_fragment, container, false);

        back_btn = view.findViewById(R.id.back_likelist_fragment);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right).replace(R.id.user_fragment_layout,new UserList_Fragment()).commit();
            }
        });
        init_recyclerview(view);
        return view;
    }
    public void init_recyclerview(View view) {

        Play_list_View = view.findViewById(R.id.play_list);
        layoutManager = new LinearLayoutManager(getContext());
        openHelper = new Playlist_Database_OpenHelper(getActivity());
        chartData = openHelper.get_Music_chart();
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        Play_list_View.setLayoutManager(layoutManager);
        playlist_apdapter = new Playlist_Apdapter();
        playlist_apdapter.setData(chartData);
        Play_list_View.setAdapter(playlist_apdapter);

        Play_list_View.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), Play_list_View,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        AudioApplication.getInstance().getServiceInterface().setPlayList(chartData);
                        AudioApplication.getInstance().getServiceInterface().play(position);
                    }
                    @Override public void onLongItemClick(final View view, final int position) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        final AlertDialog alertDialog ;
                        builder.setTitle("좋아요를 취소 하시겠습니까?");
                        builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast myToast = Toast.makeText(view.getContext(),"좋아요를 취소하였습니다.",Toast.LENGTH_SHORT);
                                myToast.setGravity(Gravity.CENTER,0,0);
                                myToast.show();
                                Playlist_Database_OpenHelper playlist_database_openHelper = new Playlist_Database_OpenHelper(view.getContext());
                                chartData = playlist_database_openHelper.get_Music_chart();
                                playlist_database_openHelper.deleteData(chartData.get(position).getTitle());
                                refresh();
                            }
                        });
                        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        alertDialog = builder.create();
                        alertDialog.show();
                    }
                })
        );
    }
    private void refresh(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }
}
