package com.decard.dblibs.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
class UserEntity(
    var name: String,
    @PrimaryKey
    var idNumber: String,
    var sex: String,
    var address: String?

) {
    override fun toString(): String {
        return "UserEntity(name='$name', idNumber='$idNumber', sex='$sex', address='$address')"
    }
}