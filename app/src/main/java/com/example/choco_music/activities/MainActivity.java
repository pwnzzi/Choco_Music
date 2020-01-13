package com.example.choco_music.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.choco_music.Audio.AudioApplication;
import com.example.choco_music.R;
import com.example.choco_music.adapters.SwipeViewPager;
import com.example.choco_music.adapters.ViewPagerAdpater;
import com.google.android.material.tabs.TabLayout;

import static com.example.choco_music.R.id.viewpager;

public class MainActivity extends AppCompatActivity {

    SwipeViewPager viewPager;
    LinearLayout playing_bar;

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

        //탭 레이아윳
        TabLayout tabLayout=findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

        //음악 재생 버튼
        playing_bar = findViewById(R.id.layout_playing_bar);
        playing_bar.setVisibility(View.GONE);

        //탭래이아웃

        tabLayout.getTabAt(0).setIcon(R.drawable.home_tab_icon);
        tabLayout.getTabAt(1).setIcon(R.drawable.chart_tab_icon);
        tabLayout.getTabAt(2).setIcon(R.drawable.search_tab_icon);
        tabLayout.getTabAt(3).setIcon(R.drawable.playlist_tab_icon);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0){
                    playing_bar.setVisibility(View.GONE);
                } else {
                    playing_bar.setVisibility(View.VISIBLE);
                    AudioApplication.getInstance().getServiceInterface().pause_home_fragment();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

}
