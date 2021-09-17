package com.decard.printlibs.print

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.usb.UsbDevice
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.dascom.print.PrintCommands.ESCPOS
import com.dascom.print.Transmission.Pipe
import com.dascom.print.Transmission.UsbPipe
import com.dascom.print.Utils.UsbUtils
import com.decard.printlibs.bean.DailyStatisticsBean
import com.decard.printlibs.bean.TradeEntity
import com.decard.printlibs.utils.DCUtils
import com.decard.printlibs.utils.DateUtils
import java.text.DecimalFormat

@SuppressLint("StaticFieldLeak")
object DeShiPrintManager {
    private val TAG = "---DeShiPrintManager"
    private var usbUtils: UsbUtils? = null
    private var usbDevice: UsbDevice? = null
    private var pipe: Pipe? = null
    private lateinit var mContext: Context
    var connectStatus = ""
    public val printConnectResult = MutableLiveData<String>()
    private val df = DecimalFormat("0.00")

    /**
     * 初始化打印机
     * 扫描设备---》连接设备
     */
    fun init(context: Context) {
        mContext = context
        connectStatus = ""
        usbUtils = UsbUtils.getInstance(context)
        if (usbUtils != null) {
            usbUtils!!.getConnectUsbDevice(UsbUtils.Permission { device: UsbDevice?, grant: Boolean ->
                if (grant) {
                    usbDevice = device
                    connectDevice()
                    Log.d(TAG, "scanDevice: 扫描到可用usb设备")
                } else {
                    printConnectResult.postValue("disConnect")
                    connectStatus = "failed"
                    if (device != null) {
                        Log.d(TAG, "scanDevice: 没有usb授权")
                    } else {
                        Log.d(TAG, "scanDevice: 没有扫描到Usb设备")
                    }
                }
            })
        }
    }


    /**
     * 连接设备
     */
    private fun connectDevice() {
        if (pipe != null) {
            pipe!!.close()
            pipe = null
        }
        if (usbDevice != null) {
            try {
                connectStatus = "success"
                printConnectResult.postValue("connect")
                pipe = UsbPipe(mContext, usbDevice)
                escPos = ESCPOS(pipe)
                Log.d(TAG, "connectDevice: usb设备连接成功")
            } catch (e: Exception) {
                e.printStackTrace()
                connectStatus = "failed"
                printConnectResult.postValue("disConnect")
                Log.d(TAG, "connectDevice: usb设备连接失败")
            }
        } else {
            connectStatus = "failed"
            printConnectResult.postValue("disConnect")
            Log.d(TAG, "connectDevice: 请先扫描usb设备并授予权限")
        }
    }

    var escPos: ESCPOS? = null
    fun printPayInfo(
        patientId: String,
        payMoney: String,
        idName: String,
        idNumber: String,
        payTime: String,
        phoneNumber: String
    ) {

        if (pipe!!.isConnected) {
            escPos!!.let {
                val justification = it.setJustification('1'.toByte())
                Log.d(TAG, "printPayInfo: 居中显示 $justification ")
                //设置字符的宽高的倍数.width - 范围[0,7]字符宽的倍数,0表示1倍,1表示2倍,以此类推.
                //height - 范围[0,7]字符高的倍数,0表示1倍,1表示2倍,以此类推.
                it.setCharacterSize(1, 1)
                //打印文本
                it.printText("解放军第九八八医院")
                //打印缓冲器内容并换行.
                it.printFeedLines(2)
                it.setCharacterSize(0, 0)
                it.printText("核酸检测缴费凭证")
                it.printLineFeed()
                val lineSpacing = it.setLineSpacing(100)
                Log.d(TAG, "printPayInfo: 设置行间距$lineSpacing")
                it.setJustification('0'.toByte())
                it.setLeftMargin(50)
                it.printText("--------------------------------------------")
                it.printLineFeed()
                it.printText("就诊卡号 : $patientId")
                it.printLineFeed()
                val startStrShow = idNumber.substring(0, 6)
                val endShow = idNumber.substring(idNumber.length - 4, idNumber.length)
                it.printText("身份证号 : $startStrShow********$endShow")
                it.printLineFeed()
                it.printText("患者姓名 ：$idName")
                it.printLineFeed()
                it.printText("手机号码 ：$phoneNumber")
                it.printLineFeed()
                it.printText("交易时间 ：$payTime")
                it.printLineFeed()
                it.printText("缴费金额 ：$payMoney")
                it.printLineFeed()
                it.printText("--------------------------------------------")
                it.setLineSpacing(50)
                it.printLineFeed()
                it.setLeftMargin(0)
                it.setJustification('1'.toByte())
                //height - 条码的高度,范围[1,255],默认值为50.
                //width - 条码中的竖条的宽度(此宽度非条码宽度),范围[2,6],默认值为3.
                //HRIPosition - 人工识别符的位置,范围['0','3'],'0':不打印;'1':在条码上面;'2':在条码下面;'3':在条码上面和下面,默认值为'0'.
                //HRIFont - 人工识别符的字体,范围['0','2'],'0':FontA;'1'FontB;'2':FontC,默认值为'0'.
                it.printBarCodeSetting(100, 4, '2'.toByte(), '0'.toByte())
                it.printCode128('B', patientId)
                it.printLineFeed()
                it.setLineSpacing(70)
                it.setCharacterSize(0, 0)
                it.printText("请妥善保管好此凭证")
                it.printLineFeed()
                it.printText("解放军第九八八医院祝您健康")
                it.printFeedLines(5)
                it.cut()
            }

        } else {
            Log.d(TAG, "printPayInfo: 请连接打印机")
        }
    }

    fun printPayInfo(tradeEntity: TradeEntity) {
        val format = df.format(tradeEntity.amount.toDouble() / 100)
        val payMode = if (tradeEntity.payChannel == "05") {
            "支付宝支付"
        } else {
            "微信支付"
        }
        if (pipe!!.isConnected) {
            escPos!!.let {
                val justification = it.setJustification('1'.toByte())
                Log.d(TAG, "printPayInfo: 居中显示 $justification ")
                //设置字符的宽高的倍数.width - 范围[0,7]字符宽的倍数,0表示1倍,1表示2倍,以此类推.
                //height - 范围[0,7]字符高的倍数,0表示1倍,1表示2倍,以此类推.
                it.setCharacterSize(1, 1)
                //打印文本
                it.printText("新乡市第二人民医院")
                //打印缓冲器内容并换行.
                it.printFeedLines(2)
                it.printText("检查费缴费凭据")
                it.printLineFeed()
                it.setCharacterSize(0, 0)
                val lineSpacing = it.setLineSpacing(80)
                Log.d(TAG, "printPayInfo: 设置行间距$lineSpacing")
                it.setJustification('0'.toByte())
                it.setLeftMargin(50)
                it.printText("--------------------------------------------")
                it.printLineFeed()
                it.printText("姓    名: ${tradeEntity.cardName}")
                it.printLineFeed()
                it.printText("身份证号: ${DCUtils.idToPass(tradeEntity.cardIdNumber)}")
                it.printLineFeed()
                it.printText("--------------------------------------------")
                it.printLineFeed()
                it.printText("缴费金额: $format 元")
                it.printLineFeed()
                it.printText("支付方式: $payMode")
                it.printLineFeed()
                it.printText("支付订单号: ${tradeEntity.rearServiceOrderNo}")
                it.printLineFeed()
                it.printText("交易时间: ${tradeEntity.transTime}")
                it.printLineFeed()
                it.printText("--------------------------------------------")
                it.printLineFeed()
                it.printText("终端 SN: ${tradeEntity.sn}")
                it.printLineFeed()
                it.printText("凭据序号：0001")
                it.printLineFeed()
                it.printText("--------------------------------------------")
                it.printLineFeed()
                it.setJustification('1'.toByte())
                it.setLeftMargin(0)
                it.printText("此缴费凭据不作为报销凭证")
                it.printFeedLines(5)
                it.cut()
            }

        } else {
            Log.d(TAG, "printPayInfo: 请连接打印机")
        }
    }

    /**
     * 打印日缴费信息
     */
    fun printDailyStatistics(dailyStatisticsBean: DailyStatisticsBean) {

        if (pipe!!.isConnected) {
            escPos!!.let {
                val justification = it.setJustification('1'.toByte())
                val lineSpacing = it.setLineSpacing(80)
                Log.d(TAG, "printPayInfo: 居中显示 $justification ")
                //设置字符的宽高的倍数.width - 范围[0,7]字符宽的倍数,0表示1倍,1表示2倍,以此类推.
                //height - 范围[0,7]字符高的倍数,0表示1倍,1表示2倍,以此类推.
                it.setCharacterSize(1, 1)
                //打印文本
                it.printText("新乡市第二人民医院")
                //打印缓冲器内容并换行.
                it.printFeedLines(2)
                it.printText("体检缴费日报表")
                it.printFeedLines(2)
                it.setCharacterSize(0, 0)
                Log.d(TAG, "printPayInfo: 设置行间距$lineSpacing")
                it.setJustification('0'.toByte())
                it.setLeftMargin(50)
                it.printText("统计时间: ${dailyStatisticsBean.statisticsTime}")
                it.printLineFeed()
                it.printText("终端  SN: 454545454545454}")
                it.printLineFeed()
                it.printText("打印时间: ${DateUtils.getCurrentTime()}")
                it.printFeedLines(2)
                it.printText("合计:")
                it.printLineFeed()
                it.printText("总笔数: ${dailyStatisticsBean.totalCount}")
                it.printLineFeed()
                it.printText("总金额: ${dailyStatisticsBean.totalMoney}")
                it.printLineFeed()
                it.printText("--------------------------------------------")
                it.printLineFeed()
                it.printText("微信支付:")
                it.printLineFeed()
                it.printText("笔数: ${dailyStatisticsBean.wxCount}")
                it.printLineFeed()
                it.printText("金额: ${dailyStatisticsBean.wxMoney}")
                it.printLineFeed()
                it.printText("--------------------------------------------")
                it.printLineFeed()
                it.printText("支付宝:")
                it.printLineFeed()
                it.printText("笔数: ${dailyStatisticsBean.aliCount}")
                it.printLineFeed()
                it.printText("金额: ${dailyStatisticsBean.aliMoney}")
                it.printLineFeed()
                it.printText("--------------------------------------------")
                it.printLineFeed()
                it.setJustification('1'.toByte())
                it.setLeftMargin(0)
                it.printText("此缴费凭据不作为报销凭证")
                it.printFeedLines(5)
                it.cut()
            }

        } else {
            Log.d(TAG, "printPayInfo: 请连接打印机")
        }
    }


    fun close() {
        if (pipe != null)
            pipe!!.close()
    }

}