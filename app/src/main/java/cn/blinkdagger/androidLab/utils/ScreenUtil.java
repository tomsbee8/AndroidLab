package cn.blinkdagger.androidLab.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * @Author ls
 * @Date 2018/11/9
 * @Description
 * @Version
 */
public class ScreenUtil {

    /**
     * 获取屏幕可操作区域宽度
     *
     * @param activity
     * @return 屏幕可操作区域宽度
     */
    public static int getScreenWidth(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    /**
     * 获取屏幕可操作区域高度
     *
     * @param activity
     * @return 屏幕可操作区域高度
     */
    public static int getScreenHeight(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

}
