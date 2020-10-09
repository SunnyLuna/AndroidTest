package com.example.commonlibs.utils;

import java.math.BigDecimal;

/**
 * 金额转换
 *
 * @author ZJ
 * created at 2020/9/25 9:41
 */
public class ChangeUtils {
    /**
     * 元转分
     *
     * @return
     */
    public static int formatDoubleTOInt(double dou1) {
        BigDecimal big1 = new BigDecimal(Double.valueOf(dou1)).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal big2 = new BigDecimal(Double.valueOf("100.0"));
        return big1.multiply(big2).intValue();
    }

    /**
     * 分转元，转换为bigDecimal在toString
     *
     * @return
     */
    public static String changeF2Y(int amount) {
        return new BigDecimal(amount).divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

}
