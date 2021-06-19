package com.decard.dblibs.column

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "PEER")
data class PeerBean(
    // 使用ColumnInfo注解定义一个字段名, 不用注解默认取变量名
    @ColumnInfo(name = "peerName")
    var name: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}