package com.decard.androidtest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.decard.androidtest.repository.TradeRepository

class TradeViewModelFactory(private val repository: TradeRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TradeViewModel(repository) as T
    }
}