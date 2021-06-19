package com.decard.dblibs.relationship.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.decard.dblibs.relationship.entity.Department
import com.decard.dblibs.relationship.InnerJoinResult
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface DepartmentDao {

    @Insert
    fun addDepartments(departments: MutableList<Department>): Completable

    @Query("SELECT * FROM DEPARTMENT")
    fun getAllDepartment(): Observable<MutableList<Department>>

    //使用内连接查询
    @Query("SELECT empId,name,dept from company inner join department on Company.id=DEPARTMENT.empId")
    fun getDepartmentFormCompany(): Observable<MutableList<InnerJoinResult>>
}