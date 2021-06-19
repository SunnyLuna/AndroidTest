package com.decard.dblibs.convert

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * 班级类型转换
 * @author ZJ
 * created at 2021/1/15 16:43
 */
class ClassConvert {
    /**
     * 字符串转结构体
     */
    @TypeConverter
    fun stringToObject(value: String): ArrayList<ClassBean> {
        val listType = object : TypeToken<ArrayList<ClassBean>>() {}.type
        return Gson().fromJson(value, listType)
    }

    /**
     * 实体类转字符串
     */
    @TypeConverter
    fun objectToString(list: ArrayList<ClassBean>): String {
        return Gson().toJson(list)
    }
}