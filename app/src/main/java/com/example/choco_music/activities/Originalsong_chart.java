package com.example.choco_music.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.choco_music.Interface.RetrofitExService;
import com.example.choco_music.R;
import com.example.choco_music.adapters.ChartAdapter;
import com.example.choco_music.model.VerticalData;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Originalsong_chart extends AppCompatActivity {

    private RecyclerView  OriginalSong_View;
    private ChartAdapter Original_Adapter;
    private LinearLayoutManager Original_LayoutManager;
    private Retrofit retrofit;
    private RetrofitExService retrofitExService;
    private ArrayList<VerticalData> Original_datas;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_list);

        init_retrofit();

    }
    private void init_retrofit(){
        OriginalSong_View = (RecyclerView)findViewById(R.id.chart_list);
        //init LayoutManager
        Original_LayoutManager  = new LinearLayoutManager(this);
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
                    Original_LayoutManager .setOrientation(LinearLayoutManager.VERTICAL); // 기본값이 VERTICAL
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
    }

}
