package com.example.choco_music.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.choco_music.R;
import com.example.choco_music.adapters.VerticalAdapter;
import com.example.choco_music.model.VerticalData;

import java.util.ArrayList;

public class Home_Fragment extends Fragment {

    private Button music_evaluate_btn;
    private RecyclerView mVerticalView;
    private VerticalAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private int MAX_ITEM_COUNT = 50;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.home_fragment,null,false);

        //RecyclerVier binding

        music_evaluate_btn=(Button)view.findViewById(R.id.music_evaluate_btn);

        music_evaluate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        mVerticalView = (RecyclerView)view.findViewById(R.id.vertivcal_list);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mVerticalView);

        //init Data

        ArrayList<VerticalData> data = new ArrayList<>();

        int i=0;
        while(i< MAX_ITEM_COUNT){
            data.add(new VerticalData(R.mipmap.ic_launcher,i+"번째 데이터"));
            i++;
        }

        //init LayoutManager

        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());

        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // 기본값이 VERTICAL

        // setLayoutManager
        mVerticalView.setLayoutManager(mLayoutManager);

        // init Adapter
        mAdapter = new VerticalAdapter();

        // set Data
        mAdapter.setData(data);

        // set Adapter
        mVerticalView.setAdapter(mAdapter);





        return view;

    }
}
