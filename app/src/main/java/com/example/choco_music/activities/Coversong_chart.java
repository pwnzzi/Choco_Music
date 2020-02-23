package com.example.choco_music.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.choco_music.Audio.AudioApplication;
import com.example.choco_music.Interface.RetrofitExService;
import com.example.choco_music.R;
import com.example.choco_music.adapters.ChartAdapter;
import com.example.choco_music.adapters.CoverAdapter;
import com.example.choco_music.model.AlbumData;
import com.example.choco_music.model.ChartData;
import com.example.choco_music.model.CoverData;
import com.example.choco_music.model.RecentPlaySongs_OpenHelper;
import com.example.choco_music.model.RecyclerItemClickListener;
import com.example.choco_music.model.VerticalData;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Coversong_chart extends AppCompatActivity {

    private RecyclerView CoverSong_View;
    private ChartAdapter CoverAdapter;
    private LinearLayoutManager Cover_LayoutManager;
    private Retrofit retrofit;
    RetrofitExService retrofitExService;
    private ArrayList<ChartData> Cover_Chart;
    private HashMap<Integer, ChartData> CoverMap;
    private RecentPlaySongs_OpenHelper recentPlaySongs_openHelper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_list);
        setup_view();
        init_retrofit();


    }

    private void init_retrofit(){

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
                    ArrayList<CoverData> coverChart = response.body();
                    Cover_Chart = new ArrayList<>();

                    for(CoverData data: coverChart){
                        Cover_Chart.add(new ChartData(data.getTitle(), data.getVocal(), data.getFileurl(), false,2));
                        CoverMap.put(data.getId(), Cover_Chart.get(Cover_Chart.size()-1));
                        //Log.d(data.getTitle(), data.getFileurl());

                        final CoverData v = data;
                        Call<ArrayList<AlbumData>> call2 = retrofitExService.AlbumData_Cover(data.getId());
                        call2.enqueue(new Callback<ArrayList<AlbumData>>()  {
                            @Override
                            public void onResponse(@NonNull Call<ArrayList<AlbumData>> call, @NonNull Response<ArrayList<AlbumData>> response) {
                                if (response.isSuccessful()) {
                                    ArrayList<AlbumData> albumDatas = response.body();
                                    if (albumDatas != null) {
                                        for (int i = 0; i < albumDatas.size(); i++) {
                                            if(v.getAlbum() == albumDatas.get(i).getId()){
                                                //Log.d("da"+v.getId(), albumDatas.get(i).getImg_path());
                                                CoverMap.get(v.getId()).setImg_path(albumDatas.get(i).getImg_path());
                                                CoverAdapter.notifyDataSetChanged();
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

                    // set Data
                    CoverAdapter.setData(Cover_Chart);
                    // set Adapter
                    CoverSong_View.setAdapter(CoverAdapter);


                }
            }

            @Override
            public void onFailure(Call<ArrayList<CoverData>> call, Throwable t) {
            }
        });
    }
    private void setup_view(){
        CoverSong_View = (RecyclerView)findViewById(R.id.chart_list);

        CoverMap = new HashMap<>();
        //init LayoutManager
        Cover_LayoutManager  = new LinearLayoutManager(this);
        recentPlaySongs_openHelper = new RecentPlaySongs_OpenHelper(this);
        CoverSong_View.setLayoutManager(Cover_LayoutManager);
        Cover_LayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        CoverAdapter = new ChartAdapter();

        CoverSong_View.addOnItemTouchListener(new RecyclerItemClickListener(this, CoverSong_View,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        AudioApplication.getInstance().getServiceInterface().setPlayList(Cover_Chart);
                        AudioApplication.getInstance().getServiceInterface().play(position);
                        //최근 들은 곡 리스트에 추가 한다.
                        if(Cover_Chart.get(position).getImg_path() != null) {
                            boolean db_check = recentPlaySongs_openHelper.recent_list_Check(Cover_Chart.get(position).getTitle(), Cover_Chart.get(position).getVocal(),
                                    Cover_Chart.get(position).getFileurl(), Cover_Chart.get(position).getImg_path());
                            if(db_check){
                                String type;
                                if(Cover_Chart.get(position).getType())
                                    type = "자작곡";
                                else
                                    type = "커버곡";
                                recentPlaySongs_openHelper.insertData(Cover_Chart.get(position).getTitle(), Cover_Chart.get(position).getVocal(),
                                        Cover_Chart.get(position).getFileurl(), Cover_Chart.get(position).getImg_path(), type);
                                if(recentPlaySongs_openHelper.get_recent_music().size()>6)
                                    recentPlaySongs_openHelper.deleteData(1);
                            }
                        }
                        Intent intent = new Intent(Coversong_chart.this, MusicPlayActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    @Override public void onLongItemClick(View view, int position) { }
                })
        );

    }

}
