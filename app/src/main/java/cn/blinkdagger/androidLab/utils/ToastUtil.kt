package cn.blinkdagger.androidLab.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import cn.blinkdagger.androidLab.tools.ActivityManager.Companion.instance
import com.orhanobut.logger.Logger

/**
 * Toast 工具类
 */
object ToastUtil {
    private val sHandler = Handler(Looper.getMainLooper())
    private var sToast: Toast? = null
    /**
     * 显示短 Toast
     *
     * @param context 上下文
     * @param text    显示内容
     */
    fun showShortToast(context: Context?, text: CharSequence) {
        sHandler.post { showToast(context, text, Toast.LENGTH_SHORT) }
    }

    /**
     * 显示短 Toast
     *
     *
     * 适配 去除小米自动在Toast中添加应用名提示
     *
     * @param context 上下文
     * @param text    显示内容
     */
    fun showAdapterShortToast(context: Context?, text: CharSequence?) {
        sHandler.post {
            val mToast = Toast.makeText(context, null, Toast.LENGTH_SHORT)
            mToast.setText(text)
            mToast.show()
        }
    }

    /**
     * 显示短 Toast
     *
     * @param context 上下文
     * @param resId   显示内容 id
     */
    fun showShortToast(context: Context?, resId: Int) {
        sHandler.post { showToast(context, resId, Toast.LENGTH_SHORT) }
    }

    /**
     * 显示长 Toast
     *
     * @param context 上下文
     * @param text    显示内容
     */
    fun showLongToast(context: Context?, text: CharSequence) {
        sHandler.post { showToast(context, text, Toast.LENGTH_LONG) }
    }

    /**
     * 显示长 Toast
     *
     * @param context 上下文
     * @param resId   显示内容 id
     */
    fun showLongToast(context: Context?, resId: Int) {
        sHandler.post { showToast(context, resId, Toast.LENGTH_LONG) }
    }

    /**
     * 居中显示 Toast
     *
     * @param context 上下文
     * @param text    显示内容
     */
    fun showCenterToast(context: Context?, text: CharSequence?, duration: Int) {
        if (context != null && text != null) {
            val toast = Toast.makeText(context, text, duration)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
    }

    /**
     * 播放器全屏 收藏 Toast
     *
     * @param context
     * @param text
     */
    fun showFullScreenCenterToast(context: Context?, text: CharSequence?) {
        if (context != null && text != null) {
            val mToast = Toast.makeText(context, null, Toast.LENGTH_SHORT)
            mToast.setText(text)
            mToast.setGravity(Gravity.CENTER, 0, 0)
            mToast.show()
        }
    }

    /**
     * 显示避免重复 Toast
     *
     * @param context  上下文
     * @param text     显示内容
     * @param duration 显示时间
     */
    @SuppressLint("ShowToast")
    fun antiRepeatToast(context: Context?, text: CharSequence?, duration: Int) {
        if (sToast == null) {
            sToast = Toast.makeText(context, text, duration)
        } else {
            sToast!!.setText(text)
            sToast!!.duration = duration
        }
        sToast!!.show()
    }

    //    private static void showToast(Context context, CharSequence text, int duration) {
//        if (context != null && !TextUtils.isEmpty(text)) {
//            Toast.makeText(context, text, duration).show();
//        }
//    }
//
//    private static void showToast(Context context, int resId, int duration) {
//        if (context != null && resId != 0) {
//            Toast.makeText(context, resId, duration).show();
//        }
//    }
    private fun showToast(context: Context?, resId: Int, duration: Int) {
        if (context != null && resId != 0) {
            if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
                val toast = SystemToast(Toast.makeText(context, resId, duration))
                toast.show()
            } else {
                val toast = ToastWithoutNotification(Toast.makeText(context, resId, duration))
                toast.show()
            }
        }
    }

    private fun showToast(context: Context?, text: CharSequence, duration: Int) {
        if (context != null && !TextUtils.isEmpty(text)) {
            if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
                val toast = SystemToast(Toast.makeText(context, text, duration))
                toast.show()
            } else {
                val toast = ToastWithoutNotification(Toast.makeText(context, text, duration))
                toast.show()
            }
        }
    }

    /**
     * 系统级别toast
     */
    internal class SystemToast(private val mToast: Toast) {
        fun show() {
            mToast.show()
        }

        fun cancel() {
            mToast.cancel()
        }

        internal class SafeHandler(private val impl: Handler) : Handler() {
            override fun handleMessage(msg: Message) {
                impl.handleMessage(msg)
            }

            override fun dispatchMessage(msg: Message) {
                try {
                    impl.dispatchMessage(msg)
                } catch (e: Exception) {
                    Logger.e("ToastUtil", e.toString())
                }
            }

        }

        init {
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
                try {
                    val mTNField = Toast::class.java.getDeclaredField("mTN")
                    mTNField.isAccessible = true
                    val mTN = mTNField[mToast]
                    val mTNmHandlerField = mTNField.type.getDeclaredField("mHandler")
                    mTNmHandlerField.isAccessible = true
                    val tnHandler = mTNmHandlerField[mTN] as Handler
                    mTNmHandlerField[mTN] = SafeHandler(tnHandler)
                } catch (ignored: Exception) { /**/
                }
            }
        }
    }

    /**
     * 当通知权限关闭时候，使用的Toast
     * 适配部分机型在通知关闭的时候无法弹出toast
     */
    internal class ToastWithoutNotification(private var mToast: Toast?) {
        private var mView: View? = null
        private var mWM: WindowManager? = null
        private val mParams = WindowManager.LayoutParams()
        fun show() {
            mView = mToast!!.view
            if (mView == null) return
            val context = mToast!!.view.context
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1) {
                mWM = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                mParams.type = WindowManager.LayoutParams.TYPE_TOAST
            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
                val currentActivity: Activity?
                currentActivity = if (context is Activity) {
                    context
                } else {
                    instance!!.topActivity
                }
                if (currentActivity == null) {
                    Logger.e("ToastUtils", "Couldn't get top Activity.")
                    return
                }
                if (currentActivity.isFinishing || currentActivity.isDestroyed) {
                    Logger.e("ToastUtils", "$currentActivity is useless")
                    return
                }
                mWM = currentActivity.windowManager
                mParams.type = WindowManager.LayoutParams.LAST_APPLICATION_WINDOW
            } else {
                mToast!!.show()
                return
            }
            val config = context.resources.configuration
            val gravity = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) Gravity.getAbsoluteGravity(mToast!!.gravity, config.layoutDirection) else mToast!!.gravity
            mParams.y = mToast!!.yOffset
            mParams.height = WindowManager.LayoutParams.WRAP_CONTENT
            mParams.width = WindowManager.LayoutParams.WRAP_CONTENT
            mParams.format = PixelFormat.TRANSLUCENT
            mParams.windowAnimations = android.R.style.Animation_Toast
            mParams.title = "ToastWithoutNotification"
            mParams.flags = (WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            mParams.gravity = gravity
            if (gravity and Gravity.HORIZONTAL_GRAVITY_MASK == Gravity.FILL_HORIZONTAL) {
                mParams.horizontalWeight = 1.0f
            }
            if (gravity and Gravity.VERTICAL_GRAVITY_MASK == Gravity.FILL_VERTICAL) {
                mParams.verticalWeight = 1.0f
            }
            mParams.x = mToast!!.xOffset
            mParams.packageName = context.packageName
            try {
                if (mWM != null) {
                    mWM!!.addView(mView, mParams)
                }
            } catch (ignored: Exception) { /**/
            }
            sHandler.postDelayed({ cancel() }, if (mToast!!.duration == Toast.LENGTH_SHORT) 2000 else 3500.toLong())
        }

        fun cancel() {
            try {
                if (mWM != null) {
                    mWM!!.removeViewImmediate(mView)
                }
            } catch (ignored: Exception) { /**/
            }
            mView = null
            mWM = null
            mToast = null
        }

    }
}