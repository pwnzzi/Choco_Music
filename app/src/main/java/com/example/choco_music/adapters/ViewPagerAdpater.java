package com.example.choco_music.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import com.example.choco_music.R;
import com.example.choco_music.fragments.Chart_Fragment;
import com.example.choco_music.fragments.Home_Fragment;
import com.example.choco_music.fragments.Playlist_Fragment;
import com.example.choco_music.fragments.Search_Fragment;

public class ViewPagerAdpater extends FragmentPagerAdapter {

    Home_Fragment f1;
    Chart_Fragment f2;
    Search_Fragment f3;
    Playlist_Fragment f4;

    public ViewPagerAdpater(FragmentManager fm) {
        super(fm);

        f1= new Home_Fragment();
        f2= new Chart_Fragment();
        f3= new Search_Fragment();
        f4= new Playlist_Fragment();
    }

    @Override
    public Fragment getItem(int position) {

        if(position==0)
            return f1;
        else if(position==1)
            return f2;
        else if(position==2)
            return f3;
        else if(position==3)
            return f4;
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
