package com.decard.androidtest.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.decard.androidtest.db.Trade

@Dao
interface TradeDao {

    @Query("SELECT * FROM trade")
    fun getAll(): LiveData<List<Trade>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(plants: List<Trade>)

    @Delete
    fun delete(trade: Trade)


}