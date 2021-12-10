/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.decard.androidtest.net

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.decard.androidtest.db.TradeDatabase
import kotlinx.coroutines.runBlocking

class SeedDatabaseWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {


    private val TAG = "---SeedDatabaseWorker"
    override suspend fun doWork(): Result = runBlocking {
        val await = WebService.create().loadDataAsync().await()
        Log.d(TAG, "doWork   code: ${await.resultCode}  msg: ${await.message}")
        val tradeList = await.data.trade
        Log.d(TAG, "doWork: ${tradeList.size}")
        if (tradeList.isNotEmpty()) {
            val database =
                TradeDatabase.getInstance(
                    applicationContext
                )
            database.userDao().insertAll(tradeList)
            Result.success()
        } else {
            Result.failure()
        }
    }

    companion object {
        private const val TAG = "SeedDatabaseWorker"
    }
}