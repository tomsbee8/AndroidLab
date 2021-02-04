package cn.blinkdagger.androidLab.widget

import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.annotation.AnimatorRes
import androidx.annotation.FloatRange
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes

/**
 * 类描述：通用PopupWindow
 * 创建人：ls
 * 创建时间：2017/12/7
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
class CustomPopupWindow private constructor(contentView: View, width: Int, height: Int) : PopupWindow(contentView, width, height) {
    private var activity: Activity? = null
    private var onShowBgAlpha  = 0f //显示的时候背景的透明度
    private var onShowChangeBg  = false //显示的时候背景透明度是否变化
    private var onDismissChangeBg = false //消失的时候背景透明度是否变化

    fun setActivity(activity: Activity?) {
        this.activity = activity
    }

    fun setOnShowBgAlpha(@FloatRange(from = 0.0, to = 1.0) onShowBgAlpha: Float) {
        this.onShowBgAlpha = onShowBgAlpha
    }

    fun setOnShowChangeBg(onShowChangeBg: Boolean) {
        this.onShowChangeBg = onShowChangeBg
    }

    fun setOnDismissChangeBg(onDismissChangeBg: Boolean) {
        this.onDismissChangeBg = onDismissChangeBg
    }

    fun showChangeBg() {
        if (onShowChangeBg) {
            activity?.let {
                setBackgroundAlpha(it, onShowBgAlpha)
            }
        }
    }

    override fun showAtLocation(parent: View, gravity: Int, x: Int, y: Int) {
        showChangeBg()
        super.showAtLocation(parent, gravity, x, y)
    }

    override fun showAsDropDown(anchor: View, xoff: Int, yoff: Int, gravity: Int) {
        super.showAsDropDown(anchor, xoff, yoff, gravity)
        showChangeBg()
    }

    override fun dismiss() {
        if (onDismissChangeBg) {
            activity?.let {
                setBackgroundAlpha(it, 1.0f)
            }
        }
        super.dismiss()
    }

    class Builder(private val activity: Activity) {
        private var contentView // 内容视图
                : View? = null
        private var width = ViewGroup.LayoutParams.WRAP_CONTENT //宽度
        private var height = ViewGroup.LayoutParams.WRAP_CONTENT //高度
        @StyleRes
        private var animationStyle  = 0 // 动画
        private var focusable = false //是否可以获取焦点
        private var outsideTouchable  = false //外部是否可以点击
        private var onShowChangeBg = true //显示的时候背景透明度是否变透明【默认变化】
        private var onDismissChangeBg = true //消失的时候背景透明度是否还原【默认还原】
        private var onShowBgAlpha = 0.5f //显示的时候背景的透明度
        private var onKeyBackDissmiss = false //是否拦截返回键消失【默认不拦截】
        private var onDismissListener // 消失监听事件
                : OnDismissListener? = null

        fun setContentView(contentView: View?): Builder {
            this.contentView = contentView
            return this
        }

        fun setContentView(@LayoutRes contentViewId: Int): Builder {
            contentView = activity.layoutInflater.inflate(contentViewId, null)
            return this
        }

        fun setContentView(contentView: View?, width: Int, height: Int): Builder {
            this.width = width
            this.height = height
            return this
        }

        fun setContentView(@LayoutRes contentViewId: Int, width: Int, height: Int): Builder {
            contentView = activity.layoutInflater.inflate(contentViewId, null)
            this.width = width
            this.height = height
            return this
        }

        fun setAnimationStyle(@AnimatorRes animationStyle: Int): Builder {
            this.animationStyle = animationStyle
            return this
        }

        fun setFocusable(focusable: Boolean): Builder {
            this.focusable = focusable
            return this
        }

        fun setOutsideTouchable(outsideTouchable: Boolean): Builder {
            this.outsideTouchable = outsideTouchable
            return this
        }

        fun setOnShowChangeBg(onShowChangeBg: Boolean): Builder {
            this.onShowChangeBg = onShowChangeBg
            focusable = true
            return this
        }

        fun setOnDismissChangeBg(onDismissChangeBg: Boolean): Builder {
            this.onDismissChangeBg = onDismissChangeBg
            return this
        }

        fun setOnShowBgAlpha(@FloatRange(from = 0.0, to = 255.0) onShowBgAlpha: Float): Builder {
            this.onShowBgAlpha = onShowBgAlpha
            return this
        }

        fun setOnDismissListener(onDismissListener: OnDismissListener?) {
            this.onDismissListener = onDismissListener
        }

        /**
         * 设置点击外部本身是否消失
         *
         * @param onOutsideTouchDissmiss
         */
        fun setOnOutsideTouchDissmiss(onOutsideTouchDissmiss: Boolean): Builder {
            if (onOutsideTouchDissmiss) {
                outsideTouchable = true
                focusable = true
            } else {
                outsideTouchable = false
                focusable = false
            }
            return this
        }

        /**
         * 设置点击返回键本身是否消失
         *
         * @return
         */
        fun setOnKeyBackDissmiss(onKeyBackDissmiss: Boolean): Builder {
            this.onKeyBackDissmiss = onKeyBackDissmiss
            return this
        }

        fun build(): CustomPopupWindow {
            if (contentView == null) {
                throw NullPointerException("contentView con't be null ")
            }
            val popupWindow = CustomPopupWindow(contentView!!, width, height)
            popupWindow.setActivity(activity)
            popupWindow.isOutsideTouchable = outsideTouchable
            popupWindow.isFocusable = focusable
            popupWindow.setOnDismissChangeBg(onDismissChangeBg)
            popupWindow.setOnShowChangeBg(onShowChangeBg)
            popupWindow.setOnShowBgAlpha(onShowBgAlpha)
            popupWindow.setOnDismissListener(onDismissListener)
            popupWindow.setBackgroundDrawable(ColorDrawable())
            if (onKeyBackDissmiss) {
                contentView?.isFocusable = true // 这个很重要
                contentView?.isFocusableInTouchMode = true
                contentView?.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        popupWindow.dismiss()
                        return@OnKeyListener true
                    }
                    false
                })
            }
            return popupWindow
        }

    }

    /**
     * 设置页面的透明度
     *
     * @param bgAlpha 1表示不透明
     */
    private fun setBackgroundAlpha(activity: Activity, bgAlpha: Float) {
        val lp = activity.window.attributes
        lp.alpha = bgAlpha
        if (bgAlpha == 1f) {
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        } else {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }
        activity.window.attributes = lp
    }
}