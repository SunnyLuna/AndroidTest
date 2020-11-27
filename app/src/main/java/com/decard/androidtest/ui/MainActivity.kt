package com.decard.androidtest.ui

import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.work.impl.utils.PreferenceUtils
import com.decard.androidtest.MyApplication
import com.decard.androidtest.R
import com.decard.androidtest.adapter.TradeOldAdapter
import com.decard.androidtest.bean.TestBean
import com.decard.androidtest.databinding.ActivityMainBinding
import com.decard.androidtest.net.InjectorUtils
import com.decard.androidtest.net.WebService
import com.decard.androidtest.viewmodel.TradeViewModel
import com.example.commonlibs.net.TrafficStatisticsUtils
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import java.io.FileOutputStream
import java.io.InputStream

class MainActivity : AppCompatActivity() {
    private val TAG = "---MainActivity"
    lateinit var mBinding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        mBinding =
            DataBindingUtil.setContentView(
                this,
                R.layout.activity_main
            )
        RxPermissions(this).request(android.Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe {
            if (it) {
//                initData()
//                testNet()
//                downloadAV()
                testTime()
            }
        }
//        runBlocking {
//            val await = WebService.create().loadData().await()
//            Log.d(TAG, "code: ${await.code}  msg: ${await.msg}");
//            Log.d(TAG, "trade: ${await.result.trade}")
//        }
    }

    private fun testTime() {

        btn_send.setOnClickListener {
//            WebService.create().load().subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread()).subscribe({
//                    Log.d(TAG, "testTime: ${it.code} + ${it.msg}")
//                    val adapter = TradeOldAdapter(it.result.trade)
//                    mBinding.rvTrade.adapter = adapter
//                }, {
//                    Log.d(TAG, "testTime: " + it.message)
//                })
            WebService.create().test().subscribeOn(Schedulers.io()).subscribe {
                Log.d(TAG, "testTime: " + it.toLong())
            }
            for (i in 0..20) {
                Log.d(TAG, "testTime: ${i.toString()}")
            }
        }
    }

    private fun downloadAV() {
//        WebService.create().getPhoto("今年").subscribeOn(Schedulers.io()).subscribe({
//            Log.d(TAG, "downloadAV: $it")
//        }, {
//            Log.d(TAG, "downloadAV: ${it.message}")
//        })

        WebService.create().getPhoto("测试接口").subscribeOn(Schedulers.io())
            .flatMap(object : Function<TestBean, Observable<ResponseBody>> {
                override fun apply(t: TestBean): Observable<ResponseBody> {
                    Log.d(TAG, "apply: $t")
                    return WebService.createAV().getAV()
                }
            }).subscribe({
                Log.d(TAG, "downloadAV: 成功")
                readAV(it)
            }, {
                Log.d(TAG, "downloadAV: ${it.message}")
            })
//        WebService.createAV().getAV().subscribeOn(Schedulers.io()).subscribe({
//            readAV(it)
//        }, {
//            Log.d(TAG, "downloadAV: ${it.message}")
//        })
    }

    private fun readAV(it: ResponseBody) {
        var inputStream: InputStream? = null
        val responseLength: Long
        val fos: FileOutputStream? = null
        try {
            val buf = ByteArray(1024 * 10)
            var byteread = 0
            responseLength = it.contentLength()
            inputStream = it.byteStream()
            Log.d(TAG, "downloadAV:responseLength $responseLength")
            val newPath = Environment.getExternalStorageDirectory()
                .absolutePath + "/2019_06_18_11_01.mp4"
            val fs = FileOutputStream(newPath)
            val buffer = ByteArray(1024 * 10)
            var bytesum = 0
            while (inputStream.read(buffer).also { byteread = it } != -1) {
                bytesum += byteread //字节数 文件大小
                println(bytesum)
                fs.write(buffer, 0, byteread)
            }
            inputStream.close()
        } catch (e: Exception) {
            Log.d(TAG, "downloadAV: ${e.message}")
        } finally {
            try {
                inputStream?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun testNet() {
        val trafficStatistics = TrafficStatisticsUtils.getTrafficStatistics(this, 1) / (1000 * 1024)
        Log.d(TAG, "testNet: $trafficStatistics")
    }

    //    val adapter = TradeAdapter()

    private fun initData() {
        val viewModel: TradeViewModel =
            InjectorUtils.provideTradeViewModelFactory(MyApplication.instance)
                .create(TradeViewModel::class.java)


        viewModel.tradeData.observe(this, Observer {
            Log.d(TAG, "onCreate: ${it.size}")
            val adapter = TradeOldAdapter(it)
            mBinding.rvTrade.adapter = adapter
            for (trade in it) {
                Log.d(TAG, "onCreate: $trade")
            }
        })
    }


}