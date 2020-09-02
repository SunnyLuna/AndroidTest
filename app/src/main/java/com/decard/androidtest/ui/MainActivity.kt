package com.decard.androidtest.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.decard.androidtest.MyApplication
import com.decard.androidtest.R
import com.decard.androidtest.adapter.TradeOldAdapter
import com.decard.androidtest.databinding.ActivityMainBinding
import com.decard.androidtest.net.InjectorUtils
import com.decard.androidtest.viewmodel.TradeViewModel
import com.tbruyelle.rxpermissions2.RxPermissions

class MainActivity : AppCompatActivity() {
    private val TAG = "---MainActivity"
    lateinit var mBinding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        mBinding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this,
                R.layout.activity_main
            )
        RxPermissions(this).request(android.Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe {
            if (it) {
                initData()
            }
        }
//        runBlocking {
//            val await = WebService.create().loadData().await()
//            Log.d(TAG, "code: ${await.code}  msg: ${await.msg}");
//            Log.d(TAG, "trade: ${await.result.trade}")
//        }
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