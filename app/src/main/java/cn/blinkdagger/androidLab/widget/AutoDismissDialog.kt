package cn.blinkdagger.androidLab.widget

import android.content.DialogInterface
import android.content.DialogInterface.OnShowListener
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.CallSuper
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
class AutoDismissDialog : DialogFragment() {
    private var mOnCancelListener: DialogInterface.OnCancelListener? = null
    private var mOnDismissListener: DialogInterface.OnDismissListener? = null
    private var mOnShowListener: OnShowListener? = null
    private var marginLeft = 0
    private var marginRight = 0
    private var dialogWidthPercent = 0f
    private var dialogDismissDuration: Long = 0
    private var cancelEnable: Boolean = false
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
            dialogDismissDuration = bundle.getLong(KEY_AUTO_DISMISS_DURATION, 0)
            cancelEnable = bundle.getBoolean(KEY_CANCELABLE, true)
        }
        dialog!!.window.decorView.setPadding(marginLeft, 0, marginRight, 0) //消除边距
        val lp = dialog!!.window.attributes
        isCancelable = cancelEnable
        if (activity != null && marginLeft == 0 && marginRight == 0) {
            lp.width = (ScreenUtil.getScreenWidth(activity!!) * dialogWidthPercent).toInt()
        } else {
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
        }
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog!!.window.attributes = lp
        dialog!!.window.setGravity(Gravity.CENTER)
        if (dialogDismissDuration > 0) {
            Handler().postDelayed({ dismiss() }, dialogDismissDuration)
        }
        return inflater.inflate(R.layout.dialog_auto_dismiss, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val titleTV = view.findViewById<TextView>(R.id.dialog_title_tv)
        val messageTV = view.findViewById<TextView>(R.id.dialog_message_tv)
        val contentIV = view.findViewById<ImageView>(R.id.dialog_content_iv)
        val cardView: CardView = view.findViewById(R.id.dialog_card_view)
        val bundle = arguments ?: return
        val title = bundle.getCharSequence(KEY_TITLE)
        val message = bundle.getCharSequence(KEY_MESSAGE)
        val materialStyle = bundle.getBoolean(KEY_MATERIAL_STYLE)
        val imageIcon = bundle.getInt(KEY_IMAGE_ICON, 0)
        val cardRadius = bundle.getInt(KEY_CARD_RADIUS)
        if (materialStyle) {
            titleTV.gravity = Gravity.START or Gravity.CENTER_VERTICAL
            messageTV.gravity = Gravity.START or Gravity.CENTER_VERTICAL
        } else {
            titleTV.gravity = Gravity.CENTER
            messageTV.gravity = Gravity.CENTER
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
        if (imageIcon == 0) {
            contentIV.visibility = View.GONE
        } else {
            contentIV.visibility = View.VISIBLE
            contentIV.setImageResource(imageIcon)
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
        dialog!!.setOnShowListener { dialog ->
            if (mOnShowListener != null) {
                mOnShowListener!!.onShow(dialog)
            }
        }
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

    class Builder {
        private val dialog: AutoDismissDialog
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

        fun setCardRadius(cardRadius: Int): Builder {
            mBundle.putInt(KEY_CARD_RADIUS, cardRadius)
            return this
        }

        fun setBgImageId(@DrawableRes bgImageId: Int): Builder {
            mBundle.putInt(KEY_IMAGE_ICON, bgImageId)
            return this
        }

        fun setCancelable(cancelable: Boolean): Builder {
            mBundle.putBoolean(KEY_CANCELABLE, cancelable)
            return this
        }

        fun setHorizontalMargin(left: Int, right: Int): Builder {
            mBundle.putInt(KEY_MARGIN_LEFT, left)
            mBundle.putInt(KEY_MARGIN_RIGHT, right)
            return this
        }

        fun setWidthPercent(@FloatRange(from = 0.00, to = 1.0) marginPercent: Float): Builder {
            mBundle.putFloat(KEY_WIDTH_PERCENT, marginPercent)
            return this
        }

        fun setMaterialStyle(materialStyle: Boolean): Builder {
            mBundle.putBoolean(KEY_MATERIAL_STYLE, materialStyle)
            return this
        }

        fun setAutoDismissDuration(duration: Long): Builder {
            mBundle.putLong(KEY_AUTO_DISMISS_DURATION, duration)
            return this
        }

        fun build(): AutoDismissDialog {
            dialog.arguments = mBundle
            dialog.setOnDismissListener(onDismissListener)
            dialog.setOnShowListener(onShowListener)
            return dialog
        }

        init {
            mBundle = Bundle()
            dialog = AutoDismissDialog()
        }
    }

    companion object {
        private const val TAG = "ConfirmDialog"
        protected const val KEY_TITLE = "TITLE" //对话框标题
        protected const val KEY_IMAGE_ICON = "IMAGE_RES_ID" //对话框图标Id
        protected const val KEY_MESSAGE = "MESSAGE" //对话框内容
        protected const val KEY_CANCELABLE = "CANCEL" //对话框是否可以取消
        protected const val KEY_CARD_RADIUS = "DIALOG_RADIUS" //对话框圆角半径
        protected const val KEY_MARGIN_LEFT = "MARGIN_LEFT" //对话框左边距[将覆盖宽度百分比设置]
        protected const val KEY_MARGIN_RIGHT = "MARGIN_RIGHT" //对话框右边距[将覆盖宽度百分比设置]
        protected const val KEY_WIDTH_PERCENT = "WIDTH_PERCENT" //对话框宽度百分比
        protected const val KEY_MATERIAL_STYLE = "MATERIAL_STYLE" //对话框原生风格
        protected const val KEY_AUTO_DISMISS_DURATION = "DURATION" //对话框显示时间
    }
}