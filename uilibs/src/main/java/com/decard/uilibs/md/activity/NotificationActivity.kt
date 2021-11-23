package com.decard.uilibs.md.activity

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.RemoteViews
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.decard.uilibs.R
import com.decard.uilibs.ThumbSeekBarActivity
import kotlinx.android.synthetic.main.activity_notification.*

/**
 * 通知栏
 * @author ZJ
 */
class NotificationActivity : AppCompatActivity() {

    private val TAG = "---NotificationActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        btn_general.setOnClickListener {
            generalNotification()
        }
        btn_fold.setOnClickListener {
            foldNotification()
        }

        btn_hang.setOnClickListener {
            hangNotification()
        }



    }

    private fun generalNotification() {
        val resultIntent = Intent(this, ThumbSeekBarActivity::class.java)
        resultIntent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent =
            PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = NotificationCompat.Builder(this, "1")
            .setSmallIcon(R.mipmap.ic_fail)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_success))
            .setAutoCancel(true)
            .setContentTitle("普通通知")
            .setContentIntent(pendingIntent)
            .setTicker("测试通知来啦")//通知首次出现在通知栏，带上升动画效果的
            .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示
            .setPriority(Notification.PRIORITY_DEFAULT)//设置该通知优先级
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, builder.build())
    }


    @SuppressLint("RemoteViewLayout")
    private fun foldNotification() {
        val remoteView = RemoteViews(packageName, R.layout.activity_anim)

        val resultIntent = Intent(this, ThumbSeekBarActivity::class.java)
        resultIntent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent =
            PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = NotificationCompat.Builder(this, "1")
            .setSmallIcon(R.mipmap.ic_fail)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_success))
            .setAutoCancel(true)
            .setContentTitle("普通通知")
            .setContentIntent(pendingIntent)
            .setTicker("测试通知来啦")//通知首次出现在通知栏，带上升动画效果的
            .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示
            .setPriority(Notification.PRIORITY_DEFAULT)//设置该通知优先级
            .setCustomBigContentView(remoteView)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, builder.build())
    }

    @SuppressLint("RemoteViewLayout")
    private fun hangNotification() {
        val remoteView = RemoteViews(packageName, R.layout.activity_anim)

        val resultIntent = Intent(this, ThumbSeekBarActivity::class.java)
        resultIntent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent =
            PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val hangPendingIntent =
            PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT)
        val builder = NotificationCompat.Builder(this, "1")
            .setSmallIcon(R.mipmap.ic_fail)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_success))
            .setAutoCancel(true)
            .setContentTitle("普通通知")
            .setContentIntent(pendingIntent)
            .setTicker("测试通知来啦")//通知首次出现在通知栏，带上升动画效果的
            .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示
            .setPriority(Notification.PRIORITY_DEFAULT)//设置该通知优先级
            .setCustomBigContentView(remoteView)
            .setFullScreenIntent(hangPendingIntent, true)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, builder.build())
    }
}