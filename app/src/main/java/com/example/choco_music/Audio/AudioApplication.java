package com.example.choco_music.Audio;

import android.app.Application;

import com.example.choco_music.Login.GlobalApplication;

public class AudioApplication extends Application {
    private static AudioApplication mInstance;
    private AudioServiceInterface mInterface;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mInterface = new AudioServiceInterface(getApplicationContext());
    }

    public static AudioApplication getInstance() {
        return mInstance;
    }

    public AudioServiceInterface getServiceInterface() {
        return mInterface;
    }
}
