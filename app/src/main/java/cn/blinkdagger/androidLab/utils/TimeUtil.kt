package cn.blinkdagger.androidLab.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by d on 2016/11/16.
 */
object TimeUtil {
    val DEFAULT_SDF = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    /**
     * 获取当前时间的默认的格式化字符串
     */
    val cuerentTimeString: String
        get() = DEFAULT_SDF.format(Date(System.currentTimeMillis()))

    /**
     * 获取当前时间的默认的格式化字符串
     */
    fun getCurrentTimeStringInFormat(milliseconds: Long): String {
        return DEFAULT_SDF.format(Date(milliseconds))
    }

    /**
     * 获取时间的对应格式化字符串
     */
    fun getTimeStringInFormat(milliseconds: Long, format: SimpleDateFormat): String {
        return format.format(Date(milliseconds))
    }
}