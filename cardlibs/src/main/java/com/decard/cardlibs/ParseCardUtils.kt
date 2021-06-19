package com.decard.cardlibs

object ParseCardUtils {


    /**
     * 获取性别
     * 0--女
     * 1--男
     */
    fun getGender(code: String): String {
        if (code == "0") {
            return "女"
        } else if (code == "1") {
            return "男"
        }
        return ""
    }



    fun getNation(code: String): String? {
        val map = mapOf(
            "01" to NATION_01,
            "02" to NATION_02,
            "03" to NATION_03,
            "04" to NATION_04,
            "05" to NATION_05,
            "06" to NATION_06,
            "07" to NATION_07,
            "08" to NATION_08,
            "09" to NATION_09,
            "10" to NATION_10,
            "11" to NATION_11,
            "12" to NATION_12,
            "13" to NATION_13,
            "14" to NATION_14,
            "15" to NATION_15,
            "16" to NATION_16,
            "17" to NATION_17,
            "18" to NATION_18,
            "19" to NATION_19,
            "20" to NATION_20,
            "21" to NATION_21,
            "22" to NATION_22,
            "23" to NATION_23,
            "24" to NATION_24,
            "25" to NATION_25,
            "26" to NATION_26,
            "27" to NATION_27,
            "28" to NATION_28,
            "29" to NATION_29,
            "30" to NATION_30,
            "31" to NATION_31,
            "32" to NATION_32,
            "33" to NATION_33,
            "34" to NATION_34,
            "35" to NATION_35,
            "36" to NATION_36,
            "37" to NATION_37,
            "38" to NATION_38,
            "39" to NATION_39,
            "40" to NATION_40,
            "41" to NATION_41,
            "42" to NATION_42,
            "43" to NATION_43,
            "44" to NATION_44,
            "45" to NATION_45,
            "46" to NATION_46,
            "47" to NATION_47,
            "48" to NATION_48,
            "49" to NATION_49,
            "50" to NATION_50,
            "51" to NATION_51,
            "52" to NATION_52,
            "53" to NATION_53,
            "54" to NATION_54,
            "55" to NATION_55,
            "56" to NATION_56,
            "97" to NATION_97,
            "98" to NATION_98
        )
        return map[code]
    }


    //省份证对应的民族
  private const val  NATION_01 = "汉"
  private const val  NATION_02 = "蒙古"
  private const val  NATION_03 = "回"
  private const val  NATION_04 = "藏"
  private const val  NATION_05 = "维吾尔"
  private const val  NATION_06 = "苗"
  private const val  NATION_07 = "彝"
  private const val  NATION_08 = "壮"
  private const val  NATION_09 = "布依"
  private const val  NATION_10 = "朝鲜"
  private const val  NATION_11 = "满"
  private const val  NATION_12 = "侗"
  private const val  NATION_13 = "瑶"
  private const val  NATION_14 = "白"
  private const val  NATION_15 = "土家"
  private const val  NATION_16 = "哈尼"
  private const val  NATION_17 = "哈萨克"
  private const val  NATION_18 = "傣"
  private const val  NATION_19 = "黎"
  private const val  NATION_20 = "傈僳"
  private const val  NATION_21 = "佤"
  private const val  NATION_22 = "畲"
  private const val  NATION_23 = "高山"
  private const val  NATION_24 = "拉祜"
  private const val  NATION_25 = "水"
  private const val  NATION_26 = "东乡"
  private const val  NATION_27 = "纳西"
  private const val  NATION_28 = "景颇"
  private const val  NATION_29 = "柯尔克孜"
  private const val  NATION_30 = "土"
  private const val  NATION_31 = "达斡尔"
  private const val  NATION_32 = "仫佬"
  private const val  NATION_33 = "羌"
  private const val  NATION_34 = "布朗"
  private const val  NATION_35 = "撒拉"
  private const val  NATION_36 = "毛南"
  private const val  NATION_37 = "仡佬"
  private const val  NATION_38 = "锡伯"
  private const val  NATION_39 = "阿昌"
  private const val  NATION_40 = "普米"
  private const val  NATION_41 = "塔吉克"
  private const val  NATION_42 = "怒"
  private const val  NATION_43 = "乌孜别克"
  private const val  NATION_44 = "俄罗斯"
  private const val  NATION_45 = "鄂温克"
  private const val  NATION_46 = "德昂"
  private const val  NATION_47 = "保安"
  private const val  NATION_48 = "裕固"
  private const val  NATION_49 = "京"
  private const val  NATION_50 = "塔塔尔"
  private const val  NATION_51 = "独龙"
  private const val  NATION_52 = "鄂伦春"
  private const val  NATION_53 = "赫哲"
  private const val  NATION_54 = "门巴"
  private const val  NATION_55 = "珞巴"
  private const val  NATION_56 = "基诺"
  private const val  NATION_97 = "其他"
  private const val  NATION_98 = "外国血统中国籍人士"

}