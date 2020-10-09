package com.decard.uilibs.spinner


import android.widget.TextView
import androidx.databinding.BindingAdapter

object SunnyBinding {

    @BindingAdapter("showColor")
    @JvmStatic
    fun showColor(textView: TextView, color: Int) {
        textView.setTextColor(color)
    }
}