package com.example.choco_music.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.choco_music.Audio.AudioAdapter;
import com.example.choco_music.R;
import com.example.choco_music.model.VerticalData;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class TempActivity extends AppCompatActivity /*implements View.OnClickListener*/ {
    private final static int LOADER_ID = 0x001;

    private AudioAdapter mAdapter;
    ArrayList<VerticalData> datas;

    private ImageView mImgAlbumArt,imageView;
    private TextView mTxtTitle;
    private ImageButton mBtnPlayPause;
    Bitmap bmImg;

   // private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
    /*    @Override
        public void onReceive(Context context, Intent intent) {
            updateUI();
        }
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);

        File f = new File("https://chocomusic.s3.ap-northeast-2.amazonaws.com/StaticFile/SongOwn/%EC%88%A8_6DAYLAYOFF.jpg");
    //    Bitmap myBitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
        imageView = findViewById(R.id.img_temp);
    //    imageView.setImageBitmap(myBitmap);

        Thread mThread = new Thread() {
            public void run(){


                URL myFileUrl = null;
                try {
                    myFileUrl = new URL("https://chocomusic.s3.ap-northeast-2.amazonaws.com/StaticFile/SongOwn/%EC%88%A8_6DAYLAYOFF.jpg");

                    HttpURLConnection conn= (HttpURLConnection)myFileUrl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    bmImg = BitmapFactory.decodeStream(is);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }

            }

        };

        mThread.start();

        try{
            mThread.join();
            imageView.setImageBitmap(bmImg);
        }catch (InterruptedException e){
            e.printStackTrace();
        }







/*
        getAudioListFromMediaDatabase();

        mImgAlbumArt = (ImageView) findViewById(R.id.img_albumart);
        mTxtTitle = (TextView) findViewById(R.id.txt_title);
        mBtnPlayPause = (ImageButton) findViewById(R.id.btn_play_pause);
        findViewById(R.id.lin_miniplayer).setOnClickListener(this);
        findViewById(R.id.btn_rewind).setOnClickListener(this);
        mBtnPlayPause.setOnClickListener(this);
        findViewById(R.id.btn_forward).setOnClickListener(this);

        //서버 통신을 위한 레스트로핏 적용
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitExService.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitExService retrofitExService = retrofit.create(RetrofitExService.class);

        // 데이터베이스에 데이터 받아오기
        retrofitExService.getData2().enqueue(new Callback<ArrayList<VerticalData>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<VerticalData>> call, @NonNull Response<ArrayList<VerticalData>> response) {
                if (response.isSuccessful()) {
                    datas = response.body();
                    AudioApplication.getInstance().getServiceInterface().setPlayList(datas); // 재생목록등록
               //     AudioApplication.getInstance().getServiceInterface().play(0);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<VerticalData>> call, Throwable t) {
                    t.printStackTrace();
            }
        });

        registerBroadcast();
      //  updateUI();
    }

    private void registerBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastActions.PREPARED);
        filter.addAction(BroadcastActions.PLAY_STATE_CHANGED);
        registerReceiver(mBroadcastReceiver, filter);
    }

    private void unregisterBroadcast() {
        unregisterReceiver(mBroadcastReceiver);
    }

    private void updateUI() {
        if (AudioApplication.getInstance().getServiceInterface().isPlaying()) {
            mBtnPlayPause.setImageResource(R.drawable.playing_btn);
        } else {
            mBtnPlayPause.setImageResource(R.drawable.play_btn);
        }
    }

    private void getAudioListFromMediaDatabase() {
       //DB접근
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_miniplayer:
                // 플레이어 화면으로 이동할 코드가 들어갈 예정
                break;
            case R.id.btn_rewind:
                // 이전곡으로 이동
                AudioApplication.getInstance().getServiceInterface().rewind();
                break;
            case R.id.btn_play_pause:
                // 재생 또는 일시정지
                AudioApplication.getInstance().getServiceInterface().togglePlay();
                break;
            case R.id.btn_forward:
                // 다음곡으로 이동
                AudioApplication.getInstance().getServiceInterface().forward();
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterBroadcast();*/
        }

}

