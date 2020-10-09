package com.example.commonlibs.utils;


import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

import java.io.IOException;
import java.util.HashMap;

/**
 * 播报本地语音
 *
 * @author ZJ
 * created at 2020/9/25 9:18
 */
public class SoundUtils implements SoundPool.OnLoadCompleteListener {

    private final String TAG = "SoundUtils";

    private HashMap<String, Integer> mSoundMap;
    private SoundPool mSoundPool;

    private int MAX_NUM = 5;// 同时播放的流的最大数量

    private AssetManager mAssetManager;
    private AudioManager mAudioManager;
    private static volatile SoundUtils mSoundUtils;


    public static SoundUtils getInstance() {
        if (mSoundUtils == null) {
            synchronized (SoundUtils.class) {
                mSoundUtils = new SoundUtils();
            }
        }
        return mSoundUtils;
    }


    public boolean init(Context context) {

        mSoundMap = new HashMap<>();
        mAssetManager = context.getAssets();
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mSoundPool = new SoundPool(MAX_NUM, AudioManager.STREAM_MUSIC, 0);
        mSoundPool.setOnLoadCompleteListener(this);
        return loadSound();

    }


    private boolean loadSound() {
        try {
            String[] filePaths = mAssetManager.list("sound");

            for (final String path : filePaths) {
//                if(path.equals("fonts")){//过滤掉fonts等系统文件夹
//                    continue;
//                }
//                Log.e("-->loadSound",path);
                AssetFileDescriptor afd = mAssetManager.openFd("sound/" + path);

                final int soundId = mSoundPool.load(afd, 1);

                mSoundMap.put(path, soundId);


            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }


    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        if (status == 0) {
        }
    }

    public boolean playSound(String fileName) {
        if (mSoundMap == null) {
            return false;
        }
        final float currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
                / (float) mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        int soundId = mSoundMap.get(fileName + ".wav");
        return mSoundPool.play(soundId, currentVolume, currentVolume, 1, 0, 1.0f) != 0;
    }


    public boolean release() {
        if (mSoundPool != null) {

            mSoundPool.release();
//            mAssetManager.close();
            mSoundMap.clear();

            mSoundMap = null;
            mAssetManager = null;
            mSoundPool = null;
        }
        return true;
    }

}

