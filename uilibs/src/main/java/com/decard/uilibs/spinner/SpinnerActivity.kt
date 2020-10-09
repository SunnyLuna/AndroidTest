package com.decard.uilibs.spinner

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.decard.uilibs.R
import kotlinx.android.synthetic.main.activity_spinner.*

/**
 * 测试Spinner
 * @author ZJ
 * created at 2020/9/11 15:46
 */
class SpinnerActivity : AppCompatActivity() {
    private val TAG = "---SpinnerActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spinner)

        val dataset = ArrayList<String>(listOf("One", "Two", "Three", "Four", "Five"))
        nice_spinner.attachDataSource(dataset)
        sunny_spinner.attachDataSource(dataset)

//        val adapter: ArrayAdapter<*> = ArrayAdapter.createFromResource(
//            this,
//            R.array.grade,
//            android.R.layout.simple_spinner_item
//        )
////        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        nice_spinner.adapter = adapter
    }


}