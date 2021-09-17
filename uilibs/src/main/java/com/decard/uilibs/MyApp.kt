package com.decard.uilibs

import com.example.commonlibs.BaseApplication

class MyApp : BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: MyApp
    }

}