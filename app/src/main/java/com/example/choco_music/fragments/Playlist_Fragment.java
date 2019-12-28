package com.example.choco_music.fragments;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.choco_music.R;

import java.io.IOException;

public class Playlist_Fragment extends Fragment {

    Button buttonStop, buttonStart;

    String AudioURL= "https://";

    MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.playlist_fragment, null, false);

      buttonStart =(Button) view.findViewById(R.id.button1);
      buttonStop =(Button) view.findViewById(R.id.button2);

      mediaPlayer = new MediaPlayer();
      mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);


      buttonStart.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              try{
                  mediaPlayer.setDataSource(AudioURL);
                  mediaPlayer.prepare();
              } catch (IllegalArgumentException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
              } catch (SecurityException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
              } catch (IllegalStateException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
              } catch (IOException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
              }
              mediaPlayer.start();
          }
      });

      buttonStart.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              mediaPlayer.stop();
          }
      });
        return view;
    }
}
