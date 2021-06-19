package com.decard.dblibs.column

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.decard.dblibs.AppDataBase
import com.decard.dblibs.R
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.schedulers.Schedulers

class Test : AppCompatActivity() {
    private val TAG = "---Test"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test)
        RxPermissions(this).request(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ).subscribe {
            Log.d(TAG, "onCreate: $it")
            if (it) {
                val peerBean = PeerBean("雪花")
                val subscribe = AppDataBase.getInstance(this).getPeerDao().addPeer(peerBean)
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        AppDataBase.getInstance(this).getPeerDao().getPeers().subscribe({
                            Log.d(TAG, "onCreate: ${it.size}")
                            for (peer in it) {
                                Log.d(TAG, "onCreate: $peer")
                            }
                        }, {
                            Log.d(TAG, "onCreate: ${it.message}")
                        })
                    }, {
                        Log.d(TAG, "onCreate: ${it.message}")
                    })
            }
        }

    }
}