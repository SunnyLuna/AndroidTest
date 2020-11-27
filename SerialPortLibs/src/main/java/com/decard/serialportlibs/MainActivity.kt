package com.decard.serialportlibs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.decard.serialportlibs.serialport.SerialPortManager

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SerialPortManager.instance().openZ90N()
        SerialPortManager.instance().sendCommand("")
    }
}