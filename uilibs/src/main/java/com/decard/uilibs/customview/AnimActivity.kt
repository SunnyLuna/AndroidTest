package com.decard.uilibs.customview

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.decard.uilibs.R
import kotlinx.android.synthetic.main.activity_anim.*

class AnimActivity : AppCompatActivity() {

    private val TAG = "---AnimActivity"
    private var level = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anim)

//        val transitionDrawable =
        //            ResourcesCompat.getDrawable(resources, R.drawable.transition, null) as TransitionDrawable
//        iv_transaction.setImageDrawable(transitionDrawable)
//        transitionDrawable.startTransition(5000)

//        val drawable =
//            ResourcesCompat.getDrawable(resources, R.drawable.rotate, null) as RotateDrawable
//        iv_transaction.setImageDrawable(drawable)
//        iv_transaction.setOnClickListener {
//            drawable.level += 1000
//        }

//        iv_transaction.rotation = 180f

        btn_gradient.setOnClickListener {
            myRing.setRingColor(Color.parseColor("#008BFF"), Color.parseColor("#FFFFFF"))
        }
        btn_stop.setOnClickListener {
            myRing.setRingColor(Color.parseColor("#00F99D"))
        }
    }
}