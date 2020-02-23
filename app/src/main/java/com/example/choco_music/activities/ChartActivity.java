package com.example.choco_music.activities;

import android.app.AlertDialog;
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
import com.example.choco_music.adapters.Playlist_Apdapter;
import com.example.choco_music.model.AlbumData;
import com.example.choco_music.model.ChartData;
import com.example.choco_music.model.CoverData;
import com.example.choco_music.model.Playlist_Database_OpenHelper;
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

public class ChartActivity extends AppCompatActivity {

    private RecyclerView CoverSong_View;
    private ChartAdapter CoverAdapter;
    private LinearLayoutManager Cover_LayoutManager;
    private Retrofit retrofit;
    RetrofitExService retrofitExService;
    private ArrayList<ChartData> ChartData;
    private HashMap<Integer, ChartData> ChartMap;
    private Playlist_Database_OpenHelper openHelper;
    private RecentPlaySongs_OpenHelper recentPlaySongs_openHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_list);
        setup_view();
        init_retrofit();


    }
    private void init_retrofit(){
        //type_number이 1일때 자작곡, 2일때 커버곡, 3일때 playlist곡을 반환;
        final int type_number = AudioApplication.getInstance().getServiceInterface().getAudioItem().getType_number();

        //서버 통신을 위한 레스트로핏 적용
        retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitExService.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitExService = retrofit.create(RetrofitExService.class);

        if(type_number == 1){
            // 데이터베이스에 데이터 받아오기
            retrofitExService.getData_Original().enqueue(new Callback<ArrayList<VerticalData>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<VerticalData>> call, @NonNull Response<ArrayList<VerticalData>> response) {
                    if (response.isSuccessful()) {
                        ArrayList<VerticalData> verticalChart = response.body();
                        ChartData= new ArrayList<>();

                        for(VerticalData data: verticalChart){
                            ChartData.add(new ChartData(data.getTitle(), data.getVocal(), data.getFileurl(), true,1));
                            ChartMap.put(data.getId(), ChartData.get(ChartData.size()-1));
                            //Log.d(data.getTitle(), data.getFileurl());

                            final VerticalData v = data;
                            Call<ArrayList<AlbumData>> call2 = retrofitExService.AlbumData_Original(data.getId());
                            call2.enqueue(new Callback<ArrayList<AlbumData>>()  {
                                @Override
                                public void onResponse(@NonNull Call<ArrayList<AlbumData>> call, @NonNull Response<ArrayList<AlbumData>> response) {
                                    if (response.isSuccessful()) {
                                        ArrayList<AlbumData> albumDatas = response.body();
                                        if (albumDatas != null) {
                                            for (int i = 0; i < albumDatas.size(); i++) {
                                                if(v.getAlbum() == albumDatas.get(i).getId()){
                                                    //Log.d("da"+v.getId(), albumDatas.get(i).getImg_path());
                                                    ChartMap.get(v.getId()).setImg_path(albumDatas.get(i).getImg_path());
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
                        CoverAdapter.setData(ChartData);
                        // set Adapter
                        CoverSong_View.setAdapter(CoverAdapter);
                    }
                }
                @Override
                public void onFailure(Call<ArrayList<VerticalData>> call, Throwable t) {
                }
            });

        }else if(type_number == 2){
            // 데이터베이스에 데이터 받아오기
            retrofitExService.getData_Cover().enqueue(new Callback<ArrayList<CoverData>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<CoverData>> call, @NonNull Response<ArrayList<CoverData>> response) {
                    if (response.isSuccessful()) {
                        ArrayList<CoverData> coverChart = response.body();
                        ChartData = new ArrayList<>();

                        for(CoverData data: coverChart){
                            ChartData.add(new ChartData(data.getTitle(), data.getVocal(), data.getFileurl(), false,2));
                            ChartMap.put(data.getId(), ChartData.get(ChartData.size()-1));
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
                                                    ChartMap.get(v.getId()).setImg_path(albumDatas.get(i).getImg_path());
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
                        CoverAdapter.setData(ChartData);
                        // set Adapter
                        CoverSong_View.setAdapter(CoverAdapter);
                    }
                }
                @Override
                public void onFailure(Call<ArrayList<CoverData>> call, Throwable t) {
                }
            });
        }
        else if(type_number == 3){
            openHelper = new Playlist_Database_OpenHelper(this);
            ChartData = openHelper.get_Music_chart();
            // set Data
            CoverAdapter.setData(ChartData);
            // set Adapter
            CoverSong_View.setAdapter(CoverAdapter);
        }
    }
    private void setup_view(){
        CoverSong_View = (RecyclerView)findViewById(R.id.chart_list);
        recentPlaySongs_openHelper = new RecentPlaySongs_OpenHelper(this);
        ChartMap = new HashMap<>();
        //init LayoutManager
        Cover_LayoutManager  = new LinearLayoutManager(this);

        CoverSong_View.setLayoutManager(Cover_LayoutManager);
        Cover_LayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        CoverAdapter = new ChartAdapter();

        CoverSong_View.addOnItemTouchListener(new RecyclerItemClickListener(this, CoverSong_View,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                       /* AudioApplication.getInstance().getServiceInterface().setPlayList(ChartData);
                        AudioApplication.getInstance().getServiceInterface().play(position);
                        Intent intent = new Intent(ChartActivity.this, MusicPlay_activity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();*/
                        AudioApplication.getInstance().getServiceInterface().setPlayList(ChartData);
                        AudioApplication.getInstance().getServiceInterface().play(position);
                        //최근 들은 곡 리스트에 추가 한다.
                        if(ChartData.get(position).getImg_path() != null) {
                            boolean db_check = recentPlaySongs_openHelper.recent_list_Check(ChartData.get(position).getTitle(), ChartData.get(position).getVocal(),
                                    ChartData.get(position).getFileurl(), ChartData.get(position).getImg_path());
                            if(db_check){
                                String type;
                                if(ChartData.get(position).getType())
                                    type = "자작곡";
                                else
                                    type = "커버곡";
                                recentPlaySongs_openHelper.insertData(ChartData.get(position).getTitle(), ChartData.get(position).getVocal(),
                                        ChartData.get(position).getFileurl(), ChartData.get(position).getImg_path(), type);
                                if(recentPlaySongs_openHelper.get_recent_music().size()>6)
                                    recentPlaySongs_openHelper.deleteData(1);
                            }
                        }
                        Intent intent = new Intent(ChartActivity.this, MusicPlayActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                    @Override public void onLongItemClick(View view, int position) { }
                })
        );

    }
}
