package cn.blinkdagger.androidLab.widget

import android.content.DialogInterface
import android.content.DialogInterface.OnShowListener
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.*
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
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
class ConfirmInputDialog : DialogFragment() {
    private var onLeftButtonClickListener: OnActionButtonClickListener? = null
    private var onRightButtonClickListener: OnActionButtonClickListener? = null
    private var onInitEditViewListener: OnInitEditViewListener? = null
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

        arguments?.let{ bundle ->
            marginLeft = bundle.getInt(KEY_MARGIN_LEFT, 0)
            marginRight = bundle.getInt(KEY_MARGIN_RIGHT, 0)
            dialogWidthPercent = bundle.getFloat(KEY_WIDTH_PERCENT, 0.85f)
        }
        dialog!!.window.decorView.setPadding(marginLeft, 0, marginRight, 0) //消除边距
        val lp = dialog!!.window.attributes
        if (activity != null && marginLeft == 0 && marginRight == 0) {
            lp.width = (ScreenUtil.getScreenWidth(requireActivity()) * dialogWidthPercent).toInt()
        } else {
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
        }
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog!!.window.attributes = lp
        dialog!!.window.setGravity(Gravity.CENTER)
        return inflater.inflate(R.layout.dialog_confirm_input, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val titleTV = view.findViewById<TextView>(R.id.dialog_title_tv)
        val messageTV = view.findViewById<TextView>(R.id.dialog_message_tv)
        val inputET = view.findViewById<EditText>(R.id.dialog_input_et)
        val leftActionTV = view.findViewById<TextView>(R.id.dialog_left_action_tv)
        val rightActionTV = view.findViewById<TextView>(R.id.dialog_right_action_tv)
        val centerLine = view.findViewById<View>(R.id.dialog_center_line)
        val cardView: CardView = view.findViewById(R.id.dialog_card_view)
        val bundle = arguments ?: return
        val title = bundle.getCharSequence(KEY_TITLE)
        val message = bundle.getCharSequence(KEY_MESSAGE)
        val leftBtnText = bundle.getCharSequence(KEY_LEFT_BTN_TEXT)
        val rightBtnText = bundle.getCharSequence(KEY_RIGHT_BTN_TEXT)
        val materialStyle = bundle.getBoolean(KEY_MATERIAL_STYLE)
        val leftBtnColor = bundle.getInt(KEY_LEFT_TEXT_COLOR)
        val rightBtnColor = bundle.getInt(KEY_RIGHT_TEXT_COLOR)
        val cardRadius = bundle.getInt(KEY_CARD_RADIUS)
        if (materialStyle) {
            titleTV.gravity = Gravity.START or Gravity.CENTER_VERTICAL
            val leftLp = leftActionTV.layoutParams as ConstraintLayout.LayoutParams
            leftLp.rightToLeft = R.id.dialog_right_action_tv
            leftActionTV.layoutParams = leftLp
            centerLine.visibility = View.GONE
        } else {
            titleTV.gravity = Gravity.CENTER
            val leftLp = leftActionTV.layoutParams as ConstraintLayout.LayoutParams
            leftLp.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
            leftLp.rightToLeft = R.id.dialog_center_line
            leftActionTV.layoutParams = leftLp
            val rightLp = rightActionTV.layoutParams as ConstraintLayout.LayoutParams
            rightLp.leftToLeft = R.id.dialog_center_line
            rightActionTV.layoutParams = rightLp
            centerLine.visibility = View.VISIBLE
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
        if (leftBtnColor != 0) {
            leftActionTV.setTextColor(leftBtnColor)
        }
        if (rightBtnColor != 0) {
            rightActionTV.setTextColor(rightBtnColor)
        }
        if (onInitEditViewListener != null) {
            onInitEditViewListener!!.initEditView(inputET)
        }
        leftActionTV.text = leftBtnText
        rightActionTV.text = rightBtnText
        leftActionTV.setOnClickListener {
            if (onLeftButtonClickListener != null) {
                onLeftButtonClickListener!!.onButtonClick(inputET.text)
            }
            dismissAllowingStateLoss()
        }
        rightActionTV.setOnClickListener {
            if (onRightButtonClickListener != null) {
                onRightButtonClickListener!!.onButtonClick(inputET.text)
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

    fun setOnLeftButtonClickListener(onLeftButtonClickListener: OnActionButtonClickListener?) {
        this.onLeftButtonClickListener = onLeftButtonClickListener
    }

    fun setOnRightButtonClickListener(onRightButtonClickListener: OnActionButtonClickListener?) {
        this.onRightButtonClickListener = onRightButtonClickListener
    }

    fun setOnInitEditViewListener(onInitEditViewListener: OnInitEditViewListener?) {
        this.onInitEditViewListener = onInitEditViewListener
    }

    /**
     * 用于初始化EditText
     */
    fun interface OnInitEditViewListener {
        fun initEditView(editText: EditText?)
    }

    /**
     * 用于监听EditText
     */
    fun interface OnActionButtonClickListener {
        fun onButtonClick(editable: Editable?)
    }

    class Builder {
        private val dialog: ConfirmInputDialog
        private val mBundle: Bundle
        private var onLeftActionListener: OnActionButtonClickListener? = null
        private var onRightActionListener: OnActionButtonClickListener? = null
        private var onInitEditViewListener: OnInitEditViewListener? = null
        private var onDismissListener: DialogInterface.OnDismissListener? = null
        private var onShowListener: OnShowListener? = null
        fun setOnLeftActionListener(onLeftActionListener: OnActionButtonClickListener?): Builder {
            this.onLeftActionListener = onLeftActionListener
            return this
        }

        fun setOnRightActionListener(onRightActionListener: OnActionButtonClickListener?): Builder {
            this.onRightActionListener = onRightActionListener
            return this
        }

        fun setOnInitEditViewListener(onInitEditViewListener: OnInitEditViewListener?): Builder {
            this.onInitEditViewListener = onInitEditViewListener
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

        fun setLeftActionText(btnString: CharSequence?): Builder {
            mBundle.putCharSequence(KEY_LEFT_BTN_TEXT, btnString)
            return this
        }

        fun setRightActionText(btnString: CharSequence?): Builder {
            mBundle.putCharSequence(KEY_RIGHT_BTN_TEXT, btnString)
            return this
        }

        fun setLeftActionTextColor(@ColorInt btnTextColor: Int): Builder {
            mBundle.putInt(KEY_LEFT_TEXT_COLOR, btnTextColor)
            return this
        }

        fun setRightActionTextColor(@ColorInt btnTextColor: Int): Builder {
            mBundle.putInt(KEY_RIGHT_TEXT_COLOR, btnTextColor)
            return this
        }

        fun setCardRadius(cardRadius: Int): Builder {
            mBundle.putInt(KEY_CARD_RADIUS, cardRadius)
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

        fun build(): ConfirmInputDialog {
            dialog.arguments = mBundle
            dialog.setOnDismissListener(onDismissListener)
            dialog.setOnShowListener(onShowListener)
            dialog.setOnLeftButtonClickListener(onLeftActionListener)
            dialog.setOnRightButtonClickListener(onRightActionListener)
            dialog.setOnInitEditViewListener(onInitEditViewListener)
            return dialog
        }

        init {
            mBundle = Bundle()
            dialog = ConfirmInputDialog()
        }
    }

    companion object {
        private const val TAG = "ConfirmInputDialog"
        protected const val KEY_TITLE = "TITLE" //对话框标题
        protected const val KEY_MESSAGE = "MESSAGE" //对话框内容
        protected const val KEY_LEFT_BTN_TEXT = "LEFT_BUTTON_TEXT" //对话框左边按钮文字
        protected const val KEY_RIGHT_BTN_TEXT = "RIGHT_BUTTON_TEXT" //对话框右边按钮文字
        protected const val KEY_LEFT_TEXT_COLOR = "BUTTON_TEXT_COLOR" //对话框左边按钮颜色
        protected const val KEY_RIGHT_TEXT_COLOR = "BUTTON_TEXT_COLOR" //对话框右边按钮颜色
        protected const val KEY_CARD_RADIUS = "DIALOG_RADIUS" //对话框圆角半径
        protected const val KEY_MARGIN_LEFT = "MARGIN_LEFT" //对话框左边距[将覆盖宽度百分比设置]
        protected const val KEY_MARGIN_RIGHT = "MARGIN_RIGHT" //对话框右边距[将覆盖宽度百分比设置]
        protected const val KEY_WIDTH_PERCENT = "WIDTH_PERCENT" //对话框宽度百分比
        protected const val KEY_MATERIAL_STYLE = "MATERIAL_STYLE" //对话框原生风格
    }
}