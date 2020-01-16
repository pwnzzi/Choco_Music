package com.example.choco_music.fragments;

import android.content.ClipData;
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

import com.example.choco_music.Interface.RetrofitExService;
import com.example.choco_music.R;
import com.example.choco_music.adapters.SearchAdapter;
import com.example.choco_music.adapters.TodaySongAdapter;
import com.example.choco_music.model.SearchData;
import com.example.choco_music.model.TodaySongData;
import com.example.choco_music.model.VerticalData;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Search_Fragment extends Fragment implements View.OnClickListener {

    private RecyclerView Search_View , TodaySong_view;
    private RecyclerView.LayoutManager mLayoutManager, tLayoutManager;
    private SearchAdapter mAdapter;
    private TodaySongAdapter tAdapter;
    private Button search_btn;
    private InputMethodManager mInputMethodManager;
    private EditText mEtKeyword;
    private Retrofit retrofit;
    private RetrofitExService retrofitExService;
    private ArrayList<SearchData> Search_datas;
    private TextView genre, todaysong_list;
    private ArrayList<VerticalData> musics;
    private ArrayList<TodaySongData> todaySongDatas;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, null, false);
        Setup_Recyclerview(view);
        Setup_Searchview(view);
        return view;
    }
    private void Setup_Recyclerview(View view){
        genre= view.findViewById(R.id.song_search_fragment);
        todaysong_list= view.findViewById(R.id.todaysong_list);
        Search_datas = new ArrayList<SearchData>();
        todaySongDatas = new ArrayList<TodaySongData>();
        Search_View = view.findViewById(R.id.search_view);
        TodaySong_view = view.findViewById(R.id.todaysong_view);
        //Search_View.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        tLayoutManager = new LinearLayoutManager(getContext());
        Search_View.setLayoutManager(mLayoutManager);
        TodaySong_view.setLayoutManager(tLayoutManager);
        //오늘의 추천곡을 리사이클러뷰에 연결한다.
        todaySongDatas.add(new TodaySongData("그시간속","백선욱"));
        tAdapter = new TodaySongAdapter();
        tAdapter.setData(todaySongDatas);
        TodaySong_view.setAdapter(tAdapter);
        Log.e("--------",""+todaySongDatas.get(0).getTitle());

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
    }
    public void showNotFoundMessage(String keyword) {
        Toast myToast = Toast.makeText(getActivity().getApplicationContext(),"\'" + keyword + "\' 를 찾을 수 없습니다",Toast.LENGTH_SHORT);
        myToast.setGravity(Gravity.CENTER,0,0);
        myToast.show();
    }
    public void getMusic(final String title) {
        //서버 통신을 위한 레스트로핏 적용
        retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitExService.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitExService = retrofit.create(RetrofitExService.class);
        Call<ArrayList<VerticalData>> call = retrofitExService.getData_home(title,1);
        // 데이터베이스에 데이터 받아오기
        call.enqueue(new Callback<ArrayList<VerticalData>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<VerticalData>> call, @NonNull Response<ArrayList<VerticalData>> response) {
                if (response.isSuccessful()) {
                    musics = response.body();
                    Search_datas.clear();
                    genre.setVisibility(View.GONE);
                    // 리스트의 모든 데이터를 검색한다.
                    for(int i = 0;i <  musics.size(); i++)
                    {
                        // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                        if (musics.get(i).getTitle().contains(title) )
                        {
                            // 검색된 데이터를 리스트에 추가한다.
                            String search_title = musics.get(i).getTitle();
                            String search_vocal = musics.get(i).getVocal();
                            Search_datas.add(new SearchData(search_title,search_vocal));
                            }
                        if (musics.get(i).getVocal().contains(title) )
                        {
                            // 검색된 데이터를 리스트에 추가한다.
                            String search_title = musics.get(i).getTitle();
                            String search_vocal = musics.get(i).getVocal();
                            Search_datas.add(new SearchData(search_title,search_vocal));
                        }
                    }
                if(Search_datas.isEmpty()){
                        showNotFoundMessage(title);
                        Search_datas.clear();
                    }else{
                    genre.setVisibility(View.VISIBLE);
                    TodaySong_view.setVisibility(View.GONE);
                    todaysong_list.setVisibility(View.GONE);

                }

                // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
                    mAdapter = new SearchAdapter();
                    mAdapter.setData(Search_datas);
                    Search_View.setAdapter(mAdapter);
                }
            }
            @Override
            public void onFailure(Call<ArrayList<VerticalData>> call, Throwable t) {
            }
        });
    }
}
