package com.example.choco_music.fragments;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.bumptech.glide.Glide;
import com.example.choco_music.Audio.AudioApplication;
import com.example.choco_music.Audio.BroadcastActions;
import com.example.choco_music.Holder.VerticalViewHolder;
import com.example.choco_music.Interface.RetrofitExService;
import com.example.choco_music.R;
import com.example.choco_music.adapters.HomeSongAdapter;
import com.example.choco_music.model.AlbumData;
import com.example.choco_music.model.CoverData;
import com.example.choco_music.model.HomeData;
import com.example.choco_music.model.ChartData;
import com.example.choco_music.model.Playlist_Database_OpenHelper;
import com.example.choco_music.model.Star_OpenHelper;
import com.example.choco_music.model.VerticalData;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class Home_Fragment extends Fragment implements View.OnClickListener {

    private RecyclerView mVerticalView;
    private HomeSongAdapter mAdapter;
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
    private ArrayList<ImageView> stars,stars_evaluate;
    private int currentStar = 5;
    private boolean playPause;
    private boolean initalStage = true;
    private AlertDialog progressDialog;
    private int position;
    private ArrayList<ChartData> datas;
    private BottomSheetBehavior sheetBehavior;
    private RetrofitExService retrofitExService;
    private Retrofit retrofit;
    private final SnapHelper snapHelper = new PagerSnapHelper();
    private ArrayList<AlbumData> albumDatas;
    private HashMap<Integer, ChartData> chartMap;
    private ArrayList<HomeData> homeDatas;
    Playlist_Database_OpenHelper playlist_database_openHelper;
    private ImageView img;
    private LinearLayout stars_evaluate_layout;
    private TextView wifi_not_connected;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI();
        }
    };
    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        chartMap = new HashMap<>();
        datas = new ArrayList<>();
        homeDatas= new ArrayList<>();
        mAdapter = new HomeSongAdapter(this);
        setup_retrofit();
        init_retrofit();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, null, false);
       btn_tag();
       setup_view(view);

       return view;
    }
    private void init_retrofit(){
        // 데이터베이스에 데이터 받아오기
        retrofitExService.getData_Original().enqueue(new Callback<ArrayList<VerticalData>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<VerticalData>> call, @NonNull Response<ArrayList<VerticalData>> response) {
                if (response.isSuccessful()) {
                    ArrayList<VerticalData> vertical = response.body();
                    if (vertical != null) {
                        for (int i = 0; i <vertical.size(); i++) {
                            datas.add(new ChartData(vertical.get(i).getTitle(), vertical.get(i).getVocal(),
                                    vertical.get(i).getFileurl(), vertical.get(i).getGenre().equals("자작곡"),1));
                            chartMap.put(vertical.get(i).getId(), datas.get(datas.size()-1));
                            final VerticalData v = vertical.get(i);
                            Call<ArrayList<AlbumData>> call2 = retrofitExService.AlbumData_Original(vertical.get(i).getId());
                            call2.enqueue(new Callback<ArrayList<AlbumData>>()  {
                                @Override
                                public void onResponse(@NonNull Call<ArrayList<AlbumData>> call, @NonNull Response<ArrayList<AlbumData>> response) {
                                    if (response.isSuccessful()) {
                                        albumDatas = response.body();
                                        if (albumDatas != null) {
                                            for (int i = 0; i < albumDatas.size(); i++) {
                                                if(v.getAlbum() == albumDatas.get(i).getId()){
                                                    chartMap.get(v.getId()).setImg_path(albumDatas.get(i).getImg_path());
                                                    mAdapter.notifyDataSetChanged();
                                                }
                                            }
                                        }
                                    }
                                }
                                @Override
                                public void onFailure(Call<ArrayList<AlbumData>> call, Throwable t) {}
                            });
                        }
                    }
                }else{
                    //인터넷 연결이 안될시
                    wifi_not_connected.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(Call<ArrayList<VerticalData>> call, Throwable t) {
            }
        });

        registerBroadcast();
        // updateUI();
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
    private void setup_retrofit(){
        //서버 통신을 위한 레스트로핏 적용
        retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitExService.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitExService = retrofit.create(RetrofitExService.class);
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
            music_play_btn.setImageResource(R.drawable.play_btn);
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
    private void setup_view(View view){
        wifi_not_connected = view.findViewById(R.id.wifi_not_connected);
        // setLayoutManager
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // 기본값이 VERTICAL

        //RecyclerVier binding
        mVerticalView = (RecyclerView) view.findViewById(R.id.vertivcal_list);
        snapHelper.attachToRecyclerView(mVerticalView);
        mVerticalView.addItemDecoration(new OffsetItemDecoration(getContext()));
        mVerticalView.setLayoutManager(mLayoutManager);
        mAdapter.setData(datas);
        mVerticalView.setAdapter(mAdapter);

        // 취소, 완료 버튼
        cancelButton = view.findViewById(R.id.sheet_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast myToast = Toast.makeText(getActivity().getApplicationContext(),"평가가 취소 되었습니다.",Toast.LENGTH_SHORT);
                myToast.setGravity(Gravity.CENTER,0,0);
                myToast.show();
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                cancelButton.setBackgroundResource(R.drawable.button_evaluate_homefragment);
            }
        });
        //확인 버튼
        confirmButton = view.findViewById(R.id.sheet_confirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Star_OpenHelper star_openHelper = new Star_OpenHelper(v.getContext());
                int star_point = star_openHelper.check_star(position);
                if(star_point==0){
                    confirmButton.setBackgroundResource(R.drawable.button_evaluate_homefragment);
                    Toast myToast = Toast.makeText(getActivity().getApplicationContext(),"평가가 완료 되었습니다.",Toast.LENGTH_SHORT);
                    myToast.setGravity(Gravity.CENTER,0,0);
                    myToast.show();
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    Log.e("별 평점",""+ currentStar);
                    post_star_data(currentStar);
                }else{
                    Toast myToast = Toast.makeText(getActivity().getApplicationContext(),"이미 평가를 완료 하였습니다.",Toast.LENGTH_SHORT);
                    myToast.setGravity(Gravity.CENTER,0,0);
                    myToast.show();
                }


            }
        });
        //init LayoutManager
        layoutBottomSheet = view.findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        music_play_btn = (ImageView) view.findViewById(R.id.home_fragment_play_btn);
        if(datas != null)
            setBackground(position);
        music_evaluate_btn = (Button) view.findViewById(R.id.music_evaluate_btn);
        stars_evaluate_layout = view.findViewById(R.id.stars_evaluate);
        stars_evaluate = new ArrayList<>();
        stars_evaluate.add((ImageView) view.findViewById(R.id.star_1_evaluate));
        stars_evaluate.add((ImageView) view.findViewById(R.id.star_2_evaluate));
        stars_evaluate.add((ImageView) view.findViewById(R.id.star_3_evaluate));
        stars_evaluate.add((ImageView) view.findViewById(R.id.star_4_evaluate));
        stars_evaluate.add((ImageView) view.findViewById(R.id.star_5_evaluate));

        //평가가 되었는지 체크
        Star_OpenHelper star_openHelper = new Star_OpenHelper(view.getContext());
        int star_point = star_openHelper.check_star(position);
        if(star_point!=0){
            music_evaluate_btn.setVisibility(View.GONE);
            stars_evaluate_layout.setVisibility(View.VISIBLE);
            for(int i=0;i<5;i++){
                stars_evaluate.get(i).setImageResource(R.drawable.star_unselected);
            }
            for(int i=0;i<star_point;i++){
                stars_evaluate.get(i).setImageResource(R.drawable.star_selected);
            }
        }
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
        // 리사이클러 뷰가 스크롤 될때 현재 위치를 받아오기 위해서 사용하는 코드
        mVerticalView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                music_evaluate_btn.setVisibility(View.VISIBLE);
                stars_evaluate_layout.setVisibility(View.GONE);
                //리사이 클러뷰 화면 전환시 play 버튼 다시 적용 하는 코드
                initalStage = true;
                music_play_btn.setImageResource(R.drawable.play_btn);
                playPause = false;
                // 현재 포지션 값을 받아 오는 코드
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    View centerView = snapHelper.findSnapView(mLayoutManager);
                    position = mLayoutManager.getPosition(centerView);
                    Log.e("Snapped Item Position:","" + position);
                    setBackground(position);
                    Star_OpenHelper star_openHelper = new Star_OpenHelper(centerView.getContext());
                    //별점데이터가 db에 있을경우
                    int star_point = star_openHelper.check_star(position);
                    if(star_point!=0){
                        music_evaluate_btn.setVisibility(View.GONE);
                        stars_evaluate_layout.setVisibility(View.VISIBLE);
                        Log.e("점수",""+ star_point);
                        for(int i=0;i<5;i++){
                            stars_evaluate.get(i).setImageResource(R.drawable.star_unselected);
                        }
                        for(int i=0;i<star_point;i++){
                            stars_evaluate.get(i).setImageResource(R.drawable.star_selected);
                        }
                    }
                }
                //별 선택시
                for(int i=0; i!=5; ++i){
                    stars.get(i).setImageResource(R.drawable.star_selected);
                }
                currentStar = 5;
                for(int i=0; i!=10; ++i) {
                    clicks.set(i, false);
                    btn_tags.get(i).setBackgroundResource(R.drawable.button_border);
                }
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
                AudioApplication.getInstance().getServiceInterface().setPlayList(datas);
                if (!playPause) {
                    if (initalStage) {
                        AudioApplication.getInstance().getServiceInterface().play(position);
                        initalStage =!initalStage;
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
    }

    public void add_playlist (final int pos , final View view){

        playlist_database_openHelper= new Playlist_Database_OpenHelper(view.getContext());
        if(datas.get(pos).getImg_path() != null) {
            boolean db_check =playlist_database_openHelper.Play_list_Check(datas.get(pos).getTitle(), datas.get(pos).getVocal(),
                    datas.get(pos).getFileurl(), datas.get(pos).getImg_path());
            if(db_check){
                Toast myToast = Toast.makeText(view.getContext(),"초코뮤직님의 좋아요",Toast.LENGTH_SHORT);
                myToast.setGravity(Gravity.CENTER,0,0);
                myToast.show();
                String type;
                Log.d("w",pos+"하");
                if(datas.get(pos).getType())
                    type = "자작곡";
                else
                    type = "커버곡";
                playlist_database_openHelper.insertData(datas.get(pos).getTitle(), datas.get(pos).getVocal(),
                        datas.get(pos).getFileurl(), datas.get(pos).getImg_path(), type);
            }else{
                Toast myToast = Toast.makeText(view.getContext(),"이미 좋아요 하였습니다.",Toast.LENGTH_SHORT);
                myToast.setGravity(Gravity.CENTER,0,0);
                myToast.show();
            }

        }
    }
    private void setBackground(final int pos){
        img = view.findViewById(R.id.home_fragment_background);
        retrofitExService.getData_Original().enqueue(new Callback<ArrayList<VerticalData>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<VerticalData>> call, @NonNull Response<ArrayList<VerticalData>> response) {
                if (response.isSuccessful()) {
                    ArrayList<VerticalData> vertical = response.body();
                    if (vertical != null) {
                        final int album = vertical.get(pos).getAlbum();

                        Call<ArrayList<AlbumData>> call2 = retrofitExService.AlbumData_Original(vertical.get(pos).getId());
                        call2.enqueue(new Callback<ArrayList<AlbumData>>()  {
                            @Override
                            public void onResponse(@NonNull Call<ArrayList<AlbumData>> call, @NonNull Response<ArrayList<AlbumData>> response) {
                                if (response.isSuccessful()) {
                                    albumDatas = response.body();
                                    if (albumDatas != null) {
                                        for (int i = 0; i < albumDatas.size(); i++) {
                                            if(album == albumDatas.get(i).getId()){
                                                Glide.with(getContext()).load(albumDatas.get(i).getImg_path()).into(img);
                                            }
                                        }
                                    }
                                }
                            }
                            @Override
                            public void onFailure(Call<ArrayList<AlbumData>> call, Throwable t) {}
                        });
                    }
                }
            }
            @Override
            public void onFailure(Call<ArrayList<VerticalData>> call, Throwable t) {
            }
        });
    }
    public void post_star_data(final int Star_Point){
        retrofitExService.getData_Original().enqueue(new Callback<ArrayList<VerticalData>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<VerticalData>> call, @NonNull Response<ArrayList<VerticalData>> response) {
                if (response.isSuccessful()) {
                    ArrayList<VerticalData> vertical = response.body();
                    if (vertical != null) {
                        HashMap<String,Object> input = new HashMap<>();
                        input.put("id", position );
                        input.put("score",Star_Point);
                        input.put("song",vertical.get(position).getId());

                        Star_OpenHelper star_openHelper = new Star_OpenHelper(view.getContext());
                        star_openHelper.insertData(position,Star_Point);

                        retrofitExService.postData(input).enqueue(new Callback<VerticalData>() {
                            @Override
                            public void onResponse(Call<VerticalData> call, Response<VerticalData> response) {
                                if(response.isSuccessful()){
                                    VerticalData body = response.body();
                                    if(body != null){
                                    }
                                }
                            }
                            @Override
                            public void onFailure(Call<VerticalData> call, Throwable t) {
                            }
                        });
                    }
                }
            }
            @Override
            public void onFailure(Call<ArrayList<VerticalData>> call, Throwable t) {
            }
        });
    }
}


