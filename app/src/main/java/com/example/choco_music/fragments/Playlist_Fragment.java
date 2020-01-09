package com.example.choco_music.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.choco_music.R;
import com.example.choco_music.model.VerticalData;

import java.io.IOException;
import java.util.ArrayList;

public class Playlist_Fragment extends Fragment {

    //RecyclerView audiolistView;
    //private ArrayList<VerticalData> fileList= new ArrayList<>();//스트리밍 전체 리스트
    //private RecyclerView.Adapter listViewAdapter;
    //private RecyclerView.LayoutManager layoutManager;
    private Button buttonStop, buttonStart;
    private boolean playPause;
    private ProgressDialog progressDialog;
    private boolean initalStage = true;
    String url= "https://chocomusic.s3.ap-northeast-2.amazonaws.com/%EC%95%BC%EC%83%9D%ED%99%94.mp3";
    String url1="https://chocomusic.s3.ap-northeast-2.amazonaws.com/SongOwn/%EC%95%BC%EC%83%9D%ED%99%94.mp3";
    private MediaPlayer mediaPlayer;
    //private static PowerManager.WakeLock wakeLock;
    //Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.playlist_fragment, null, false);

        buttonStart = (Button) view.findViewById(R.id.button1);
        buttonStop = (Button) view.findViewById(R.id.button2);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        progressDialog = new ProgressDialog(getContext());
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!playPause){
                    buttonStart.setText("Pause Streaming");
                    if(initalStage){
                        new Player().execute(url1);
                    }else {
                        if (!mediaPlayer.isPlaying()) {
                            mediaPlayer.start();
                        }
                    }
                    playPause = true;
                }else {
                    buttonStart.setText("Launch Streaming");
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    }
                    playPause = false;
                }
            }
        });


        return view;
    }
    @Override
    public void onPause() {
        super.onPause();
        if(mediaPlayer != null){
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    class Player extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Buffering...");
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            Boolean prepared = false;

            try {
                mediaPlayer.setDataSource(strings[0]);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        initalStage = true;
                        playPause = false;
                        buttonStart.setText("Launch Streaming");
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                    }
                });

                mediaPlayer.prepare();
                prepared = true;
            } catch (IOException e) {
                Log.e("MyAudioStreamingApp",e.getMessage());
                prepared = false;
                e.printStackTrace();
            }
            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(progressDialog.isShowing()){
                progressDialog.cancel();
            }

            mediaPlayer.start();
            initalStage = false;
        }
    }


}
