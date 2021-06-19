package com.decard.dblibs.convert

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.*


@Entity(tableName = "MANAGER")
@TypeConverters(ClassConvert::class, DateConvert::class)
data class Manager(
    var classBeans: ArrayList<ClassBean>,
    var date: Date,
    @Embedded //表示要分解成表中子字段的对象
    var schoolBean: SchoolBean

) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
    override fun toString(): String {
        return "Manager(classBeans=$classBeans, date=$date)"
    }


}