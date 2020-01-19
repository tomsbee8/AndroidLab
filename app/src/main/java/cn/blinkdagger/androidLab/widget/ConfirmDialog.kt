package cn.blinkdagger.androidLab.widget

import android.content.DialogInterface
import android.content.DialogInterface.OnShowListener
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import androidx.cardview.widget.CardView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import cn.blinkdagger.androidLab.R
import cn.blinkdagger.androidLab.utils.ScreenUtil
import com.orhanobut.logger.Logger

/**
 * @Author ls
 * @Date 2018/11/24
 * @Description
 * @Version
 */
class ConfirmDialog : DialogFragment() {
    private var onConfirmClickListener: View.OnClickListener? = null
    private var mOnCancelListener: DialogInterface.OnCancelListener? = null
    private var mOnDismissListener: DialogInterface.OnDismissListener? = null
    private var mOnShowListener: OnShowListener? = null
    private var marginLeft = 0
    private var marginRight = 0
    private var dialogWidthPercent = 0f
    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val bundle = arguments
        if (bundle != null) {
            marginLeft = bundle.getInt(KEY_MARGIN_LEFT, 0)
            marginRight = bundle.getInt(KEY_MARGIN_RIGHT, 0)
            dialogWidthPercent = bundle.getFloat(KEY_WIDTH_PERCENT, 0.85f)
        }
        dialog!!.window.decorView.setPadding(marginLeft, 0, marginRight, 0) //消除边距
        val lp = dialog!!.window.attributes
        if (activity != null && marginLeft == 0 && marginRight == 0) {
            lp.width = (ScreenUtil.getScreenWidth(activity!!) * dialogWidthPercent).toInt()
        } else {
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
        }
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog!!.window.attributes = lp
        dialog!!.window.setGravity(Gravity.CENTER)
        return inflater.inflate(R.layout.dialog_confirm, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val titleTV = view.findViewById<TextView>(R.id.dialog_title_tv)
        val messageTV = view.findViewById<TextView>(R.id.dialog_message_tv)
        val messageTipsTV = view.findViewById<TextView>(R.id.dialog_message_tips_tv)
        val confirmTV = view.findViewById<TextView>(R.id.dialog_confirm_tv)
        val contentIV = view.findViewById<ImageView>(R.id.dialog_content_iv)
        val cardView: CardView = view.findViewById(R.id.dialog_card_view)
        val bundle = arguments ?: return
        val title = bundle.getCharSequence(KEY_TITLE)
        val message = bundle.getCharSequence(KEY_MESSAGE)
        val messageTips = bundle.getCharSequence(KEY_MESSAGE_TIPS)
        val btnText = bundle.getCharSequence(KEY_BTN_TEXT)
        val materialStyle = bundle.getBoolean(KEY_MATERIAL_STYLE)
        val imageIcon = bundle.getInt(KEY_IMAGE_ICON, 0)
        val btnTextColor = bundle.getInt(KEY_BTN_TEXT_COLOR)
        val cardRadius = bundle.getInt(KEY_CARD_RADIUS)
        val lp = confirmTV.layoutParams as LinearLayout.LayoutParams
        if (materialStyle) {
            titleTV.gravity = Gravity.START or Gravity.CENTER_VERTICAL
            messageTV.gravity = Gravity.START or Gravity.CENTER_VERTICAL
            lp.gravity = Gravity.END or Gravity.CENTER_VERTICAL
            confirmTV.layoutParams = lp
        } else {
            titleTV.gravity = Gravity.CENTER
            messageTV.gravity = Gravity.CENTER
            lp.gravity = Gravity.CENTER
            confirmTV.layoutParams = lp
        }
        if (TextUtils.isEmpty(title)) {
            titleTV.visibility = View.GONE
        } else {
            titleTV.visibility = View.VISIBLE
            titleTV.text = title
        }
        if (TextUtils.isEmpty(message)) {
            messageTV.visibility = View.GONE
        } else {
            messageTV.visibility = View.VISIBLE
            messageTV.text = message
        }
        if (TextUtils.isEmpty(messageTips)) {
            messageTipsTV.visibility = View.GONE
        } else {
            messageTipsTV.visibility = View.VISIBLE
            messageTipsTV.text = messageTips
        }
        if (imageIcon == 0) {
            contentIV.visibility = View.GONE
        } else {
            contentIV.visibility = View.VISIBLE
            contentIV.setImageResource(imageIcon)
        }
        if (btnTextColor != 0) {
            confirmTV.setTextColor(btnTextColor)
        }
        confirmTV.text = btnText
        confirmTV.setOnClickListener { v ->
            if (onConfirmClickListener != null) {
                onConfirmClickListener!!.onClick(v)
            }
            dismissAllowingStateLoss()
        }
        cardView.radius = cardRadius.toFloat()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        if (mOnCancelListener != null) {
            mOnCancelListener!!.onCancel(dialog)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (mOnDismissListener != null) {
            mOnDismissListener!!.onDismiss(dialog)
        }
    }

    override fun onStart() {
        super.onStart()
        dialog!!.setOnShowListener(mOnShowListener)
    }

    fun show(fm: FragmentManager?) {
        if (fm == null) {
            Logger.e(TAG, "show error : fragment manager is null.")
            return
        }
        this.show(fm, TAG)
    }

    fun setOnCancelListener(mOnCancelListener: DialogInterface.OnCancelListener?) {
        this.mOnCancelListener = mOnCancelListener
    }

    fun setOnDismissListener(mOnDismissListener: DialogInterface.OnDismissListener?) {
        this.mOnDismissListener = mOnDismissListener
    }

    fun setOnShowListener(mOnShowListener: OnShowListener?) {
        this.mOnShowListener = mOnShowListener
    }

    fun setOnConfirmClickListener(onConfirmClickListener: View.OnClickListener?) {
        this.onConfirmClickListener = onConfirmClickListener
    }

    class Builder {
        private val dialog: ConfirmDialog
        private val mBundle: Bundle
        private var onConfirmClickListener: View.OnClickListener? = null
        private var onDismissListener: DialogInterface.OnDismissListener? = null
        private var onShowListener: OnShowListener? = null
        fun setOnConfirmClickListener(onConfirmClickListener: View.OnClickListener?): Builder {
            this.onConfirmClickListener = onConfirmClickListener
            return this
        }

        fun setOnDismissListener(onDismissListener: DialogInterface.OnDismissListener?): Builder {
            this.onDismissListener = onDismissListener
            return this
        }

        fun setOnShowListener(onShowListener: OnShowListener?): Builder {
            this.onShowListener = onShowListener
            return this
        }

        fun setTitle(title: CharSequence?): Builder {
            mBundle.putCharSequence(KEY_TITLE, title)
            return this
        }

        fun setMessage(message: CharSequence?): Builder {
            mBundle.putCharSequence(KEY_MESSAGE, message)
            return this
        }

        fun setMessageTips(messageTips: CharSequence?): Builder {
            mBundle.putCharSequence(KEY_MESSAGE_TIPS, messageTips)
            return this
        }

        fun setConfirmText(btnString: CharSequence?): Builder {
            mBundle.putCharSequence(KEY_BTN_TEXT, btnString)
            return this
        }

        fun setConfirmTextColor(@ColorInt btnTextColor: Int): Builder {
            mBundle.putInt(KEY_BTN_TEXT_COLOR, btnTextColor)
            return this
        }

        fun setCardRadius(cardRadius: Int): Builder {
            mBundle.putInt(KEY_CARD_RADIUS, cardRadius)
            return this
        }

        fun setBgImageId(@DrawableRes bgImageId: Int): Builder {
            mBundle.putInt(KEY_IMAGE_ICON, bgImageId)
            return this
        }

        fun setCancelable(cancelable: Boolean): Builder {
            dialog.isCancelable = cancelable
            return this
        }

        fun setHorizontalMargin(left: Int, right: Int): Builder {
            mBundle.putInt(KEY_MARGIN_LEFT, left)
            mBundle.putInt(KEY_MARGIN_RIGHT, right)
            return this
        }

        fun setWidthPercent(@FloatRange(from = 0.0, to = 1.0) marginPercent: Float): Builder {
            mBundle.putFloat(KEY_WIDTH_PERCENT, marginPercent)
            return this
        }

        fun setMaterialStyle(materialStyle: Boolean): Builder {
            mBundle.putBoolean(KEY_MATERIAL_STYLE, materialStyle)
            return this
        }

        fun build(): ConfirmDialog {
            dialog.arguments = mBundle
            dialog.setOnDismissListener(onDismissListener)
            dialog.setOnShowListener(onShowListener)
            dialog.setOnConfirmClickListener(onConfirmClickListener)
            return dialog
        }

        init {
            mBundle = Bundle()
            dialog = ConfirmDialog()
        }
    }

    companion object {
        private const val TAG = "ConfirmDialog"
        protected const val KEY_TITLE = "TITLE" //对话框标题
        protected const val KEY_IMAGE_ICON = "IMAGE_RES_ID" //对话框图标Id
        protected const val KEY_MESSAGE = "MESSAGE" //对话框内容
        protected const val KEY_MESSAGE_TIPS = "MESSAGE_TIPS" //对话框描述
        protected const val KEY_BTN_TEXT = "BUTTON_TEXT" //对话框按钮文字
        protected const val KEY_BTN_TEXT_COLOR = "BUTTON_TEXT_COLOR" //对话框按钮颜色
        protected const val KEY_CARD_RADIUS = "DIALOG_RADIUS" //对话框圆角半径
        protected const val KEY_MARGIN_LEFT = "MARGIN_LEFT" //对话框左边距[将覆盖宽度百分比设置]
        protected const val KEY_MARGIN_RIGHT = "MARGIN_RIGHT" //对话框右边距[将覆盖宽度百分比设置]
        protected const val KEY_WIDTH_PERCENT = "WIDTH_PERCENT" //对话框宽度百分比
        protected const val KEY_MATERIAL_STYLE = "MATERIAL_STYLE" //对话框原生风格
    }
}