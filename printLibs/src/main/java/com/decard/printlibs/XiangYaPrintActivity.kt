package com.decard.printlibs

import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.decard.printlibs.utils.UsbPrintUtils
import com.google.gson.Gson
import com.tbruyelle.rxpermissions2.RxPermissions
import com.tx.printlib.Const
import com.tx.printlib.UsbPrinter
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader

class XiangYaPrintActivity : AppCompatActivity() {
    private val TAG = "---XiangYaPrintActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_xiang_ya_print)
        Log.d(TAG, "onCreate: ${Build.MODEL}")
        RxPermissions(this).request(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ).subscribe {
            if (it) {
//                dealData()
                printEHealthCard()
            }
        }
    }


    fun printEHealthCard() {
        val mUsbPrinter = UsbPrinter(this)
        val dev = UsbPrintUtils.getCorrectDevice(this)
        if (dev == null || !mUsbPrinter.open(dev)) {
            Log.d(TAG, "printTakeNumber: 打印机打开失败")
            return
        }
        val stat1: Long = mUsbPrinter.status.toLong()
        //获取打印机状态
        val stat2: Long = mUsbPrinter.status2
        //            TODO TX_STAT_PAPEREND 缺纸   32
        Log.d(TAG, "状态stat1:$stat1   stat2:$stat2")
        mUsbPrinter.init()

        //TODO TX_FONT_SIZE:放大系数，0为原始大小，1为增大1倍，如此类推，最大为7；参数1为宽，参数2为高
        mUsbPrinter.doFunction(Const.TX_ALIGN, Const.TX_ALIGN_CENTER, 0)
//        mUsbPrinter.printImage(Environment.getExternalStorageDirectory().absolutePath + File.separator + "123.jpg")
        mUsbPrinter.doFunction(Const.TX_QR_DOTSIZE, Const.TX_SIZE_8X, 0)
        mUsbPrinter.printQRcode("123456789456454454554")
        mUsbPrinter.newline()
        mUsbPrinter.resetFont()
        mUsbPrinter.doFunction(Const.TX_ALIGN, Const.TX_ALIGN_CENTER, 0)
        mUsbPrinter.doFunction(Const.TX_CHINESE_MODE, Const.TX_ON, 0)
        mUsbPrinter.doFunction(Const.TX_LINE_SP, 40, 0)
        mUsbPrinter.outputStringLn("白皓月")
        mUsbPrinter.resetFont()
        mUsbPrinter.doFunction(Const.TX_ALIGN, Const.TX_ALIGN_CENTER, 0)
        mUsbPrinter.doFunction(Const.TX_CHINESE_MODE, Const.TX_ON, 0)
        mUsbPrinter.doFunction(Const.TX_LINE_SP, 40, 0)
        mUsbPrinter.outputStringLn("电子健康卡就医凭证")
        mUsbPrinter.resetFont()
        mUsbPrinter.doFunction(Const.TX_LINE_SP, 40, 0)
        mUsbPrinter.doFunction(Const.TX_CHINESE_MODE, Const.TX_ON, 0)
        mUsbPrinter.doFunction(Const.TX_FONT_BOLD, Const.TX_ON, 0)
        mUsbPrinter.doFunction(Const.TX_FONT_BOLD, Const.TX_ON, 0)
        mUsbPrinter.outputStringLn("-----------------------------")
        mUsbPrinter.resetFont()
        mUsbPrinter.doFunction(Const.TX_LINE_SP, 40, 0)
        mUsbPrinter.doFunction(Const.TX_CHINESE_MODE, Const.TX_ON, 0)
        mUsbPrinter.doFunction(Const.TX_ALIGN, Const.TX_ALIGN_CENTER, 0)
        mUsbPrinter.outputStringLn("请妥善保管好此凭证")
        mUsbPrinter.doFunction(Const.TX_LINE_SP, 40, 0)
        mUsbPrinter.outputStringLn("德卡科技智慧医院祝您健康!")

        mUsbPrinter.doFunction(Const.TX_UNIT_TYPE, Const.TX_UNIT_PIXEL, 0)
        mUsbPrinter.doFunction(Const.TX_FEED, 75, 0)
        mUsbPrinter.doFunction(Const.TX_CUT, Const.TX_CUT_FULL, 0)
        mUsbPrinter.close()

    }

    fun printTakeNumber() {
        val mUsbPrinter = UsbPrinter(this)
        val dev = UsbPrintUtils.getCorrectDevice(this)
        if (dev == null || !mUsbPrinter.open(dev)) {
            Log.d(TAG, "printTakeNumber: 打印机打开失败")
            return
        }
        val stat1: Long = mUsbPrinter.status.toLong()
        val stat2: Long = mUsbPrinter.status2
        //            TODO TX_STAT_PAPEREND 缺纸   32
        Log.d(TAG, "状态stat1:$stat1   stat2:$stat2")
        mUsbPrinter.init()

        //TODO TX_FONT_SIZE:放大系数，0为原始大小，1为增大1倍，如此类推，最大为7；参数1为宽，参数2为高
        mUsbPrinter.resetFont()
        mUsbPrinter.doFunction(Const.TX_FONT_BOLD, Const.TX_ON, 0)
        mUsbPrinter.doFunction(Const.TX_FONT_SIZE, Const.TX_SIZE_1X, Const.TX_SIZE_1X)
        mUsbPrinter.doFunction(Const.TX_ALIGN, Const.TX_ALIGN_CENTER, 0)
        mUsbPrinter.doFunction(Const.TX_CHINESE_MODE, Const.TX_ON, 0)
        mUsbPrinter.doFunction(Const.TX_LINE_SP, 40, 0)
        mUsbPrinter.outputStringLn("湘雅博爱康复医院")

        mUsbPrinter.resetFont()
        mUsbPrinter.doFunction(Const.TX_ALIGN, Const.TX_ALIGN_CENTER, 0)
        mUsbPrinter.doFunction(Const.TX_CHINESE_MODE, Const.TX_ON, 0)
        mUsbPrinter.doFunction(Const.TX_LINE_SP, 40, 0)
        mUsbPrinter.outputStringLn("挂号取号凭证")

        mUsbPrinter.resetFont()
        mUsbPrinter.doFunction(Const.TX_LINE_SP, 40, 0)
        mUsbPrinter.doFunction(Const.TX_CHINESE_MODE, Const.TX_ON, 0)
        mUsbPrinter.doFunction(Const.TX_FONT_BOLD, Const.TX_ON, 0)
        mUsbPrinter.doFunction(Const.TX_FONT_BOLD, Const.TX_ON, 0)
        mUsbPrinter.outputStringLn("-----------------------------")

        mUsbPrinter.resetFont()
        mUsbPrinter.doFunction(Const.TX_CHINESE_MODE, Const.TX_ON, 0)
        mUsbPrinter.doFunction(Const.TX_ALIGN, Const.TX_ALIGN_LEFT, 0)
        val mIdNumber = "610523199005236987"
        val startStrShow = mIdNumber.substring(0, 6)
        val endShow = mIdNumber.substring(mIdNumber.length - 4, mIdNumber.length)
        mUsbPrinter.outputStringLn("  就诊卡号:$startStrShow********$endShow")

        mUsbPrinter.resetFont()
        mUsbPrinter.doFunction(Const.TX_ALIGN, Const.TX_ALIGN_LEFT, 0)
        mUsbPrinter.outputStringLn("  姓名:" + "张静")

        mUsbPrinter.resetFont()
        mUsbPrinter.doFunction(Const.TX_CHINESE_MODE, Const.TX_ON, 0)
        mUsbPrinter.doFunction(Const.TX_FONT_BOLD, Const.TX_ON, 0)
        mUsbPrinter.doFunction(Const.TX_FONT_BOLD, Const.TX_ON, 0)
        mUsbPrinter.outputStringLn("-----------------------------")

        mUsbPrinter.resetFont()
        mUsbPrinter.doFunction(Const.TX_ALIGN, Const.TX_ALIGN_LEFT, 0)
        mUsbPrinter.outputStringLn("  就诊科室:" + "这个额可是")

        mUsbPrinter.resetFont()
        mUsbPrinter.doFunction(Const.TX_CHINESE_MODE, Const.TX_ON, 0)
        mUsbPrinter.doFunction(Const.TX_ALIGN, Const.TX_ALIGN_LEFT, 0)
        mUsbPrinter.outputStringLn("  医生姓名:" + "焰灵姬")

        mUsbPrinter.resetFont()
        mUsbPrinter.doFunction(Const.TX_CHINESE_MODE, Const.TX_ON, 0)
        mUsbPrinter.doFunction(Const.TX_ALIGN, Const.TX_ALIGN_LEFT, 0)
        mUsbPrinter.outputStringLn("  就诊时间:" + "2210-03-29 16:00:00")

        mUsbPrinter.resetFont()
        mUsbPrinter.doFunction(Const.TX_CHINESE_MODE, Const.TX_ON, 0)
        mUsbPrinter.doFunction(Const.TX_ALIGN, Const.TX_ALIGN_LEFT, 0)
        mUsbPrinter.outputStringLn("  挂号费用:" + 100 + "元")

//        mUsbPrinter.resetFont()
//        mUsbPrinter.doFunction(Const.TX_CHINESE_MODE, Const.TX_ON, 0)
//        mUsbPrinter.doFunction(Const.TX_ALIGN, Const.TX_ALIGN_LEFT, 0)
//        mUsbPrinter.outputStringLn("  支付方式:${takeNumberBean.payType}")

        mUsbPrinter.resetFont()
        mUsbPrinter.doFunction(Const.TX_CHINESE_MODE, Const.TX_ON, 0)
        mUsbPrinter.doFunction(Const.TX_FONT_BOLD, Const.TX_ON, 0)
        mUsbPrinter.doFunction(Const.TX_FONT_BOLD, Const.TX_ON, 0)
        mUsbPrinter.outputStringLn("-----------------------------")

        mUsbPrinter.resetFont()
        mUsbPrinter.doFunction(Const.TX_CHINESE_MODE, Const.TX_ON, 0)
        mUsbPrinter.doFunction(Const.TX_ALIGN, Const.TX_ALIGN_LEFT, 0)
        mUsbPrinter.outputStringLn("  交易时间:" + "2210-03-29 16:00:00")

        mUsbPrinter.resetFont()
        mUsbPrinter.doFunction(Const.TX_CHINESE_MODE, Const.TX_ON, 0)
        mUsbPrinter.doFunction(Const.TX_ALIGN, Const.TX_ALIGN_LEFT, 0)
        mUsbPrinter.outputStringLn("  HIS订单号:" + "6545645465465465465")

        mUsbPrinter.doFunction(Const.TX_LINE_SP, 40, 0)

        mUsbPrinter.resetFont()
        mUsbPrinter.doFunction(Const.TX_CHINESE_MODE, Const.TX_ON, 0)
        mUsbPrinter.doFunction(Const.TX_FONT_BOLD, Const.TX_ON, 0)
        mUsbPrinter.doFunction(Const.TX_FONT_BOLD, Const.TX_ON, 0)
        mUsbPrinter.outputStringLn("-----------------------------")

        mUsbPrinter.doFunction(Const.TX_LINE_SP, 40, 0)
        // TODO CODE-B和CODE_C结合打印,code-c(123,67),code-b(123,66)
        if (!TextUtils.isEmpty(mIdNumber)) {
            val cmd: ByteArray
            val tempB = ByteArray(9)
            for (i in 0..8) {
                val tempStr = mIdNumber.substring(i * 2, i * 2 + 2)
                if (tempStr.endsWith("X") || tempStr.endsWith("x")) {
                    tempB[i] = 0x00
                } else {
                    tempB[i] = tempStr.toInt().toByte()
                }
            }
            cmd = if (mIdNumber.endsWith("X") || mIdNumber.endsWith("x")) {
                val beforeChar =
                    mIdNumber.substring(mIdNumber.length - 2, mIdNumber.length - 1).toInt()
                byteArrayOf(
                    29,
                    107,
                    73,
                    14,
                    123,
                    67,
                    tempB[0],
                    tempB[1],
                    tempB[2],
                    tempB[3],
                    tempB[4],
                    tempB[5],
                    tempB[6],
                    tempB[7],
                    123,
                    66,
                    (48 + beforeChar).toByte(),
                    88
                )
            } else { //610323199401091114
                byteArrayOf(
                    29,
                    107,
                    73,
                    11,
                    123,
                    67,
                    tempB[0],
                    tempB[1],
                    tempB[2],
                    tempB[3],
                    tempB[4],
                    tempB[5],
                    tempB[6],
                    tempB[7],
                    tempB[8]
                )
            }
            mUsbPrinter.doFunction(Const.TX_ALIGN, Const.TX_ALIGN_CENTER, 0)
            mUsbPrinter.doFunction(Const.TX_BARCODE_HEIGHT, 90, 0)
            mUsbPrinter.doFunction(Const.TX_BARCODE_WIDTH, 2, 0)
            mUsbPrinter.write(cmd)
            mUsbPrinter.resetFont()
            mUsbPrinter.doFunction(Const.TX_LINE_SP, 40, 0)
            mUsbPrinter.doFunction(Const.TX_CHINESE_MODE, Const.TX_ON, 0)
            mUsbPrinter.doFunction(Const.TX_ALIGN, Const.TX_ALIGN_CENTER, 0)
            mUsbPrinter.outputStringLn("")
            mUsbPrinter.resetFont()
            mUsbPrinter.doFunction(Const.TX_LINE_SP, 20, 0)
            mUsbPrinter.doFunction(Const.TX_CHINESE_MODE, Const.TX_ON, 0)
            mUsbPrinter.doFunction(Const.TX_ALIGN, Const.TX_ALIGN_CENTER, 0)
            val startStr = mIdNumber.substring(0, 6)
            val endStr = mIdNumber.substring(mIdNumber.length - 4, mIdNumber.length)
            mUsbPrinter.outputStringLn("$startStr********$endStr")
        }

        mUsbPrinter.resetFont()
        mUsbPrinter.doFunction(Const.TX_LINE_SP, 40, 0)
        mUsbPrinter.doFunction(Const.TX_CHINESE_MODE, Const.TX_ON, 0)
        mUsbPrinter.doFunction(Const.TX_ALIGN, Const.TX_ALIGN_CENTER, 0)
        mUsbPrinter.outputStringLn("")

        mUsbPrinter.resetFont()
        mUsbPrinter.doFunction(Const.TX_LINE_SP, 40, 0)
        mUsbPrinter.doFunction(Const.TX_CHINESE_MODE, Const.TX_ON, 0)
        mUsbPrinter.doFunction(Const.TX_ALIGN, Const.TX_ALIGN_CENTER, 0)
        mUsbPrinter.outputStringLn("请妥善保管好此凭证")
        mUsbPrinter.doFunction(Const.TX_LINE_SP, 40, 0)
        mUsbPrinter.outputStringLn("德卡科技智慧医院祝您健康!")

        mUsbPrinter.doFunction(Const.TX_UNIT_TYPE, Const.TX_UNIT_PIXEL, 0)
        mUsbPrinter.doFunction(Const.TX_FEED, 75, 0)
        mUsbPrinter.doFunction(Const.TX_CUT, Const.TX_CUT_FULL, 0)
        mUsbPrinter.close()

    }


    private fun dealData() {
        val path = Environment.getExternalStorageDirectory().absolutePath + "/recharge.json"
        val file = File(path)
        if (file.exists()) {
            Log.d(TAG, "dealData: 文件存在")
            val inputStream = FileInputStream(file)
            val inputStreamReader = InputStreamReader(inputStream)
            val buffreader = BufferedReader(inputStreamReader)
            var line: String?
            var content: String? = "" // 文件内容字符串
            // 分行读取
            while (buffreader.readLine().also { line = it } != null) {
                Log.d(TAG, "dealData: $line")
                content += line
            }
            inputStream.close()
            val rechargeBean = RechargeBean(
                "111111111", "666666", "焰灵姬",
                "nv", "0.01", "1", "人脸支付", "Sunny",
                "2020-01-01", "88888888", "123456789"
            )
            val map = mutableMapOf<String, String>()
            map["cardNo"] = rechargeBean.cardNo
            map["idCardNo"] = rechargeBean.idCardNo
            map["patientName"] = rechargeBean.patientName
            map["sex"] = rechargeBean.sex
            map["amount"] = rechargeBean.amount
            map["balance"] = rechargeBean.balance
            map["payWay"] = rechargeBean.payWay
            map["optUserName"] = rechargeBean.optUserName
            map["time"] = rechargeBean.time
            map["hisOrderNo"] = rechargeBean.hisOrderNo
            map["snNo"] = rechargeBean.snNo

            val printBean = Gson().fromJson(content, PrintBean::class.java)

            Log.d(TAG, "dealData: " + printBean.toString())

        } else {
            Log.d(TAG, "dealData: 文件不存在")
        }
    }

    /**
     * 打印取号的信息
     * @author ZJ
     * created at 2021/1/5 16:39
     */
    fun printTakeNumber(mUsbPrinter: UsbPrinter, printBean: PrintBean, map: Map<String, String>) {
        val mUsbPrinter = UsbPrinter(this)
        val dev = UsbPrintUtils.getCorrectDevice(this)
        if (dev == null || !mUsbPrinter.open(dev)) {
            Toast.makeText(this, "打印机打开失败", Toast.LENGTH_LONG).show()
            return
        }
        val stat1: Long = mUsbPrinter.status.toLong()
        val stat2: Long = mUsbPrinter.status2
        //            TODO TX_STAT_PAPEREND 缺纸   32
        Log.d(TAG, "状态stat1:$stat1   stat2:$stat2")
        mUsbPrinter.init()
        for (print in printBean.datas) {
            when (print.type) {
                //普通文本，直接打印
                "common" -> {
                    mUsbPrinter.resetFont()
                    mUsbPrinter.outputStringLn(print.text)
                }
                //需要赋值
                "data" -> {
                    val data = print.key
                    mUsbPrinter.resetFont()
                    mUsbPrinter.outputStringLn(print.text + map[data])
                }
                //一维码
                "barcode" -> {

                }
                //二维码
                "qrcode" -> {

                }
            }

        }

//
//        //TODO TX_FONT_SIZE:放大系数，0为原始大小，1为增大1倍，如此类推，最大为7；参数1为宽，参数2为高
//        mUsbPrinter.resetFont()
//        mUsbPrinter.doFunction(Const.TX_FONT_BOLD, Const.TX_ON, 0)
//        mUsbPrinter.doFunction(Const.TX_FONT_SIZE, Const.TX_SIZE_1X, Const.TX_SIZE_1X)
//        mUsbPrinter.doFunction(Const.TX_ALIGN, Const.TX_ALIGN_CENTER, 0)
//        mUsbPrinter.doFunction(Const.TX_CHINESE_MODE, Const.TX_ON, 0)
//        mUsbPrinter.doFunction(Const.TX_LINE_SP, 40, 0)
//        mUsbPrinter.outputStringLn("湘雅博爱康复医院")
//
//        mUsbPrinter.resetFont()
//        mUsbPrinter.doFunction(Const.TX_ALIGN, Const.TX_ALIGN_CENTER, 0)
//        mUsbPrinter.doFunction(Const.TX_CHINESE_MODE, Const.TX_ON, 0)
//        mUsbPrinter.doFunction(Const.TX_LINE_SP, 40, 0)
//        mUsbPrinter.outputStringLn("挂号取号凭证")
//
//        mUsbPrinter.resetFont()
//        mUsbPrinter.doFunction(Const.TX_LINE_SP, 40, 0)
//        mUsbPrinter.doFunction(Const.TX_CHINESE_MODE, Const.TX_ON, 0)
//        mUsbPrinter.doFunction(Const.TX_FONT_BOLD, Const.TX_ON, 0)
//        mUsbPrinter.doFunction(Const.TX_FONT_BOLD, Const.TX_ON, 0)
//        mUsbPrinter.outputStringLn("-----------------------------")
//
//        mUsbPrinter.resetFont()
//        mUsbPrinter.doFunction(Const.TX_CHINESE_MODE, Const.TX_ON, 0)
//        mUsbPrinter.doFunction(Const.TX_ALIGN, Const.TX_ALIGN_LEFT, 0)
//
//        val startStrShow = rechargeBean.idCardNo.substring(0, 6)
//        val endShow = rechargeBean.idCardNo.substring(
//            rechargeBean.idCardNo.length - 4,
//            rechargeBean.idCardNo.length
//        )
//        mUsbPrinter.outputStringLn("  就诊卡号:$startStrShow********$endShow")
//
//        mUsbPrinter.resetFont()
//        mUsbPrinter.doFunction(Const.TX_ALIGN, Const.TX_ALIGN_LEFT, 0)
//        mUsbPrinter.outputStringLn("  姓名:" + rechargeBean.patientName)
//
//        mUsbPrinter.resetFont()
//        mUsbPrinter.doFunction(Const.TX_CHINESE_MODE, Const.TX_ON, 0)
//        mUsbPrinter.doFunction(Const.TX_FONT_BOLD, Const.TX_ON, 0)
//        mUsbPrinter.doFunction(Const.TX_FONT_BOLD, Const.TX_ON, 0)
//        mUsbPrinter.outputStringLn("-----------------------------")
//
//        mUsbPrinter.resetFont()
//        mUsbPrinter.doFunction(Const.TX_ALIGN, Const.TX_ALIGN_LEFT, 0)
//        mUsbPrinter.outputStringLn("  充值金额:" + rechargeBean.amount)
//
//        mUsbPrinter.resetFont()
//        mUsbPrinter.doFunction(Const.TX_CHINESE_MODE, Const.TX_ON, 0)
//        mUsbPrinter.doFunction(Const.TX_ALIGN, Const.TX_ALIGN_LEFT, 0)
//        mUsbPrinter.outputStringLn("  账户余额:" + rechargeBean.balance)
//
//        mUsbPrinter.resetFont()
//        mUsbPrinter.doFunction(Const.TX_CHINESE_MODE, Const.TX_ON, 0)
//        mUsbPrinter.doFunction(Const.TX_ALIGN, Const.TX_ALIGN_LEFT, 0)
//        mUsbPrinter.outputStringLn("  支付方式:" + rechargeBean.payWay)
//
//        mUsbPrinter.resetFont()
//        mUsbPrinter.doFunction(Const.TX_CHINESE_MODE, Const.TX_ON, 0)
//        mUsbPrinter.doFunction(Const.TX_ALIGN, Const.TX_ALIGN_LEFT, 0)
//        mUsbPrinter.outputStringLn("  挂号费用:" + rechargeBean.amount + "元")
//
////        mUsbPrinter.resetFont()
////        mUsbPrinter.doFunction(Const.TX_CHINESE_MODE, Const.TX_ON, 0)
////        mUsbPrinter.doFunction(Const.TX_ALIGN, Const.TX_ALIGN_LEFT, 0)
////        mUsbPrinter.outputStringLn("  支付方式:${takeNumberBean.payType}")
//
//        mUsbPrinter.resetFont()
//        mUsbPrinter.doFunction(Const.TX_CHINESE_MODE, Const.TX_ON, 0)
//        mUsbPrinter.doFunction(Const.TX_FONT_BOLD, Const.TX_ON, 0)
//        mUsbPrinter.doFunction(Const.TX_FONT_BOLD, Const.TX_ON, 0)
//        mUsbPrinter.outputStringLn("-----------------------------")
//
//        mUsbPrinter.resetFont()
//        mUsbPrinter.doFunction(Const.TX_CHINESE_MODE, Const.TX_ON, 0)
//        mUsbPrinter.doFunction(Const.TX_FONT_BOLD, Const.TX_ON, 0)
//        mUsbPrinter.doFunction(Const.TX_FONT_BOLD, Const.TX_ON, 0)
//        mUsbPrinter.outputStringLn("-----------------------------")
//
//        mUsbPrinter.doFunction(Const.TX_LINE_SP, 40, 0)
//        // TODO CODE-B和CODE_C结合打印,code-c(123,67),code-b(123,66)
//        if (!TextUtils.isEmpty(rechargeBean.idCardNo)) {
//            val cmd: ByteArray
//            val tempB = ByteArray(9)
//            for (i in 0..8) {
//                val tempStr = rechargeBean.idCardNo.substring(i * 2, i * 2 + 2)
//                if (tempStr.endsWith("X") || tempStr.endsWith("x")) {
//                    tempB[i] = 0x00
//                } else {
//                    tempB[i] = tempStr.toInt().toByte()
//                }
//            }
//            cmd = if (rechargeBean.idCardNo.endsWith("X") || rechargeBean.idCardNo.endsWith("x")) {
//                val beforeChar =
//                    rechargeBean.idCardNo.substring(
//                        rechargeBean.idCardNo.length - 2,
//                        rechargeBean.idCardNo.length - 1
//                    ).toInt()
//                byteArrayOf(
//                    29,
//                    107,
//                    73,
//                    14,
//                    123,
//                    67,
//                    tempB[0],
//                    tempB[1],
//                    tempB[2],
//                    tempB[3],
//                    tempB[4],
//                    tempB[5],
//                    tempB[6],
//                    tempB[7],
//                    123,
//                    66,
//                    (48 + beforeChar).toByte(),
//                    88
//                )
//            } else { //610323199401091114
//                byteArrayOf(
//                    29,
//                    107,
//                    73,
//                    11,
//                    123,
//                    67,
//                    tempB[0],
//                    tempB[1],
//                    tempB[2],
//                    tempB[3],
//                    tempB[4],
//                    tempB[5],
//                    tempB[6],
//                    tempB[7],
//                    tempB[8]
//                )
//            }
//            mUsbPrinter.doFunction(Const.TX_ALIGN, Const.TX_ALIGN_CENTER, 0)
//            mUsbPrinter.doFunction(Const.TX_BARCODE_HEIGHT, 90, 0)
//            mUsbPrinter.doFunction(Const.TX_BARCODE_WIDTH, 2, 0)
//            mUsbPrinter.write(cmd)
//            mUsbPrinter.resetFont()
//            mUsbPrinter.doFunction(Const.TX_LINE_SP, 40, 0)
//            mUsbPrinter.doFunction(Const.TX_CHINESE_MODE, Const.TX_ON, 0)
//            mUsbPrinter.doFunction(Const.TX_ALIGN, Const.TX_ALIGN_CENTER, 0)
//            mUsbPrinter.outputStringLn("")
//            mUsbPrinter.resetFont()
//            mUsbPrinter.doFunction(Const.TX_LINE_SP, 20, 0)
//            mUsbPrinter.doFunction(Const.TX_CHINESE_MODE, Const.TX_ON, 0)
//            mUsbPrinter.doFunction(Const.TX_ALIGN, Const.TX_ALIGN_CENTER, 0)
//            val startStr = rechargeBean.idCardNo.substring(0, 6)
//            val endStr = rechargeBean.idCardNo.substring(
//                rechargeBean.idCardNo.length - 4,
//                rechargeBean.idCardNo.length
//            )
//            mUsbPrinter.outputStringLn("$startStr********$endStr")
//        }
//
//        mUsbPrinter.resetFont()
//        mUsbPrinter.doFunction(Const.TX_LINE_SP, 40, 0)
//        mUsbPrinter.doFunction(Const.TX_CHINESE_MODE, Const.TX_ON, 0)
//        mUsbPrinter.doFunction(Const.TX_ALIGN, Const.TX_ALIGN_CENTER, 0)
//        mUsbPrinter.outputStringLn("")
//
//        mUsbPrinter.resetFont()
//        mUsbPrinter.doFunction(Const.TX_LINE_SP, 40, 0)
//        mUsbPrinter.doFunction(Const.TX_CHINESE_MODE, Const.TX_ON, 0)
//        mUsbPrinter.doFunction(Const.TX_ALIGN, Const.TX_ALIGN_CENTER, 0)
//        mUsbPrinter.outputStringLn("请妥善保管好此凭证")
//        mUsbPrinter.doFunction(Const.TX_LINE_SP, 40, 0)
//        mUsbPrinter.outputStringLn("湘雅博爱康复医院祝您健康!")
//
//        mUsbPrinter.doFunction(Const.TX_UNIT_TYPE, Const.TX_UNIT_PIXEL, 0)
//        mUsbPrinter.doFunction(Const.TX_FEED, 75, 0)
//        mUsbPrinter.doFunction(Const.TX_CUT, Const.TX_CUT_FULL, 0)
//        mUsbPrinter.close()

    }
}