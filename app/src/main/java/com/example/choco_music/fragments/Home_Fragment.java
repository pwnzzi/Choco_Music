package com.example.choco_music.fragments;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.choco_music.Interface.RetrofitExService;
import com.example.choco_music.R;
import com.example.choco_music.adapters.VerticalAdapter;
import com.example.choco_music.model.Blur;
import com.example.choco_music.model.VerticalData;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Home_Fragment extends Fragment implements View.OnClickListener{

    private RecyclerView mVerticalView;
    private VerticalAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private LinearLayout layoutBottomSheet;
    private CoordinatorLayout background;
    private ArrayList<Button> btn_tags;
    private Button music_evaluate_btn, confirmButton;
    private  Boolean isRunning = false;
    private ArrayList<Boolean> clicks;
    public MediaPlayer mediaPlayer;
    private ImageView music_play_btn;
    private View view;
    private Button cancelButton;
    private ArrayList<ImageView> stars;
    private int currentStar = 5;
    private boolean playPause;
    private boolean initalStage = true;
    //private String url;
    private String[] url_list=null;
    private int MAX_ITEM_COUNT = 50;
    private ProgressDialog progressDialog;
    private int position;
    ArrayList<VerticalData> datas;

    BottomSheetBehavior sheetBehavior;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, null, false);

        //서버 통신을 위한 레스트로핏 적용
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitExService.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitExService retrofitExService = retrofit.create(RetrofitExService.class);


        //init LayoutManager
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // 기본값이 VERTICAL


        //태그 버튼 적용
        //  btn_tag();

        layoutBottomSheet = view.findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        music_evaluate_btn = (Button) view.findViewById(R.id.music_evaluate_btn);
        music_play_btn = (ImageView) view.findViewById(R.id.home_fragment_play_btn);

     /*   music_play_btn.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRunning){
                    mediaPlayer.stop();
                    music_play_btn.setImageResource(R.drawable.ic_triangle_right);
                } else {
                    mediaPlayer = MediaPlayer.create(getContext(), R.raw.test);
                    music_play_btn.setImageResource(R.drawable.playing_btn);

                    mediaPlayer.start();
                }
                isRunning = !isRunning;
            }
        });*/

        music_evaluate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        //RecyclerVier binding
        mVerticalView = (RecyclerView) view.findViewById(R.id.vertivcal_list);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mVerticalView);


        // 홈 배경화면에 앨범 이미지 어둡게 세팅
        background = view.findViewById(R.id.home_fragment_layout);
        Drawable drawable = getResources().getDrawable(R.drawable.elbum_img);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        Blur blur = new Blur(getContext(), background, bitmap, 10, getActivity());
        blur.run();

        // 데이터베이스에 데이터 받아오기
        retrofitExService.getData2(1).enqueue(new Callback<ArrayList<VerticalData>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<VerticalData>> call, @NonNull Response<ArrayList<VerticalData>> response) {
                if (response.isSuccessful()) {
                    datas = response.body();

                    if (datas != null) {
                        for (int i = 0; i < datas.size(); i++) {
                            Log.d("data" + i, datas.get(i).getTitle() + "");
                            Log.d("data" + i, datas.get(i).getId() + "");
                            Log.d("data" + i, datas.get(i).getLyricist() + "");
                            Log.d("data" + i, datas.get(i).getVocal() + "");
                            Log.d("data" + i, datas.get(i).getBucketName() + "");
                            Log.d("data" + i, datas.get(i).getComment() + "");
                            Log.d("data" + i, datas.get(i).getLyrics() + "");
                            Log.d("data" + i, datas.get(i).getGenre() + "");
                            Log.d("data" + i, datas.get(i).getFilerul() + "");
                            //곡 url을 저장한다.
                            //     url_list[i]=datas.get(i).getFilerul();

                        }
                        Log.d("getData2 end", "======================================");
                    }
                    //     filerul_data.add(datas.get(i).getFilerul());
                    // setLayoutManager
                    mVerticalView.setLayoutManager(mLayoutManager);
                    // init Adapter
                    mAdapter = new VerticalAdapter();
                    // set Data
                    mAdapter.setData(datas);
                    // set Adapter
                    mVerticalView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<VerticalData>> call, Throwable t) {

            }
        });
        // 취소, 완료 버튼
        cancelButton = view.findViewById(R.id.sheet_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                cancelButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_evaluate_homefragment));
            }
        });
        // 리사이클러 뷰가 스크롤 될때 현재 위치를 받아오기 위해서 사용하는 코드
        mVerticalView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                initalStage = true;
                music_play_btn.setImageResource(R.drawable.ic_triangle_right);
                playPause = false;
                if(mediaPlayer != null)
                    mediaPlayer.reset();
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    position = getCurrentItem();
                }
            }
        });


        confirmButton = view.findViewById(R.id.sheet_confirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_evaluate_homefragment));
            }
        });
        // 별
        stars = new ArrayList<>();
        stars.add((ImageView) view.findViewById(R.id.star_1));
        stars.add((ImageView) view.findViewById(R.id.star_2));
        stars.add((ImageView) view.findViewById(R.id.star_3));
        stars.add((ImageView) view.findViewById(R.id.star_4));
        stars.add((ImageView) view.findViewById(R.id.star_5));
        for (int i = 0; i < 5; ++i)
            stars.get(i).setOnClickListener(this);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        music_play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //데이터 베이스에서 받아온 데이터를 리사이클러뷰의 위치에 따라 url을 받아온다.
                //  String url=filerul_data.get(position1+1);
                System.out.println("current " + position);

                if (!playPause) {
                    //     buttonStart.setText("Pause Streaming");
                    music_play_btn.setImageResource(R.drawable.playing_btn);
                    if (initalStage) {
                        new Player().execute(datas.get(position).getFilerul());
                        //      new Player().execute(url);
                    } else {
                        if (!mediaPlayer.isPlaying()) {
                            mediaPlayer.start();
                        }
                    }
                    playPause = true;
                } else {
                    //    buttonStart.setText("Launch Streaming");
                    music_play_btn.setImageResource(R.drawable.ic_triangle_right);
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    }
                    playPause = false;
                }
            }
        });
        return view;

 /*if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {

                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }*/
    }



    private void btn_tag() {
        btn_tags = new ArrayList<>();
        clicks = new ArrayList<>();
        btn_tags.add((Button) view.findViewById(R.id.btn_tag_1));
        btn_tags.add((Button) view.findViewById(R.id.btn_tag_2));
        btn_tags.add((Button) view.findViewById(R.id.btn_tag_3));
        btn_tags.add((Button) view.findViewById(R.id.btn_tag_4));
        btn_tags.add((Button) view.findViewById(R.id.btn_tag_5));
        btn_tags.add((Button) view.findViewById(R.id.btn_tag_6));
        btn_tags.add((Button) view.findViewById(R.id.btn_tag_7));
        btn_tags.add((Button) view.findViewById(R.id.btn_tag_8));
        btn_tags.add((Button) view.findViewById(R.id.btn_tag_9));
        btn_tags.add((Button) view.findViewById(R.id.btn_tag_10));

        for (int i = 0; i != 10; ++i) {
            btn_tags.get(i).setOnClickListener(this);
            clicks.add(true);
        }
    }

    @Override
    public void onClick(View view) {
        int btns = 0;
        switch (view.getId()){
            case R.id.star_1:
                currentStar = 1; break;
            case R.id.star_2:
                currentStar = 2; break;
            case R.id.star_3:
                currentStar = 3; break;
            case R.id.star_4:
                currentStar = 4; break;
            case R.id.star_5:
                currentStar = 5; break;
            case R.id.btn_tag_1:
                btns = 1; break;
            case R.id.btn_tag_2:
                btns = 2; break;
            case R.id.btn_tag_3:
                btns = 3; break;
            case R.id.btn_tag_4:
                btns = 4; break;
            case R.id.btn_tag_5:
                btns = 5; break;
            case R.id.btn_tag_6:
                btns = 6; break;
            case R.id.btn_tag_7:
                btns = 7; break;
            case R.id.btn_tag_8:
                btns = 8; break;
            case R.id.btn_tag_9:
                btns = 9; break;
            case R.id.btn_tag_10:
                btns = 10; break;
        }

        for(int i = 0; i < 5; ++i)
            stars.get(i).setImageResource(R.drawable.star_unselected);
        for(int i=0; i<currentStar; ++i)
            stars.get(i).setImageResource(R.drawable.star_selected);
    }

    private int getCurrentItem(){
        return ((LinearLayoutManager)mVerticalView.getLayoutManager())
                .findFirstVisibleItemPosition();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mediaPlayer != null){
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
            progressDialog.dismiss();
        }
    }

    //음악 플레이어

    class Player extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getContext(), "Buffering.", "Buffering");
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            Boolean prepared = false;

            try {
                mediaPlayer.setDataSource(strings[0]);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        initalStage = true;
                        playPause = false;
                        music_play_btn.setImageResource(R.drawable.ic_triangle_right);
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                    }
                });

                mediaPlayer.prepare();
                prepared = true;
            } catch (IOException e) {
                Log.e("MyAudioStreamingApp",e.getMessage());
                prepared = false;
                e.printStackTrace();
            }
            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            progressDialog.dismiss();
            mediaPlayer.start();
            initalStage = false;
        }
    }

}


