package cn.blinkdagger.androidLab.widget

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import cn.blinkdagger.androidLab.R

/**
 * @Author tomsbee8
 * @Date  2020/9/15
 * @Description 水平单选控件
 * @Version
 */
class SwitchRadioGroup : FrameLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttr(context, attrs, defStyleAttr)
    }

    // 选中的按钮的索引
    private var mCheckedIndex = 0

    // 选项宽度
    private var itemWidth = 0

    // 选项高度
    private var itemHeight = 0

    // 背景圆角半径
    private var bgRadius = 0f

    // 背景色
    private var bgColor = Color.WHITE

    // 选中选项背景圆角半径
    private var selectedBgRadius = 0f

    // 选中选项背景色
    private var selectedBgColor = Color.GRAY

    // 选项文字大小
    private var textSize = 12f

    // 选项文字颜色
    private var textColor = Color.BLACK

    // 选中的选项文字
    private var selectedTextSize = 12f

    // 选中的选项颜色
    private var selectedTextColor = Color.BLACK

    // 选项的TextView数组
    private var mTextViewArray = mutableListOf<TextView>()

    // 选中的选项背景View(可以水平移动)
    private var selectedItemBgView: View? = null

    // 切换选中选项的监听
    private var mOnCheckedChangeListener: OnCheckedChangeListener? = null

    // 是否已经初始化过选项背景View
    private var mInitialized = false

    private fun initAttr(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        var itemArray = listOf<CharSequence>()
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.SwitchRadioGroup, defStyleAttr, 0)

        for (i in 0 until a.indexCount) {
            when (val attr = a.getIndex(i)) {
                R.styleable.SwitchRadioGroup_itemWidth -> itemWidth = a.getDimensionPixelSize(attr, 40)
                R.styleable.SwitchRadioGroup_itemHeight -> itemHeight = a.getDimensionPixelSize(attr, 20)
                R.styleable.SwitchRadioGroup_bgRadius -> bgRadius = a.getDimension(attr, 10f)
                R.styleable.SwitchRadioGroup_bgColor -> bgColor = a.getColor(attr, Color.WHITE)
                R.styleable.SwitchRadioGroup_selectedBgRadius -> selectedBgRadius = a.getDimension(attr, 0f)
                R.styleable.SwitchRadioGroup_selectedBgColor -> selectedBgColor = a.getColor(attr, Color.GRAY)
                R.styleable.SwitchRadioGroup_textSize -> textSize = a.getDimension(attr, 14f)
                R.styleable.SwitchRadioGroup_textColor -> textColor = a.getColor(attr, Color.BLACK)
                R.styleable.SwitchRadioGroup_selectedTextSize -> selectedTextSize = a.getDimension(attr, 14f)
                R.styleable.SwitchRadioGroup_selectedTextColor -> selectedTextColor = a.getColor(attr, Color.BLACK)
                R.styleable.SwitchRadioGroup_itemArray -> itemArray = a.getTextArray(attr).toList()
            }
        }
        a.recycle()
        init(context, itemArray)
    }

    private fun init(context: Context, items: List<CharSequence>) {
        if (items.isNotEmpty()) {
            setMenuItems(context, items.toTypedArray())
        }
    }

    /**
     * 设置选项数据
     */
    fun setMenuItems(context: Context, array: Array<CharSequence>) {
        mTextViewArray.clear()

        if (array.isNotEmpty()) {

            // 设置背景
            val bgDrawable = GradientDrawable()
            bgDrawable.setColor(bgColor)
            bgDrawable.cornerRadius = bgRadius
            this.background = bgDrawable

            // 设置选中背景
            selectedItemBgView = View(context)
            val selectedDrawable = GradientDrawable()
            selectedDrawable.setColor(selectedBgColor)
            selectedDrawable.cornerRadius = selectedBgRadius
            selectedItemBgView?.background = selectedDrawable

            selectedItemBgView?.apply {
                val lp = LayoutParams(itemWidth, itemHeight)
                lp.gravity = Gravity.CENTER_VERTICAL
                addView(this, lp)
            }

            // 设置选项容器
            val containerLL = LinearLayout(context)
            containerLL.orientation = LinearLayout.HORIZONTAL
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            val containerLayoutParams = LayoutParams(itemWidth * array.size, itemHeight)
            containerLayoutParams.gravity = Gravity.CENTER
            addView(containerLL, containerLayoutParams)

            // 添加选项TextView
            array.forEachIndexed { index, charSequence ->
                val tv = TextView(context)
                tv.text = charSequence
                tv.setBackgroundColor(Color.TRANSPARENT)
                tv.gravity = Gravity.CENTER
                if (index == mCheckedIndex) {
                    tv.textSize = selectedTextSize
                    tv.setTextColor(selectedTextColor)
                } else {
                    tv.textSize = textSize
                    tv.setTextColor(textColor)
                }
                tv.setOnClickListener {
                    setChecked(index)
                }
                mTextViewArray.add(tv)
                containerLL.addView(tv, LayoutParams(itemWidth, itemHeight))
            }
        }
    }

    /**
     *  设置选中选项
     */
    fun setChecked(checkedIndex: Int) {
        if (this.mCheckedIndex == checkedIndex) {
            return
        }

        // 水平移动效果
        selectedItemBgView?.let {
            val marginHorizon = if (measuredWidth > itemWidth * mTextViewArray.size) {
                (measuredWidth - itemWidth * mTextViewArray.size) / 2
            } else {
                0
            }
            val currentX = mCheckedIndex * itemWidth.toFloat() + marginHorizon
            val targetX = checkedIndex * itemWidth.toFloat() + marginHorizon
            val animator = ObjectAnimator.ofFloat(it, View.TRANSLATION_X, currentX, targetX)
            animator.duration = 300
            animator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {}
                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationStart(animation: Animator?) {}
                override fun onAnimationEnd(animation: Animator?) {
                    mCheckedIndex = checkedIndex
                    mTextViewArray.forEachIndexed { index, tv ->
                        if (index == checkedIndex) {
                            tv.textSize = selectedTextSize
                            tv.setTextColor(selectedTextColor)
                            mOnCheckedChangeListener?.onCheckedChanged(index, tv)
                        } else {
                            tv.textSize = textSize
                            tv.setTextColor(textColor)
                        }
                    }
                }
            })
            animator.start()
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        // 初始化选中背景的位置
        selectedItemBgView?.takeIf { !mInitialized }?. let {
            val marginHorizon = if (measuredWidth > itemWidth * mTextViewArray.size) {
                (measuredWidth - itemWidth * mTextViewArray.size) / 2
            } else {
                0
            }
            val targetX = mCheckedIndex * itemWidth + marginHorizon
            val lp = (it.layoutParams as LayoutParams)
            lp.leftMargin = targetX
            it.layoutParams = lp
            mInitialized = true
        }
    }

    fun setOnCheckedChangeListener(listener: OnCheckedChangeListener) {
        this.mOnCheckedChangeListener = listener
    }

    interface OnCheckedChangeListener {
        fun onCheckedChanged(index: Int, textView: TextView)
    }
}