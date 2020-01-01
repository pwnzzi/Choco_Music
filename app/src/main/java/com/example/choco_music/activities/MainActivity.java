package com.example.choco_music.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

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

        Intent intent = new Intent(this, LoadingActivity.class);
        startActivity(intent);

        viewPager= findViewById(viewpager);
        ViewPagerAdpater viewPagerAdpater= new ViewPagerAdpater(getSupportFragmentManager());
        viewPager.setPagingEnabled(false);

        viewPager.setAdapter(viewPagerAdpater);

        TabLayout tabLayout=findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

        playing_bar = findViewById(R.id.layout_playing_bar);
        playing_bar.setVisibility(View.GONE);

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
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

      /*  View view1 = getLayoutInflater().inflate(R.layout.view_home_tab, null);
        view1.findViewById(R.id.icon).setBackgroundResource(R.drawable.home_tab);
        tabLayout.getTabAt(0).setCustomView(view1);

        View view2 = getLayoutInflater().inflate(R.layout.view_home_tab, null);
        view2.findViewById(R.id.icon).setBackgroundResource(R.drawable.chart_tab);
        tabLayout.getTabAt(1).setCustomView(view2);

        View view3 = getLayoutInflater().inflate(R.layout.view_home_tab, null);
        view3.findViewById(R.id.icon).setBackgroundResource(R.drawable.search_tab);
        tabLayout.getTabAt(2).setCustomView(view3);

        View view4 = getLayoutInflater().inflate(R.layout.view_home_tab, null);
        view4.findViewById(R.id.icon).setBackgroundResource(R.drawable.playlist_tab);
        tabLayout.getTabAt(3).setCustomView(view4);
*/


    }
}
