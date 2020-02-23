package com.example.choco_music.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.choco_music.R;

public class User_Fragment extends Fragment {

    public boolean Login = true;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_fragment, null, false);
        //  init_recyclerview(view);
        if (Login == false){
            getFragmentManager().beginTransaction().replace(R.id.user_fragment_layout,new Login_Fragment()).commit();
        }else{
            getFragmentManager().beginTransaction().replace(R.id.user_fragment_layout,new UserList_Fragment()).commit();
        }
        return view;
    }
}
