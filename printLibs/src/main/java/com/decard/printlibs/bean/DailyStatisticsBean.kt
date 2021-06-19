package com.decard.printlibs.bean

data class DailyStatisticsBean(
    var statisticsTime: String,
    var totalCount: String,
    var totalMoney: String,
    var wxCount: String,
    var wxMoney: String,
    var aliCount: String,
    var aliMoney: String
)
