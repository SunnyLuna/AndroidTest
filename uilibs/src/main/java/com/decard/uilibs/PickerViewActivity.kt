package com.decard.uilibs

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import kotlinx.android.synthetic.main.activity_picker_view.*
import java.util.*

class PickerViewActivity : AppCompatActivity() {

    private val TAG = "---PickerViewActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picker_view)
        btn_select_time.setOnClickListener {
            val timePicker = TimePickerBuilder(this, object : OnTimeSelectListener {
                override fun onTimeSelect(date: Date?, v: View?) {
                    Log.d(TAG, "onTimeSelect: $date")
                }

            }).build()
            timePicker.show()
        }
    }
}