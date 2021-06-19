package com.example.commonlibs

/**
 * 报文头
 * @author ZJ
 * created at 2021/6/10 13:21
 */
data class MessageHeadBean(
    var version: String,  //报文版本
    var encryptType: String,//报文加密类型
    var type: String,//报文类型
    var flow: String,//报文唯一流水
    var machineManufacturer: String,//机具厂商
    var itemId: String,//ItemId(产品型号)
    var supplierID: String,//SupplierID（供应商）
    var acquirerNumber: String,//收单机构编号
    var branchNumber: String,//分公司编号
    var lineNumber: String,//线路编号
    var carNumber: String,//车辆编号
    var driverNumber: String,//司机编号
    var signStatus: String,//司机签到状态(00:表示未签到或已签退 01:已签到)
    var deviceNumber: String,//设备编号
    var psamNumber: String,//PSAM卡编号
    var iccNumber: String,//流量卡ICCID号
    var dataTransmissionTime: String,//数据传输时间
    var endFlag: String,//数据传输结束标志
    var responseCode: String,//交易应答码
    var reservedDomain: String = "0000000000",//保留域
) {
    public fun getMessageHead(): String {
        return version + encryptType + type + flow + machineManufacturer +
                itemId + supplierID + acquirerNumber + branchNumber +
                lineNumber + carNumber + driverNumber + signStatus + deviceNumber +
                psamNumber + iccNumber + dataTransmissionTime + endFlag +
                responseCode + reservedDomain
    }
}
