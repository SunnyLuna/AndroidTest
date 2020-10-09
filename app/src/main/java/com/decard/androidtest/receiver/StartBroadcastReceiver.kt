package com.decard.androidtest.receiver

import  android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.decard.androidtest.ui.MainActivity

class StartBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val action = intent.action
        Log.d("StartBroadcastReceiver", "=============>$action")
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            startActivity(context)
        }
    }

    private fun startActivity(context: Context) {
        val startSelfIntent = Intent(context, MainActivity::class.java)
        startSelfIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startSelfIntent.action = "android.intent.action.MAIN"
        startSelfIntent.addCategory("android.intent.category.default")
        context.startActivity(startSelfIntent)
    }
}
