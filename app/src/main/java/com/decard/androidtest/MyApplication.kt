package com.decard.androidtest

import com.example.commonlibs.BaseApplication

class MyApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: MyApplication
    }
}