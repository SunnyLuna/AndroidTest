package com.decard.androidtest.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.decard.androidtest.R
import com.decard.androidtest.databinding.FragmentShowSocialCardBinding
import com.decard.androidtest.viewmodel.SocialCardViewModel
import com.decard.androidtest.viewmodel.TimerViewModel
import com.example.commonlibs.utils.ScreenUtils

class SocialCardActivity : AppCompatActivity() {
    private val TAG = "---CardActivity"
    private lateinit var mBinding: FragmentShowSocialCardBinding
    private var timerViewModel: TimerViewModel? = null
    private var socialViewModel: SocialCardViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(
            this,
            R.layout.fragment_show_social_card
        )
        socialViewModel = ViewModelProvider(this)[SocialCardViewModel::class.java]
        mBinding.socialCard = socialViewModel
        socialViewModel!!.readSocialCard(this)

        //显示倒计时
        timerViewModel = ViewModelProvider(this)[TimerViewModel::class.java]
        timerViewModel!!.timerLiveData.observe(this, observer)
        timerViewModel!!.startTimer(100)
        mBinding.timer = timerViewModel
        val smallestWidthDP = ScreenUtils.getSmallestWidthDP(this)
        Log.d(TAG, "onCreate: $smallestWidthDP")
    }

    private val observer: Observer<String> = Observer { s: String? ->
        Log.d(TAG, ": 时间到了")
    }
}