package cn.blinkdagger.androidLab.utils

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.app.Dialog
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager

/**
 * @Author ls
 * @Date  2020-01-17
 * @Description
 * @Version
 */
class KeyboardUtil {

    companion object {

        // 软键盘高度与屏幕高度百分比
        const val KEYBOARD_MIN_HEIGHT_RATIO = 0.15f

        /**
         *  设置软键盘弹出和收起的监听
         */
        @JvmStatic
        fun setVisibleChangeListener(targetActivity: Activity, listener: KeyboardVisibleChangeListener) {

            val softInputAdjust = targetActivity.window.attributes.softInputMode and WindowManager.LayoutParams.SOFT_INPUT_MASK_ADJUST

            // 如果当前InputMode 为SOFT_INPUT_ADJUST_NOTHING,此时窗口高度不会变化，无需监听
            val isAdjustNothing = softInputAdjust and WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING == WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING

            if (isAdjustNothing) {
                return
            }

            val activityRoot = (targetActivity.findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0) ?: return

            val layoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {

                private val r = Rect()

                private var wasOpened = false

                override fun onGlobalLayout() {
                    activityRoot.getWindowVisibleDisplayFrame(r)

                    val screenHeight = activityRoot.rootView.height
                    val heightDiff = screenHeight - r.height()

                    val isVisible = heightDiff > screenHeight * KEYBOARD_MIN_HEIGHT_RATIO

                    if (isVisible == wasOpened) {
                        return
                    }

                    wasOpened = isVisible

                    listener.onVisibilityChanged(isVisible)
                }
            }
            activityRoot.viewTreeObserver.addOnGlobalLayoutListener(layoutListener)

            targetActivity.application.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
                override fun onActivityPaused(activity: Activity?) {}

                override fun onActivityResumed(activity: Activity?) {}

                override fun onActivityStarted(activity: Activity?) {}

                override fun onActivityDestroyed(activity: Activity?) {
                    activity?.let {
                        if (it == targetActivity) {
                            activityRoot.viewTreeObserver.removeOnGlobalLayoutListener(layoutListener)
                            targetActivity.application.unregisterActivityLifecycleCallbacks(this)
                        }
                    }
                }

                override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {}

                override fun onActivityStopped(activity: Activity?) {}

                override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {}
            })
        }

        /**
         * 判断当前是否正显示软键盘
         *
         * @param activity Activity
         */
        fun isKeyboardVisible(activity: Activity): Boolean {
            val r = Rect()

            val activityRoot = (activity.findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0)

            activityRoot.getWindowVisibleDisplayFrame(r)

            val screenHeight = activityRoot.rootView.height
            val heightDiff = screenHeight - r.height()

            return heightDiff > screenHeight * KEYBOARD_MIN_HEIGHT_RATIO
        }


        /**
         * 显示键盘
         *
         * @param view
         */
        @JvmStatic
        fun showKeyboard(context: Context?, view: View?) {
            if (context != null && view != null) {
                view.isFocusable = true
                view.isFocusableInTouchMode = true
                view.requestFocus()
                (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(view, 0)
            }
        }

        /**
         * 隐藏键盘
         */
        @JvmStatic
        fun hideKeyboard(activity: Activity?) {
            if (activity != null) {
                var view = activity.currentFocus
                if (view == null) {
                    view = View(activity)
                }
                (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(view.applicationWindowToken, 0)
            }
        }

        /**
         * dialog中隐藏键盘
         * @param dialog
         */
        @JvmStatic
        fun hideDialogKeyboard(dialog: Dialog?) {
            if (dialog != null) {
                var view = dialog.currentFocus
                if (view == null) {
                    view = View(dialog.context)
                }
                (dialog.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(view.applicationWindowToken, 0)
            }
        }
    }

    interface KeyboardVisibleChangeListener {
        fun onVisibilityChanged(visible: Boolean)
    }
}