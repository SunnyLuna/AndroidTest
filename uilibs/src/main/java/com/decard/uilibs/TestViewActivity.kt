package com.decard.uilibs

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.decard.uilibs.utils.DensityUtils
import kotlinx.android.synthetic.main.activity_test_view.*

class TestViewActivity : AppCompatActivity() {
    private val TAG = "---TestViewActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_view)
        val dp2px = DensityUtils.dp2px(this, 300f)
        Log.d(TAG, "dp2px: $dp2px")
//        customEvaluator.setSelectNum(4)
//        customEvaluator.setOnSelectStarListener {
//            tv_select.text = it.toString()
//        }
    }
}