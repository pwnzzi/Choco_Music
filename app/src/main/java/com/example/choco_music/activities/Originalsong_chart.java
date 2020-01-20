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
import com.example.choco_music.model.AlbumData;
import com.example.choco_music.model.ChartData;
import com.example.choco_music.model.VerticalData;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Originalsong_chart extends AppCompatActivity {

    private RecyclerView  OriginalSong_View;
    private ChartAdapter OriginalAdapter;
    private LinearLayoutManager Original_LayoutManager;
    private Retrofit retrofit;
    private RetrofitExService retrofitExService;
    private ArrayList<ChartData> Original_Chart;
    private HashMap<Integer, ChartData> OriginalMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_list);

        init_retrofit();

    }
    private void init_retrofit(){
        OriginalSong_View = (RecyclerView)findViewById(R.id.chart_list);
        OriginalMap = new HashMap<>();
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
                    ArrayList<VerticalData> verticalChart = response.body();
                    Original_Chart = new ArrayList<>();

                    for(VerticalData data: verticalChart){
                        Original_Chart.add(new ChartData(data.getTitle(), data.getVocal(), data.getFileurl(), true));
                        OriginalMap.put(data.getId(), Original_Chart.get(Original_Chart.size()-1));
                        //Log.d(data.getTitle(), data.getFileurl());

                        final VerticalData v = data;
                        Call<ArrayList<AlbumData>> call2 = retrofitExService.AlbumData(data.getId());
                        call2.enqueue(new Callback<ArrayList<AlbumData>>()  {
                            @Override
                            public void onResponse(@NonNull Call<ArrayList<AlbumData>> call, @NonNull Response<ArrayList<AlbumData>> response) {
                                if (response.isSuccessful()) {
                                    ArrayList<AlbumData> albumDatas = response.body();
                                    if (albumDatas != null) {
                                        for (int i = 0; i < albumDatas.size(); i++) {
                                            if(v.getAlbum() == albumDatas.get(i).getId()){
                                                //Log.d("da"+v.getId(), albumDatas.get(i).getImg_path());
                                                OriginalMap.get(v.getId()).setImg_path(albumDatas.get(i).getImg_path());
                                                OriginalAdapter.notifyDataSetChanged();
                                            }
                                        }
                                    }
                                }
                            }
                            @Override
                            public void onFailure(Call<ArrayList<AlbumData>> call, Throwable t) {
                                t.printStackTrace();
                            }
                        });
                    }
                    OriginalSong_View.setLayoutManager(Original_LayoutManager);
                    Original_LayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    OriginalAdapter = new ChartAdapter();
                    // set Data
                    OriginalAdapter.setData(Original_Chart);
                    // set Adapter
                    OriginalSong_View.setAdapter(OriginalAdapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<VerticalData>> call, Throwable t) {
            }
        });
    }

}
