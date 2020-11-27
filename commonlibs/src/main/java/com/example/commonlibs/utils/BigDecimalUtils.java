package com.example.commonlibs.utils;

import java.math.BigDecimal;

/**
 * 金额转换
 *
 * @author ZJ
 * created at 2020/9/25 9:41
 */
public class BigDecimalUtils {

    public static BigDecimal add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2);
    }

    public static BigDecimal subtract(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2);
    }

    public static BigDecimal multiply(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2);
    }


    public static BigDecimal divide(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        // 2 = 保留小数点后两位   ROUND_HALF_UP = 四舍五入
        return b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP);// 应对除不尽的情况
    }

    /**
     * 元转分
     *
     * @return
     */
    public static int yuanToFen(double dou1) {
        BigDecimal big1 = new BigDecimal(Double.valueOf(dou1)).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal big2 = new BigDecimal(Double.valueOf("100.0"));
        return big1.multiply(big2).intValue();
    }

    /**
     * 分转元，转换为bigDecimal在toString
     *
     * @return
     */
    public static String fenToYuan(int amount) {
        return new BigDecimal(amount).divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

}
