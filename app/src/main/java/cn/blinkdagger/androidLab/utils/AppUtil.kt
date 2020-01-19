package cn.blinkdagger.androidLab.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.text.TextUtils

/**
 * Created by d on 2016/11/16.
 * APP信息辅助类
 */
object AppUtil {
    /**
     * 获取包名
     *
     * @param context 上下文
     * @return 本APP的包名
     */
    fun getPackageName(context: Context): String {
        return context.packageName
    }

    /**
     * 获取App名称
     *
     * @param context 上下文
     * @return 本App名称
     */
    fun getAppName(context: Context): String? {
        return getAppName(context, context.packageName)
    }

    /**
     * 获取App名称
     *
     * @param context     上下文
     * @param packageName 包名
     * @return App名称
     */
    fun getAppName(context: Context, packageName: String?): String? {
        return if (TextUtils.isEmpty(packageName)) null else try {
            val pm = context.packageManager
            val pi = pm.getPackageInfo(packageName, 0)
            pi?.applicationInfo?.loadLabel(pm)?.toString()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 获取版本号
     *
     * @param context 上下文
     * @return 本APP的版本号
     */
    fun getVersionCode(context: Context): Int {
        return getVersionCode(context, context.packageName)
    }

    /**
     * 获取App版本号
     *
     * @param context     上下文
     * @param packageName 包名
     * @return App版本号
     */
    fun getVersionCode(context: Context, packageName: String?): Int {
        return if (TextUtils.isEmpty(packageName)) -1 else try {
            val pm = context.packageManager
            val pi = pm.getPackageInfo(packageName, 0)
            pi?.versionCode ?: -1
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            -1
        }
    }

    /**
     * 获取版本名称
     *
     * @param context 上下文
     * @return 本APP的版本名称
     */
    fun getVersionName(context: Context): String? {
        return getVersionName(context, context.packageName)
    }

    /**
     * 获取App版本名称
     *
     * @param context     上下文
     * @param packageName 包名
     * @return App版本名称
     */
    fun getVersionName(context: Context, packageName: String?): String? {
        return if (TextUtils.isEmpty(packageName)) null else try {
            val pm = context.packageManager
            val pi = pm.getPackageInfo(packageName, 0)
            pi?.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 判断App是否是Debug版本
     * （注意混淆的时候排除相应的BuildConfig类 ）
     *
     * @param packageName 包名
     * @return boolean
     */
    fun isAppDebug(packageName: String): Boolean {
        return if (TextUtils.isEmpty(packageName)) false else try {
            val clazz = Class.forName("$packageName.BuildConfig")
            val isDebug = clazz.getField("DEBUG").getBoolean(clazz)
            isDebug ?: false
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            false
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
            false
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 进入App具体设置
     *
     * @param context 上下文
     */
    fun getAppDetailsSetting(context: Context) {
        getAppDetailsSetting(context, context.packageName)
    }

    /**
     * 进入App具体设置
     *
     * @param context     上下文
     * @param packageName 包名
     */
    fun getAppDetailsSetting(context: Context, packageName: String) {
        if (TextUtils.isEmpty(packageName)) return
        context.startActivity(IntentUtil.getAppDetailsSettingIntent(packageName))
    }

    /**
     * 进入系统设置
     *
     * @param context     上下文
     * @param packageName 包名
     */
    fun getSystemSetting(context: Context, packageName: String) {
        if (TextUtils.isEmpty(packageName)) return
        context.startActivity(IntentUtil.getAppDetailsSettingIntent(packageName))
    }

    /**
     * 　获取渠道号
     *
     * @return
     */
    fun getChannelName(context: Context, channelMetaNameKey: String?): String? {
        var channelName: String? = null
        val appInfo: ApplicationInfo
        try {
            appInfo = context.packageManager.getApplicationInfo(getPackageName(context), PackageManager.GET_META_DATA)
            channelName = appInfo.metaData.getString(channelMetaNameKey)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return channelName
    }
}