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

    private ImageView music_play_btn, replay_btn, shuffle_btn;
    private Boolean isRunning = false;
    private View runLayout;
    private FrameLayout background;
    private ImageView btn_shuffle, btn_repeat;
    private int chart;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI();
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_play_layout);

        music_play_btn = (ImageView)findViewById(R.id.music_play);
        btn_shuffle = findViewById(R.id.music_play_shuffle);
        btn_repeat = findViewById(R.id.music_play_replay);
        runLayout = findViewById(R.id.linear_running_btn);

        background = findViewById(R.id.musicplay_activity_layout);
        //Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        //Blur blur = new Blur(this, background, bitmap, 10, this);
        //blur.run();

        music_play_btn.setOnClickListener(this);
        findViewById(R.id.play_layout_list).setOnClickListener(this);
        findViewById(R.id.play_layout_finish).setOnClickListener(this);
        findViewById(R.id.play_back).setOnClickListener(this);
        findViewById(R.id.play_front).setOnClickListener(this);
        btn_repeat.setOnClickListener(this);
        btn_shuffle.setOnClickListener(this);


        registerBroadcast();
        updateUI();
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
                AudioApplication.getInstance().getServiceInterface().togglePlay();
                updateUI();
                isRunning = !isRunning;
                break;

            case R.id.play_back: // 이전 곡
                AudioApplication.getInstance().getServiceInterface().rewind();
                updateUI();
                break;

            case R.id.play_front: // 다음 곡
                AudioApplication.getInstance().getServiceInterface().forward();
                updateUI();
                break;

            case R.id.music_play_shuffle:
                AudioApplication.getInstance().getServiceInterface().toggleShuffle();
                updateUI();
                break;

            case R.id.music_play_replay:
                AudioApplication.getInstance().getServiceInterface().toggleRepeat();
                updateUI();
                break;

        }
    }

    private void updateUI() {
        if (AudioApplication.getInstance().getServiceInterface().isPlaying()) {
            music_play_btn.setImageResource(R.drawable.playing_btn);
        } else {
            music_play_btn.setImageResource(R.drawable.play_btn);
        }
        if (AudioApplication.getInstance().getServiceInterface().getShuffle()) {
            btn_shuffle.setImageResource(R.drawable.shuffle);
        } else {
            btn_shuffle.setImageResource(R.drawable.shuffle_unselected);
        }
        switch (AudioApplication.getInstance().getServiceInterface().getRepeat()) {
            case 0:
                btn_repeat.setImageResource(R.drawable.replay_unselected);
                break;
            case 1:
                btn_repeat.setImageResource(R.drawable.replay);
                break;
            case 2:
                btn_repeat.setImageResource(R.drawable.replay_2);
                break;
        }
        ChartData audioItem = AudioApplication.getInstance().getServiceInterface().getAudioItem();
        ((TextView)findViewById(R.id.play_title)).setText(audioItem.getTitle());
        ((TextView)findViewById(R.id.play_vocal)).setText(audioItem.getVocal());

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
