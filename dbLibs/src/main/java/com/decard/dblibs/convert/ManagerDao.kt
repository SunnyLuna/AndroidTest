package com.decard.dblibs.convert

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface ManagerDao {

    @Insert
    fun addManager(manager: Manager): Completable

    @Query("SELECT * FROM MANAGER")
    fun getManagers(): Observable<MutableList<Manager>>
}