package cn.blinkdagger.androidLab.entity

/**
 * @Author ls
 * @Date  2020/12/14
 * @Description
 * @Version
 */
data class BaseResponse<out T>(
        val success: Boolean,
        val errmsg: String? = null ,
        val data: T? = null )