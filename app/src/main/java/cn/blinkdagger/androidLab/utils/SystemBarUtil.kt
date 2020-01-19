package cn.blinkdagger.androidLab.utils

import android.R
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.*
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment

/**
 * 类描述：系统bar相关
 * 创建人：ls
 * 创建时间：2018/2/11
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
class SystemBarUtil {
    /**
     * 获取虚拟导航栏高度
     *
     * @param context
     * @return
     */
    private fun getNavigationHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    companion object {
        /**
         * 获取状态栏高度
         *
         * @param context
         * @return
         */
        @JvmStatic
        fun getStatusBarHeight(context: Context): Int {
            var result = 0
            val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = context.resources.getDimensionPixelSize(resourceId)
            }
            return result
        }

        /**
         * 设置Immersive式
         *
         * @param mActivity
         */
        @JvmStatic
        fun setImmersiveMode(mActivity: Activity) {
            val decorView = mActivity.window.decorView
            var uiOptions = decorView.systemUiVisibility
            uiOptions = uiOptions and View.SYSTEM_UI_FLAG_LOW_PROFILE.inv()
            uiOptions = uiOptions or View.SYSTEM_UI_FLAG_FULLSCREEN
            uiOptions = uiOptions or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            uiOptions = uiOptions or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            uiOptions = uiOptions or View.SYSTEM_UI_FLAG_IMMERSIVE
            uiOptions = uiOptions and View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY.inv()
            decorView.systemUiVisibility = uiOptions
        }

        /**
         * 设置Leanback式
         *
         * @param mActivity
         */
        fun setLeanbackMode(mActivity: Activity) {
            val decorView = mActivity.window.decorView
            var uiOptions = decorView.systemUiVisibility
            uiOptions = uiOptions and View.SYSTEM_UI_FLAG_LOW_PROFILE.inv()
            uiOptions = uiOptions or View.SYSTEM_UI_FLAG_FULLSCREEN
            uiOptions = uiOptions or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            uiOptions = uiOptions and View.SYSTEM_UI_FLAG_IMMERSIVE.inv()
            uiOptions = uiOptions and View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY.inv()
            decorView.systemUiVisibility = uiOptions
        }

        /**
         * 设置透明状态栏
         *
         * @param activity          当前activity
         * @return
         */
        @TargetApi(19)
        fun setTransparentStatusBar(activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val window = activity.window
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = Color.TRANSPARENT
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                val window = activity.window
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
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
        fun setStatusBarColor(activity: Activity, @ColorInt color: Int, useLightStatusBar: Boolean) {
            val c = activity.baseContext
            var uiOptions = activity.window.decorView.systemUiVisibility
            uiOptions = uiOptions and View.SYSTEM_UI_FLAG_LOW_PROFILE.inv()
            uiOptions = uiOptions or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            uiOptions = uiOptions and View.SYSTEM_UI_FLAG_HIDE_NAVIGATION.inv()
            uiOptions = uiOptions or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            uiOptions = uiOptions and View.SYSTEM_UI_FLAG_IMMERSIVE.inv()
            uiOptions = uiOptions and View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY.inv()
            activity.window.decorView.systemUiVisibility = uiOptions
            val contentView = activity.findViewById<ViewGroup>(R.id.content)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !RomUtil.isEMUI) {
                activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                activity.window.statusBarColor = color
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                if (color != Color.TRANSPARENT) {
                    val statusBarView = View(activity)
                    val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            getStatusBarHeight(activity))
                    statusBarView.setBackgroundColor(color)
                    contentView.addView(statusBarView, lp)
                }
            }
            // 兼容DrawerLayout
            val parentView = contentView.getChildAt(0)
            if (parentView is DrawerLayout) {
                (activity.window.decorView as ViewGroup).addView(createStatusBarView(activity, color))
                val drawerLayout = parentView
                drawerLayout.fitsSystemWindows = false
                drawerLayout.clipToPadding = false
                val drawerContentView = drawerLayout.getChildAt(0)
                if (drawerContentView != null) {
                    drawerContentView.fitsSystemWindows = true
                }
            } else {
                setLayoutFitsSystemWindows(activity)
            }
            // 设状态栏字体颜色
            setLightStatusBar(activity, useLightStatusBar, color)
        }

        /**
         * 设置状态栏的颜色
         *
         * @param v4Fragment        当前Fragment
         * @param color             颜色
         * @param useLightStatusBar 是否启用LightStatusBar【状态栏字体动态变色】
         * @return
         */
        fun setStatusBarColor(v4Fragment: Fragment, @ColorInt color: Int, useLightStatusBar: Boolean) {
            setStatusBarColor(v4Fragment.activity!!, color, useLightStatusBar)
        }

        /**
         * 设置FitsSystemWindows
         *
         * @param activity 当前activity
         */
        private fun setLayoutFitsSystemWindows(activity: Activity) {
            val contentView = activity.window.decorView.findViewById<ViewGroup>(R.id.content)
            val parentView = contentView.getChildAt(0)
            if (parentView != null && Build.VERSION.SDK_INT >= 14) {
                parentView.fitsSystemWindows = true
            }
        }

        /**
         * 设置动态变色的状态栏
         *
         * @param activity          当前activity
         * @param useLightStatusBar 是否启用Light
         * @param color             状态栏颜色
         */
        private fun setLightStatusBar(activity: Activity, useLightStatusBar: Boolean, @ColorInt color: Int) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                var uiOptions = activity.window.decorView.systemUiVisibility
                uiOptions = if (useLightStatusBar) {
                    uiOptions or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                } else {
                    uiOptions and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                }
                activity.window.decorView.systemUiVisibility = uiOptions
            } else {
                if (useLightStatusBar) {
                    val isLightColor = StatusbarColorUtils.isLightColor(color)
                    if (RomUtil.isMIUI) {
                        setMIUIStatusBarDarkMode(activity, isLightColor)
                    } else if (RomUtil.isFlyme) {
                        setFlymeStatusBarDarkMode(activity, isLightColor)
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
        private fun setMIUIStatusBarDarkMode(activity: Activity, darkmode: Boolean) {
            val clazz: Class<out Window> = activity.window.javaClass
            try {
                var darkModeFlag = 0
                val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
                val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
                darkModeFlag = field.getInt(layoutParams)
                val extraFlagField = clazz.getMethod("setExtraFlags", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
                extraFlagField.invoke(activity.window, if (darkmode) darkModeFlag else 0, darkModeFlag)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        /**
         * Flyme设置状态栏字体颜色
         *
         * @param activity 当前activity
         * @param darkmode 是否为深色字体图标
         */
        private fun setFlymeStatusBarDarkMode(activity: Activity, darkmode: Boolean) {
            StatusbarColorUtils.setStatusBarDarkIcon(activity, darkmode)
        }

        /**
         * 生成状态栏高度的View
         *
         * @param context
         * @param color
         * @return
         */
        private fun createStatusBarView(context: Context, @ColorInt color: Int): View {
            val statusBarView = View(context)
            val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, getStatusBarHeight(context))
            params.gravity = Gravity.TOP
            statusBarView.layoutParams = params
            statusBarView.setBackgroundColor(color)
            return statusBarView
        }
    }
}