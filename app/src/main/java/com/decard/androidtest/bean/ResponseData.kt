package com.decard.androidtest.bean

import com.decard.androidtest.db.Trade

data class ResponseData(
    val total: Int,
    val trade: List<Trade>,
    val trade_num: Int
)