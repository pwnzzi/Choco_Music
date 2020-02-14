package com.example.choco_music.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.choco_music.Audio.AudioApplication;
import com.example.choco_music.Audio.BroadcastActions;
import com.example.choco_music.R;
import com.example.choco_music.model.ChartData;
import com.example.choco_music.model.Playlist_Database_OpenHelper;

import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class MusicPlayActivity extends AppCompatActivity  {


    private ImageView play,foward,backward,musiclist;
    private ImageView albumcover;
    private SeekBar sb;
    boolean seekBarControl = true;
    private TextView txt_current, txt_length;
    private Button heart;
    private FrameLayout background1,background2;
    private ImageView btn_shuffle, btn_repeat, finish_btn;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.musicplayactivity);

        txt_current = findViewById(R.id.music_play_current_txt);
        txt_length = findViewById(R.id.music_play_length_txt);
        play = findViewById(R.id.playmusic);
        foward = findViewById(R.id.foward);
        backward = findViewById(R.id.backward);
        albumcover = findViewById(R.id.albumcover);
        musiclist = findViewById(R.id.musiclist);
        sb = findViewById(R.id.playing_bar_seekbar_musicplayactivity);
        heart = findViewById(R.id.heart_music_play_layout_music);
        background1 = findViewById(R.id.music_play_frame);
        background2 = findViewById(R.id.musicplay_activity_layout);
        btn_shuffle = findViewById(R.id.music_play_shuffle_music);
        btn_repeat = findViewById(R.id.music_play_replay_music);
        finish_btn = findViewById(R.id.play_layout_finish_music_music);
        play.getLayoutParams().height = 200;
        play.getLayoutParams().width = 200;
        play.requestLayout();
        play.setImageResource(R.drawable.play_btn);

        finish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.anim_slide_in_top,R.anim.anim_slide_out_bottom);
            }
        });
        btn_repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioApplication.getInstance().getServiceInterface().toggleRepeat();
                updateUI();
            }
        });

        btn_shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioApplication.getInstance().getServiceInterface().toggleShuffle();
                updateUI();
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioApplication.getInstance().getServiceInterface().togglePlay();
                updateUI();
            }
        });
        foward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AudioApplication.getInstance().getServiceInterface().forward();
                updateUI();
            }
        });
        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioApplication.getInstance().getServiceInterface().rewind();
                updateUI();
            }
        });
        musiclist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ChartActivity.class);
                startActivity(intent);
            }
        });

        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChartData audioItem = AudioApplication.getInstance().getServiceInterface().getAudioItem();
                Playlist_Database_OpenHelper playlist_database_openHelper= new Playlist_Database_OpenHelper(v.getContext());
                if(audioItem.getImg_path() != null) {
                    boolean db_check = playlist_database_openHelper.Play_list_Check( audioItem.getTitle(),  audioItem.getVocal(),
                            audioItem.getFileurl(),  audioItem.getImg_path());
                    if (db_check) {
                        heart.setBackgroundResource(R.drawable.heart_selected);
                        Toast myToast = Toast.makeText(v.getContext(), "초코뮤직님의 좋아요", Toast.LENGTH_SHORT);
                        myToast.setGravity(Gravity.CENTER, 0, 0);
                        myToast.show();
                        String type;
                        if ( audioItem.getType())
                            type = "자작곡";
                        else
                            type = "커버곡";
                        playlist_database_openHelper.insertData( audioItem.getTitle(),  audioItem.getVocal(),
                                audioItem.getFileurl(),  audioItem.getImg_path(), type);
                    } else {
                        Toast myToast = Toast.makeText(v.getContext(), "이미 좋아요 하였습니다.", Toast.LENGTH_SHORT);
                        myToast.setGravity(Gravity.CENTER, 0, 0);
                        myToast.show();
                    }
                }
            }
        });

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBarControl = true;
                AudioApplication.getInstance().getServiceInterface().seekTo(sb.getProgress());
                updateUI();
            }
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) { }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekBarControl = false;
            }
        });
        registerBroadcast();
        updateUI();



    }

    private void updateUI() {
        ChartData audioItem = AudioApplication.getInstance().getServiceInterface().getAudioItem();
        if (AudioApplication.getInstance().getServiceInterface().isPlaying()) {
            play.getLayoutParams().height = 200;
            play.getLayoutParams().width = 200;
            play.requestLayout();
            play.setImageResource(R.drawable.playing_btn);
            sb.setMax(AudioApplication.getInstance().getServiceInterface().getDuration());
            txt_length.setText(secondsToString(AudioApplication.getInstance().getServiceInterface().getDuration() / 1000));
            new SeekThread().start();
        } else {
            play.setImageResource(R.drawable.play_btn);
        }
        if (AudioApplication.getInstance().getServiceInterface().getShuffle()) {
            btn_shuffle.setImageResource(R.drawable.shuffle);
        } else {
            btn_shuffle.setImageResource(R.drawable.shuffle_unselected);
        }
        switch (AudioApplication.getInstance().getServiceInterface().getRepeat()) {
            case 0:
                btn_repeat.setImageResource(R.drawable.replay_unselected);
                break;
            case 1:
                btn_repeat.setImageResource(R.drawable.replay);
                break;
            case 2:
                btn_repeat.setImageResource(R.drawable.replay_2);
                break;
        }
        ((TextView)findViewById(R.id.play_title_music)).setText(audioItem.getTitle());
        ((TextView)findViewById(R.id.play_vocal_music)).setText(audioItem.getVocal());
        ((TextView)findViewById(R.id.play_vocal_music)).setTextSize(TypedValue.COMPLEX_UNIT_SP,12f);
        ((TextView)findViewById(R.id.play_title_music)).setTextSize(TypedValue.COMPLEX_UNIT_SP,14f);

        if(audioItem.getType()){
            ((TextView)findViewById(R.id.music_play_type_music)).setText("자작곡");
            ((TextView)findViewById(R.id.music_play_type_music)).setTextColor(Color.parseColor("#ffffff"));
            ((TextView)findViewById(R.id.music_play_type_music)).setTextSize(TypedValue.COMPLEX_UNIT_SP,12f);
            findViewById(R.id.music_play_type_music).setBackgroundResource(R.drawable.round_songtype_orig);
            findViewById(R.id.music_play_cover_frame_music).setBackgroundResource(R.drawable.border_cover_orig);
            ((ImageView)findViewById(R.id.music_play_cover_tri_music)).setImageResource(R.drawable.ic_triangle_original);
        } else {
            ((TextView)findViewById(R.id.music_play_type_music)).setText("커버곡");
            ((TextView)findViewById(R.id.music_play_type_music)).setTextColor(Color.parseColor("#000000"));
            ((TextView)findViewById(R.id.music_play_type_music)).setTextSize(TypedValue.COMPLEX_UNIT_SP,12f);
            findViewById(R.id.music_play_type_music).setBackgroundResource(R.drawable.round_songtype);
            findViewById(R.id.music_play_cover_frame_music).setBackgroundResource(R.drawable.border_cover);
            ((ImageView)findViewById(R.id.music_play_cover_tri_music)).setImageResource(R.drawable.ic_triangle_cover);
        }
        try{
            ImageView img = (ImageView)findViewById(R.id.music_play_cover_frame_music);
            Glide.with(getApplicationContext()).load(audioItem.getImg_path()).into(img);
            Glide.with(getApplicationContext()).load(audioItem.getImg_path())
                    .apply(bitmapTransform(new BlurTransformation(15, 3)))
                    .into(((ImageView)findViewById(R.id.music_play_background_music)));
        }
        catch(Exception e){}

    }
    private String secondsToString(int pTime) {
        if(pTime >= 3600)
            return String.format("%d:%02d:%02d", pTime / 3600, pTime / 60 % 60, pTime % 60);
        else
            return String.format("%d:%02d", pTime / 60 % 60, pTime % 60);
    }

    class SeekThread extends Thread {
        @Override
        public void run() { // 쓰레드가 시작되면 콜백되는 메서드
            // 씨크바 막대기 조금씩 움직이기 (노래 끝날 때까지 반복)
            int times = 0;
            while(AudioApplication.getInstance().getServiceInterface().isPlaying() && seekBarControl) {
                final int current = AudioApplication.getInstance().getServiceInterface().getCurrentPosition();
                sb.setProgress(current);
                times = (times + 1) % 100;
                if(times == 0)
                    runOnUiThread(new Runnable() {
                        public void run() { // 메시지 큐에 저장될 메시지의 내용
                            if(current >= 3600000)
                                txt_current.setText(String.format("%d:%02d:%02d", current / 3600000, current / 60000 % 60, current / 1000 % 60));
                            else
                                txt_current.setText(String.format("%d:%02d", current / 60000 % 60, current / 1000 % 60));
                        }
                    });
            }
        }
    }
    private void registerBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastActions.PREPARED);
        filter.addAction(BroadcastActions.PLAY_STATE_CHANGED);
        registerReceiver(mBroadcastReceiver, filter);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterBroadcast();
    }

    private void unregisterBroadcast() {
        unregisterReceiver(mBroadcastReceiver);
    }

}
