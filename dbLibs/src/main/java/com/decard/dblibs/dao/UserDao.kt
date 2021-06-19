package com.decard.dblibs.dao

import androidx.room.*
import com.decard.dblibs.entity.UserEntity
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface UserDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: UserEntity): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: MutableList<UserEntity>)

    @Query("SELECT * FROM User")
    fun queryAll(): Observable<MutableList<UserEntity>>

    @Query("SELECT * FROM User WHERE address is not null")
    fun queryByAddress(): Observable<MutableList<UserEntity>>


    @Query("SELECT * FROM User WHERE name=:name")
    fun queryByName(name: String): Observable<MutableList<UserEntity>>


    @Delete
    fun delete(element: UserEntity): Completable

    @Delete
    fun deleteList(elements: MutableList<UserEntity>)

    @Delete
    fun deleteSome(vararg elements: UserEntity)

    @Update
    fun update(element: UserEntity): Completable

}