package com.example.choco_music.fragments;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.example.choco_music.Audio.AudioAdapter;
import com.example.choco_music.Audio.AudioApplication;
import com.example.choco_music.Audio.AudioService;
import com.example.choco_music.Audio.BroadcastActions;
import com.example.choco_music.Interface.RetrofitExService;
import com.example.choco_music.R;
import com.example.choco_music.adapters.VerticalAdapter;
import com.example.choco_music.model.Blur;
import com.example.choco_music.model.VerticalData;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Home_Fragment extends Fragment implements View.OnClickListener{

    private RecyclerView mVerticalView;
    private VerticalAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private LinearLayout layoutBottomSheet;
    private CoordinatorLayout background;
    private ArrayList<Button> btn_tags;
    private Button music_evaluate_btn, confirmButton;
    private ArrayList<Boolean> clicks;
    public MediaPlayer mediaPlayer;
    private ImageView music_play_btn;
    private View view;
    private Button cancelButton;
    private ArrayList<ImageView> stars;
    private int currentStar = 5;
    private boolean playPause;
    private boolean initalStage = true;
    private AlertDialog progressDialog;
    private int position;
    private ArrayList<VerticalData> datas;
    private BottomSheetBehavior sheetBehavior;
    AudioService mservice;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI();
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, null, false);

        getAudioListFromMediaDatabase();
        //서버 통신을 위한 레스트로핏 적용
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitExService.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitExService retrofitExService = retrofit.create(RetrofitExService.class);



        //init LayoutManager
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // 기본값이 VERTICAL

        //태그 버튼 적용
        btn_tag();

        layoutBottomSheet = view.findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        music_evaluate_btn = (Button) view.findViewById(R.id.music_evaluate_btn);
        music_play_btn = (ImageView) view.findViewById(R.id.home_fragment_play_btn);
        //음악 평가 클릭 이벤트
        music_evaluate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        // bottom sheet dragable
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        //RecyclerVier binding
        mVerticalView = (RecyclerView) view.findViewById(R.id.vertivcal_list);
        final SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mVerticalView);
        mVerticalView.addItemDecoration(new OffsetItemDecoration(getContext()));


        // 홈 배경화면에 앨범 이미지 어둡게 세팅
        background = view.findViewById(R.id.home_fragment_layout);
        Drawable drawable = getResources().getDrawable(R.drawable.elbum_img);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        Blur blur = new Blur(getContext(), background, bitmap, 10, getActivity());
        blur.run();

        // 데이터베이스에 데이터 받아오기
        retrofitExService.getData2().enqueue(new Callback<ArrayList<VerticalData>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<VerticalData>> call, @NonNull Response<ArrayList<VerticalData>> response) {
                if (response.isSuccessful()) {
                    datas = response.body();
                    //음악 재생기에 넣어줄 리스트를 만들어준다.
                    ArrayList<String> urls = new ArrayList<>();

                    if (datas != null) {
                        for (int i = 0; i < datas.size(); i++) {
                            Log.d("data" + i, datas.get(i).getTitle() + "");
                            Log.d("data" + i, datas.get(i).getId() + "");
                            Log.d("data" + i, datas.get(i).getLyricist() + "");
                            Log.d("data" + i, datas.get(i).getVocal() + "");
                            Log.d("data" + i, datas.get(i).getBucketName() + "");
                            Log.d("data" + i, datas.get(i).getComment() + "");
                            Log.d("data" + i, datas.get(i).getLyrics() + "");
                            Log.d("data" + i, datas.get(i).getGenre() + "");
                            Log.d("data" + i, datas.get(i).getFilerul() + "");
                            urls.add(datas.get(i).getFilerul());
                        }
                        Log.d("getData2 end", "======================================");
                    }
                    // setLayoutManager
                    mVerticalView.setLayoutManager(mLayoutManager);
                    // init Adapter
                    mAdapter = new VerticalAdapter();
                    // set Data
                    mAdapter.setData(datas);
                    // set Adapter
                    mVerticalView.setAdapter(mAdapter);
                    //오디오 어플리 케이션에 재생할 음악 url을 담아준다.
                    AudioApplication.getInstance().getServiceInterface().setPlayList(urls);
                    //AudioApplication.getInstance().getServiceInterface().play(0);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<VerticalData>> call, Throwable t) {

            }
        });

        registerBroadcast();
      // updateUI();


        // 취소, 완료 버튼
        cancelButton = view.findViewById(R.id.sheet_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                cancelButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_evaluate_homefragment));
            }
        });
        // 리사이클러 뷰가 스크롤 될때 현재 위치를 받아오기 위해서 사용하는 코드
        mVerticalView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //리사이 클러뷰 화면 전환시 play 버튼 다시 적용 하는 코드
                initalStage = true;
                music_play_btn.setImageResource(R.drawable.ic_triangle_right);
                playPause = false;
                // 미디어 플레이 리셋 하는 버튼
               /* if (mediaPlayer != null)
                    mediaPlayer.reset();*/

                // 현재 포지션 값을 받아 오는 코드
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    View centerView = snapHelper.findSnapView(mLayoutManager);
                    position = mLayoutManager.getPosition(centerView);
                    Log.e("Snapped Item Position:","" + position);
                    }

                for(int i=0; i!=5; ++i)
                    stars.get(i).setImageResource(R.drawable.star_selected);
                currentStar = 5;
                for(int i=0; i!=10; ++i){
                    clicks.set(i, false);
                    btn_tags.get(i).setBackgroundResource(R.drawable.button_border);
                }


            }
        });


        confirmButton = view.findViewById(R.id.sheet_confirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_evaluate_homefragment));
            }
        });
        // 별
        stars = new ArrayList<>();
        stars.add((ImageView) view.findViewById(R.id.star_1));
        stars.add((ImageView) view.findViewById(R.id.star_2));
        stars.add((ImageView) view.findViewById(R.id.star_3));
        stars.add((ImageView) view.findViewById(R.id.star_4));
        stars.add((ImageView) view.findViewById(R.id.star_5));
        for (int i = 0; i < 5; ++i)
            stars.get(i).setOnClickListener(this);

        music_play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // AudioApplication.getInstance().getServiceInterface().togglePlay();
                //데이터 베이스에서 받아온 데이터를 리사이클러뷰의 위치에 따라 url을 받아온다.
                if (!playPause) {
                    if (initalStage) {
                        AudioApplication.getInstance().getServiceInterface().play(position);
                    } else {
                        if (!AudioApplication.getInstance().getServiceInterface().isPlaying()) {
                            AudioApplication.getInstance().getServiceInterface().play_home_fragment();
                        }
                    }
                    playPause = true;
                } else {
                    if ( AudioApplication.getInstance().getServiceInterface().isPlaying()) {
                       AudioApplication.getInstance().getServiceInterface().pause_home_fragment();
                    }
                    playPause = false;
                }
            }
        });
        return view;
    }

    private void btn_tag() {
        btn_tags = new ArrayList<>();
        clicks = new ArrayList<>();
        btn_tags.add((Button) view.findViewById(R.id.btn_tag_1));
        btn_tags.add((Button) view.findViewById(R.id.btn_tag_2));
        btn_tags.add((Button) view.findViewById(R.id.btn_tag_3));
        btn_tags.add((Button) view.findViewById(R.id.btn_tag_4));
        btn_tags.add((Button) view.findViewById(R.id.btn_tag_5));
        btn_tags.add((Button) view.findViewById(R.id.btn_tag_6));
        btn_tags.add((Button) view.findViewById(R.id.btn_tag_7));
        btn_tags.add((Button) view.findViewById(R.id.btn_tag_8));
        btn_tags.add((Button) view.findViewById(R.id.btn_tag_9));
        btn_tags.add((Button) view.findViewById(R.id.btn_tag_10));

        for (int i = 0; i != 10; ++i) {
            btn_tags.get(i).setOnClickListener(this);
            clicks.add(false);
        }
    }


    @Override
    public void onClick(View view) {
        int btns = 0;
        switch (view.getId()){
            case R.id.star_1:
                currentStar = 1; break;
            case R.id.star_2:
                currentStar = 2; break;
            case R.id.star_3:
                currentStar = 3; break;
            case R.id.star_4:
                currentStar = 4; break;
            case R.id.star_5:
                currentStar = 5; break;
            case R.id.btn_tag_1:
                btns = 1; break;
            case R.id.btn_tag_2:
                btns = 2; break;
            case R.id.btn_tag_3:
                btns = 3; break;
            case R.id.btn_tag_4:
                btns = 4; break;
            case R.id.btn_tag_5:
                btns = 5; break;
            case R.id.btn_tag_6:
                btns = 6; break;
            case R.id.btn_tag_7:
                btns = 7; break;
            case R.id.btn_tag_8:
                btns = 8; break;
            case R.id.btn_tag_9:
                btns = 9; break;
            case R.id.btn_tag_10:
                btns = 10; break;
        }
        if(btns != 0){
            btns--;
            if(clicks.get(btns))
                btn_tags.get(btns).setBackgroundResource(R.drawable.button_border);
            else
                btn_tags.get(btns).setBackgroundResource(R.drawable.button_state_focused);
            clicks.set(btns, !clicks.get(btns));

        }
        btns = 0;
        for(int i = 0; i < 5; ++i)
            stars.get(i).setImageResource(R.drawable.star_unselected);
        for(int i=0; i<currentStar; ++i)
            stars.get(i).setImageResource(R.drawable.star_selected);
    }


    @Override
    public void onPause() {
        super.onPause();
        if(mediaPlayer != null){
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if(progressDialog != null)
            progressDialog.dismiss();
    }
    // 평가화면 리사이클러뷰 여백 조정

    public class OffsetItemDecoration extends RecyclerView.ItemDecoration {

        private Context ctx;

        public OffsetItemDecoration(Context ctx) {

            this.ctx = ctx;
        }
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

            super.getItemOffsets(outRect, view, parent, state);
            int offset = (int) (getScreenWidth() / (float) (2)) - view.getLayoutParams().width / 2;
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            if (parent.getChildAdapterPosition(view) == 0) {
                ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).leftMargin = 0;
                setupOutRect(outRect, offset, true);
            } else if (parent.getChildAdapterPosition(view) == state.getItemCount() - 1) {
                ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).rightMargin = 0;
                setupOutRect(outRect, offset, false);
            }
        }
        private void setupOutRect(Rect rect, int offset, boolean start) {

            if (start) {
                rect.left = offset;
            } else {
                rect.right = offset;
            }
        }
        private int getScreenWidth() {

            WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            return size.x;
        }
    }

    private void updateUI() {
        if (AudioApplication.getInstance().getServiceInterface().isPlaying()) {
            music_play_btn.setImageResource(R.drawable.playing_btn);
        } else {
            music_play_btn.setImageResource(R.drawable.ic_triangle_right);
        }
        AudioAdapter.AudioItem audioItem = AudioApplication.getInstance().getServiceInterface().getAudioItem();
        if (audioItem != null) {
          //  Uri albumArtUri = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), audioItem.mAlbumId);
        //    Picasso.with(getContext()).load(albumArtUri).error(R.drawable.elbum_img).into(mImgAlbumArt);
           // mTxtTitle.setText(audioItem.mTitle);
        } else {
           //  mImgAlbumArt.setImageResource(R.drawable.elbum_img);
          //  mTxtTitle.setText("재생중인 음악이 없습니다.");
        }
    }

    private void registerBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastActions.PREPARED);
        filter.addAction(BroadcastActions.PLAY_STATE_CHANGED);
        getActivity().registerReceiver(mBroadcastReceiver, filter);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterBroadcast();
    }
    private void unregisterBroadcast() {
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }
    private void getAudioListFromMediaDatabase() {
        //DB접근
    }

}


