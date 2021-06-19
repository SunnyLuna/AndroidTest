package com.decard.dblibs.relationship.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.decard.dblibs.relationship.entity.Company
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface CompanyDao {

    @Query("SELECT * FROM COMPANY")
    fun getAllCompany(): Observable<MutableList<Company>>


    @Insert
    fun insertCompanyList(companies: MutableList<Company>): Completable


    @Query("DELETE FROM COMPANY")
    fun deleteAll(): Completable
}