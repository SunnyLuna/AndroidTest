package com.decard.uilibs.password

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.decard.uilibs.R
import kotlinx.android.synthetic.main.activity_password.*

class PasswordActivity : AppCompatActivity() {

    private val TAG = "---PasswordActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)

        passView.setPasswordListener(object : PhysicalKeyboardPasswordView.PasswordListener {
            override fun keyEnterPress(password: String?, isComplete: Boolean) {
                Log.d(TAG, "keyEnterPress: $password  $isComplete")
            }

            override fun keyEscape() {
                Log.d(TAG, "keyEscape: ")
            }

            override fun passwordChange(changeText: String?) {
                Log.d(TAG, "passwordChange: ")

            }

            override fun passwordComplete(password: String?) {
                Log.d(TAG, "passwordComplete: ")
            }
        })

        key_password.setOnPasswordInputFinish {
            Log.d(TAG, "onCreate: $it")
        }
    }
}