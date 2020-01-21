package com.example.choco_music.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.choco_music.Audio.AudioApplication;
import com.example.choco_music.Interface.RetrofitExService;
import com.example.choco_music.R;
import com.example.choco_music.adapters.IntroduceSongAdapter;
import com.example.choco_music.adapters.SearchAdapter;
import com.example.choco_music.model.AlbumData;
import com.example.choco_music.model.ChartData;
import com.example.choco_music.model.CoverData;
import com.example.choco_music.model.IntroduceData;
import com.example.choco_music.model.RecyclerItemClickListener;
import com.example.choco_music.model.SearchData;
import com.example.choco_music.model.TodaySongData;
import com.example.choco_music.model.VerticalData;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Search_Fragment extends Fragment implements View.OnClickListener {

    private RecyclerView Search_View , TodaySong_view;
    private RecyclerView.LayoutManager mLayoutManager, tLayoutManager;
    private SearchAdapter mAdapter;
    private IntroduceSongAdapter tAdapter;
    private Button search_btn;
    private InputMethodManager mInputMethodManager;
    private EditText mEtKeyword;
    private Retrofit retrofit;
    private RetrofitExService retrofitExService;
    private ArrayList<SearchData> Search_datas;
    private TextView genre, todaysong_list;
    private ArrayList<VerticalData> musics;
    private ArrayList<CoverData> musics_cover;
    private ArrayList<TodaySongData> todaySongDatas;
    private ArrayList<IntroduceData> introduceDatas;
    private ArrayList<AlbumData> albumDatas;
    private int songOwn, songCovered;
    private String img_path;
    private View view;
    private HashMap<Integer, SearchData> SearchMap;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search_fragment, null, false);

        Setup_Retrofit2();
        Start_TodaySong_view();
        Setup_Recyclerview(view);
        Setup_Searchview(view);
        return view;

    }
    private void Setup_Recyclerview(View view){
        genre= view.findViewById(R.id.song_search_fragment);
        todaysong_list= view.findViewById(R.id.todaysong_list);
        Search_datas = new ArrayList<SearchData>();
      //  todaySongDatas = new ArrayList<TodaySongData>();
        introduceDatas= new ArrayList<IntroduceData>();
        Search_View = view.findViewById(R.id.search_view);
        TodaySong_view = view.findViewById(R.id.todaysong_view);
        //Search_View.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        tLayoutManager = new LinearLayoutManager(getContext());
        Search_View.setLayoutManager(mLayoutManager);
        TodaySong_view.setLayoutManager(tLayoutManager);

        TodaySong_view.addOnItemTouchListener(new RecyclerItemClickListener(getContext(),  TodaySong_view,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        //AudioApplication.getInstance().getServiceInterface().setPlayList(Original_Chart);
                        AudioApplication.getInstance().getServiceInterface().play(position);

                    }

                    @Override public void onLongItemClick(View view, int position) { }
                })
        );
    }
    private void Setup_Retrofit2(){
        retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitExService.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitExService = retrofit.create(RetrofitExService.class);
    }
    private void Setup_Searchview(View view){
        mEtKeyword = view.findViewById(R.id.search_edit_frame);
        search_btn = view.findViewById(R.id.search_fragment_btn);
        search_btn.setOnClickListener(this);
        mInputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_fragment_btn:
                hideKeyboard();
                startSearch(mEtKeyword.getText().toString());
                break;
        }
    }
    public void hideKeyboard() {
        mInputMethodManager.hideSoftInputFromWindow(Search_View.getWindowToken(), 0);
    }

    public void startSearch(String title) {
        if (title.isEmpty()) {
            showEmptyFieldMessage();
        } else {
            mLayoutManager.scrollToPosition(0);
            getMusic(title);
        }
    }

    public void showEmptyFieldMessage() {
        genre.setVisibility(View.GONE);
        Search_datas.clear();
        Toast myToast = Toast.makeText(getActivity().getApplicationContext(),"검색어를 입력해주세요",Toast.LENGTH_SHORT);
        myToast.setGravity(Gravity.CENTER,0,0);
        myToast.show();
        Setup_Recyclerview(view);
    }
    public void showNotFoundMessage(String keyword) {
        Toast myToast = Toast.makeText(getActivity().getApplicationContext(),"\'" + keyword + "\' 를 찾을 수 없습니다",Toast.LENGTH_SHORT);
        myToast.setGravity(Gravity.CENTER,0,0);
        myToast.show();
    }
    public void getMusic(final String search_word) {

        //서버 통신을 위한 레스트로핏 적용
        retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitExService.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitExService = retrofit.create(RetrofitExService.class);
        Call<ArrayList<VerticalData>> call = retrofitExService.getData_home(search_word,1);
        // 데이터베이스에 데이터 받아오기

        call.enqueue(new Callback<ArrayList<VerticalData>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<VerticalData>> call, @NonNull Response<ArrayList<VerticalData>> response) {
                if (response.isSuccessful()) {
                    musics = response.body();
                    Search_datas.clear();
                    genre.setVisibility(View.GONE);
                    mAdapter = new SearchAdapter();
                    // 리스트의 모든 데이터를 검색한다.
                    for(VerticalData data: musics)
                    {
                        // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                        if (data.getTitle().contains(search_word) )
                        {
                            // 검색된 데이터를 리스트에 추가한다.
                            String search_title = data.getTitle();
                            String search_vocal = data.getVocal();
                            String search_fileurl= data.getFileurl();
                            int search_album= data.getAlbum();
                            get_img_search_data(search_title,search_vocal,search_fileurl,search_album,search_word);
                            }
                        if (data.getVocal().contains(search_word) )
                        {
                            // 검색된 데이터를 리스트에 추가한다.
                            String search_title = data.getTitle();
                            String search_vocal = data.getVocal();
                            String search_fileurl= data.getFileurl();
                            int search_album= data.getAlbum();
                            get_img_search_data(search_title,search_vocal,search_fileurl,search_album,search_word);
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<ArrayList<VerticalData>> call, Throwable t) {
            }
        });

        Call<ArrayList<CoverData>> call1 = retrofitExService.getData_Cover();

        call1.enqueue(new Callback<ArrayList<CoverData>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<CoverData>> call, @NonNull Response<ArrayList<CoverData>> response) {
                if (response.isSuccessful()) {
                    musics_cover = response.body();
                    // 리스트의 모든 데이터를 검색한다.
                    for(CoverData data: musics_cover)
                    {
                        // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                        if (data.getTitle().contains(search_word) )
                        {
                            // 검색된 데이터를 리스트에 추가한다.
                            String search_title = data.getTitle();
                            String search_vocal = data.getVocal();
                            String search_fileurl= data.getFileurl();
                            int search_album= data.getAlbum();
                            get_img_search_data_cover(search_title,search_vocal,search_fileurl,search_album,search_word);
                        }
                        if (data.getVocal().contains(search_word) )
                        {
                            // 검색된 데이터를 리스트에 추가한다.
                            String search_title = data.getTitle();
                            String search_vocal = data.getVocal();
                            String search_fileurl= data.getFileurl();
                            int search_album= data.getAlbum();
                            get_img_search_data_cover(search_title,search_vocal,search_fileurl,search_album,search_word);
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<ArrayList<CoverData>> call, Throwable t) {
            }
        });
    }
    private void Start_TodaySong_view(){

        Call<ArrayList<TodaySongData>> call_ = retrofitExService.getData_Song_Today();

        call_.enqueue(new Callback<ArrayList<TodaySongData>>()  {
            @Override
            public void onResponse(@NonNull Call<ArrayList<TodaySongData>> call, @NonNull Response<ArrayList<TodaySongData>> response) {
                if (response.isSuccessful()) {
                    todaySongDatas= response.body();

                    if (todaySongDatas != null) {
                        for (int i = 0; i <todaySongDatas.size(); i++) {
                            Log.e("data" + i, todaySongDatas.get(i).get_songOwn() + "");
                            Log.e("data" + i, todaySongDatas.get(i).get_songCovered() + "");
                            //오늘의 곡 정보를 가져와서 데이터에 담는다.
                            songOwn = todaySongDatas.get(i).get_songOwn();
                            songCovered = todaySongDatas.get(i).get_songCovered();
                            Log.e("data",""+songCovered);
                            Log.e("data",""+songOwn);
                            get_musics_data(songOwn);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<TodaySongData>> call, Throwable t) {
            }
        });
    }
    private void get_musics_data(final int Song_Number){


        Call<ArrayList<VerticalData>> call = retrofitExService.getData3(Song_Number);

        call.enqueue(new Callback<ArrayList<VerticalData>>()  {
            @Override
            public void onResponse(@NonNull Call<ArrayList<VerticalData>> call, @NonNull Response<ArrayList<VerticalData>> response) {
                if (response.isSuccessful()) {
                    musics= response.body();

                    if (musics != null) {
                        for (int i = 0; i < musics.size(); i++) {

                            if (musics.get(i).getId() == Song_Number){

                                String todaysong_title = musics.get(i).getTitle();
                                String todaysong_vocal = musics.get(i).getVocal();
                                String todaysong_comment = musics.get(i).getComment();
                                int todaysong_album = musics.get(i).getAlbum();

                                Log.e("data", "" +  todaysong_comment);
                                Log.e("data", "" + todaysong_title);
                                Log.e("data", "" + todaysong_vocal);
                                get_img_data(todaysong_comment,todaysong_title,todaysong_vocal,todaysong_album);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<VerticalData>> call, Throwable t) {
            }
        });
    }
    private void get_img_data(final String comment , final String title ,final  String vocal ,final int Song_Number){

        Call<ArrayList<AlbumData>> call = retrofitExService.AlbumData(Song_Number);

        call.enqueue(new Callback<ArrayList<AlbumData>>()  {
            @Override
            public void onResponse(@NonNull Call<ArrayList<AlbumData>> call, @NonNull Response<ArrayList<AlbumData>> response) {
                if (response.isSuccessful()) {
                    albumDatas = response.body();

                    if ( albumDatas!= null) {
                        for (int i = 0; i < albumDatas.size(); i++) {

                            if ( albumDatas.get(i).getId() == Song_Number ){

                                img_path = albumDatas.get(i).getImg_path();

                                Log.e("이미지 정보!!!!!!!!!!!!!", "" + img_path);

                                 introduceDatas.add(new IntroduceData(title, vocal,  comment,img_path ));
                            }
                        }
                    }
                    tAdapter = new IntroduceSongAdapter();
                    tAdapter.setData(introduceDatas);
                    TodaySong_view.setAdapter(tAdapter);
                }
            }
            @Override
            public void onFailure(Call<ArrayList<AlbumData>> call, Throwable t) {
            }
        });
    }
    private void get_img_search_data(final String title , final String vocal ,final String fileurl ,final int Song_Number,final String search_word){
        Call<ArrayList<AlbumData>> call = retrofitExService.AlbumData(Song_Number);
        call.enqueue(new Callback<ArrayList<AlbumData>>()  {
            @Override
            public void onResponse(@NonNull Call<ArrayList<AlbumData>> call, @NonNull Response<ArrayList<AlbumData>> response) {
                if (response.isSuccessful()) {
                    albumDatas = response.body();
                    if ( albumDatas!= null) {
                        for (int i = 0; i < albumDatas.size(); i++) {
                            if ( albumDatas.get(i).getId() == Song_Number ){
                                img_path = albumDatas.get(i).getImg_path();
                                Log.e("이미지 정보!!!!!!!!!!!!!", "" + img_path);
                                Search_datas.add(new SearchData(title,vocal,fileurl,img_path));
                            }
                        }
                    }
                    if(Search_datas.isEmpty()){
                        showNotFoundMessage(search_word);
                        Search_datas.clear();
                    }else{
                        genre.setVisibility(View.VISIBLE);
                        TodaySong_view.setVisibility(View.GONE);
                        todaysong_list.setVisibility(View.GONE);
                    }

                    mAdapter.setData(Search_datas);
                    Search_View.setAdapter(mAdapter);
                }
            }
            @Override
            public void onFailure(Call<ArrayList<AlbumData>> call, Throwable t) {
            }
        });
    }
    private void get_img_search_data_cover(final String title , final String vocal ,final String fileurl ,final int Song_Number,final String search_word){
        Call<ArrayList<AlbumData>> call = retrofitExService.AlbumData_cover(Song_Number);
        call.enqueue(new Callback<ArrayList<AlbumData>>()  {
            @Override
            public void onResponse(@NonNull Call<ArrayList<AlbumData>> call, @NonNull Response<ArrayList<AlbumData>> response) {
                if (response.isSuccessful()) {
                    albumDatas = response.body();
                    if ( albumDatas!= null) {
                        for (int i = 0; i < albumDatas.size(); i++) {
                            if ( albumDatas.get(i).getId() == Song_Number ){
                                img_path = albumDatas.get(i).getImg_path();
                                Log.e("이미지 정보!!!!!!!!!!!!!", "" + img_path);
                                Search_datas.add(new SearchData(title,vocal,fileurl,img_path));


                            }
                        }
                    }

                    if(Search_datas.isEmpty()){
                        showNotFoundMessage(search_word);
                        Search_datas.clear();
                    }else{
                        genre.setVisibility(View.VISIBLE);
                        TodaySong_view.setVisibility(View.GONE);
                        todaysong_list.setVisibility(View.GONE);
                    }

                    mAdapter.setData(Search_datas);
                    Search_View.setAdapter(mAdapter);
                }
            }
            @Override
            public void onFailure(Call<ArrayList<AlbumData>> call, Throwable t) {
            }
        });
    }

}
