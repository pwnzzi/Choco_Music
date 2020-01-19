package com.example.choco_music.Audio;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.example.choco_music.model.ChartData;

import java.util.ArrayList;

public class AudioServiceInterface {
    private ServiceConnection mServiceConnection;
    private AudioService mService;

    public AudioServiceInterface(Context context) {
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mService = ((AudioService.AudioServiceBinder) service).getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mServiceConnection = null;
                mService = null;
            }
        };
        context.bindService(new Intent(context, AudioService.class)
                .setPackage(context.getPackageName()), mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void setPlayList(ArrayList<ChartData> audioDatas) {
        if (mService != null) {
            mService.setPlayList(audioDatas);
        }
    }

    public void play(int position) {
        if (mService != null) {
            mService.play(position);
        }
    }

    public void play() {
        if (mService != null) {
            mService.play();
        }
    }

    public void pause() {
        if (mService != null) {
            mService.play();
        }
    }

    public void pause_home_fragment() {
        if (mService != null) {
            mService.pause();
        }
    }

    public void play_home_fragment() {
        if (mService != null) {
            mService.play();
        }
    }

    public void forward() {
        if (mService != null) {
            mService.forward();
        }
    }

    public void rewind() {
        if (mService != null) {
            mService.rewind();
        }
    }

    public void togglePlay() {
        if (isPlaying()) {
            mService.pause();
        } else {
            mService.play();
        }
    }

    public boolean isPlaying() {
        if (mService != null) {
            return mService.isPlaying();
        }
        return false;
    }

    public ChartData getAudioItem() {
        if (mService != null) {
            return mService.getAudioItem();
        }
        return null;
    }

    public void toggleShuffle(){
        if (mService != null) {
            mService.toggleShuffle();
        }
    }

    public void toggleRepeat(){
        if (mService != null) {
            mService.toggleRepeat();
        }
    }

    public boolean getShuffle(){
        if (mService != null) {
            return mService.getShuffle();
        }
        return false;
    }

    public int getRepeat(){
        if (mService != null) {
            return mService.getRepeat();
        }
        return -1;
    }

    public int getCurrentPosition(){
        if (mService != null) {
            return mService.getCurrentPosition();
        }
        return 0;
    }

    public int getDuration(){
        if (mService != null) {
            return mService.getDuration();
        }
        return 0;
    }

    public void seekTo(int time){
        if (mService != null) {
            mService.seekTo(time);
        }
    }

}

