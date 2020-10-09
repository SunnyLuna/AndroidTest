package com.decard.uilibs

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_download_progress_button.*
import java.util.concurrent.TimeUnit

/**
 *
 * @author ZJ
 * created at 2020/9/11 14:48
 */
class DownloadProgressButtonActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download_progress_button)

        dpb.setButtonMode(0)
        dpb.setPbColor(resources.getColor(R.color.colorAccent))
        dpb.setPbTextColor(resources.getColor(R.color.colorBlack))
        dpb.setOnClickListener {
            var ti = 0
            dpb.setButtonMode(1)
            Observable.interval(50, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    ti++
                    dpb.setProgress(ti)
                }

        }
    }
}