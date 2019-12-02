package cn.blinkdagger.androidLab.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.drawerlayout.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 类描述：系统bar相关
 * 创建人：ls
 * 创建时间：2018/2/11
 * 修改人：
 * 修改时间：
 * 修改备注：
 */

public class SystemBarUtil {

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取虚拟导航栏高度
     *
     * @param context
     * @return
     */
    private int getNavigationHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    /**
     * 设置Immersive式
     *
     * @param mActivity
     */

    public static void setImmersiveMode(Activity mActivity) {
        View decorView = mActivity.getWindow().getDecorView();
        int uiOptions = decorView.getSystemUiVisibility();
        uiOptions &= ~View.SYSTEM_UI_FLAG_LOW_PROFILE;
        uiOptions |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        uiOptions |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        uiOptions |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        uiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE;
        uiOptions &= ~View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }


    /**
     * 设置Leanback式
     *
     * @param mActivity
     */
    public static void setLeanbackMode(Activity mActivity) {
        View decorView = mActivity.getWindow().getDecorView();
        int uiOptions = decorView.getSystemUiVisibility();
        uiOptions &= ~View.SYSTEM_UI_FLAG_LOW_PROFILE;
        uiOptions |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        uiOptions |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        uiOptions &= ~View.SYSTEM_UI_FLAG_IMMERSIVE;
        uiOptions &= ~View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }


    /**
     * 设置透明状态栏
     *
     * @param activity          当前activity
     * @return
     */
    @TargetApi(19)
    public static void setTransparentStatusBar(@NonNull Activity activity) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window =activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 设置状态栏的颜色
     *
     * @param activity          当前activity
     * @param color             颜色
     * @param useLightStatusBar 是否启用LightStatusBar【状态栏字体动态变色】
     * @return
     */
    public static void setStatusBarColor(@NonNull Activity activity, @ColorInt int color, boolean useLightStatusBar) {
        Context c =activity.getBaseContext();
        int uiOptions = activity.getWindow().getDecorView().getSystemUiVisibility();
        uiOptions &= ~View.SYSTEM_UI_FLAG_LOW_PROFILE;
        uiOptions |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        uiOptions &= ~View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        uiOptions |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        uiOptions &= ~View.SYSTEM_UI_FLAG_IMMERSIVE;
        uiOptions &= ~View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        activity.getWindow().getDecorView().setSystemUiVisibility(uiOptions);

        final ViewGroup contentView = activity.findViewById(android.R.id.content);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !RomUtil.isEMUI()) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().setStatusBarColor(color);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            if (color != Color.TRANSPARENT) {
                View statusBarView = new View(activity);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        SystemBarUtil.getStatusBarHeight(activity));
                statusBarView.setBackgroundColor(color);
                contentView.addView(statusBarView, lp);
            }
        }

        // 兼容DrawerLayout
        final View parentView = contentView.getChildAt(0);
        if ( parentView instanceof DrawerLayout) {
            ((ViewGroup) (activity.getWindow().getDecorView())).addView(createStatusBarView(activity, color));
            DrawerLayout drawerLayout = (DrawerLayout) parentView;
            drawerLayout.setFitsSystemWindows(false);
            drawerLayout.setClipToPadding(false);
            View drawerContentView = drawerLayout.getChildAt(0);
            if (drawerContentView != null) {
                drawerContentView.setFitsSystemWindows(true);
            }
        } else {
            setLayoutFitsSystemWindows(activity);
        }

        // 设状态栏字体颜色
        setLightStatusBar(activity,useLightStatusBar,color);
    }

    /**
     * 设置状态栏的颜色
     *
     * @param v4Fragment        当前Fragment
     * @param color             颜色
     * @param useLightStatusBar 是否启用LightStatusBar【状态栏字体动态变色】
     * @return
     */
    public static void setStatusBarColor(@NonNull Fragment v4Fragment, @ColorInt int color, boolean useLightStatusBar) {
        setStatusBarColor(v4Fragment.getActivity(),color,useLightStatusBar);
    }


    /**
     * 设置FitsSystemWindows
     *
     * @param activity 当前activity
     */
    private static void setLayoutFitsSystemWindows(Activity activity) {
        final ViewGroup contentView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        final View parentView = contentView.getChildAt(0);
        if (parentView != null && Build.VERSION.SDK_INT >= 14) {
            parentView.setFitsSystemWindows(true);
        }
    }

    /**
     * 设置动态变色的状态栏
     *
     * @param activity          当前activity
     * @param useLightStatusBar 是否启用Light
     * @param color             状态栏颜色
     */
    private static void setLightStatusBar(@NonNull Activity activity, boolean useLightStatusBar, @ColorInt int color) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int uiOptions = activity.getWindow().getDecorView().getSystemUiVisibility();
            if (useLightStatusBar) {
                uiOptions |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                uiOptions &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            activity.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
        } else {
            if (useLightStatusBar) {
                boolean isLightColor = StatusbarColorUtils.isLightColor(color);
                if (RomUtil.isMIUI()) {
                    setMIUIStatusBarDarkMode(activity, isLightColor);
                } else if (RomUtil.isFlyme()) {
                    setFlymeStatusBarDarkMode(activity, isLightColor);
                }
            }
        }
    }

    /**
     * 在旧的MIUI版本(Android 6.0以下)设置状态栏字体颜色
     *
     * @param activity 当前activity
     * @param darkmode 是否为黑色字体图标
     */
    private static void setMIUIStatusBarDarkMode(@NonNull Activity activity, boolean darkmode) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Flyme设置状态栏字体颜色
     *
     * @param activity 当前activity
     * @param darkmode 是否为深色字体图标
     */
    private static void setFlymeStatusBarDarkMode(@NonNull Activity activity, boolean darkmode) {
        StatusbarColorUtils.setStatusBarDarkIcon(activity, darkmode);
    }


    /**
     * 生成状态栏高度的View
     *
     * @param context
     * @param color
     * @return
     */
    private static View createStatusBarView(Context context, @ColorInt int color) {
        View statusBarView = new View(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams
                (FrameLayout.LayoutParams.MATCH_PARENT, getStatusBarHeight(context));
        params.gravity = Gravity.TOP;
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(color);
        return statusBarView;
    }
}
