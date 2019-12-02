package cn.blinkdagger.androidLab.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by d on 2016/11/16.
 */

public class TimeUtil {


    public static final SimpleDateFormat DEFAULT_SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    /**
     * 获取当前时间的默认的格式化字符串
     */
    public static String getCuerentTimeString() {
        String time = DEFAULT_SDF.format(new Date(System.currentTimeMillis()));
        return time;
    }
    /**
     * 获取当前时间的默认的格式化字符串
     */
    public static String getCurrentTimeStringInFormat(long milliseconds) {
        String time = DEFAULT_SDF.format(new Date(milliseconds));
        return time;
    }

    /**
     * 获取时间的对应格式化字符串
     */
    public static String getTimeStringInFormat(long milliseconds, SimpleDateFormat format) {
        return format.format(new Date(milliseconds));
    }
}
