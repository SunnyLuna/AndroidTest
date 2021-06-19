package com.decard.dblibs.convert

import androidx.room.TypeConverter
import java.util.*

/**
 * 日期类型转换器
 * @author ZJ
 * created at 2021/1/15 16:37
 */
class DateConvert {

    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }


    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }
}