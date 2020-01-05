package com.example.choco_music.fragments;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.choco_music.R;
import com.example.choco_music.adapters.VerticalAdapter;
import com.example.choco_music.model.Blur;
import com.example.choco_music.model.VerticalData;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;

public class Home_Fragment extends Fragment implements View.OnClickListener{

    private Button music_evaluate_btn;
    private RecyclerView mVerticalView;
    private VerticalAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private LinearLayout layoutBottomSheet;
    private CoordinatorLayout background;
    private Button cancelButton;
    private ArrayList<ImageView> stars;
    private int currentStar = 5;

    private int MAX_ITEM_COUNT = 50;

    BottomSheetBehavior sheetBehavior;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.home_fragment,null,false);

        layoutBottomSheet = view.findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);

        music_evaluate_btn=(Button)view.findViewById(R.id.music_evaluate_btn);

        music_evaluate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        //RecyclerVier binding
        mVerticalView = (RecyclerView)view.findViewById(R.id.vertivcal_list);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mVerticalView);

        background = view.findViewById(R.id.home_fragment_layout);
        Drawable drawable = getResources().getDrawable(R.drawable.elbum_img);
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        Blur blur = new Blur(getContext(), background, bitmap,10, getActivity());
        blur.run();

        //init Data
        ArrayList<VerticalData> data = new ArrayList<>();

        int i=0;
        while(i< MAX_ITEM_COUNT){
            data.add(new VerticalData(R.drawable.elbum_img,i+"번째 커버곡"));
            i++;
        }

        cancelButton = view.findViewById(R.id.sheet_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        stars = new ArrayList<>();
        stars.add((ImageView) view.findViewById(R.id.star_1));
        stars.add((ImageView) view.findViewById(R.id.star_2));
        stars.add((ImageView) view.findViewById(R.id.star_3));
        stars.add((ImageView) view.findViewById(R.id.star_4));
        stars.add((ImageView) view.findViewById(R.id.star_5));
        for(i = 0; i < 5; ++i)
            stars.get(i).setOnClickListener(this);

        //init LayoutManager
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // 기본값이 VERTICAL

        // setLayoutManager
        mVerticalView.setLayoutManager(mLayoutManager);

        // init Adapter
        mAdapter = new VerticalAdapter();

        // set Data
        mAdapter.setData(data);

        // set Adapter
        mVerticalView.setAdapter(mAdapter);

        return view;

 /*if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {

                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }*/
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.star_1:
                currentStar = 1;
                break;
            case R.id.star_2:
                currentStar = 2;
                break;
            case R.id.star_3:
                currentStar = 3;
                break;
            case R.id.star_4:
                currentStar = 4;
                break;
            case R.id.star_5:
                currentStar = 5;
                break;
        }
        for(int i=0; i<5; ++i)
            stars.get(i).setImageResource(R.drawable.star_unselected);
        for(int i=0; i<currentStar; ++i)
            stars.get(i).setImageResource(R.drawable.star_selected);
    }
}


