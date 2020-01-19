package cn.blinkdagger.androidLab.utils

import android.app.Activity
import android.util.DisplayMetrics

/**
 * @Author ls
 * @Date 2018/11/9
 * @Description
 * @Version
 */
object ScreenUtil {
    /**
     * 获取屏幕可操作区域宽度
     *
     * @param activity
     * @return 屏幕可操作区域宽度
     */
    fun getScreenWidth(activity: Activity): Int {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }

    /**
     * 获取屏幕可操作区域高度
     *
     * @param activity
     * @return 屏幕可操作区域高度
     */
    fun getScreenHeight(activity: Activity): Int {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }
}