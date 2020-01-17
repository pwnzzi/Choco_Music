package com.example.choco_music.activities;


import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.choco_music.Audio.AudioAdapter;
import com.example.choco_music.Audio.AudioApplication;
import com.example.choco_music.Audio.BroadcastActions;
import com.example.choco_music.R;
import com.example.choco_music.model.Blur;
import com.example.choco_music.model.ChartData;
import com.example.choco_music.model.CoverData;
import com.example.choco_music.model.VerticalData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MusicPlay_activity extends AppCompatActivity implements View.OnClickListener{

    private ImageView music_play_btn;
    private Boolean isRunning = false;
    private View runLayout;
    private FrameLayout background;
    private int chart;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(chart==1)
                updateUI_original();
            else
                updateUI_cover();
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_play_layout);

        music_play_btn = (ImageView)findViewById(R.id.music_play);
        runLayout = findViewById(R.id.linear_running_btn);

        background = findViewById(R.id.musicplay_activity_layout);
        Drawable drawable = getResources().getDrawable(R.drawable.elbum_img);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        Blur blur = new Blur(this, background, bitmap, 10, this);
        blur.run();

        music_play_btn.setOnClickListener(this);
        findViewById(R.id.play_layout_list).setOnClickListener(this);
        findViewById(R.id.play_layout_finish).setOnClickListener(this);
        findViewById(R.id.play_back).setOnClickListener(this);
        findViewById(R.id.play_front).setOnClickListener(this);

        Intent intent = getIntent();
        chart = intent.getIntExtra("chart", 0);

        Log.e("데이터",""+chart);
        if(chart == 1){
            ArrayList<VerticalData> original_datas = (ArrayList<VerticalData>)intent.getSerializableExtra("list");
            int position = intent.getIntExtra("position", 0);
            AudioApplication.getInstance().getServiceInterface().setPlayList(original_datas); // 재생목록등록
            AudioApplication.getInstance().getServiceInterface().play(position);
            registerBroadcast();

        }else if(chart == 2){
            ArrayList<CoverData> Cover_datas = (ArrayList<CoverData>)intent.getSerializableExtra("list");
            int position = intent.getIntExtra("position", 0);
            AudioApplication.getInstance().getServiceInterface().setPlayList_Cover(Cover_datas); // 재생목록등록
            AudioApplication.getInstance().getServiceInterface().play_cover(position);
            registerBroadcast();
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.play_layout_list: //커버곡 리스트 화면 불러오기
                Intent intent = new Intent(getBaseContext(), Coversong_chart.class);
                startActivity(intent);
                break;

            case R.id.play_layout_finish:
                finish();
                break;

            case R.id.music_play: // 음악 재생및 종료
                if(isRunning){
                    music_play_btn.setImageResource(R.drawable.play_btn);
                } else {
                    music_play_btn.setImageResource(R.drawable.playing_btn);
                }
                AudioApplication.getInstance().getServiceInterface().togglePlay();
                isRunning = !isRunning;
                break;

            case R.id.play_back: // 이전 곡
                AudioApplication.getInstance().getServiceInterface().rewind();
                if(chart==1)
                    updateUI_original();
                else
                    updateUI_cover();
                break;

            case R.id.play_front: // 다음 곡
                AudioApplication.getInstance().getServiceInterface().forward();
                if(chart==1)
                    updateUI_original();
                else
                    updateUI_cover();
                break;


        }
    }

    private void updateUI_original() {
        if (AudioApplication.getInstance().getServiceInterface().isPlaying()) {
            music_play_btn.setImageResource(R.drawable.playing_btn);
        } else {
            music_play_btn.setImageResource(R.drawable.play_btn);
        }
        VerticalData audioItem = AudioApplication.getInstance().getServiceInterface().getAudioItem();
        ((TextView)findViewById(R.id.play_title)).setText(audioItem.getTitle());
        ((TextView)findViewById(R.id.play_vocal)).setText(audioItem.getVocal());

    }

    private void updateUI_cover() {
        if (AudioApplication.getInstance().getServiceInterface().isPlaying()) {
            music_play_btn.setImageResource(R.drawable.playing_btn);
        } else {
            music_play_btn.setImageResource(R.drawable.play_btn);
        }
        CoverData audioItem_cover = AudioApplication.getInstance().getServiceInterface().getAudioItem_cover();
        ((TextView)findViewById(R.id.play_title)).setText(audioItem_cover.getTitle());
        ((TextView)findViewById(R.id.play_vocal)).setText(audioItem_cover.getVocal());

    }

    private void registerBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastActions.PREPARED);
        filter.addAction(BroadcastActions.PLAY_STATE_CHANGED);
        registerReceiver(mBroadcastReceiver, filter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterBroadcast();
    }

    private void unregisterBroadcast() {
        unregisterReceiver(mBroadcastReceiver);
    }
}
