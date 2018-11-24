package cn.blinkdagger.androidLab.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import cn.blinkdagger.androidLab.R;

/**
 * 类描述：系统bar相关
 * 创建人：ls
 * 创建时间：2018/2/11
 * 修改人：
 * 修改时间：
 * 修改备注：
 */

public class SystemBarUtil {

    private static final int TAG_KEY_HAVE_SET_OFFSET = -123;
    private static final int FAKE_TRANSLUCENT_VIEW_ID = R.id.statusbarutil_translucent_view;

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
     * @param useLightStatusBar 是否启用LightStatusBar【状态栏字体动态变色】
     * @return
     */
    public static void setTranslucentStatusBar(@NonNull Activity activity, boolean useLightStatusBar) {
        setStatusBarColor(activity, Color.TRANSPARENT, useLightStatusBar);
    }

    /**
     * 设置状态栏全透明
     *
     * @param activity 需要设置的activity
     */
    public static void setTransparent(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        transparentStatusBar(activity);
        setRootView(activity);
    }


    /**
     * 为头部是 ImageView 的界面设置状态栏全透明
     *
     * @param activity       需要设置的activity
     * @param needOffsetView 需要向下偏移的 View
     */
    public static void setTransparentForImageView(Activity activity, View needOffsetView) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        setTransparentForWindow(activity);
        addTranslucentView(activity, 0);
        if (needOffsetView != null) {
            Object haveSetOffset = needOffsetView.getTag(TAG_KEY_HAVE_SET_OFFSET);
            if (haveSetOffset != null && (Boolean) haveSetOffset) {
                return;
            }
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) needOffsetView.getLayoutParams();
            layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin + getStatusBarHeight(activity),
                    layoutParams.rightMargin, layoutParams.bottomMargin);
            needOffsetView.setTag(TAG_KEY_HAVE_SET_OFFSET, true);
        }
    }

    /**
     * 设置透明
     */
    private static void setTransparentForWindow(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
            activity.getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow()
                    .setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 使状态栏透明
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void transparentStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }


    /**
     * 设置根布局参数
     */
    private static void setRootView(Activity activity) {
        ViewGroup parent = (ViewGroup) activity.findViewById(android.R.id.content);
        for (int i = 0, count = parent.getChildCount(); i < count; i++) {
            View childView = parent.getChildAt(i);
            if (childView instanceof ViewGroup) {
                childView.setFitsSystemWindows(true);
                ((ViewGroup) childView).setClipToPadding(true);
            }
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
        if (parentView != null && parentView instanceof DrawerLayout) {
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
        setLightStatusBar(activity, useLightStatusBar, color);
    }

    /**
     * 添加半透明矩形条
     *
     * @param activity       需要设置的 activity
     * @param statusBarAlpha 透明值
     */
    private static void addTranslucentView(Activity activity, @IntRange(from = 0, to = 255) int statusBarAlpha) {
        ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
        View fakeTranslucentView = contentView.findViewById(FAKE_TRANSLUCENT_VIEW_ID);
        if (fakeTranslucentView != null) {
            if (fakeTranslucentView.getVisibility() == View.GONE) {
                fakeTranslucentView.setVisibility(View.VISIBLE);
            }
            fakeTranslucentView.setBackgroundColor(Color.argb(statusBarAlpha, 0, 0, 0));
        } else {
            contentView.addView(createTranslucentStatusBarView(activity, statusBarAlpha));
        }
    }
    /**
     * 创建半透明矩形 View
     *
     * @param alpha 透明值
     * @return 半透明 View
     */
    private static View createTranslucentStatusBarView(Activity activity, int alpha) {
        // 绘制一个和状态栏一样高的矩形
        View statusBarView = new View(activity);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
        statusBarView.setId(FAKE_TRANSLUCENT_VIEW_ID);
        return statusBarView;
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
        setStatusBarColor(v4Fragment.getActivity(), color, useLightStatusBar);
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
