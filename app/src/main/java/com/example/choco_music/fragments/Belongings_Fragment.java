package com.example.choco_music.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.choco_music.R;

public class Belongings_Fragment extends androidx.fragment.app.Fragment {

    Button back_btn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.belongings_fragment, container, false);

        back_btn = view.findViewById(R.id.back_belonging_fragment);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right).replace(R.id.user_fragment_layout,new UserList_Fragment()).commit();
            }
        });
        return view;
    }
}
