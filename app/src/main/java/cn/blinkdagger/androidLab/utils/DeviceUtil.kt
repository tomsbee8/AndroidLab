package cn.blinkdagger.androidLab.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Environment
import android.telephony.TelephonyManager
import android.text.TextUtils

/**
 * Created by d on 2016/11/16.
 */
object DeviceUtil {
    /**
     * 获取手机IMEI码
     * @return
     */
    @SuppressLint("MissingPermission")
    fun getPhoneIMEI(context: Context): String {
        val mTm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return mTm.deviceId
    }

    /**
     * 获取手机型号
     */
    val phoneModel: String
        get() = Build.MODEL

    /**
     * 获取手机厂商
     */
    val phoneManufacturer: String
        get() = Build.MANUFACTURER

    /**
     * 获取当前手机系统API的版本号
     * @return
     */
    val androidVersionCode: Int
        get() = Build.VERSION.SDK_INT

    /**
     * 获取当前手机系统API的版本名称
     * @return
     */
    val androidVersionName: String
        get() = Build.VERSION.SDK

    /**；
     * 是否安装包名为packageName的App
     */
    fun getPackageName(context: Context, packageName: String?): Boolean {
        if (TextUtils.isEmpty(packageName)) {
            return false
        }
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        return intent != null
    }

    /**
     * 检测SD卡状态
     */
    val isSDPresent: Boolean
        get() = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
}