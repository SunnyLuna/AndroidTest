package com.decard.printlibs

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.decard.printlibs.print.DeShiPrintManager
import com.decard.printlibs.print.XinYePrintManager
import com.decard.printlibs.utils.Constant
import com.decard.printlibs.utils.DateUtils
import com.decard.printlibs.utils.ToastUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_de_li.*
import java.util.concurrent.TimeUnit

class PrintActivity : AppCompatActivity() {

    private val TAG = "---DeLiActivity"

    private var subScribe: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_de_li)

//        btn_scan.setOnClickListener {
//            val trade = TradeEntity(
//                "",
//                "焰灵姬",
//                "610523199005272038",
//                "",
//                "",
//                "000061202106010004",
//                "",
//                "",
//                "2021-06-01 14:17:42",
//                "",
//                "05",
//                "04",
//                "02",
//                "2850",
//                "", "", "", "", "00010001202101000061", "01", ""
//            )
//            DeShiPrintManager.printPayInfo(
//                trade
//            )
//        }
//
//        btn_connect.setOnClickListener {
//            val dailyStatisticsBean =
//                DailyStatisticsBean(
//                    DateUtils.getCurrentTime(),
//                    "100",
//                    "￥8000",
//                    "50",
//                    "￥8000",
//                    "60",
//                    "￥8000"
//                )
//
//            DeShiPrintManager.printDailyStatistics(dailyStatisticsBean)
//        }

        btn_print.setOnClickListener {
            var iTime = 60L
            var iCount = 5L
            val time = et_time.text.toString()
            val count = et_count.text.toString()
            if (time != "") {
                iTime = time.toLong()
            }
            if (count != "") {
                iCount = count.toLong()
            }
            var printCount = 0L
            when (Constant.PRINT_MODE) {
                "0" -> {
                    subScribe =
                        Observable.interval(iTime, TimeUnit.SECONDS).subscribeOn(Schedulers.io())
                            .subscribe({
                                printCount++
                                if (printCount == iCount) {
                                    subScribe!!.dispose()
                                }
                                tv_times.text = "剩余打印次数：${iCount - printCount}"
                                val time = DateUtils.getCurrentTime()
                                Log.d(TAG, "onCreate: $time")
                                XinYePrintManager.printPayInfo(
                                    "9000126216",
                                    "80.00元", "焰灵姬", "610532199005369548", time, "18654654564"
                                )
                                XinYePrintManager.dealPowerOff()
                            }, {
                                Log.d(TAG, "onCreate: ${it.message}")
                            })
                }
                "1" -> {
                    subScribe =
                        Observable.interval(iTime, TimeUnit.SECONDS).subscribeOn(Schedulers.io())
                            .subscribe({
                                printCount++
                                if (printCount == iCount) {
                                    subScribe!!.dispose()
                                }
                                tv_times.text = "剩余打印次数：${iCount - printCount}"
                                val time = DateUtils.getCurrentTime()
                                Log.d(TAG, "onCreate: $time")
                                DeShiPrintManager.printPayInfo(
                                    "9000126216",
                                    "80.00元", "焰灵姬", "610532199005369548", time, "18654654564"
                                )
                            }, {
                                Log.d(TAG, "onCreate: ${it.message}")
                            })
                }
                else -> {
                    Log.d(TAG, "onCreate: 未连接上打印机")
                    ToastUtils.toast(this, "未连接上打印机")
                }
            }
        }


        btn_go_paper.setOnClickListener {
            XinYePrintManager.goPaper(1)
            Log.d(TAG, "onCreate: ")
            Observable.create<String> {
                var printPaper = XinYePrintManager.printPaperStatus
                while (printPaper == "") {
                    printPaper = XinYePrintManager.printPaperStatus
                }
                it.onNext(printPaper)
            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Log.d(TAG, "onCreate: $it")
                    if (it == "success") {
                        Log.d(TAG, "onCreate: 成功了")
                    } else {
                        Log.d(TAG, "onCreate: 失败了")
                    }
                }

        }

    }


    override fun onResume() {
        super.onResume()
        XinYePrintManager.init(this)
        XinYePrintManager.printConnectResult.observe(this, Observer {
            if (it == "connect") {
                Constant.PRINT_MODE = "0"
                Log.d(TAG, "onResume: 连接芯烨打印机")
                ToastUtils.toast(this, "连接芯烨打印机")
            } else {
                if (DeShiPrintManager.connectStatus == "false") {
                    Log.d(TAG, "onResume: 未连接上打印机")
                    ToastUtils.toast(this, "未连接上打印机")
                }
            }
        })

        DeShiPrintManager.init(this)
        DeShiPrintManager.printConnectResult.observe(this, Observer {
            if (it == "connect") {
                Constant.PRINT_MODE = "1"
                Log.d(TAG, "onResume: 连接得实打印机")
                ToastUtils.toast(this, "连接得实打印机")
            } else {
                if (XinYePrintManager.connectStatus == "false") {
                    Log.d(TAG, "onResume: 未连接上打印机")
                    ToastUtils.toast(this, "未连接上打印机")
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        XinYePrintManager.closeService()
        DeShiPrintManager.close()
    }

}