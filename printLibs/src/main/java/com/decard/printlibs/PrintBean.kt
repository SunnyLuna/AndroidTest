package com.decard.printlibs

data class PrintBean(
    val datas: List<Data>
)

data class Data(
    val align: Int,   //对齐方式，int类型，0表示左对齐，1表示居中，2表示右对齐，例如："align":1
    val bold: Boolean,  //是否加粗，bool类型，true表示加粗，false表示不加粗，例如："bold":true；
    val fontSize: Int,//字体大小，int类型，例如："fontSize":1，
    val key: String,//需要取值的数据项，string类型，当type为data，barcode，qrcode时有效
    val left: Int,//左边距，例如："left":100，
    val text: String,//需要打印的文本
    val type: String//打印项类型   “common”普通文本
    // “data”需要追加数据项的文本，结合具体配置的key值打印text+key数据，例如打印就诊卡号："text":"就诊卡号：
    // ","type":"data","key":"idCardNo"，则打印数据为：就诊卡号：612101********8888
//“barcode”一维码
//“qrcode”二维码
){
    override fun toString(): String {
        return "Data(align=$align, bold=$bold, fontSize=$fontSize, key='$key', left=$left, text='$text', type='$type')"
    }
}