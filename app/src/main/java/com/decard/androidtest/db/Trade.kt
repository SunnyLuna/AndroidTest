package com.decard.androidtest.db

import androidx.databinding.BaseObservable
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 交易详情
 * @author ZJ
 * created at 2020/8/12 17:19
 */
@Entity(tableName = "trade")
data class Trade(
    val out_trade_no: String,
    val pay_type: String,
    val total_fee: Int,
    val trade_pay_time: String,
    val trade_set_time: String,
    @PrimaryKey
    val transaction_id: String
) : BaseObservable()