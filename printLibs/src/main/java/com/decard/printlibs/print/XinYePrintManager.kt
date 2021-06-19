package com.decard.printlibs.print

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.decard.printlibs.ZXingUtils
import com.decard.printlibs.utils.HexUtils
import net.posprinter.posprinterface.IMyBinder
import net.posprinter.posprinterface.UiExecute
import net.posprinter.service.PosprinterService
import net.posprinter.utils.BitmapToByteData
import net.posprinter.utils.BitmapToByteData.BmpType
import net.posprinter.utils.DataForSendToPrinterPos80
import net.posprinter.utils.DataForSendToPrinterTSC
import net.posprinter.utils.PosPrinterDev
import java.io.UnsupportedEncodingException

/**
 * 打印模块管理类
 * @author ZJ
 * created at 2020/6/9 14:45
 * 第一个设置字体,0,1,48,49；0,48标准字体；1,49压缩字体
第二个设置对齐方式,参数n:0-2或48-50；分别表示：左对齐,居中,右对齐 -
第三个设置左边距,0-255
第四个设置右边距,
第五个设置行间距：0-255 
（注：目前暂不支持右边距的设置,单参数必须传进去）
 */
@SuppressLint("StaticFieldLeak")
object XinYePrintManager {
    private val TAG = "---PrintManager"

    var connectStatus = ""
    var printTextStatus = ""
    var printBarCodeStatus = ""
    var printPaperStatus = ""
    public val printConnectResult = MutableLiveData<String>()

    private lateinit var mIntent: Intent
    private var binder: IMyBinder? = null
    private var usbPath: String = ""

    private var mContext: Context? = null


    fun init(context: Context) {
        mContext = context
        connectStatus = ""
        setupService()
    }

    private fun setupService() {
        mIntent = Intent(mContext, PosprinterService::class.java)
        mContext!!.bindService(mIntent, conn, Context.BIND_AUTO_CREATE)
    }

    private val conn: ServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName) {
            Log.d(TAG, "onServiceDisconnected: ")
            printConnectResult.postValue("disConnect")
            connectStatus = "failed"
        }

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            Log.d(TAG, "onServiceConnected: ")
            binder = service as IMyBinder
            connectPrint()
        }
    }

    /**
     * 连接打印模块
     */
    fun connectPrint() {
        Log.d(TAG, "connectPrint: 开始连接")

        val usbList = PosPrinterDev.GetUsbPathNames(mContext)
        if (usbList != null && usbList.isNotEmpty()) {
            usbPath = usbList[0]
            for (i in usbList) {
                Log.d(TAG, "usb: $i")
            }
            val posdev =
                PosPrinterDev(PosPrinterDev.PortType.USB, mContext, usbList[0])
            posdev.Open()
        } else {
            connectStatus = "failed"
            printConnectResult.postValue("disConnect")
            return
        }

        binder!!.connectUsbPort(mContext, usbPath, object : UiExecute {
            override fun onfailed() {
                Log.d(TAG, "connectPrint   onfailed: ")
                connectStatus = "failed"
                printConnectResult.postValue("disConnect")
            }

            override fun onsucess() {
                Log.d(TAG, "connectPrint   onsucess: ")
                connectStatus = "success"
                printConnectResult.postValue("connect")
            }
        })
    }

    private var mFontSize = 0  //字体：
    private var mAlignment = 0  // 对齐  0-2
    private var mLeftMargin = 0  //左边距
    private var mRightMargin = 0  //右边距
    private var mRowPitch = 0   //   行间隔


    fun printPayInfo(
        patientId: String,
        payMoney: String,
        idName: String,
        idNumber: String,
        payTime: String,
        phoneNumber: String
    ) {

        printTextStatus = ""
        Log.d(TAG, "printSomething: ")
        binder!!.writeDataByYouself(object : UiExecute {
            override fun onfailed() {
                Log.d(TAG, "printSomething  onfailed: ")
                printTextStatus = "failed"
            }

            override fun onsucess() {
                printTextStatus = "success"
                Log.d(TAG, "printSomething  onsucess: ")
            }
        }) {
            val list: MutableList<ByteArray> =
                ArrayList()
            //初始化打印机
            list.add(DataForSendToPrinterPos80.initializePrinter())
            //设置字体大小  0，1，48，49；0，48标准字体；1，49压缩字体
            list.add(DataForSendToPrinterPos80.selectOrCancelChineseCharDoubleWH(1))
            //选择对齐方式  参数n:0-2或48-50；分别表示：左对齐，居中，右对齐 -
            list.add(DataForSendToPrinterPos80.selectAlignment(1))
            //设置左边距
            list.add(DataForSendToPrinterPos80.setLeftSpace(0, 0))
            list.add(strToBytes("解放军第九八八医院")!!)
            list.add(DataForSendToPrinterPos80.selectOrCancelChineseCharDoubleWH(0))
            list.add(DataForSendToPrinterPos80.printAndFeedForward(2))
            list.add(strToBytes("核酸检测缴费凭证")!!)
            list.add(DataForSendToPrinterPos80.printAndFeedLine())
            list.add(DataForSendToPrinterPos80.selectAlignment(0))
            list.add(DataForSendToPrinterPos80.setLeftSpace(40, 0))
            list.add(strToBytes("--------------------------------------------")!!)
            list.add(DataForSendToPrinterPos80.printAndFeedForward(2))
            list.add(DataForSendToPrinterPos80.setLineSpaceing(20))
            list.add(strToBytes("就诊卡号 : $patientId")!!)
            list.add(DataForSendToPrinterPos80.printAndFeedForward(5))
            val startStrShow = idNumber.substring(0, 6)
            val endShow = idNumber.substring(idNumber.length - 4, idNumber.length)
            list.add(strToBytes("身份证号 : $startStrShow********$endShow")!!)
            list.add(DataForSendToPrinterPos80.printAndFeedForward(5))
            list.add(strToBytes("患者姓名 ：$idName")!!)
            list.add(DataForSendToPrinterPos80.printAndFeedForward(5))
            list.add(strToBytes("手机号码 ：$phoneNumber")!!)
            list.add(DataForSendToPrinterPos80.printAndFeedForward(5))
            list.add(strToBytes("交易时间 ：$payTime")!!)
            list.add(DataForSendToPrinterPos80.printAndFeedForward(5))
            list.add(strToBytes("缴费金额 ：$payMoney")!!)
            list.add(DataForSendToPrinterPos80.printAndFeedForward(5))
            list.add(strToBytes("--------------------------------------------")!!)
            list.add(DataForSendToPrinterPos80.printAndFeedLine())
            list.add(DataForSendToPrinterPos80.selectAlignment(1))
            list.add(DataForSendToPrinterPos80.setLeftSpace(0, 0))
            list.add(DataForSendToPrinterPos80.setBarcodeWidth(2))
            //选择条码高度  参数n：1-255；默认162
            list.add(DataForSendToPrinterPos80.setBarcodeHeight(130))
            //选择对齐方式  参数n:0-2或48-50；分别表示：左对齐，居中，右对齐 -
            list.add(DataForSendToPrinterPos80.selectAlignment(1))
            //选择HRI字符打印位置; HRI是对条码内容注释的字符;  参数n:范围0-3或者48-51；代表字符在条码的打印位置 -
            list.add(DataForSendToPrinterPos80.selectHRICharacterPrintPosition(2))
            //打印条码      m - 条码类型：65-73，当m=73时，打印条码的内容必须加上字符集选择，如：{A，{B，{C等；
            //              n - 指示条码数据的个数,字符串长度不能超过n规定的范围
            //         content - 条码内容字符串，字符串的长度和字符范围，参考打印条码指令
            list.add(
                DataForSendToPrinterPos80.printBarcode(
                    73, patientId.length + 2, "{B$patientId"
                )
            )
            list.add(DataForSendToPrinterPos80.printAndFeedLine())
            list.add(strToBytes("请妥善保管好此凭证")!!)
            list.add(DataForSendToPrinterPos80.printAndFeedForward(4))
            list.add(strToBytes("解放军第九八八医院祝您健康")!!)
            //打印并换行
            list.add(DataForSendToPrinterPos80.printAndFeedForward(12))
            list
        }
    }

    /**
     * 打印pos文本
     */
    fun printPosText(printMsg: String) {

        printTextStatus = ""
        Log.d(TAG, "printSomething: ")
        binder!!.writeDataByYouself(object : UiExecute {
            override fun onfailed() {
                Log.d(TAG, "printSomething  onfailed: ")
                printTextStatus = "failed"
            }

            override fun onsucess() {
                printTextStatus = "success"
                Log.d(TAG, "printSomething  onsucess: ")
            }
        }) {
            val list: MutableList<ByteArray> =
                ArrayList()
            //初始化打印机
            list.add(DataForSendToPrinterPos80.initializePrinter())
            //设置字体大小  0，1，48，49；0，48标准字体；1，49压缩字体
            list.add(DataForSendToPrinterPos80.selectOrCancelChineseCharDoubleWH(mFontSize))
            //选择对齐方式  参数n:0-2或48-50；分别表示：左对齐，居中，右对齐 -
            list.add(DataForSendToPrinterPos80.selectAlignment(mAlignment))
            //设置左边距
            list.add(DataForSendToPrinterPos80.setLeftSpace(mLeftMargin, 0))
            //设置行间距
            list.add(DataForSendToPrinterPos80.setLineSpaceing(mRowPitch))
            list.add(strToBytes(printMsg)!!)
            list.add(DataForSendToPrinterPos80.horizontalPositioning())
            //打印并换行
            list.add(DataForSendToPrinterPos80.printAndFeedLine())
            //打印并换行
            list.add(DataForSendToPrinterPos80.printAndFeedLine())
            list
        }
    }

    /**
     * 打印文本
     */
    fun printText() {

        printTextStatus = ""
        Log.d(TAG, "printSomething: ")
        binder!!.writeDataByYouself(object : UiExecute {
            override fun onfailed() {
                Log.d(TAG, "printSomething  onfailed: ")
                printTextStatus = "failed"
            }

            override fun onsucess() {
                printTextStatus = "success"
                Log.d(TAG, "printSomething  onsucess: ")
            }
        }) {
            //初始化一个list
            val list: MutableList<ByteArray> =
                ArrayList()

            //在打印请可以先设置打印内容的字符编码类型，默认为gbk，请选择打印机可识别的类型，参看编程手册，打印代码页
            DataForSendToPrinterTSC.setCharsetName("gbk") //不设置，默认为gbk
            //通过工具类得到一个指令的byte[]数据,以文本为例
            //首先得设置size标签尺寸,宽60mm,高30mm,也可以调用以dot或inch为单位的方法具体换算参考编程手册
            val data0 = DataForSendToPrinterTSC
                .sizeBymm(60.0, 30.0)
            list.add(data0)
            //设置Gap,同上
            list.add(DataForSendToPrinterTSC.gapBymm(0.0, 0.0))
            //清除缓存
            list.add(DataForSendToPrinterTSC.cls())
            //条码指令，参数：int x，x方向打印起始点；int y，y方向打印起始点；
            //string font，字体类型；int rotation，旋转角度；
            //int x_multiplication，字体x方向放大倍数
            //int y_multiplication,y方向放大倍数
            //string content，打印内容
            val data1 = DataForSendToPrinterTSC
                .text(
                    10, 10, "0", 0, 1, 1,
                    "abc123"
                )
            list.add(data1)
            //打印直线,int x;int y;int width,线的宽度，int height,线的高度
            list.add(
                DataForSendToPrinterTSC.bar(
                    20,
                    40, 400, 3
                )
            )
            //打印条码
            list.add(
                DataForSendToPrinterTSC.barCode(
                    60, 50, "128", 100, 1, 0, 2, 2,
                    "abcdef12345"
                )
            )
            //打印
            list.add(DataForSendToPrinterTSC.print(1))
            list
        }
    }


    /**.
     * 打印条形码，一维码
     */
    fun printPosOneDimensional(msg: String) {
        printBarCodeStatus = ""

        binder!!.writeDataByYouself(object : UiExecute {
            override fun onfailed() {
                printBarCodeStatus = "failed"
                Log.d(TAG, "printOnedimensional  onfailed: ")
            }

            override fun onsucess() {
                printBarCodeStatus = "success"
                Log.d(TAG, "printOnedimensional  onsucess: ")
            }
        }) {
            val list: MutableList<ByteArray> =
                ArrayList()
            list.add(DataForSendToPrinterPos80.initializePrinter())
            //设置条码宽度  参数n：2-6；默认3； -
            list.add(DataForSendToPrinterPos80.setBarcodeWidth(3))
            //选择条码高度  参数n：1-255；默认162
            list.add(DataForSendToPrinterPos80.setBarcodeHeight(130))
            //选择对齐方式  参数n:0-2或48-50；分别表示：左对齐，居中，右对齐 -
            list.add(DataForSendToPrinterPos80.selectAlignment(1))
            //选择HRI字符打印位置; HRI是对条码内容注释的字符;  参数n:范围0-3或者48-51；代表字符在条码的打印位置 -
            list.add(DataForSendToPrinterPos80.selectHRICharacterPrintPosition(2))
            //打印条码      m - 条码类型：65-73，当m=73时，打印条码的内容必须加上字符集选择，如：{A，{B，{C等；
            //              n - 指示条码数据的个数,字符串长度不能超过n规定的范围
            //         content - 条码内容字符串，字符串的长度和字符范围，参考打印条码指令
            list.add(
                DataForSendToPrinterPos80.printBarcode(
                    73, msg.length + 2, "{B$msg"
                )
            )
            //打印并换行
            list.add(DataForSendToPrinterPos80.printAndFeedLine())
            list
        }
    }

    /**.
     * 打印条形码，一维码
     */
    fun printOneDimensional(msg: String, type: Int) {
        printBarCodeStatus = ""

        binder!!.writeDataByYouself(object : UiExecute {
            override fun onfailed() {
                printBarCodeStatus = "failed"
                Log.d(TAG, "printOnedimensional  onfailed: ")
            }

            override fun onsucess() {
                printBarCodeStatus = "success"
                Log.d(TAG, "printOnedimensional  onsucess: ")
            }
        }) {
            val list: MutableList<ByteArray> =
                ArrayList()
            //通过工具类得到一个指令的byte[]数据,以文本为例
            //首先得设置size标签尺寸,宽60mm,高30mm,也可以调用以dot或inch为单位的方法具体换算参考编程手册
            val data0 = DataForSendToPrinterTSC.sizeBymm(60.0, 30.0)
            list.add(data0)
            //设置Gap,同上
            list.add(DataForSendToPrinterTSC.gapBymm(0.0, 0.0))
            //清除缓存
            list.add(DataForSendToPrinterTSC.cls())
            //打印条码
            list.add(
                DataForSendToPrinterTSC.barCode(
                    60, 50,
                    "128", 100, 1, 0, 2, 2, msg
                )
            )
            //打印
            list.add(DataForSendToPrinterTSC.print(1))
            list
        }
    }

    /**
     * 打印二维码
     */
    fun printPosQRCode() {
        binder!!.writeDataByYouself(object : UiExecute {
            override fun onfailed() {
                Log.d(TAG, "printSomething  onfailed: ")
            }

            override fun onsucess() {
                Log.d(TAG, "printSomething  onsucess: ")
            }

        }) {
            val list: MutableList<ByteArray> =
                ArrayList()
            list.add(DataForSendToPrinterPos80.initializePrinter())
            //选择对齐方式
            //选择对齐方式
            list.add(DataForSendToPrinterPos80.selectAlignment(1))
            list.add(DataForSendToPrinterPos80.printQRcode(3, 48, "www.xprinter.net"))
            list.add(DataForSendToPrinterPos80.printAndFeedLine())
            //打印并换行
            list.add(DataForSendToPrinterPos80.printAndFeedLine())
            list
        }
    }

    /**
     * 打印二维码
     */
    fun printQRCode(msg: String) {
        val generateBitmap = ZXingUtils.generateBitmap(msg, 200, 200)
        binder!!.writeDataByYouself(object : UiExecute {
            override fun onfailed() {
                Log.d(TAG, "printSomething  onfailed: ")
            }

            override fun onsucess() {
                Log.d(TAG, "printSomething  onsucess: ")
            }

        }) {
            val list: MutableList<ByteArray> =
                ArrayList()

            list.add(
                DataForSendToPrinterTSC.bitmap(
                    10, 10, 0,
                    generateBitmap, BmpType.Threshold
                )
            )
            list.add(DataForSendToPrinterTSC.print(1))
            list
        }
    }

    /**
     * 打印图片
     */
    fun printBitmap(printMsg: String) {
        binder!!.writeDataByYouself(object : UiExecute {
            override fun onfailed() {
                Log.d(TAG, "printSomething  onfailed: ")
            }

            override fun onsucess() {
                Log.d(TAG, "printSomething  onsucess: ")
            }

        }) {
            val list: MutableList<ByteArray> =
                ArrayList()
            list.add(DataForSendToPrinterPos80.initializePrinter())
            //打印光栅位图
            //m - 打印模式0-3或48-51；正常打印m=0；
            //bitmap你希望打印光栅位图的图片的bitmap对象，该模式的位图打印规则请参考编程手册 -
            //bmpType你希望的位图处理成单色图的方式 -
            val bitmap = convertStringToIcon(printMsg)
            list.add(
                DataForSendToPrinterPos80.printRasterBmp(
                    0,
                    bitmap,
                    BitmapToByteData.BmpType.Threshold
                )
            )
            //打印并换行
            list.add(DataForSendToPrinterPos80.printAndFeedLine())
            list
        }
    }

    private fun convertStringToIcon(bmpStr: String?): Bitmap? {
        // OutputStream out;
        var bitmap: Bitmap? = null
        return try {
            val bitmapArray: ByteArray = HexUtils.hexStringToBytes(bmpStr)
            bitmap = BitmapFactory.decodeByteArray(
                bitmapArray, 0,
                bitmapArray.size
            )
            bitmap
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 走纸
     */
    fun goPaper(printMsg: Int) {
        printPaperStatus = ""
        binder!!.writeDataByYouself(object : UiExecute {
            override fun onfailed() {
                Log.d(TAG, "onfailed: ")
                printPaperStatus = "failed"
            }

            override fun onsucess() {
                Log.d(TAG, "onsucess: ")
                printPaperStatus = "success"
            }
        }) {
            val list: MutableList<ByteArray> =
                ArrayList()
            list.add(DataForSendToPrinterPos80.printAndFeedForward(printMsg))//打印并向前走纸n行
            list
        }
    }


    fun closeService() {
        if (binder != null && connectStatus != "") {
            binder!!.disconnectCurrentPort(object : UiExecute {
                override fun onfailed() {
                    Log.d(TAG, "disconnectCurrentPort    onfailed: ")
                }

                override fun onsucess() {
                    Log.d(TAG, "disconnectCurrentPort    onsucess: ")
                }
            })
        }
        Log.d(TAG, "closeService: ")
        mContext!!.unbindService(conn)

    }

    private fun strToBytes(str: String): ByteArray? {
        var b: ByteArray? = null
        var data: ByteArray? = null
        try {
            b = str.toByteArray(charset("utf-8"))
            data = String(b, charset("utf-8")).toByteArray(charset("gbk"))
        } catch (e: UnsupportedEncodingException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        return data
    }

}