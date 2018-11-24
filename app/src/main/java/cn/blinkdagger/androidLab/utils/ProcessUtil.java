package cn.blinkdagger.androidLab.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;

/**
 * Created by d on 2016/11/16.
 */

public class ProcessUtil {

    /**
     * 获取当前进程的名称
     * @param appContext 上下文
     * @return 当前进程名称
     */
    public static String getProcessName(Context appContext) {
        String currentProcessName = "";
        int pid = android.os.Process.myPid();
        ActivityManager manager = (ActivityManager) appContext.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == pid) {
                currentProcessName = processInfo.processName;
                break;
            }
        }
        return currentProcessName;
    }

    /**
     * 判断是否处于主进程
     * @param context 上下文
     * @return boolean
     */
    public static boolean isOnMainProcess(Context context){
        return TextUtils.equals(context.getPackageName(),getProcessName(context));
    }
    /**
     * 判断是否处于主线程（UI线程）
     * @return boolean
     */
    public static boolean isOnMainThread(){
        return Looper.myLooper() == Looper.getMainLooper();
    }

}
