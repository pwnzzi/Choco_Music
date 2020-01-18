package com.example.choco_music.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.choco_music.Audio.AudioApplication;
import com.example.choco_music.Interface.RetrofitExService;
import com.example.choco_music.R;
import com.example.choco_music.activities.Coversong_chart;
import com.example.choco_music.activities.MusicPlay_activity;
import com.example.choco_music.activities.Originalsong_chart;
import com.example.choco_music.adapters.ChartAdapter;
import com.example.choco_music.adapters.CoverAdapter;
import com.example.choco_music.adapters.PagerSnapWithSpanCountHelper;
import com.example.choco_music.model.ChartData;
import com.example.choco_music.model.CoverData;
import com.example.choco_music.model.RecyclerItemClickListener;
import com.example.choco_music.model.VerticalData;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Chart_Fragment extends Fragment {

    private RecyclerView CoverSong_View,OriginalSong_View;
    private ChartAdapter Original_Adapter;
    private CoverAdapter coverAdapter;
    private LinearLayoutManager Cover_LayoutManager,Original_LayoutManager;
    private Button Cover_btn, Originall_btn;
    private Retrofit retrofit;
    private RetrofitExService retrofitExService;
    private ArrayList<VerticalData> Original_datas;
    private ArrayList<CoverData> Cover_datas;
    private ArrayList<ChartData> Original_Chart;
    private ArrayList<ChartData> Cover_Chart;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.chart_fragment,null,false);

        init_btn(view);
        init_recyclerview(view);
        init_retrofit();
        return view;
    }

    public void init_btn(View view){
        // Button binding
        Cover_btn =(Button) view.findViewById(R.id.cover_btn);
        Originall_btn=(Button)view.findViewById(R.id.original_btn);

        //클릭 이벤트
        Cover_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Coversong_chart.class);
                startActivity(intent);

            }
        });

        Originall_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Originalsong_chart.class);
                startActivity(intent);

            }
        });
    }
    public void init_recyclerview(View view){

        //자자곡, 커버곡 리사이클러뷰를 만들어준다.

        CoverSong_View = (RecyclerView)view.findViewById(R.id.CoverSong_list);
        OriginalSong_View=(RecyclerView)view.findViewById(R.id.OriginalSong_list);
        PagerSnapWithSpanCountHelper snapHelper = new PagerSnapWithSpanCountHelper(5);
        PagerSnapWithSpanCountHelper snapHelper2 = new PagerSnapWithSpanCountHelper(5);

        snapHelper.attachToRecyclerView(CoverSong_View);
        snapHelper2.attachToRecyclerView(OriginalSong_View);

        OriginalSong_View.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), OriginalSong_View,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        AudioApplication.getInstance().getServiceInterface().setPlayList(Original_Chart);
                        AudioApplication.getInstance().getServiceInterface().play(position);
                    }

                    @Override public void onLongItemClick(View view, int position) { }
                })
        );

        CoverSong_View.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), CoverSong_View,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        AudioApplication.getInstance().getServiceInterface().setPlayList(Cover_Chart);
                        AudioApplication.getInstance().getServiceInterface().play(position);
                    }

                    @Override public void onLongItemClick(View view, int position) { }
                })
        );
    }


    private void init_retrofit(){

        Original_LayoutManager = new GridLayoutManager(getContext(), 5, GridLayoutManager.HORIZONTAL, false);
        Cover_LayoutManager= new GridLayoutManager(getContext(), 5, GridLayoutManager.HORIZONTAL, false);
        //서버 통신을 위한 레스트로핏 적용
        retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitExService.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitExService = retrofit.create(RetrofitExService.class);

        // 데이터베이스에 데이터 받아오기
        retrofitExService.getData2().enqueue(new Callback<ArrayList<VerticalData>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<VerticalData>> call, @NonNull Response<ArrayList<VerticalData>> response) {
                if (response.isSuccessful()) {
                    Original_datas = response.body();

                    Original_Chart = new ArrayList<>();
                    for(VerticalData data: Original_datas)
                        Original_Chart.add(new ChartData(data.getTitle(), data.getVocal(), data.getFileurl(), true));

                    if (Original_datas != null) {
                        for (int i = 0; i < Original_datas.size(); i++) {
                            Log.d("data" + i, Original_datas.get(i).getTitle() + "");
                            Log.d("data" + i, Original_datas.get(i).getVocal() + "");
                        }
                        Log.d("getData1 end", "======================================");
                    }
                    //     filerul_data.add(datas.get(i).getFilerul());
                    // setLayoutManager
                    OriginalSong_View.setLayoutManager(Original_LayoutManager);
                    Original_LayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    // init Adapter
                    Original_Adapter = new ChartAdapter();
                    // set Data
                    Original_Adapter.setData(Original_datas);
                    // set Adapter
                    OriginalSong_View.setAdapter(Original_Adapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<VerticalData>> call, Throwable t) {
            }
        });



        // 데이터베이스에 데이터 받아오기
        retrofitExService.getData_Cover().enqueue(new Callback<ArrayList<CoverData>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<CoverData>> call, @NonNull Response<ArrayList<CoverData>> response) {
                if (response.isSuccessful()) {
                    Cover_datas = response.body();
                    Cover_Chart = new ArrayList<>();
                    for(CoverData data: Cover_datas)
                        Cover_Chart.add(new ChartData(data.getTitle(), data.getVocal(), data.getFileurl(), true));

                    if (Cover_datas != null) {
                        for (int i = 0; i < Cover_datas.size(); i++) {
                            Log.d("data" + i, Cover_datas.get(i).getTitle() + "");
                            Log.d("data" + i, Cover_datas.get(i).getVocal() + "");
                        }
                        Log.d("getData_Cover end", "======================================");
                    }
                    //     filerul_data.add(datas.get(i).getFilerul());
                    // setLayoutManager
                    CoverSong_View.setLayoutManager(Cover_LayoutManager);
                    Cover_LayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    // init Adapter
                    coverAdapter = new CoverAdapter();
                    // set Data
                    coverAdapter.setData(Cover_datas);
                    // set Adapter
                    CoverSong_View.setAdapter(coverAdapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CoverData>> call, Throwable t) {
            }
        });
    }
}
