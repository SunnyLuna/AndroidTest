package com.decard.cplibs

import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import org.slf4j.LoggerFactory

/**
 * 服务
 * @author ZJ
 * created at 2021/8/16 10:07
 */
class ManagerService : Service() {

    private val logger = LoggerFactory.getLogger("---ManagerService")

    override fun onCreate() {
        val param = Bundle()
        param.putString("appVersionName", "")
        val contentProviderClient =
            this.contentResolver.acquireUnstableContentProviderClient(
                Uri.parse("content://com.decard.testmanagerservice")
            )
        if (contentProviderClient == null) {
            logger.debug("onCreate: 空")
        } else {
            val result = contentProviderClient.call("GetAppInfo", "GetAppInfo", param)
            logger.debug("onCreate: $result")
        }
    }

    override fun onBind(intent: Intent): IBinder? {

        val bundle = intent.extras
        if (bundle != null) {
            val authority = bundle.getString("Authority")
            logger.debug("onBind: authority $authority")

        }
        return null
    }


}