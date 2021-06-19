package com.example.commonlibs.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.example.commonlibs.BaseApplication
import kotlin.reflect.KProperty

/**
 * 通过委托模式实现本地数据存储
 * @author ZJ
 * created at 2021/6/18 17:13
 */
class PreferenceUtils<T>(val name: String, private val default: T) {

    private val mSharedPreferences: SharedPreferences by lazy {
        BaseApplication.instance.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return getSharePreferences(name, default)
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putSharePreferences(name, value)
    }

    @SuppressLint("CommitPrefEdits")
    private fun putSharePreferences(name: String, value: T) = with(mSharedPreferences.edit()) {
        when (value) {
            is Long -> putLong(name, value)
            is String -> putString(name, value)
            is Int -> putInt(name, value)
            is Boolean -> putBoolean(name, value)
            is Float -> putFloat(name, value)
            else -> throw IllegalArgumentException("This type of data cannot be saved!")
        }.apply()
    }

    private fun getSharePreferences(name: String, default: T): T = with(mSharedPreferences) {
        val res: Any? = when (default) {
            is Long -> getLong(name, default)
            is String -> getString(name, default)
            is Int -> getInt(name, default)
            is Boolean -> getBoolean(name, default)
            is Float -> getFloat(name, default)
            else -> throw IllegalArgumentException("This type of data cannot be saved!")
        }
        return res as T
    }
}