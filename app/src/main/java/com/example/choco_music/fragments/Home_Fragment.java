package com.example.choco_music.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
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

    private RecyclerView mVerticalView;
    private VerticalAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private LinearLayout layoutBottomSheet;
    private CoordinatorLayout background;
    private Button btn_tag_1,btn_tag_2,btn_tag_3,btn_tag_4,btn_tag_5,btn_tag_6,btn_tag_7,btn_tag_8,btn_tag_9,btn_tag_10,music_evaluate_btn,confirmButton;
    private  Boolean isRunning = false, click_1 = true,click_2= true,click_3= true,click_4 = true,click_5 = true,click_6 = true,click_7 = true,click_8 = true,click_9 = true,click_10 = true;
    public MediaPlayer mediaPlayer;
    private ImageView music_play_btn;
    private View view;
    private Button cancelButton;
    private ArrayList<ImageView> stars;
    private int currentStar = 5;

    private int MAX_ITEM_COUNT = 50;

    BottomSheetBehavior sheetBehavior;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.home_fragment,null,false);

        //태그 버튼 적용
        btn_tag();

        layoutBottomSheet = view.findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        music_evaluate_btn=(Button)view.findViewById(R.id.music_evaluate_btn);
        music_play_btn=(ImageView)view.findViewById(R.id.home_fragment_play_btn);

        music_play_btn.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRunning){
                    mediaPlayer.stop();
                    music_play_btn.setImageResource(R.drawable.ic_triangle_right);
                } else {
                    mediaPlayer = MediaPlayer.create(getContext(), R.raw.test);
                    music_play_btn.setImageResource(R.drawable.playing_btn);

                    mediaPlayer.start();
                }
                isRunning = !isRunning;
            }
        });

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
                cancelButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_evaluate_homefragment));
            }
        });

        confirmButton = view.findViewById(R.id.sheet_confirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_evaluate_homefragment));
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

    private void btn_tag() {

        btn_tag_1 = (Button) view.findViewById(R.id.btn_tag_1);
        btn_tag_2 = (Button) view.findViewById(R.id.btn_tag_2);
        btn_tag_3 = (Button) view.findViewById(R.id.btn_tag_3);
        btn_tag_4 = (Button) view.findViewById(R.id.btn_tag_4);
        btn_tag_5 = (Button) view.findViewById(R.id.btn_tag_5);
        btn_tag_6 = (Button) view.findViewById(R.id.btn_tag_6);
        btn_tag_7 = (Button) view.findViewById(R.id.btn_tag_7);
        btn_tag_8 = (Button) view.findViewById(R.id.btn_tag_8);
        btn_tag_9 = (Button) view.findViewById(R.id.btn_tag_9);
        btn_tag_10 = (Button) view.findViewById(R.id.btn_tag_10);


        btn_tag_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (click_1) {
                    btn_tag_1.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_state_focused));
                    click_1 = false;
                } else {
                    btn_tag_1.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_border));
                    click_1 = true;
                }

            }
        });
        btn_tag_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (click_2) {
                    btn_tag_2.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_state_focused));
                    click_2 = false;
                } else {
                    btn_tag_2.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_border));
                    click_2 = true;
                }

            }
        });
        btn_tag_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (click_3) {
                    btn_tag_3.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_state_focused));
                    click_3 = false;
                } else {
                    btn_tag_3.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_border));
                    click_3 = true;
                }

            }
        });
        btn_tag_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (click_4) {
                    btn_tag_4.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_state_focused));
                    click_4 = false;
                } else {
                    btn_tag_4.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_border));
                    click_4 = true;
                }

            }
        });
        btn_tag_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (click_5) {
                    btn_tag_5.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_state_focused));
                    click_5 = false;
                } else {
                    btn_tag_5.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_border));
                    click_5 = true;
                }

            }
        });
        btn_tag_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (click_6) {
                    btn_tag_6.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_state_focused));
                    click_6 = false;
                } else {
                    btn_tag_6.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_border));
                    click_6 = true;
                }

            }
        });
        btn_tag_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (click_7) {
                    btn_tag_7.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_state_focused));
                    click_7 = false;
                } else {
                    btn_tag_7.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_border));
                    click_7 = true;
                }

            }
        });
        btn_tag_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (click_8) {
                    btn_tag_8.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_state_focused));
                    click_8 = false;
                } else {
                    btn_tag_8.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_border));
                    click_8 = true;
                }

            }
        });
        btn_tag_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (click_9) {
                    btn_tag_9.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_state_focused));
                    click_9 = false;
                } else {
                    btn_tag_9.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_border));
                    click_9 = true;
                }

            }
        });
        btn_tag_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (click_10) {
                    btn_tag_10.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_state_focused));
                    click_10 = false;
                } else {
                    btn_tag_10.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_border));
                    click_10 = true;
                }

            }
        });
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


