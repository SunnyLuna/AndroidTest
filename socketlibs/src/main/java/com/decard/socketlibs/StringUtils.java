package com.decard.socketlibs;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;

/**
 * File Description
 *
 * @author Dell
 * @date 2018/8/1
 */
public class StringUtils {

    private final static char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String toHexString(byte[] array) {
        return toHexString(array, 0, array.length);
    }

    public static String toHexString(byte[] array, int offset, int length) {
        char[] buf = new char[length * 2];

        int bufIndex = 0;
        for (int i = offset; i < offset + length; i++) {
            byte b = array[i];
            buf[bufIndex++] = HEX_DIGITS[(b >>> 4) & 0x0F];
            buf[bufIndex++] = HEX_DIGITS[b & 0x0F];
        }

        return new String(buf);
    }

    public static String toHexString(byte b) {
        return toHexString(toByteArray(b));
    }

    public static byte[] toByteArray(byte b) {
        byte[] array = new byte[1];
        array[0] = b;
        return array;
    }


    public static byte[] concatAll(byte[] first, byte[]... rest) {
        int totalLength = first.length;
        for (byte[] array : rest) {
            totalLength += array.length;
        }
        byte[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (byte[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }

    /**
     * 单个数组合并
     *
     * @param first
     * @param second
     * @return
     */
    public static byte[] concat(byte[] first, byte[] second) {
        byte[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    //压缩成BCD码
    public static byte[] ASCII_To_BCD(byte[] ascii) {
        int asc_len = ascii.length;
        byte[] bcd = new byte[asc_len / 2];
        int j = 0;
        for (int i = 0; i < (asc_len + 1) / 2; i++) {
            bcd[i] = ASCII_To_BCD(ascii[j++]);
            bcd[i] = (byte) (((j >= asc_len) ? 0x00 : ASCII_To_BCD(ascii[j++])) + (bcd[i] << 4));
        }
        return bcd;
    }

    //单个字节压缩成BCD
    public static byte ASCII_To_BCD(byte asc) {
        byte bcd;

        if ((asc >= '0') && (asc <= '9'))
            bcd = (byte) (asc - '0');
        else if ((asc >= 'A') && (asc <= 'F'))
            bcd = (byte) (asc - 'A' + 10);
        else if ((asc >= 'a') && (asc <= 'f'))
            bcd = (byte) (asc - 'a' + 10);
        else
            bcd = (byte) (asc - 48);
        return bcd;
    }

    /**
     * 10进制转bcd
     *
     * @param asc
     * @return bcd码
     */
    public static String str2BcdStr(String asc) {
        int len = asc.length();
        int mod = len % 2;
        if (mod != 0) {
            asc = "0" + asc;
            len = asc.length();
        }
        byte abt[] = new byte[len];
        if (len >= 2) {
            len = len / 2;
        }
        byte bbt[] = new byte[len];
        abt = asc.getBytes();
        int j, k;
        for (int p = 0; p < asc.length() / 2; p++) {
            if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
                j = abt[2 * p] - '0';
            } else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
                j = abt[2 * p] - 'a' + 0x0a;
            } else {
                j = abt[2 * p] - 'A' + 0x0a;
            }
            if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
                k = abt[2 * p + 1] - '0';
            } else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
                k = abt[2 * p + 1] - 'a' + 0x0a;
            } else {
                k = abt[2 * p + 1] - 'A' + 0x0a;
            }
            int a = (j << 4) + k;
            byte b = (byte) a;
            bbt[p] = b;
        }
        return new String(bbt);
    }


    public static String BCD_To_Str(byte[] bytes) {
        char temp[] = new char[bytes.length * 2], val;

        for (int i = 0; i < bytes.length; i++) {
            val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
            temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');

            val = (char) (bytes[i] & 0x0f);
            temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
        }
        return new String(temp);
    }


    /**
     * 位数不够左补0
     *
     * @param str
     * @param strLength
     * @return
     */
    public static String addZeroForString(String str, int strLength) {

        int strLen = str.length();
        if (strLen == strLength) {
            return str;
        }
        StringBuffer sb = new StringBuffer();
        while (strLen < strLength) {
            sb.append("0");
            strLen += 1;
        }
        sb.append(str);

        return sb.toString();
    }

    public static String addZeroForNum(String str, int strLength) {
        int strLen = str.length();
        String result = String.valueOf(strLen);
        int length = result.length();
        if (length < strLength) {
            while (length < strLength) {
                StringBuffer sb = new StringBuffer();
                sb.append("0").append(result);//左补0
//			    	sb.append(str).append("0");//右补0
                result = sb.toString();
                length = result.length();
            }
        }
        return result;
    }

    /**
     * 将int转换为固定长度的String（前补0，或者后补0）
     *
     * @return String
     */
    public static String addZeroForNum(long num, int strLength) {
        String str = String.valueOf(num);
        int strLen = str.length();
        if (strLen < strLength) {
            while (strLen < strLength) {
                StringBuffer sb = new StringBuffer();
                sb.append("0").append(str);//左补0
//		    	sb.append(str).append("0");//右补0
                str = sb.toString();
                strLen = str.length();
            }
        }
        return str;
    }

    /**
     * 位数不够右补空格
     *
     * @param str
     * @param strLength
     * @return
     */
    public static String addSpaceForStringRight(String str, int strLength) {

        int strLen = str.length();
        if (strLen == strLength) {
            return str;
        }

        return String.format("%-" + strLength + "s", str);
    }


    /**
     * 位数不够左补空格
     *
     * @param str
     * @param strLength
     * @return
     */
    public static String addSpaceForStringLeft(String str, int strLength) {

        int strLen = str.length();
        if (strLen == strLength) {
            return str;
        }

        return String.format("%" + strLength + "s", str);
    }


    /**
     * 按传的参数补固定长度的空格
     *
     * @param num
     * @return
     */
    public static String setSpace(int num) {
        StringBuffer sb = new StringBuffer();
        while (true) {
            if (sb.length() == num) {
                break;
            }
            sb.append(" ");
        }
        return sb.toString();
    }

    /**
     * @param value
     * @return
     */
    public static String stringToAscii(String value) {
        StringBuffer sbu = new StringBuffer();
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (i != chars.length - 1) {
                sbu.append((int) chars[i]).append(",");
            } else {
                sbu.append((int) chars[i]);
            }
        }
        return sbu.toString();
    }



}
