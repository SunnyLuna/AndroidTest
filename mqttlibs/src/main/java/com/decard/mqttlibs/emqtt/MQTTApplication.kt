package com.decard.mqttlibs.emqtt

import android.app.Application

class MQTTApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: MQTTApplication
    }
}