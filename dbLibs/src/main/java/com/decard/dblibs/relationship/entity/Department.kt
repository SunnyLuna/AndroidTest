package com.decard.dblibs.relationship.entity

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.decard.dblibs.relationship.entity.Company

@Entity(
    foreignKeys = [ForeignKey(
        entity = Company::class,
        parentColumns = ["id"],
        childColumns = ["empId"],
        onDelete = CASCADE
    )], indices = [Index(value = ["empId"], unique = true)]
    , tableName = "DEPARTMENT"
)
data class Department(
    var dept: String,
    var empId: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}