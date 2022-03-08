package com.example.commonlibs.utils

import android.annotation.SuppressLint
import com.example.commonlibs.BaseApplication
import com.tencent.mmkv.MMKV
import kotlin.reflect.KProperty

class MMKVUtils<T>(val name: String, private val default: T) {

    private val mmkv: MMKV by lazy {
        MMKV.initialize(BaseApplication.instance)
        MMKV.defaultMMKV()
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return getSharePreferences(name, default)
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putSharePreferences(name, value)
    }

    @SuppressLint("CommitPrefEdits")
    private fun putSharePreferences(name: String, value: T) {
        when (value) {
            is Long -> mmkv.encode(name, value)
            is String -> mmkv.encode(name, value)
            is Int -> mmkv.encode(name, value)
            is Boolean -> mmkv.encode(name, value)
            is Float -> mmkv.encode(name, value)
            else -> throw IllegalArgumentException("This type of data cannot be saved!")
        }
    }

    private fun getSharePreferences(name: String, default: T): T = with(mmkv) {
        val res: Any? = when (default) {
            is Long -> mmkv.decodeLong(name, default)
            is String -> mmkv.decodeString(name, default)
            is Int -> mmkv.decodeInt(name, default)
            is Boolean -> mmkv.decodeBool(name, default)
            is Float -> mmkv.decodeFloat(name, default)
            else -> throw IllegalArgumentException("This type of data cannot be saved!")
        }
        return res as T
    }
}