package com.example.choco_music.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import com.example.choco_music.R;
import com.example.choco_music.adapters.SwipeViewPager;
import com.example.choco_music.adapters.ViewPagerAdpater;
import com.google.android.material.tabs.TabLayout;

import static com.example.choco_music.R.id.viewpager;

public class MainActivity extends AppCompatActivity {

    SwipeViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager= findViewById(viewpager);
        ViewPagerAdpater viewPagerAdpater= new ViewPagerAdpater(getSupportFragmentManager());
        viewPager.setPagingEnabled(false);

        viewPager.setAdapter(viewPagerAdpater);

        TabLayout tabLayout=findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText("HOME");
        tabLayout.getTabAt(1).setText("CHART");
        tabLayout.getTabAt(2).setText("SEARCH");
        tabLayout.getTabAt(3).setText("PLAYLIST");








    }
}
