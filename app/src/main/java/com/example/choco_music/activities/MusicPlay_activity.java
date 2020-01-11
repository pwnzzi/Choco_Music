package com.example.choco_music.activities;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.example.choco_music.R;
import com.example.choco_music.model.Blur;

public class MusicPlay_activity extends AppCompatActivity {


    private ImageView music_play_btn,music_finish,music_list_btn;
    private MediaPlayer mediaPlayer;
    private Boolean isRunning = false;
    private View runLayout;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.music_play_layout);

        music_play_btn = (ImageView)findViewById(R.id.music_play);
        music_finish= (ImageView)findViewById(R.id.play_layout_finish);
        music_list_btn=(ImageView)findViewById(R.id.play_layout_list);
        runLayout = findViewById(R.id.linear_running_btn);

        //커버곡 리스트 화면 불러오기

        music_list_btn.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Coversong_chart.class);


                startActivity(intent);
            }
        });
        // activity 종료

        music_finish.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 음악 재생및 종료
        music_play_btn.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRunning){
                    mediaPlayer.stop();
                    music_play_btn.setImageResource(R.drawable.play_btn);
                } else {
                    mediaPlayer = MediaPlayer.create(MusicPlay_activity.this, R.raw.test);
                    music_play_btn.setImageResource(R.drawable.playing_btn);
                    mediaPlayer.start();
                }
                isRunning = !isRunning;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // MediaPlayer 해지
        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
