package com.decard.androidtest.viewmodel;

import android.os.CountDownTimer;
import android.util.Log;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * 倒计时Viewmodel
 */
public class TimerViewModel extends ViewModel {
    private static final String TAG = "---TimerViewModel";
    private MutableLiveData<String> timerLiveData = new MutableLiveData<>();
    public ObservableField<String> timeObservable = new ObservableField<>();
    private CountDownTimer countDownTimer;


    public MutableLiveData<String> getTimerLiveData() {
        return timerLiveData;
    }

    public void startTimer(int time) {
        Log.d(TAG, "startTimer: " + time);
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        time++;
        countDownTimer = new CountDownTimer(time * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeObservable.set(String.valueOf(millisUntilFinished / 1000));
                if (millisUntilFinished / 1000 == 1) {
                    timerLiveData.postValue("END");
                }
            }

            @Override

            public void onFinish() {
                Log.d(TAG, "onFinish: ");
            }
        }.start();
    }

    public void stopTimer() {
        Log.d(TAG, "stopTimer: ");
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
