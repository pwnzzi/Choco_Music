package com.example.choco_music.activities;


import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.bumptech.glide.Glide;
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

import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class MusicPlay_activity extends AppCompatActivity implements View.OnClickListener{

    private ImageView music_play_btn, replay_btn, shuffle_btn;
    private Boolean isRunning = false;
    private View runLayout;
    private FrameLayout background;
    private ImageView btn_shuffle, btn_repeat;
    private TextView txt_current, txt_length;
    private int chart;
    SeekBar sb;
    boolean seekBarControl = true;

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

        music_play_btn.setOnClickListener(this);
        findViewById(R.id.play_layout_list).setOnClickListener(this);
        findViewById(R.id.play_layout_finish).setOnClickListener(this);
        findViewById(R.id.play_back).setOnClickListener(this);
        findViewById(R.id.play_front).setOnClickListener(this);
        btn_repeat.setOnClickListener(this);
        btn_shuffle.setOnClickListener(this);
        sb = findViewById(R.id.music_play_seekbar);
        txt_current = findViewById(R.id.music_play_current_txt);
        txt_length = findViewById(R.id.music_play_length_txt);
        background = findViewById(R.id.music_play_frame);

        //background.sethei

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBarControl = true;
                AudioApplication.getInstance().getServiceInterface().seekTo(sb.getProgress());
                updateUI();
            }
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) { }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekBarControl = false;
            }
        });

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
            sb.setMax(AudioApplication.getInstance().getServiceInterface().getDuration());
            new SeekThread().start();
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
        if(audioItem.getType()){
            ((TextView)findViewById(R.id.music_play_type)).setText("자작곡");
            ((TextView)findViewById(R.id.music_play_type)).setTextColor(Color.parseColor("#ffffff"));
            findViewById(R.id.music_play_type).setBackgroundResource(R.drawable.round_songtype_orig);
            findViewById(R.id.music_play_cover_frame).setBackgroundResource(R.drawable.border_cover_orig);
            ((ImageView)findViewById(R.id.music_play_cover_tri)).setImageResource(R.drawable.ic_triangle_original);
        } else {
            ((TextView)findViewById(R.id.music_play_type)).setText("커버곡");
            ((TextView)findViewById(R.id.music_play_type)).setTextColor(Color.parseColor("#000000"));
            findViewById(R.id.music_play_type).setBackgroundResource(R.drawable.round_songtype);
            findViewById(R.id.music_play_cover_frame).setBackgroundResource(R.drawable.border_cover);
            ((ImageView)findViewById(R.id.music_play_cover_tri)).setImageResource(R.drawable.ic_triangle_cover);
        }

        txt_length.setText(secondsToString(AudioApplication.getInstance().getServiceInterface().getDuration() / 1000));
        try{
            ImageView img = (ImageView)findViewById(R.id.music_play_cover_frame);
            Glide.with(getApplicationContext()).load(audioItem.getImg_path()).into(img);
            Glide.with(getApplicationContext()).load(audioItem.getImg_path())
                    .apply(bitmapTransform(new BlurTransformation(15, 3)))
                    .into(((ImageView)findViewById(R.id.music_play_background)));
        }
        catch(Exception e){}

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

    private String secondsToString(int pTime) {
        if(pTime >= 3600)
            return String.format("%d:%02d:%02d", pTime / 3600, pTime / 60 % 60, pTime % 60);
        else
            return String.format("%d:%02d", pTime / 60 % 60, pTime % 60);
    }

    class SeekThread extends Thread {
        @Override
        public void run() { // 쓰레드가 시작되면 콜백되는 메서드
            // 씨크바 막대기 조금씩 움직이기 (노래 끝날 때까지 반복)
            int times = 0;
            while(AudioApplication.getInstance().getServiceInterface().isPlaying() && seekBarControl) {
                final int current = AudioApplication.getInstance().getServiceInterface().getCurrentPosition();
                sb.setProgress(current);
                times = (times + 1) % 100;
                if(times == 0)
                    runOnUiThread(new Runnable() {
                        public void run() { // 메시지 큐에 저장될 메시지의 내용
                            if(current >= 3600000)
                                txt_current.setText(String.format("%d:%02d:%02d", current / 3600000, current / 60000 % 60, current / 1000 % 60));
                            else
                                txt_current.setText(String.format("%d:%02d", current / 60000 % 60, current / 1000 % 60));
                        }
                    });
            }
        }
    }
}
