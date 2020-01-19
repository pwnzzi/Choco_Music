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
import com.example.choco_music.adapters.CoverAdapter;
import com.example.choco_music.model.CoverData;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Coversong_chart extends AppCompatActivity {

    private RecyclerView Cover_View;
    private CoverAdapter Cover_Adapter;
    private LinearLayoutManager Cover_LayoutManager;
    private Retrofit retrofit;
    RetrofitExService retrofitExService;
    private ArrayList<CoverData> Cover_datas;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_list);
       // init_retrofit();

    }

    private void init_retrofit(){
   /*     Cover_View = (RecyclerView)findViewById(R.id.chart_list);
        //init LayoutManager
        Cover_LayoutManager  = new LinearLayoutManager(this);
        //서버 통신을 위한 레스트로핏 적용
        retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitExService.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitExService = retrofit.create(RetrofitExService.class);

        // 데이터베이스에 데이터 받아오기
        retrofitExService.getData_Cover().enqueue(new Callback<ArrayList<CoverData>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<CoverData>> call, @NonNull Response<ArrayList<CoverData>> response) {
                if (response.isSuccessful()) {
                    Cover_datas = response.body();

                    if (Cover_datas != null) {
                        for (int i = 0; i < Cover_datas.size(); i++) {
                            Log.d("data" + i, Cover_datas.get(i).getTitle() + "");
                            Log.d("data" + i, Cover_datas.get(i).getVocal() + "");
                        }
                        Log.d("getData1 end", "======================================");
                    }
                    //     filerul_data.add(datas.get(i).getFilerul());
                    // setLayoutManager
                    Cover_View.setLayoutManager(Cover_LayoutManager);
                    Cover_LayoutManager .setOrientation(LinearLayoutManager.VERTICAL); // 기본값이 VERTICAL
                    // init Adapter
                    Cover_Adapter = new CoverAdapter();
                    // set Data
                    Cover_Adapter.setData(Cover_datas);
                    // set Adapter
                    Cover_View.setAdapter(Cover_Adapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CoverData>> call, Throwable t) {
            }
        });*/
    }
}
