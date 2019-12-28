package com.example.choco_music.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.choco_music.R;
import com.example.choco_music.adapters.ChartAdapter;
import com.example.choco_music.model.ChartData;

import java.util.ArrayList;

public class Coversong_chart extends AppCompatActivity {

    private RecyclerView mVerticalView;
    private ChartAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;


    private int MAX_ITEM_COUNT = 50;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_list);

        mVerticalView = (RecyclerView)findViewById(R.id.chart_list);

        //init Data

        ArrayList<ChartData> data = new ArrayList<>();

        int i=0;
        while(i< MAX_ITEM_COUNT){
            data.add(new ChartData(R.mipmap.ic_launcher,i+"번째 데이터"));
            i++;
        }

        //init LayoutManager

        mLayoutManager = new LinearLayoutManager(this);

        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // 기본값이 VERTICAL

        // setLayoutManager
        mVerticalView.setLayoutManager(mLayoutManager);

        // init Adapter
        mAdapter = new ChartAdapter();

        // set Data
        mAdapter.setData(data);

        // set Adapter
        mVerticalView.setAdapter(mAdapter);

    }
}
