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
import com.decard.androidtest.db.TradeDatabase
import com.decard.androidtest.repository.TradeRepository
import com.decard.androidtest.viewmodel.TradeViewModelFactory


/**
 * Static methods used to inject classes needed for various Activities and Fragments.
 */
object InjectorUtils {

    private val TAG = "---InjectorUtils"
    private fun getTradeRepository(context: Context): TradeRepository {
        Log.d(TAG, "getTradeRepository: ")
        return TradeRepository.getInstance(
            TradeDatabase.getInstance(context)
                .userDao()
        )
    }

    public fun provideTradeViewModelFactory(context: Context): TradeViewModelFactory {
        Log.d(TAG, "provideTradeViewModelFactory: ")
        return TradeViewModelFactory(
            getTradeRepository(
                context
            )
        )
    }
}
