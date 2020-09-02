package com.decard.androidtest.repository

import com.decard.androidtest.db.TradeDao

class TradeRepository private constructor(private val userDao: TradeDao) {

    fun getPlants() = userDao.getAll()


    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: TradeRepository? = null

        fun getInstance(plantDao: TradeDao) =
            instance
                ?: synchronized(this) {
                instance
                    ?: TradeRepository(plantDao)
                        .also { instance = it }
            }
    }
}