package com.decard.cplibs

import android.util.Log
import com.example.commonlibs.BaseApplication
import com.example.commonlibs.utils.LogUtils

class MyApp : BaseApplication() {
    private val TAG = "---MyApp"

    override fun onCreate() {
        super.onCreate()
        instance = this

        LogUtils.config(true, true)
        Log.d(TAG, "onCreate")
    }

    companion object {
        lateinit var instance: MyApp
    }
}
