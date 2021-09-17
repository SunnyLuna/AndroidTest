package com.decard.cplibs

import android.app.Application
import android.util.Log

class MyApp : Application() {
    private val TAG = "---MyApp"

    override fun onCreate() {
        super.onCreate()
        instance = this
        Log.d(TAG, "onCreate")
    }

    companion object {
        lateinit var instance: MyApp
    }
}
