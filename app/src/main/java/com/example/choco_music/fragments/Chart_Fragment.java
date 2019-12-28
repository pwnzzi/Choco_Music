package com.example.choco_music.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.choco_music.R;
import com.example.choco_music.adapters.ChartAdapter;
import com.example.choco_music.adapters.PagerSnapWithSpanCountHelper;
import com.example.choco_music.adapters.VerticalAdapter;
import com.example.choco_music.model.ChartData;
import com.example.choco_music.model.VerticalData;

import java.util.ArrayList;

public class Chart_Fragment extends Fragment {

    private RecyclerView CoverSong_View,OriginalSong_View;
    private ChartAdapter mAdapter;
    private LinearLayoutManager Cover_LayoutManager,Original_LayoutManager;

    private int MAX_ITEM_COUNT = 50;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.chart_fragment,null,false);


        CoverSong_View = (RecyclerView)view.findViewById(R.id.CoverSong_list);
        OriginalSong_View=(RecyclerView)view.findViewById(R.id.OriginalSong_list);
        PagerSnapWithSpanCountHelper snapHelper = new PagerSnapWithSpanCountHelper(3);
        PagerSnapWithSpanCountHelper snapHelper2 = new PagerSnapWithSpanCountHelper(3);

        snapHelper.attachToRecyclerView(CoverSong_View);
        snapHelper2.attachToRecyclerView(OriginalSong_View);

        //init Data

        ArrayList<ChartData> data = new ArrayList<>();

        int i=0;
        while(i< MAX_ITEM_COUNT){
            data.add(new ChartData(R.mipmap.ic_launcher,i+"번째 데이터"));
            i++;
        }

        //init LayoutManager
        Cover_LayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.HORIZONTAL, false);
        Original_LayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.HORIZONTAL, false);

        Cover_LayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // 기본값이 VERTICAL
        Original_LayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        // setLayoutManager
        CoverSong_View.setLayoutManager(Cover_LayoutManager);
        OriginalSong_View.setLayoutManager(Original_LayoutManager);

        // init Adapter
        mAdapter = new ChartAdapter();

        // set Data
        mAdapter.setData(data);

        // set Adapter
        CoverSong_View.setAdapter(mAdapter);
        OriginalSong_View.setAdapter(mAdapter);

        return view;
    }
}
