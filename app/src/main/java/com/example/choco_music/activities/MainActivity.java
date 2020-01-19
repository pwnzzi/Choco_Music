package com.example.choco_music.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.choco_music.Audio.AudioApplication;
import com.example.choco_music.Audio.BroadcastActions;
import com.example.choco_music.R;
import com.example.choco_music.adapters.SwipeViewPager;
import com.example.choco_music.adapters.ViewPagerAdpater;
import com.example.choco_music.fragments.Playlist_Fragment;
import com.google.android.material.tabs.TabLayout;

import static com.example.choco_music.R.id.viewpager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SwipeViewPager viewPager;
    LinearLayout playing_bar;
    ImageView btn_front, btn_back, btn_play;
    SeekBar sb;
    boolean seekBarControl = true;
    TextView txt_title, txt_vocal;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 스플래쉬 화면
        Intent intent = new Intent(this, LoadingActivity.class);
        startActivity(intent);

        // 뷰페이져
        viewPager= findViewById(viewpager);
        ViewPagerAdpater viewPagerAdpater= new ViewPagerAdpater(getSupportFragmentManager());
        viewPager.setPagingEnabled(false);

        viewPager.setAdapter(viewPagerAdpater);

        btn_back = findViewById(R.id.playing_bar_back);
        btn_front = findViewById(R.id.playing_bar_front);
        btn_play = findViewById(R.id.playing_bar_play);

        txt_title = findViewById(R.id.playing_bar_title);
        txt_vocal = findViewById(R.id.playing_bar_vocal);
        sb = findViewById(R.id.playing_bar_seekbar);

        //탭 레이아윳
        TabLayout tabLayout=findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

        //음악 재생 버튼
        playing_bar = findViewById(R.id.layout_playing_bar);
        playing_bar.setVisibility(View.GONE);

        btn_back.setOnClickListener(this);
        btn_play.setOnClickListener(this);
        btn_front.setOnClickListener(this);
        playing_bar.setOnClickListener(this);

        //탭래이아웃
        View view1= getLayoutInflater().inflate(R.layout.customtab,null);
        view1.findViewById(R.id.icon).setBackgroundResource(R.drawable.home_tab_icon);
        View view2= getLayoutInflater().inflate(R.layout.customtab,null);
        view2.findViewById(R.id.icon).setBackgroundResource(R.drawable.chart_tab_icon);
        View view3= getLayoutInflater().inflate(R.layout.customtab,null);
        view3.findViewById(R.id.icon).setBackgroundResource(R.drawable.search_tab_icon);
        View view4= getLayoutInflater().inflate(R.layout.customtab,null);
        view4.findViewById(R.id.icon).setBackgroundResource(R.drawable.playlist_tab_icon);

        registerBroadcast();
        updateUI();

      /*  tabLayout.getTabAt(0).setIcon(R.drawable.home_tab_icon);
        tabLayout.getTabAt(1).setIcon(R.drawable.chart_tab_icon);
        tabLayout.getTabAt(2).setIcon(R.drawable.search_tab_icon);
        tabLayout.getTabAt(3).setIcon(R.drawable.playlist_tab_icon);*/
        tabLayout.getTabAt(0).setCustomView(view1);
        tabLayout.getTabAt(1).setCustomView(view2);
        tabLayout.getTabAt(2).setCustomView(view3);
        tabLayout.getTabAt(3).setCustomView(view4);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0){
                    playing_bar.setVisibility(View.GONE);
                } else {
                    playing_bar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

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

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.layout_playing_bar:
                Intent intent = new Intent(this, MusicPlay_activity.class);
                startActivity(intent);
                break;
            case R.id.playing_bar_back:
                AudioApplication.getInstance().getServiceInterface().rewind();
                updateUI();
                break;
            case R.id.playing_bar_front:
                AudioApplication.getInstance().getServiceInterface().forward();
                updateUI();
                break;
            case R.id.playing_bar_play:
                AudioApplication.getInstance().getServiceInterface().togglePlay();
                updateUI();
        }
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

    public void updateUI(){
        if (AudioApplication.getInstance().getServiceInterface().isPlaying()) {
            btn_play.setImageResource(R.drawable.playing_btn);
            sb.setMax(AudioApplication.getInstance().getServiceInterface().getDuration());
            new SeekThread().start();
            txt_title.setText(AudioApplication.getInstance().getServiceInterface().getAudioItem().getTitle());
            txt_vocal.setText(AudioApplication.getInstance().getServiceInterface().getAudioItem().getVocal());
        } else {
            btn_play.setImageResource(R.drawable.play_btn);
        }
    }

    class SeekThread extends Thread {
        @Override
        public void run() { // 쓰레드가 시작되면 콜백되는 메서드
            // 씨크바 막대기 조금씩 움직이기 (노래 끝날 때까지 반복)
            while(AudioApplication.getInstance().getServiceInterface().isPlaying() && seekBarControl) {
                sb.setProgress(AudioApplication.getInstance().getServiceInterface().getCurrentPosition());
            }
        }
    }

}
