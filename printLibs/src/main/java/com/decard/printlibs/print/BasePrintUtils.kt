package com.decard.printlibs.print

import android.content.Context

abstract class BasePrintUtils {

    //连接打印机
    abstract fun connectPrinter(context: Context)

    abstract fun printText()
}