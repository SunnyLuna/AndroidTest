package com.decard.dblibs

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.decard.dblibs.entity.UserEntity
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : AppCompatActivity(), View.OnClickListener {
    private val TAG = "---TestActivity"
    private var mUser: UserEntity? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        RxPermissions(this).request(android.Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe {
            Log.d(TAG, "申请权限: $it")
        }
        mUser = UserEntity("焰灵姬", "610", "女", "韩国")
        btn_add.setOnClickListener(this)
        btn_query.setOnClickListener(this)
        btn_update.setOnClickListener(this)
        btn_delete.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view) {
            btn_add -> {

                AppDataBase.getInstance(this).getUserDao().insert(mUser!!)
                    .subscribeOn(Schedulers.io()).subscribe()
            }
            btn_query -> {
                AppDataBase.getInstance(this).getUserDao().queryAll()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe {
                        Log.d(TAG, "query: " + it.size)
                        for (user in it) {
                            Log.d(TAG, "query: $user")
                        }
                    }

            }
            btn_update -> {
                val user = mUser
                user!!.name = "111111"
                Log.d(TAG, "update: $mUser")
                AppDataBase.getInstance(this).getUserDao().update(user)
                    .subscribeOn(Schedulers.io()).subscribe({
                        Log.d(TAG, "update: 修改成功")
                    }, {
                        Log.d(TAG, "update: " + it.message)
                    })
            }
            btn_delete -> {
                Log.d(TAG, "delete: $mUser")
                AppDataBase.getInstance(this).getUserDao().delete(mUser!!)
                    .subscribeOn(Schedulers.io()).subscribe()
            }
        }
    }
}