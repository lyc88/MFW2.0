package com.zhm.util;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 时间类型相关操作
 * Created by 赵红明 on 2019/11/1.
 */
public class DateUtil {
    public static SimpleDateFormat longFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat shortFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-M");//格式化为年月

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};
    /**
     * 获取现在时间
     *
     * @return 返回时间类型 yyyy-MM-dd
     */
    public static Date getNowShortDate() {
        String dateString = shortFormat.format(new Date());
        ParsePosition pos = new ParsePosition(0);
        Date currentTime_2 = shortFormat.parse(dateString, pos);
        return currentTime_2;
    }

    /**
     * 获取现在时间
     *
     * @return 返回时间类型 yyyy-MM-dd HH:mm:ss
     */
    public static Date getNowDate() {
        String dateString = longFormat.format(new Date());
        ParsePosition pos = new ParsePosition(0);
        Date currentTime_2 = longFormat.parse(dateString, pos);
        return currentTime_2;
    }

    /**
     * 获取现在时间
     *
     * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
     */
    public static String getNowStringDate() {
        String dateString = longFormat.format(new Date());
        return dateString;
    }

    /**
     * 获取现在时间
     *
     * @return返回字符串格式 yyyy-MM-dd
     */
    public static String getNowShortStringDate() {
        String dateString = shortFormat.format(new Date());
        return dateString;
    }

    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     *
     * @param strDate
     * @return
     */
    public static Date strToDate(String strDate) {
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = longFormat.parse(strDate, pos);
        return strtodate;
    }

    public static String getWeeHours () {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return dateToString(calendar.getTime());
    }

    /**
     * 将长时间格式时间转换为字符串 yyyy-MM-dd HH:mm:ss
     *
     * @param dateDate
     * @return
     */
    public static String dateToString(Date dateDate) {
        String dateString  = null;
        if(dateDate!=null){
            dateString = longFormat.format(dateDate);
        }
        return dateString;
    }

    /**
     * 时间是否有重合
     *
     * @param startDate          开始时间
     * @param endDate            结束时间
     * @param startReferenceDate 比对的开始时间
     * @param endReferenceDate   比对的结束时间
     * @return true 有重合 false 没有重合
     */
    public static boolean isTimeSuperposition(Date startDate, Date endDate, Date startReferenceDate, Date endReferenceDate) {
        Integer i1 = compareDate(startDate, startReferenceDate);
        Integer i2 = compareDate(startDate, endReferenceDate);
        Integer i3 = compareDate(endDate, startReferenceDate);
        Integer i4 = compareDate(endDate, endReferenceDate);
        if (i1 != null && i2 != null && 1 == i1 && -1 == i2) {
            return true;
        }
        if (i3 != null && i4 != null && 1 == i3 && -1 == i4) {
            return true;
        }
        if (i1 != null && i4 != null && i1 <= 0 && i4 >= 0) {
            return true;
        }
        return false;
    }

    /**
     * 获取两个日期中间的月份
     * @author      bob
     * @Param       [minDate, maxDate]
     * @return      java.util.List<java.lang.String>
     * @exception
     * @date        2018/7/10 9:57
     */
    public static List<String> getMonthBetween(String minDate, String maxDate) {
        ArrayList<String> result = new ArrayList<String>();
        try {
            Calendar min = Calendar.getInstance();
            Calendar max = Calendar.getInstance();
            min.setTime(monthFormat.parse(minDate));
            min.set(min.get(Calendar.YEAR),
                    min.get(Calendar.MONTH), 1);
            max.setTime(monthFormat.parse(maxDate));
            max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

            Calendar curr = min;
            while (curr.before(max)) {
                result.add(monthFormat.format(curr.getTime()));
                curr.add(Calendar.MONTH, 1);
            }
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }


    private static Integer compareDate(Date date1, Date date2) {
        if (date1 == null && date2 == null) {
            return null;
        }
        return date1.compareTo(date2);
    }

    public static String getNowSubtraction (int type, int len) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(type, calendar.get(type) - len);
        return dateToString(calendar.getTime());
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }
    public static final String parseDateToStr(final String format, final Date date)
    {
        return new SimpleDateFormat(format).format(date);
    }



    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate()
    {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate)
    {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

}
