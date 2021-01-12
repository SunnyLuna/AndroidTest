package com.decard.uilibs

import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GestureDetectorCompat
import kotlinx.android.synthetic.main.activity_gesture.*

class GestureActivity : AppCompatActivity(), GestureDetector.OnGestureListener,
    GestureDetector.OnDoubleTapListener {

    private val TAG = "---GestureActivity"

    private lateinit var mGestureDetectorCompat: GestureDetectorCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gesture)
        mGestureDetectorCompat = GestureDetectorCompat(this, this)
        mGestureDetectorCompat.setOnDoubleTapListener(this)
        btn.setOnClickListener {
            btn.isEnabled = false
        }
        val transition = ResourcesCompat.getDrawable(
            resources,
            R.drawable.transition,
            null
        ) as TransitionDrawable

        val image: ImageView = findViewById(R.id.iv_yan)

        image.setImageDrawable(transition)
//
//        // Description of the initial state that the drawable represents.
        image.contentDescription = "看看效果"
//
//        // Then you can call the TransitionDrawable object's methods.
        transition.startTransition(5000)

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        mGestureDetectorCompat.onTouchEvent(event)
        return super.onTouchEvent(event)

    }

    override fun onShowPress(e: MotionEvent?) {
        Log.d(TAG, "onShowPress: ")
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        Log.d(TAG, "onSingleTapUp: ")
        return false
    }

    override fun onDown(e: MotionEvent?): Boolean {
        Log.d(TAG, "onDown: ")
        return false
    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        Log.d(TAG, "onFling: ")
        return false
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        Log.d(TAG, "onScroll: ")
        return false
    }

    override fun onLongPress(e: MotionEvent?) {
        Log.d(TAG, "onLongPress: ")
    }

    override fun onDoubleTap(e: MotionEvent?): Boolean {
        Log.d(TAG, "onDoubleTap: ")
        return false
    }

    override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
        Log.d(TAG, "onDoubleTapEvent: ")
        return false
    }

    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        Log.d(TAG, "onSingleTapConfirmed: ")
        return false
    }
}