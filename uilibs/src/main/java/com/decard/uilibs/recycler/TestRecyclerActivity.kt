package com.decard.uilibs.recycler

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.decard.uilibs.R
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_test_recycler.*
import java.util.concurrent.TimeUnit

class TestRecyclerActivity : AppCompatActivity() {

    lateinit var subscribe: Disposable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_recycler)

        val tradeList = ArrayList<Trade>()
        for (i in 1..10) {
            val trade = Trade("$i")
            tradeList.add(trade)
        }
        val centerLayoutManager = CenterLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_trade.layoutManager = centerLayoutManager
        val adapter = TestAdapter(tradeList)
        rv_trade.adapter = adapter
        var currentPos = 0
        subscribe = Observable.interval(3, TimeUnit.SECONDS).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (currentPos < tradeList.size - 1) {
                    currentPos++
//                    rv_trade.smoothScrollToPosition(currentPos)
                    adapter.markCurrentPosition(currentPos)
                } else {
                    currentPos = 0
                    adapter.markCurrentPosition(currentPos) 
//                    rv_trade.smoothScrollToPosition(currentPos)
                }
                    centerLayoutManager.smoothScrollToPosition(
                        rv_trade,
                        RecyclerView.State(),
                        currentPos
                    )
            }
    }
}