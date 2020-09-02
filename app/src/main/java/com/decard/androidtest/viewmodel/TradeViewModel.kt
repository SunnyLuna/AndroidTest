package com.decard.androidtest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.decard.androidtest.db.Trade
import com.decard.androidtest.repository.TradeRepository

class TradeViewModel internal constructor(tradeRepository: TradeRepository) : ViewModel() {


    public var tradeData: LiveData<List<Trade>> = tradeRepository.getPlants()
}