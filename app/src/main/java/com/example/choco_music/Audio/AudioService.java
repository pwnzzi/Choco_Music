package com.example.choco_music.Audio;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;

import com.example.choco_music.model.CoverData;
import com.example.choco_music.model.VerticalData;

import java.util.ArrayList;

public class AudioService extends Service {
    private final IBinder mBinder = new AudioServiceBinder();
    private ArrayList<VerticalData> mAudioDatas = new ArrayList<>();
    private ArrayList<CoverData> Cover_AudioDatas = new ArrayList<>();
    private MediaPlayer mMediaPlayer;
    private boolean isPrepared;
    private int mCurrentPosition;
    private VerticalData currentData;
    private CoverData currentData_Cover;
    private NotificationPlayer mNotificationPlayer;



    public class AudioServiceBinder extends Binder {
        AudioService getService() {
            return AudioService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                isPrepared = true;
                mp.start();
                sendBroadcast(new Intent(BroadcastActions.PREPARED)); // prepared 전송
                updateNotificationPlayer();
            }
        });
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                isPrepared = false;
                sendBroadcast(new Intent(BroadcastActions.PLAY_STATE_CHANGED)); // 재생상태 변경 전송
                updateNotificationPlayer();
            }
        });
        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                isPrepared = false;
                sendBroadcast(new Intent(BroadcastActions.PLAY_STATE_CHANGED)); // 재생상태 변경 전송
                updateNotificationPlayer();
                return false;
            }
        });


        mNotificationPlayer = new NotificationPlayer(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void queryAudioItem(int position) {
        mCurrentPosition = position;
        currentData = mAudioDatas.get(position);
    }

    private void queryAudioItem_cover(int position) {
        mCurrentPosition = position;
        currentData_Cover = Cover_AudioDatas.get(position);
    }

    private void prepare() {
        try {
            mMediaPlayer.setDataSource(currentData.getFilerul());
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void prepare_cover() {
        try {
            mMediaPlayer.setDataSource(currentData_Cover.getFilerul());
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stop() {
        mMediaPlayer.stop();
        mMediaPlayer.reset();
    }

    public void setPlayList(ArrayList<VerticalData> audioDatas) {
        if (!mAudioDatas.equals(audioDatas)) {
            mAudioDatas.clear();
            mAudioDatas.addAll(audioDatas);
        }
    }

    public void setPlayList_Cover(ArrayList<CoverData> audioDatas_cover) {
        if (!Cover_AudioDatas.equals(audioDatas_cover)) {
            Cover_AudioDatas.clear();
            Cover_AudioDatas.addAll(audioDatas_cover);
        }
    }

    public void play(int position) {
        queryAudioItem(position);
        stop();
        prepare();
    }

    public void play_cover(int position) {
        queryAudioItem_cover(position);
        stop();
        prepare_cover();
    }

    public void play() {
        if (isPrepared) {
            mMediaPlayer.start();
            sendBroadcast(new Intent(BroadcastActions.PLAY_STATE_CHANGED)); // 재생상태 변경 전송
            updateNotificationPlayer();
        }
    }

    public void pause() {
        if (isPrepared) {
            mMediaPlayer.pause();
            sendBroadcast(new Intent(BroadcastActions.PLAY_STATE_CHANGED)); // 재생상태 변경 전송
            updateNotificationPlayer();
        }
    }


    public void forward() {
        if (mAudioDatas.size() - 1 > mCurrentPosition) {
            mCurrentPosition++; // 다음 포지션으로 이동.
        } else {
            mCurrentPosition = 0; // 처음 포지션으로 이동.
        }
        play(mCurrentPosition);
    }

    public void rewind() {
        if (mCurrentPosition > 0) {
            mCurrentPosition--; // 이전 포지션으로 이동.
        } else {
            mCurrentPosition = mAudioDatas.size() - 1; // 마지막 포지션으로 이동.
        }
        play(mCurrentPosition);
    }

    public VerticalData getAudioItem() {
        return mAudioDatas.get(mCurrentPosition);
    }

    public CoverData getAudioItem_cover() {
        return  Cover_AudioDatas.get(mCurrentPosition);
    }

    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    private void updateNotificationPlayer() {
        if (mNotificationPlayer != null) {
            mNotificationPlayer.updateNotificationPlayer();
        }
    }

    private void removeNotificationPlayer() {
        if (mNotificationPlayer != null) {
            mNotificationPlayer.removeNotificationPlayer();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (CommandActions.TOGGLE_PLAY.equals(action)) {
                if (isPlaying()) {
                    pause();
                } else {
                    play();
                }
            } else if (CommandActions.REWIND.equals(action)) {
                rewind();
            } else if (CommandActions.FORWARD.equals(action)) {
                forward();
            } else if (CommandActions.CLOSE.equals(action)) {
                pause();
                removeNotificationPlayer();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

}

