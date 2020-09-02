package com.decard.androidtest.db

import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.decard.androidtest.net.SeedDatabaseWorker
import java.io.File

@Database(entities = [Trade::class], version = 1, exportSchema = false)
abstract class TradeDatabase : RoomDatabase() {
    abstract fun userDao(): TradeDao

    companion object {
        private val TAG = "---TradeDatabase"

        private val dbPath =
            Environment.getExternalStorageDirectory().absolutePath + "/zj_test/"
        private val dbName = "UserDatabase"

        // For Singleton instantiation
        @Volatile
        private var instance: TradeDatabase? = null

        fun getInstance(context: Context): TradeDatabase {
            Log.d(TAG, "TradeDatabase getInstance: ")
            val file = File(dbPath)
            if (!File(dbPath).exists()) {
                file.mkdirs()
            }

            return instance ?: synchronized(this) {
                Log.d(TAG, "getInstance: ")
                instance
                    ?: buildDatabase(
                        context
                    ).also {
                    instance = it
                }
            }
        }

        private fun buildDatabase(context: Context): TradeDatabase {
            Log.d(TAG, "buildDatabase: ")
            return Room.databaseBuilder(context, TradeDatabase::class.java, dbPath + dbName)
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        Log.d(TAG, "onCreate: 开启WorkManager")
                        val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>().build()
//                        val request = PeriodicWorkRequestBuilder<SeedDatabaseWorker>(
//                            1,
//                            TimeUnit.MINUTES
//                        ).build()
                        WorkManager.getInstance(context).enqueue(request)
                    }
                })
                .build()
        }
    }
}