package cn.blinkdagger.androidLab.utils

import android.app.ActivityManager
import android.content.Context
import android.os.Looper
import android.os.Process
import android.text.TextUtils

/**
 * Created by d on 2016/11/16.
 */
object ProcessUtil {
    /**
     * 获取当前进程的名称
     * @param appContext 上下文
     * @return 当前进程名称
     */
    fun getProcessName(appContext: Context): String {
        var currentProcessName = ""
        val pid = Process.myPid()
        val manager = appContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (processInfo in manager.runningAppProcesses) {
            if (processInfo.pid == pid) {
                currentProcessName = processInfo.processName
                break
            }
        }
        return currentProcessName
    }

    /**
     * 判断是否处于主进程
     * @param context 上下文
     * @return boolean
     */
    fun isOnMainProcess(context: Context): Boolean {
        return TextUtils.equals(context.packageName, getProcessName(context))
    }

    /**
     * 判断是否处于主线程（UI线程）
     * @return boolean
     */
    val isOnMainThread: Boolean
        get() = Looper.myLooper() == Looper.getMainLooper()
}