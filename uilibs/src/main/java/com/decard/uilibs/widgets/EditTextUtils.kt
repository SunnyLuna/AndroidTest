package com.decard.uilibs.widgets

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

/**
 * 监听edittext
 * @author ZJ
 * created at 2020/7/14 15:53
 */
object EditTextUtils {

    fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }
}