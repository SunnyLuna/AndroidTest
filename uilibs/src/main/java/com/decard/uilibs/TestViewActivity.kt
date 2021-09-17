package com.decard.uilibs

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.commonlibs.utils.DensityUtils
import com.example.commonlibs.utils.ScreenUtils
import kotlinx.android.synthetic.main.activity_test_view.*

class TestViewActivity : AppCompatActivity() {
    private val TAG = "---TestViewActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_view)
        val heightPixels = ScreenUtils.heightPixels(this)
        Log.d(TAG, "onCreate: heightPixels  $heightPixels")
        val widthPixels = ScreenUtils.widthPixels(this)
        Log.d(TAG, "onCreate: widthPixels  $widthPixels")
        val density = ScreenUtils.density(this)
        Log.d(TAG, "onCreate: density  $density")
        val densityDpi = ScreenUtils.densityDpi(this)
        Log.d(TAG, "onCreate: densityDpi  $densityDpi")
        val widthDP = ScreenUtils.getWidthDP(this)
        Log.d(TAG, "onCreate: widthDP  $widthDP")
        val heightDP = ScreenUtils.getHeightDP(this)
        Log.d(TAG, "onCreate: heightDP:$heightDP")
        val smallestWidthDP = ScreenUtils.getSmallestWidthDP(this)
        Log.d(TAG, "onCreate: smallestWidthDP  $smallestWidthDP")


        Log.d(TAG, "onCreate: ${DensityUtils.dp2px(this,100f)}")
        customEvaluator.setSelectNum(4)
        customEvaluator.setOnSelectStarListener {
            tv_select.text = it.toString()
        }
    }
}