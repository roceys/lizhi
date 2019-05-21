package ren.perry.lizhi.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author perrywe
 * @date 2019/4/12
 * WeChat  917351143
 */
public class DateUtils {

    /**
     * 1分钟
     */
    @SuppressWarnings("WeakerAccess")
    public static final int TIME_ONE_MINUTE = 60 * 1000;

    /**
     * 1小时
     */
    @SuppressWarnings("WeakerAccess")
    public static final int TIME_ONE_HOUR = TIME_ONE_MINUTE * 60;

    /**
     * 1天
     */
    @SuppressWarnings("WeakerAccess")
    public static final int TIME_ONE_DAY = TIME_ONE_HOUR * 24;

    /**
     * 1个月
     */
    @SuppressWarnings("WeakerAccess")
    public static final long TIME_ONE_MONTH = TIME_ONE_DAY * 30L;

    /**
     * 1年
     */
    @SuppressWarnings("WeakerAccess")
    public static final long TIME_ONE_YEAR = TIME_ONE_MONTH * 365L;

    public static final String DATE_FORMAT_1 = "yyyy-MM-dd";
    public static final String DATE_FORMAT_2 = "yyyy-MM-dd HH:mm";
    public static final String DATE_FORMAT_3 = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_4 = "HH";
    public static final String DATE_FORMAT_5 = "MM/dd";
    public static final String DATE_FORMAT_6 = "yyyy-MM";
    public static final String DATE_FORMAT_7 = "HHmmss";
    public static final String DATE_FORMAT_8 = "yyMMdd";

    /**
     * 时间戳 => 时间
     *
     * @param time 时间戳
     * @return yyyy-MM-dd
     */
    @SuppressLint("SimpleDateFormat")
    public static String stampToDate(long time, String format) {
        return new SimpleDateFormat(format).format(new Date(time));

    }

    /**
     * Date => 时间
     *
     * @param date date
     * @return yyyy-MM-dd
     */
    @SuppressWarnings("WeakerAccess")
    @SuppressLint("SimpleDateFormat")
    public static String stampToDate(Date date, String format) {
        return new SimpleDateFormat(format).format(date);

    }

    /**
     * 获取Java时间戳
     *
     * @return 时间戳
     */
    public static long getTime() {
        return System.currentTimeMillis();
    }

    /**
     * 获取5种时间段类型
     *
     * @return 凌晨=1、上午=2、中午=3、下午=4、晚上=5
     */
    public static int getDay5Types() {
        int type = 0;
        String str = stampToDate(new Date(), DATE_FORMAT_4);
        int a = Integer.parseInt(str);
        if (a >= 0 && a <= 6) {
            type = 1;
        }
        if (a > 6 && a <= 12) {
            type = 2;
        }
        if (a > 12 && a <= 13) {
            type = 3;
        }
        if (a > 13 && a <= 18) {
            type = 4;
        }
        if (a > 18 && a <= 24) {
            type = 5;
        }
        return type;
    }

    public static String getBeforeTime(String time) {
        return getBeforeTime(Long.valueOf(time));
    }

    public static String getBeforeTime(long time) {
        try {
            if (time == 0) return "";
            long diff = getTime() - time;
            if (diff > TIME_ONE_YEAR) {
                return diff / TIME_ONE_YEAR + "年前";
            } else if (diff > TIME_ONE_MONTH) {
                return diff / TIME_ONE_MONTH + "月前";
            } else if (diff > TIME_ONE_DAY) {
                long days = diff / TIME_ONE_DAY;
                return days + "天前";
            } else if (diff > TIME_ONE_HOUR) {
                long hours = diff / TIME_ONE_HOUR;
                return hours + "小时前";
            } else if (diff > TIME_ONE_MINUTE) {
                long minutes = diff / TIME_ONE_MINUTE;
                return minutes + "分钟前";
            } else {
                return "1分钟前";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 判断选择的日期是否是今天
     *
     * @param time 时间戳
     */
    public static boolean isToday(long time) {
        return isThisTime(time, DATE_FORMAT_1);
    }

    /**
     * 判断选择的日期是否是本周
     *
     * @param time 时间戳
     */
    public static boolean isThisWeek(long time) {
        Calendar c = Calendar.getInstance();
        int cw = c.get(Calendar.WEEK_OF_YEAR);
        c.setTime(new Date(time));
        int pw = c.get(Calendar.WEEK_OF_YEAR);
        return pw == cw;
    }

    /**
     * 判断选择的日期是否是本月
     *
     * @param time 时间戳
     */
    public static boolean isThisMonth(long time) {
        return isThisTime(time, DATE_FORMAT_6);
    }

    private static boolean isThisTime(long time, String pattern) {
        Date date = new Date(time);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String param = sdf.format(date);//参数时间
        String now = sdf.format(new Date());//当前时间
        return param.equals(now);
    }
}
