package com.decard.dblibs.relationship.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "COMPANY")
data class Company(
    var name: String,
    var age: Int,
    var address: String,
    var salary: Double
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    override fun toString(): String {
        return "Company(id=$id, name='$name', age=$age, address='$address', salary=$salary)"
    }
}