package com.decard.printlibs.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间工具类
 *
 * @author ZJ
 * created at 2019/11/25 10:58
 */
public class DateUtils {
    // 日期格式

    private static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final long ONE_DAY = 1000 * 60 * 60 * 24;

    /**
     * 获取年
     *
     * @return
     */
    public static int getYear() {
        Calendar cd = Calendar.getInstance();
        return cd.get(Calendar.YEAR);
    }

    /**
     * 获取月
     *
     * @return
     */
    public static int getMonth() {
        Calendar cd = Calendar.getInstance();
        return cd.get(Calendar.MONTH) + 1;
    }

    public static String getLastMonth() {

        if (getMonth() == 1) {
            return (getYear() - 1) + "/12/01 00:00:00";
        }
        if (getMonth() < 10) {

            return getYear() + "/0" + (getMonth() - 1) + "/01 00:00:00";
        } else {
            return getYear() + "/" + (getMonth() - 1) + "/01 00:00:00";
        }
    }

    /**
     * 获取日
     *
     * @return
     */
    public static int getDay() {
        Calendar cd = Calendar.getInstance();
        return cd.get(Calendar.DATE);
    }

    /**
     * 获取时
     *
     * @return
     */
    public static int getHour() {
        Calendar cd = Calendar.getInstance();
        return cd.get(Calendar.HOUR);
    }

    /**
     * 获取分
     *
     * @return
     */
    public static int getMinute() {
        Calendar cd = Calendar.getInstance();
        return cd.get(Calendar.MINUTE);
    }

    /**
     * 获取当前时间年月日
     *
     * @return
     */
    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        return sdf.format(now);
    }

    public static String getCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM", Locale.getDefault());
        return sdf.format(now);
    }


    public static String getStartTime(String time) {

        return time += "000000";
    }

    public static String getEndTime(String time) {
        return time += "235959";
    }

    //获取前一天
    public static String getLastDay(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(date);
        int day = calendar.get(Calendar.DATE);
        //此处修改为+1则是获取后一天
        calendar.set(Calendar.DATE, day - 1);

        String lastDay = sdf.format(calendar.getTime());
        return lastDay;
    }

    //获取下一天
    public static String getNextDay(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(date);
        int day = calendar.get(Calendar.DATE);
        calendar.set(Calendar.DATE, day + 1);

        String lastDay = sdf.format(calendar.getTime());
        return lastDay;
    }

    //获取前一个月
    public static String getLastMonth(String month) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        Calendar calendar = Calendar.getInstance();
        Date date = null;
        try {
            date = sdf.parse(month);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(date);
        int nowMonth = calendar.get(Calendar.MONTH);
        calendar.set(Calendar.MONTH, nowMonth - 1);

        String lastMonth = sdf.format(calendar.getTime());
        return lastMonth;
    }

    //获取后一个月
    public static String getNextMonth(String month) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        Calendar calendar = Calendar.getInstance();
        Date date = null;
        try {
            date = sdf.parse(month);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(date);
        int nowMonth = calendar.get(Calendar.MONTH);
        calendar.set(Calendar.MONTH, nowMonth + 1);

        String lastMonth = sdf.format(calendar.getTime());
        return lastMonth;
    }


    public static String getLast3Month() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHhhss");
        Calendar c = Calendar.getInstance();
        //过去三个月
        c.setTime(new Date());
        c.add(Calendar.MONTH, -3);
        Date date = c.getTime();
        String mon6 = format.format(date);
        System.out.println("过去3个月：" + mon6);
        return mon6;
    }

    public static String getLast6MonthDay() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        //过去三个月
        c.setTime(new Date());
        c.add(Calendar.MONTH, -6);
        Date date = c.getTime();
        String mon6 = format.format(date);
        System.out.println("过去6个月：" + mon6);
        return mon6;
    }

    public static String getLastYearMonth() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
        Calendar c = Calendar.getInstance();
        //过去一年
        c.setTime(new Date());
        c.add(Calendar.YEAR, -1);
        Date y = c.getTime();
        String year = format.format(y);
        return year;
    }

    public static String getLastDayOfMonth(String yearMonth) {
        int year = Integer.parseInt(yearMonth.substring(0, 4));
        int month = Integer.parseInt(yearMonth.substring(4, 6));
        int dayOfMonth = getDayOfMonth(year, month);
        return dayOfMonth + "";
    }

    /**
     * 时间比较器
     *
     * @author ZJ
     * created at 2020/6/16 20:19
     */
    public static int compareTime(String date1, String date2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        try {
            //将日期转成Date对象作比较
            Date fomatDate1 = sdf.parse(date1);
            Date fomatDate2 = sdf.parse(date2);
            //比较两个日期
            return fomatDate1.compareTo(fomatDate2);

        } catch (ParseException e) {

            e.printStackTrace();
        }
        return 0;
    }


    public static int compareYearMonth(String date1, String date2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        try {
            //将日期转成Date对象作比较
            Date fomatDate1 = sdf.parse(date1);
            Date fomatDate2 = sdf.parse(date2);
            //比较两个日期
            return fomatDate1.compareTo(fomatDate2);

        } catch (ParseException e) {

            e.printStackTrace();
        }
        return 0;
    }

    public static String dateFormat(String date) {
        SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfMonth = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat sdfDaily = new SimpleDateFormat("yyyy年MM月dd日");
        SimpleDateFormat sdfMonthly = new SimpleDateFormat("yyyy年MM月");

        try {
            Date date1 = sdfDay.parse(date);
            return sdfDaily.format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
            try {
                Date date1 = sdfMonth.parse(date);
                return sdfMonthly.format(date1);
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        }
        return "";
    }

    /**
     * 获取当前时间  年月日 时分秒
     *
     * @return
     */
    public static String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(date);
    }

    /**
     * 转换时间
     *
     * @param time
     * @return
     */
    public static String dateToFormat(long time) {
        Date date = new Date(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss", Locale.getDefault());
        return simpleDateFormat.format(date);
    }

    /**
     * 时间转换
     *
     * @param date
     * @return
     */
    public static String dateToFormat(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        return simpleDateFormat.format(date);
    }

    /**
     * 时间转换
     *
     * @param date
     * @return
     */
    public static String dateToFormatActiveDeviceOrCollect(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        return simpleDateFormat.format(date);
    }

    /**
     * 将日期格式的字符串转换为长整型
     *
     * @param date
     * @param format
     * @return
     */
    public static long convertToLong(String date, String format) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
            return formatter.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 将长整型数字转换为日期格式的字符串
     *
     * @param time
     * @return
     */
    public static String convertToString(long time) {
        if (time > 0) {
            SimpleDateFormat formatter = new SimpleDateFormat(TIME_FORMAT, Locale.getDefault());
            Date date = new Date(time);
            return formatter.format(date);
        }
        return "";
    }

    /**
     * 获取当前时间是星期几
     *
     * @param dt
     * @return
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();

        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;

        return weekDays[w];
    }

    /**
     * 获取某年某月有多少天
     */

    public static int getDayOfMonth(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, 0); //输入类型为int类型
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取两个时间相差的天数
     *
     * @return time1 - time2相差的天数
     */
    public static int getDayOffset(long time1, long time2) {
        // 将小的时间置为当天的0点
        long offsetTime;
        if (time1 > time2) {
            offsetTime = time1 - getDayStartTime(getCalendar(time2)).getTimeInMillis();
        } else {
            offsetTime = getDayStartTime(getCalendar(time1)).getTimeInMillis() - time2;
        }
        return (int) (offsetTime / ONE_DAY);
    }

    private static Calendar getCalendar(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return calendar;
    }

    private static Calendar getDayStartTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    /**
     * 将时间转化为时分秒
     *
     * @param ms
     * @return
     */
    public static String formatTime(Long ms) {
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        StringBuilder sb = new StringBuilder();
        if (day > 0) {
            sb.append(day).append("天");
        }
        if (hour > 0) {
            sb.append(hour).append("小时");
        }
        if (minute > 0) {
            sb.append(minute).append("分钟");
        }
        if (second > 0) {
            sb.append(second).append("秒");
        }
        if (milliSecond > 0) {
            sb.append(milliSecond).append("毫秒");
        }
        return sb.toString();
    }
}
