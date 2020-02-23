package com.example.choco_music.Login;

import android.app.Application;
import android.provider.MediaStore;

import com.example.choco_music.Audio.AudioApplication;
import com.kakao.auth.KakaoSDK;

public class GlobalApplication extends AudioApplication {

    private static GlobalApplication instance;

    public static GlobalApplication getGlobalApplicationContext(){
        if(instance == null){
            throw new IllegalStateException("This Application does not inherit com.kakao.GlobalApplication");
        }
        return instance;
    }

    @Override

    public void onCreate() {

        super.onCreate();

        instance = this;
        // Kakao Sdk 초기화

        KakaoSDK.init(new KaKaoSDKAdapter());

    }

    @Override

    public void onTerminate() {

        super.onTerminate();

        instance = null;

    }
}
