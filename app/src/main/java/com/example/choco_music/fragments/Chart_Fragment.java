package com.example.choco_music.fragments;

import android.content.Context;
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
import com.example.choco_music.adapters.PagerSnapWithSpanCountHelper;
import com.example.choco_music.model.AlbumData;
import com.example.choco_music.model.ChartData;
import com.example.choco_music.model.CoverData;
import com.example.choco_music.model.HomeData;
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

public class Chart_Fragment extends Fragment {

    private RecyclerView CoverSong_View,OriginalSong_View;
    private ChartAdapter OriginalAdapter;
    private ChartAdapter CoverAdapter;
    private LinearLayoutManager Cover_LayoutManager,Original_LayoutManager;
    private Button Cover_btn, Originall_btn;
    private Retrofit retrofit;
    private RetrofitExService retrofitExService;
    private ArrayList<ChartData> Original_Chart;
    private ArrayList<ChartData> Cover_Chart;
    private HashMap<Integer, ChartData> OriginalMap;
    private HashMap<Integer, ChartData> CoverMap;
    private String img_path;
    private ArrayList<HomeData> homeDatas;
    private RecentPlaySongs_OpenHelper recentPlaySongs_openHelper;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        homeDatas = new ArrayList<>();
        OriginalMap = new HashMap<>();
        CoverMap = new HashMap<>();
        Original_Chart = new ArrayList<>();
        Cover_Chart = new ArrayList<>();

        OriginalAdapter = new ChartAdapter();
        CoverAdapter = new ChartAdapter();

        init_retrofit();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.chart_fragment,null,false);

        init_btn(view);
        init_recyclerview(view);
        //init_retrofit();
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
        Original_LayoutManager = new GridLayoutManager(getContext(), 5, GridLayoutManager.HORIZONTAL, false);
        Cover_LayoutManager= new GridLayoutManager(getContext(), 5, GridLayoutManager.HORIZONTAL, false);

        OriginalSong_View.setLayoutManager(Original_LayoutManager);
        Original_LayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        // set Data
        OriginalAdapter.setData(Original_Chart);
        // set Adapter
        OriginalSong_View.setAdapter(OriginalAdapter);

        CoverSong_View.setLayoutManager(Cover_LayoutManager);
        Cover_LayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        // set Data
        CoverAdapter.setData(Cover_Chart);
        // set Adapter
        CoverSong_View.setAdapter(CoverAdapter);

        snapHelper.attachToRecyclerView(CoverSong_View);
        snapHelper2.attachToRecyclerView(OriginalSong_View);

        recentPlaySongs_openHelper = new RecentPlaySongs_OpenHelper(view.getContext());
        OriginalSong_View.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), OriginalSong_View,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        AudioApplication.getInstance().getServiceInterface().setPlayList(Original_Chart);
                        AudioApplication.getInstance().getServiceInterface().play(position);
                        //최근 들은 곡 리스트에 추가 한다.
                        if(Original_Chart.get(position).getImg_path() != null) {
                            boolean db_check = recentPlaySongs_openHelper.recent_list_Check(Original_Chart.get(position).getTitle(), Original_Chart.get(position).getVocal(),
                                    Original_Chart.get(position).getFileurl(), Original_Chart.get(position).getImg_path());
                            if(db_check){
                                String type;
                                if(Original_Chart.get(position).getType())
                                    type = "자작곡";
                                else
                                    type = "커버곡";
                                recentPlaySongs_openHelper.insertData(Original_Chart.get(position).getTitle(), Original_Chart.get(position).getVocal(),
                                        Original_Chart.get(position).getFileurl(), Original_Chart.get(position).getImg_path(), type);
                                if(recentPlaySongs_openHelper.get_recent_music().size()>6)
                                    recentPlaySongs_openHelper.deleteData(1);
                                }
                            }

                    }

                    @Override public void onLongItemClick(View view, int position) { }
                })
        );

        CoverSong_View.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), CoverSong_View,
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
                    }
                    @Override public void onLongItemClick(View view, int position) { }
                })
        );
    }


    private void init_retrofit(){
        //서버 통신을 위한 레스트로핏 적용
        retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitExService.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitExService = retrofit.create(RetrofitExService.class);

        // 데이터베이스에 데이터 받아오기
        retrofitExService.getData_Original().enqueue(new Callback<ArrayList<VerticalData>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<VerticalData>> call, @NonNull Response<ArrayList<VerticalData>> response) {
                if (response.isSuccessful()) {
                    ArrayList<VerticalData> verticalChart = response.body();

                    for(VerticalData data: verticalChart){
                        Original_Chart.add(new ChartData(data.getTitle(), data.getVocal(), data.getFileurl(), true,1));
                        OriginalMap.put(data.getId(), Original_Chart.get(Original_Chart.size()-1));
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
                    ArrayList<CoverData> coverChart = response.body();

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
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CoverData>> call, Throwable t) {
            }
        });
    }

}
