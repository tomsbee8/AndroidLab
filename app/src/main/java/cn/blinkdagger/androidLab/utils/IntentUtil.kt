package cn.blinkdagger.androidLab.utils

import android.content.Intent
import android.net.Uri
import android.provider.Settings

/**
 * 类描述：系统Intent辅助类
 * 创建人：ls
 * 创建时间：2016/11/22
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
object IntentUtil {
    /**
     * 获取系统设置的意图
     *
     * @return intent
     */
    val systemSettingIntent: Intent
        get() {
            val intent = Intent(Settings.ACTION_SETTINGS)
            return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

    /**
     * 获取系统WLAN的意图
     *
     * @return intent
     */
    val systemWifiSettingIntent: Intent
        get() {
            val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
            return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

    /**
     * 获取App具体设置的意图
     *
     * @param packageName 包名
     * @return intent
     */
    fun getAppDetailsSettingIntent(packageName: String): Intent {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:$packageName")
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    /**
     * 获取系统辅助功能设置的意图
     *
     * @return intent
     */
    val accessabiliStyettingIntent: Intent
        get() {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
}