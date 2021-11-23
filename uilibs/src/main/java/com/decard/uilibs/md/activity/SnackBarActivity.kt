package com.decard.uilibs.md.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.decard.uilibs.R
import com.example.commonlibs.utils.ToastUtils
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_snack_bar.*

/**
 * 为一个操作提供轻量级的、快速的反馈是使用Snackbar的最好时机。Snackbar显示在屏幕的底部，包含
  了文字信息与一个可选的操作按钮。它在指定时间结束之后自动消失。另外，配合CoordinatorLayout使用，
  还可以在超时之前将它滑动删除
 */
class SnackBarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_snack_bar)

        button2.setOnClickListener {
            Snackbar.make(con_snack, "标题", Snackbar.LENGTH_LONG)
                .setAction("点击") {
                    ToastUtils.toast(this, "吐司")
                }.setDuration(Snackbar.LENGTH_LONG).show()

        }
    }
}