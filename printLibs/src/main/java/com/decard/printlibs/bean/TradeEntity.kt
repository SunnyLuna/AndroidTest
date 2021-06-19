package com.decard.printlibs.bean


/**
 * 交易详情
 * @author ZJ
 * created at 2020/8/12 17:19
 */
data class TradeEntity(
    var cardType: String,//卡类型
    var cardName: String,//卡片姓名
    var cardIdNumber: String,//身份证号
    var cardNo: String,//卡号
    var orderNo: String,//订单号
    var rearServiceOrderNo: String,//后勤业务订单号
    var orderState: String,//订单状态
    var transactionNo: String,//支付订单号  微信/支付宝订单号
    var transTime: String,//交易完成时间
    var busiSummary: String,//业务摘要
    var payMode: String,//支付方式
    var payChannel: String,//支付渠道
    var busiType: String,//业务类型
    var amount: String,//交易金额
    var amountReceivable: String,//应收金额
    var totalAmount: String,//订单总金额
    var busiState: String,//业务状态
    var payTime: String,//发起支付时间
    var sn: String, //设备序列号
    var refundStatus: String?, //退款状态  默认00  01
    var refundMsg: String? //退款信息  退款返回信息

)