package cn.blinkdagger.androidLab.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;


/**
 * Created by d on 2016/11/16.
 * APP信息辅助类
 */

public class AppUtil {

    /**
     * 获取包名
     *
     * @param context 上下文
     * @return 本APP的包名
     */
    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    /**
     * 获取App名称
     *
     * @param context 上下文
     * @return 本App名称
     */
    public static String getAppName(Context context) {
        return getAppName(context, context.getPackageName());
    }

    /**
     * 获取App名称
     *
     * @param context     上下文
     * @param packageName 包名
     * @return App名称
     */
    public static String getAppName(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) return null;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.applicationInfo.loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取版本号
     *
     * @param context 上下文
     * @return 本APP的版本号
     */
    public static int getVersionCode(Context context) {
        return getVersionCode(context, context.getPackageName());
    }

    /**
     * 获取App版本号
     *
     * @param context     上下文
     * @param packageName 包名
     * @return App版本号
     */
    public static int getVersionCode(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) return -1;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? -1 : pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 获取版本名称
     *
     * @param context 上下文
     * @return 本APP的版本名称
     */
    public static String getVersionName(Context context) {
        return getVersionName(context, context.getPackageName());
    }

    /**
     * 获取App版本名称
     *
     * @param context     上下文
     * @param packageName 包名
     * @return App版本名称
     */
    public static String getVersionName(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) return null;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 判断App是否是Debug版本
     * （注意混淆的时候排除相应的BuildConfig类 ）
     *
     * @param packageName 包名
     * @return boolean
     */
    public static boolean isAppDebug(String packageName) {
        if (TextUtils.isEmpty(packageName)) return false;
        try {
            Class clazz = Class.forName(packageName + ".BuildConfig");
            Boolean isDebug = clazz.getField("DEBUG").getBoolean(clazz);
            return isDebug == null ? false : isDebug;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 进入App具体设置
     *
     * @param context 上下文
     */
    public static void getAppDetailsSetting(Context context) {
        getAppDetailsSetting(context, context.getPackageName());
    }

    /**
     * 进入App具体设置
     *
     * @param context     上下文
     * @param packageName 包名
     */
    public static void getAppDetailsSetting(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) return;
        context.startActivity(IntentUtil.getAppDetailsSettingIntent(packageName));
    }

    /**
     * 进入系统设置
     *
     * @param context     上下文
     * @param packageName 包名
     */
    public static void getSystemSetting(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) return;
        context.startActivity(IntentUtil.getAppDetailsSettingIntent(packageName));
    }
}
