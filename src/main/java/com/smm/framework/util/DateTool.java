package com.smm.framework.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Alan Chen
 * @description 时间工具类
 * @date 2020-01-13
 */
public class DateTool {

    private final static int DECEMBER =12;
    
    public static Date getDate(String date){
        return getDate(date,"yyyy-MM-dd HH:mm:ss");
    }

    public static Date getDate(String date,String pattern){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date parse = null;
        try {
            parse = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parse;
    }

    /**
     * 获取日期格式化内容
     * @param date
     * @return
     */
    public static String getFormatDate(Date date) {
        return getFormatDate(date,"yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获取指定日期是星期几
     * @param date
     * @return
     */
    public static String getDateWeekDay(Date date) {
        return getDateWeekDay(date,Locale.CHINESE);
    }

    /**
     * 获取指定日期是星期几
     * @param date
     * @return
     */
    public static String getDateWeekDay(Date date,Locale locale) {
        return getFormatDate(date,"EEEE",locale);
    }

    /**
     * 获取日期格式化内容
     * @param date
     * @param pattern
     * @return
     */
    public static String getFormatDate(Date date,String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern,Locale.CHINESE);
        String formatDate = format.format(date.getTime());
        return formatDate;
    }

    /**
     * 获取日期格式化内容
     * @param date
     * @param pattern
     * @return
     */
    public static String getFormatDate(Date date,String pattern,Locale locale) {
        SimpleDateFormat format = new SimpleDateFormat(pattern,locale);
        String formatDate = format.format(date.getTime());
        return formatDate;
    }

    /**
     * 获取当前时间往前/往后n小时的时间
     * @param hour 往后：参数传正；往前：参数传负数
     * @return
     */
    public static Date dateRollHour(int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR, hour);
        return calendar.getTime();
    }

    /**
     * 获取当前时间往前/往后n分钟的时间
     * @param minute 往后：参数传正；往前：参数传负数
     * @return
     */
    public static Date dateRollMinute(int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

    /**
     * 获取当天的开始时间
     * @return
     */
    public static Date getTodayStartTime() {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取当天的开始时间
     * @return
     */
    public static String getTodayStartTimeFormat() {
        Date date = getTodayStartTime();
        return getFormatDate(date);
    }

    /**
     * 获取当天的结束时间
     * @return
     */
    public static Date getTodayEndTime() {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }

    /**
     * 获取当天的结束时间
     * @return
     */
    public static String getTodayEndTimeFormat() {
        Date date = getTodayEndTime();
        return getFormatDate(date);
    }

    /**
     * 获取amount天前的开始时间
     * @return
     */
    public static Date getBeforeDayStartTime(int amount) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(getTodayStartTime());
        cal.add(Calendar.DAY_OF_MONTH, -amount);
        return cal.getTime();
    }


    /**
     * 获取前amount天前的结束时间
     * @return
     */
    public static Date getBeforeDayEndTime(int amount) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(getTodayEndTime());
        cal.add(Calendar.DAY_OF_MONTH, -amount);
        return cal.getTime();
    }

    /**
     * 获取amount天后的开始时间
     * @return
     */
    public static Date getAfterDayStartTime(int amount) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(getTodayStartTime());
        cal.add(Calendar.DAY_OF_MONTH, amount);
        return cal.getTime();
    }


    /**
     * 获取前amount天后的结束时间
     * @return
     */
    public static Date getAfterDayEndTime(int amount) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(getTodayEndTime());
        cal.add(Calendar.DAY_OF_MONTH, amount);
        return cal.getTime();
    }

    /**
     * 获取昨天的开始时间
     * @return
     */
    public static Date getYesterdayStartTime() {
        return getBeforeDayStartTime(1);
    }

    /**
     * 获取昨天的结束时间
     * @return
     */
    public static Date getYesterdayEndTime() {
       return getBeforeDayEndTime(1);
    }

    /**
     * 获取明天的开始时间
     * @return
     */
    public static Date getTomorrowStartTime() {
        return getAfterDayStartTime(1);
    }

    /**
     * 获取明天的结束时间
     * @return
     */
    public static Date getTomorrowEndTime() {
        return getAfterDayEndTime(1);
    }

    /**
     * 获取本周的开始时间
     * @return
     */
    public static Date getThisWeekStartTime() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayofweek == 1) {
            dayofweek += 7;
        }
        cal.add(Calendar.DATE, 2 - dayofweek);
        return getDayStartTime(cal.getTime());
    }

    /**
     * 获取本周的结束时间
     * @return
     */
    public static Date getThisWeekEndTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getThisWeekStartTime());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        Date weekEndSta = cal.getTime();
        return getDayEndTime(weekEndSta);
    }

    /**
     * 获取上周的开始时间
     * @return
     */
    public static Date getLastWeekStartTime() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayofweek == 1) {
            dayofweek += 7;
        }
        cal.add(Calendar.DATE, 2 - dayofweek - 7);
        return getDayStartTime(cal.getTime());
    }

    /**
     * 获取上周的结束时间
     * @return
     */
    public static Date getLastWeekEndTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getLastWeekStartTime());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        Date weekEndSta = cal.getTime();
        return getDayEndTime(weekEndSta);
    }

    /**
     * 获取本月的开始时间
     * @return
     */
    public static Date getThisMonthStartTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 1, 1);
        return getDayStartTime(calendar.getTime());
    }

    /**
     * 获取本月的结束时间
     * @return
     */
    public static Date getThisMonthEndTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 1, 1);
        int day = calendar.getActualMaximum(5);
        calendar.set(getNowYear(), getNowMonth() - 1, day);
        return getDayEndTime(calendar.getTime());
    }

    /**
     * 获取上月的开始时间
     * @return
     */
    public static Date getLastMonthStartTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 2, 1);
        return getDayStartTime(calendar.getTime());
    }

    /**
     * 获取上月的结束时间
     * @return
     */
    public static Date getLastMonthEndTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 2, 1);
        int day = calendar.getActualMaximum(5);
        calendar.set(getNowYear(), getNowMonth() - 2, day);
        return getDayEndTime(calendar.getTime());
    }

    /**
     * 获取本年的开始时间
     * @return
     */
    public static Date getThisYearStartTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, getNowYear());
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DATE, 1);
        return getDayStartTime(cal.getTime());
    }

    /**
     * 获取本年的结束时间
     * @return
     */
    public static Date getThisYearEndTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, getNowYear());
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DATE, 31);
        return getDayEndTime(cal.getTime());
    }

    /**
     * 获取某个日期的开始时间
     * @param d
     * @return
     */
    public static Date getDayStartTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d){
            calendar.setTime(d);
        }

        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Date(calendar.getTimeInMillis());
    }

    /**
     * 获取某个日期的结束时间
     * @param d
     * @return
     */
    public static Date getDayEndTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d){
            calendar.setTime(d);
        }
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return new Date(calendar.getTimeInMillis());
    }

    /**
     * 获取今年是哪一年
     * @return
     */
    public static Integer getNowYear() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return Integer.valueOf(gc.get(1));
    }

    /**
     * 获取本月是哪一月
     * @return
     */
    public static int getNowMonth() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return gc.get(2) + 1;
    }

    /**
     * 两个日期相减得到的天数
     * @param beginDate
     * @param endDate
     * @return
     */
    public static int getDiffDays(Date beginDate, Date endDate) {
        if (beginDate == null || endDate == null) {
            throw new IllegalArgumentException("getDiffDays param is null!");
        }
        long diff = (endDate.getTime() - beginDate.getTime())
                / (1000 * 60 * 60 * 24);
        int days = new Long(diff).intValue();
        return days;
    }

    /**
     * 两个日期相减得到的毫秒数
     * @param beginDate
     * @param endDate
     * @return
     */
    public static long dateDiff(Date beginDate, Date endDate) {
        long date1ms = beginDate.getTime();
        long date2ms = endDate.getTime();
        return date2ms - date1ms;
    }

    /**
     * 获取两个日期中的最大日期
     * @param beginDate
     * @param endDate
     * @return
     */
    public static Date max(Date beginDate, Date endDate) {
        if (beginDate == null) {
            return endDate;
        }
        if (endDate == null) {
            return beginDate;
        }
        if (beginDate.after(endDate)) {
            return beginDate;
        }
        return endDate;
    }

    /**
     * 获取两个日期中的最小日期
     * @param beginDate
     * @param endDate
     * @return
     */
    public static Date min(Date beginDate, Date endDate) {
        if (beginDate == null) {
            return endDate;
        }
        if (endDate == null) {
            return beginDate;
        }
        if (beginDate.after(endDate)) {
            return endDate;
        }
        return beginDate;
    }

    /**
     * 返回某月该季度的第一个月
     * @param date
     * @return
     */
    public static Date getFirstSeasonDate(Date date) {
        final int[] season = {1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int sean = season[cal.get(Calendar.MONTH)];
        cal.set(Calendar.MONTH, sean * 3 - 3);
        return cal.getTime();
    }

    /**
     * 返回某个日期下几天的日期
     * @param date
     * @param i
     * @return
     */
    public static Date getNextDay(Date date, int i) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.DATE, cal.get(Calendar.DATE) + i);
        return cal.getTime();
    }

    /**
     * 返回某个日期前几天的日期
     * @param date
     * @param i
     * @return
     */
    public static Date getFrontDay(Date date, int i) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.DATE, cal.get(Calendar.DATE) - i);
        return cal.getTime();
    }

    /**
     * 获取某年某月到某年某月按天的切片日期集合(间隔天数的集合)
     * @param beginYear
     * @param beginMonth
     * @param endYear
     * @param endMonth
     * @param k
     * @return
     */
    public static List getTimeList(int beginYear, int beginMonth, int endYear,
                                   int endMonth, int k) {
        List list = new ArrayList();
        if (beginYear == endYear) {
            for (int j = beginMonth; j <= endMonth; j++) {
                list.add(getTimeList(beginYear, j, k));
            }
        } else {
            {
                for (int j = beginMonth; j < DECEMBER; j++) {
                    list.add(getTimeList(beginYear, j, k));
                }
                for (int i = beginYear + 1; i < endYear; i++) {
                    for (int j = 0; j < DECEMBER; j++) {
                        list.add(getTimeList(i, j, k));
                    }
                }
                for (int j = 0; j <= endMonth; j++) {
                    list.add(getTimeList(endYear, j, k));
                }
            }
        }
        return list;
    }

    /**
     * 获取某年某月按天切片日期集合(某个月间隔多少天的日期集合)
     * @param beginYear
     * @param beginMonth
     * @param k
     * @return
     */
    public static List getTimeList(int beginYear, int beginMonth, int k) {
        List list = new ArrayList();
        Calendar begincal = new GregorianCalendar(beginYear, beginMonth, 1);
        int max = begincal.getActualMaximum(Calendar.DATE);
        for (int i = 1; i < max; i = i + k) {
            list.add(begincal.getTime());
            begincal.add(Calendar.DATE, k);
        }
        begincal = new GregorianCalendar(beginYear, beginMonth, max);
        list.add(begincal.getTime());
        return list;
    }
}
