package com.yuanqing.common.utils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * 时间工具类
 *
 * @author yuanqing
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils
{
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate()
    {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate()
    {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime()
    {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow()
    {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format)
    {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date)
    {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date)
    {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts)
    {
        try
        {
            return new SimpleDateFormat(format).parse(ts);
        }
        catch (ParseException e)
        {
            throw new RuntimeException(e);
        }
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

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str)
    {
        if (str == null)
        {
            return null;
        }
        try
        {
            return parseDate(str.toString(), parsePatterns);
        }
        catch (ParseException e)
        {
            return null;
        }
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

    public static String getDayOfMonth(String type){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        //每个月1号
        if ("1".equals(type)) {
            c.set(Calendar.DATE, c.getActualMinimum(Calendar.DATE));
            //当天的日期
        }else if("2".equals(type)){
            c.setTime(new Date());
            //下个月的1号
        }else if("3".equals(type)){
            c.set(Calendar.DATE, c.getActualMaximum(Calendar.DATE)+1);
            //昨天
        }else if("4".equals(type)){
            c.add(Calendar.DAY_OF_MONTH, -1);
            //前一周
        }else if("5".equals(type)){
            c.add(Calendar.DAY_OF_MONTH, -7);
            //明天
        }else if("6".equals(type)){
            c.add(Calendar.DAY_OF_MONTH, 1);
            //本周日(星期天)
        }else if("7".equals(type)){
            c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            //本周末（星期六）
        }else if("8".equals(type)){
            c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            c.add(Calendar.DAY_OF_WEEK, 7);
        }else if("9".equals(type)){
            c.set(Calendar.DATE, c.getActualMaximum(Calendar.DATE));
        }
        else {//其他值都认为是当天日期
            c.setTime(new Date());
        }
        Date firstDateOfMonth = c.getTime();
        return sdf.format(firstDateOfMonth);
    }

    /**
     * 获取当天日期
     * 开始时间：今天
     * 结束时间：明天
     * @return
     */
    public static JSONObject getDay(){
        String startDate = getDayOfMonth("2");
        String endDate = getDayOfMonth("6");
        JSONObject filters = new JSONObject();
        filters.put("startDate", startDate);
        filters.put("endDate", endDate);
        return filters;
    }

    /**
     * 获取当周日期
     * 开始时间：本周日
     * 结束时间：本周六
     * @return
     */
    public static JSONObject getWeek(){
        String startDate = getDayOfMonth("7");
        String endDate = getDayOfMonth("8");
        JSONObject filters = new JSONObject();
        filters.put("startDate", startDate);
        filters.put("endDate", endDate);
        return filters;
    }

    /**
     * 获取当月日期
     * 开始时间：本月1号
     * 结束时间：下月最后一天
     * @return
     */
    public static JSONObject getMonth(){
        String startDate = getDayOfMonth("1");
        String endDate = getDayOfMonth("9");
        JSONObject filters = new JSONObject();
        filters.put("startDate", startDate);
        filters.put("endDate", endDate);
        return filters;
    }

    public static JSONObject getDayTime(){
        JSONObject filters = new JSONObject();
        filters.put("startDate", LocalDateTime.of(LocalDate.now(), LocalTime.MIN).withNano(0).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        filters.put("endDate", LocalDateTime.of(LocalDate.now(), LocalTime.MAX).withNano(0).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return filters;
    }
}
