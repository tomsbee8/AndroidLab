package cn.blinkdagger.androidLab.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import cn.blinkdagger.androidLab.R
import java.util.*

/**
 * 类描述：水平分段选择器
 * 创建人：ls
 * 创建时间：2016/11/22
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
class SelectBarView : View {
    private var borderColor = 0 //边框颜色
    private var borderWidth  = 0f //边框宽度
    private var borderRadius = 0f //边框圆角半径
    private var bgColor = 0 //默认背景颜色
    private var checkedBgColor = 0 //选中条目的背景颜色
    private var textColor = 0 //默认字体颜色
    private var checkedTextColor = 0 //选中条目的字体颜色
    private var textSize = 0f //默认字体大小
    private var checkedTextSize = 0f //选中条目的字体大小
    private var splitLineColor = 0 //分割线的颜色
    private var splitLineWidth = 0f //分割线的宽度
    private var dotColor = 0 //分割线的颜色
    private var dotRadius = 0f //分割线的宽度
    private var items: Array<String>? = arrayOf() //条目的个数
    var dotsInItem = mutableListOf<Boolean>() //条目中是否有圆点
    private var itemClickListener: OnItemCheckedChangeListener? = null //点击事件
    private var checkedIndex = 0 //选中的索引
    private val dotMarginLeft = 10f //小圆点和文字的水平距离

    constructor(context: Context) : super(context) {
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.SelectBarView)
        borderColor = a.getColor(R.styleable.SelectBarView_borderColor, context.resources.getColor(R.color.main_color))
        borderWidth = a.getDimension(R.styleable.SelectBarView_borderWidth, 1f)
        borderRadius = a.getDimension(R.styleable.SelectBarView_borderRadius, 0f)
        bgColor = a.getColor(R.styleable.SelectBarView_bgColor, context.resources.getColor(R.color.main_color))
        checkedBgColor = a.getColor(R.styleable.SelectBarView_checkedBgColor, context.resources.getColor(R.color.main_color))
        textColor = a.getColor(R.styleable.SelectBarView_textColor, context.resources.getColor(R.color.main_color))
        checkedTextColor = a.getColor(R.styleable.SelectBarView_checkedTextColor, context.resources.getColor(R.color.main_color))
        textSize = a.getDimension(R.styleable.SelectBarView_textSize, 14f)
        checkedTextSize = a.getDimension(R.styleable.SelectBarView_checkedTextSize, textSize)
        splitLineColor = a.getColor(R.styleable.SelectBarView_splitLineColor, context.resources.getColor(R.color.main_color))
        splitLineWidth = a.getDimension(R.styleable.SelectBarView_splitLineWidth, 1f)
        dotColor = a.getColor(R.styleable.SelectBarView_dotColor, context.resources.getColor(R.color.main_color))
        dotRadius = a.getDimension(R.styleable.SelectBarView_dotRadius, 1f)
        //最后记得将TypedArray对象回收
        a.recycle()
    }


    fun setItems(items: Array<String>) {
        this.items = items
        for (i in items.indices) {
            dotsInItem.add(false)
        }
        invalidate()
    }

    fun setItemGroup(vararg items: String) {
        this.items = Arrays.copyOf(items,items.size)
        for (i in 0 until items.size) {
            dotsInItem.add(false)
        }
        invalidate()
    }

    fun setOnItemCheckedChangeListener(itemClickListener: OnItemCheckedChangeListener?) {
        this.itemClickListener = itemClickListener
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        var width = 0
        var height = 0
        if (widthMode == MeasureSpec.EXACTLY) { // Parent has told us how big to be. So be it.
            width = widthSize
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize
        }
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        val width = measuredWidth
        val height = measuredHeight
        //画边线
        val borderPaint = Paint()
        borderPaint.style = Paint.Style.STROKE
        borderPaint.strokeWidth = borderWidth
        borderPaint.isAntiAlias = true
        borderPaint.color = borderColor
        val rectF = RectF(borderWidth / 2, borderWidth / 2, width - borderWidth / 2, height - borderWidth / 2)
        if (borderRadius > 0) {
            canvas.drawRoundRect(rectF, borderRadius, borderRadius, borderPaint)
        } else {
            canvas.drawRect(rectF, borderPaint)
        }
        if (items != null && items!!.size > 1) {
            val itemWidth = (width - 2 * borderWidth - (items!!.size - 1) * splitLineWidth) / items!!.size
            //画分割线
            for (i in 0 until items!!.size - 1) {
                val linePaint = Paint()
                linePaint.color = splitLineColor
                linePaint.style = Paint.Style.FILL
                linePaint.isAntiAlias = true
                canvas.drawRect((itemWidth + splitLineWidth) * i + itemWidth + borderWidth, borderWidth,
                        (itemWidth + splitLineWidth) * (i + 1) + borderWidth, height - borderWidth, linePaint)
            }
            //画背景
            for (i in items!!.indices) {
                val bgPaint = Paint()
                if (checkedIndex == i) {
                    bgPaint.color = checkedBgColor
                } else {
                    bgPaint.color = bgColor
                }
                //                bgPaint.setStyle(Paint.Style.FILL);
                bgPaint.isAntiAlias = true
                if (i == 0) {
                    val leftPath = Path()
                    leftPath.moveTo(borderWidth + itemWidth, borderWidth)
                    leftPath.lineTo(borderWidth + borderRadius, borderWidth)
                    //内圆角直径
                    val radius: Float = if (borderRadius * 2 > borderWidth) borderRadius * 2 - borderWidth * 2 else 0f
                    val leftTopRectF = RectF(borderWidth, borderWidth, borderWidth + radius, borderWidth + radius)
                    leftPath.arcTo(leftTopRectF, 270f, -90f)
                    leftPath.lineTo(borderWidth, height - borderWidth - borderRadius)
                    val leftBotomRectF = RectF(borderWidth, height - radius - borderWidth, borderWidth + radius, height - borderWidth)
                    leftPath.arcTo(leftBotomRectF, 180f, -90f)
                    leftPath.lineTo(borderWidth + itemWidth, height - borderWidth)
                    leftPath.close()
                    canvas.drawPath(leftPath, bgPaint)
                } else if (i == items!!.size - 1) {
                    val rightPath = Path()
                    rightPath.moveTo(width - borderWidth - itemWidth, borderWidth)
                    rightPath.lineTo(width - borderRadius - borderWidth, borderWidth)
                    //内圆角直径
                    val radius: Float = if (borderRadius * 2 > borderWidth) borderRadius * 2 - borderWidth * 2 else 0f
                    val rightTopRectF = RectF(width - radius - borderWidth, borderWidth, width - borderWidth, radius + borderWidth)
                    rightPath.arcTo(rightTopRectF, 270f, 90f)
                    rightPath.lineTo(width - borderWidth, height - borderRadius - borderWidth)
                    val rightBottomRectF = RectF(width - radius - borderWidth, height - radius - borderWidth, width - borderWidth, height - borderWidth)
                    rightPath.arcTo(rightBottomRectF, 0f, 90f)
                    rightPath.lineTo(width - borderWidth - itemWidth, height - borderWidth)
                    rightPath.close()
                    canvas.drawPath(rightPath, bgPaint)
                } else {
                    val centRectF = RectF(i * (splitLineWidth + itemWidth) + borderWidth, borderWidth, i * (splitLineWidth + itemWidth) + borderWidth + itemWidth, height - borderWidth)
                    canvas.drawRect(centRectF, bgPaint)
                }
            }
            //画文字和小圆点
            for (i in items!!.indices) { //画文字
                val textPaint = Paint()
                textPaint.isAntiAlias = true
                if (checkedIndex == i) {
                    textPaint.color = checkedTextColor
                    textPaint.textSize = checkedTextSize
                } else {
                    textPaint.color = textColor
                    textPaint.textSize = textSize
                }
                textPaint.textAlign = Paint.Align.CENTER
                val fontMetrics = textPaint.fontMetricsInt
                val baseline = (height - fontMetrics.bottom - fontMetrics.top) / 2
                canvas.drawText(items!![i], itemWidth / 2 + itemWidth * i + splitLineWidth * i + borderWidth, baseline.toFloat(), textPaint)
                //画小圆点
                if (dotsInItem[i]) {
                    val dotPaint = Paint()
                    dotPaint.isAntiAlias = true
                    dotPaint.color = dotColor
                    dotPaint.style = Paint.Style.FILL
                    val textWidth = getTextWidth(textPaint, items!![i])
                    val dotX = itemWidth / 2 + itemWidth * i + splitLineWidth * i + borderWidth + textWidth / 2 + dotRadius + dotMarginLeft
                    val dotY = baseline + fontMetrics.top + dotRadius
                    canvas.drawCircle(dotX, dotY, dotRadius, dotPaint)
                }
            }
        }
        super.onDraw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val width = measuredWidth
        val itemWidth = (width - 2 * borderWidth - (items!!.size - 1) * splitLineWidth) / items!!.size
        if (event.action == MotionEvent.ACTION_DOWN) {
            val x = event.x
            for (i in items!!.indices) { //判断X坐标的位置
                val xInField = x >= borderWidth + splitLineWidth * i + itemWidth * i && x <= borderWidth + splitLineWidth * i + itemWidth * (i + 1)
                if (xInField && checkedIndex != i) {
                    checkedIndex = i
                    invalidate()
                    if (itemClickListener != null) {
                        itemClickListener!!.onItemClick(i)
                    }
                }
            }
        }
        return super.onTouchEvent(event)
    }

    /**
     * 设置小圆点是否可见
     *
     * @param index    分段Item的索引
     * @param visiable 小圆点是否可见
     */
    fun setItemDotVisiable(index: Int, visiable: Boolean) {
        dotsInItem[index] = visiable
        invalidate()
    }

    /**
     * 获取小圆点可见性
     *
     * @param index 分段Item的索引
     * @return visiable 小圆点是否可见
     */
    fun getItemDotVisiablity(index: Int): Boolean {
        return dotsInItem[index]
    }

    /**
     * 分段选中改变事件
     */
    interface OnItemCheckedChangeListener {
        /**
         * 改变后的索引
         *
         * @param index
         */
        fun onItemClick(index: Int)
    }

    companion object {
        /**
         * 获取文字的宽度
         *
         * @param paint
         * @param str
         * @return
         */
        fun getTextWidth(paint: Paint, str: String?): Int {
            var iRet = 0
            if (str != null && str.length > 0) {
                val len = str.length
                val widths = FloatArray(len)
                paint.getTextWidths(str, widths)
                for (j in 0 until len) {
                    iRet += Math.ceil(widths[j].toDouble()).toInt()
                }
            }
            return iRet
        }

        /**
         * 获取文字的高度
         *
         * @param
         * @return
         */
        fun getTextHeight(textSize: Float, sample: String): Float {
            val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.LINEAR_TEXT_FLAG)
            paint.style = Paint.Style.FILL
            paint.textAlign = Paint.Align.CENTER
            paint.textSize = textSize
            val bounds = Rect()
            paint.getTextBounds(sample, 0, sample.length, bounds)
            return bounds.height().toFloat()
        }
    }
}