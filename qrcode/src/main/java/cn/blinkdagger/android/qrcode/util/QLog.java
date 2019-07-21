package cn.blinkdagger.android.qrcode.util;

import android.util.Log;

import cn.blinkdagger.android.qrcode.BuildConfig;

/**
 * @Author ls
 * @Date 2019/3/8
 * @Description
 * @Version
 */
public class QLog {
    /**
     * print VERBOSE log
     *
     * @param tag 标识
     * @param msg 消息
     */
    public static void v(String tag, String msg) {
        log(tag, msg, Log.VERBOSE);
    }

    /**
     * print DEBUG log
     *
     * @param tag 标识
     * @param msg 消息
     */
    public static void d(String tag, String msg) {
        log(tag, msg, Log.DEBUG);
    }

    /**
     * print INFO log
     *
     * @param tag 标识
     * @param msg 消息
     */
    public static void i(String tag, String msg) {
        log(tag, msg, Log.INFO);
    }

    /**
     * print WARN log
     *
     * @param tag 标识
     * @param msg 消息
     */
    public static void w(String tag, String msg) {
        log(tag, msg, Log.WARN);
    }

    /**
     * print ERROR log
     *
     * @param tag 标识
     * @param msg 消息
     */
    public static void e(String tag, String msg) {
        log(tag, msg, Log.ERROR);
    }

    /**
     * print log
     *
     * @param tag 消息的标识
     * @param msg 消息
     * @param priority 日志类型
     */
    private static void log(String tag, String msg, int priority) {
        if (BuildConfig.DEBUG) {
            switch (priority) {
                case Log.VERBOSE:
                    Log.v(tag, msg);
                    break;
                case Log.DEBUG:
                    Log.d(tag, msg);
                    break;
                case Log.INFO:
                    Log.i(tag, msg);
                    break;
                case Log.WARN:
                    Log.w(tag, msg);
                    break;
                case Log.ERROR:
                    Log.e(tag, msg);
                    break;
                default:
                    Log.v(tag, msg);
                    break;
            }
        }
    }
}
