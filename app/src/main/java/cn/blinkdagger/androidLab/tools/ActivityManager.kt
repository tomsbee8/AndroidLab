package cn.blinkdagger.androidLab.tools

import android.app.Activity
import java.util.*

/**
 * @Author ls
 * @Date 2016/11/28
 * @Description Android Activity栈管理
 * @Version
 */
class ActivityManager {
    //运用list来保存们每一个activity是关键
    private val mAddActivityList: MutableList<Activity> = ArrayList()

    //为了实现每次使用该类时不创建新的对象而创建的静态对象
    fun addActivity(activity: Activity) {
        mAddActivityList.add(activity)
    }

    fun removeActivityList(activity: Activity?) {
        if (mAddActivityList.contains(activity)) {
            mAddActivityList.remove(activity)
        }
    }

    /**
     * 关闭每一个list内的activity
     * @param isSwitch 是否是环境切换
     */
    fun exit(isSwitch: Boolean) {
        try {
            for (activity in mAddActivityList) {
                activity?.finish()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally { // 视频上传service需要后台运行，进程不能被杀死
            if (isSwitch) {
                System.exit(0)
            }
        }
    }

    val topActivity: Activity?
        get() {
            if (!mAddActivityList.isEmpty()) {
                val topActivity = mAddActivityList[mAddActivityList.size - 1]
                if (topActivity != null) {
                    return topActivity
                }
            }
            return null
        }

    companion object {
        private var mInstance: ActivityManager? = ActivityManager()
        @JvmStatic
        val instance: ActivityManager?
            get() {
                if (mInstance == null) {
                    mInstance = ActivityManager()
                }
                return mInstance
            }
    }
}