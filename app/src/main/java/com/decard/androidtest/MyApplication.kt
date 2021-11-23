package com.decard.androidtest

import com.example.commonlibs.BaseApplication
import com.example.commonlibs.utils.LogUtils

class MyApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        LogUtils.config(true, true)
    }

    companion object {
        lateinit var instance: MyApplication
    }
}