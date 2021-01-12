package com.decard.printlibs

data class RechargeBean(
    val cardNo: String,//卡号
    val idCardNo: String,//身份证号
    val patientName: String,//姓名
    val sex: String,//性别
    val amount: String,//金额
    val balance: String,//余额
    val payWay: String,//支付方式
    val optUserName: String,//操作员
    val time: String,//时间
    val hisOrderNo: String,//his订单号
    val snNo: String//设备序列号
)
