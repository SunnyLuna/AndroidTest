package com.decard.cplibs

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import org.slf4j.LoggerFactory

class MainActivity : AppCompatActivity() {
    private val logger = LoggerFactory.getLogger("---MainActivity")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById<Button>(R.id.btn_get)
        button.setOnClickListener {
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
                logger.debug("onCreate: ${result!!.getString("appVersionName")}")
            }

        }



    }
}