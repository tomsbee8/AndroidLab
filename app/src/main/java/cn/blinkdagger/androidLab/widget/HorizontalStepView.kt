package cn.blinkdagger.androidLab.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.widget.Scroller
import cn.blinkdagger.androidLab.R
import cn.blinkdagger.androidLab.utils.DensityUtil

/**
 * @Author ls
 * @Date 2018/11/9
 * @Description
 * @Version
 */
class HorizontalStepView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private var stepTextList // Step 数据列表
            : List<String>? = null
    // 点的选中状态影响文字样式
    private var selectedPosition = 0 // 选中的点的位置
    private var selectedTextSize = 0f // 选中的文字的大小
    private var textSize = 0f // 未选中的文字的大小
    private var textColor = 0 // 未激活的文字的颜色
    private var activitedTextColor = 0 // 激活的文字的颜色
    // 点的激活状态影响圆点View的样式
    private var activedPosition = 0 // 激活的圆点的位置
    private var activedDotColor = 0 // 激活的圆点的背景颜色
    private var activedDotRadius = 0f // 激活的圆点的半径
    private var cureentDotRadius = 0f // 激活的最后的圆点的半径
    private var dotRadius = 0f // 未激活的圆点的半径
    private var dotColor = 0 // 未激活的圆点的背景颜色
    private var dotBitmap: Bitmap? = null // 未激活的圆点的背景
    private var shadowColor = 0  // 圆点背景颜色
    private var shadowWidth = 0f // 圆点背景宽度
    private var lineColor = 0 // 线条颜色
    private var activitedLineColor = 0 // 线条颜色
    private var lineHeight = 0f // 线条高度
    private var itemWidth = 0f // item 宽度
    private var mPaint: Paint? = null
    private var mRecfF: RectF? = null
    private var scroller: Scroller? = null
    // 当前 X坐标位置
    private var currentX = 0

    private fun initAttr(context: Context, attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.HorizontalStepView)
        selectedTextSize = a.getDimension(R.styleable.HorizontalStepView_selectedTextSize, DensityUtil.dp2px(context, 12f).toFloat())
        activitedTextColor = a.getColor(R.styleable.HorizontalStepView_activedTextColor, Color.parseColor("#333333"))
        textSize = a.getDimension(R.styleable.HorizontalStepView_normalTextSize, DensityUtil.dp2px(context, 12f).toFloat())
        textColor = a.getColor(R.styleable.HorizontalStepView_normalTextColor, Color.parseColor("#666666"))
        activedDotColor = a.getColor(R.styleable.HorizontalStepView_activedDotColor, Color.parseColor("#000000"))
        activedDotRadius = a.getDimension(R.styleable.HorizontalStepView_activedDotRadius, DensityUtil.dp2px(context, 3f).toFloat())
        cureentDotRadius = a.getDimension(R.styleable.HorizontalStepView_currentDotRadius, DensityUtil.dp2px(context, 3f).toFloat())
        dotRadius = a.getDimension(R.styleable.HorizontalStepView_normalDotRadius, DensityUtil.dp2px(context, 4f).toFloat())
        dotColor = a.getColor(R.styleable.HorizontalStepView_normalDotColor, Color.parseColor("#000000"))
        lineColor = a.getColor(R.styleable.HorizontalStepView_lineColor, Color.parseColor("#F1F1F1"))
        activitedLineColor = a.getColor(R.styleable.HorizontalStepView_activatedLineColor, Color.parseColor("#666666"))
        lineHeight = a.getDimension(R.styleable.HorizontalStepView_lineHeight, DensityUtil.dp2px(context, 2f).toFloat())
        itemWidth = a.getDimension(R.styleable.HorizontalStepView_itemWidth, DensityUtil.dp2px(context, 100f).toFloat())
        shadowColor = a.getColor(R.styleable.HorizontalStepView_shadowColor, Color.parseColor("#666666"))
        shadowWidth = a.getDimension(R.styleable.HorizontalStepView_shadowWidth, DensityUtil.dp2px(context, 0f).toFloat())
        val dotImageResId = a.getResourceId(R.styleable.HorizontalStepView_dotImage, -1)
        if (dotImageResId != -1) {
            dotBitmap = BitmapFactory.decodeResource(resources, dotImageResId)
        }
        a.recycle()
        mPaint = Paint()
        scroller = Scroller(context)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        var heightSize = 0
        if (heightMode == MeasureSpec.EXACTLY) {
            heightSize = MeasureSpec.getSize(heightMeasureSpec)
        } else if (heightMode == MeasureSpec.AT_MOST) {
            val maxDotHeight = Math.max(Math.max(activedDotRadius, dotRadius), cureentDotRadius) * 2 + shadowWidth * 2
            val minHeight = (getFontHeight(Math.max(selectedTextSize, textSize)) + maxDotHeight).toInt()
            heightSize = Math.min(MeasureSpec.getSize(heightMeasureSpec), minHeight)
        } else if (heightMode == MeasureSpec.UNSPECIFIED) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
        setMeasuredDimension(widthSize, heightSize)
        //        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
    }

    fun setStepTextList(stepTextList: List<String>?) {
        this.stepTextList = stepTextList
        invalidate()
    }

    /**
     * 设置 激活的阶段位置
     *
     * @param activedPosition
     */
    fun setActivedPosition(activedPosition: Int) {
        if (activedPosition > 0 && activedPosition < stepTextList!!.size) {
            this.activedPosition = activedPosition
            invalidate()
        }
    }

    /**
     * 设置 选中的圆点位置
     *
     * @param position
     */
    fun setSelectedPosition(position: Int) {
        if (stepTextList!!.isEmpty() || selectedPosition == position) {
            return
        }
        if (position > 0 && position < stepTextList!!.size) {
            scrollTo(position)
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        if (stepTextList != null && !stepTextList!!.isEmpty()) {
            val halfWidth = width / 2
            val dotViewHeight = Math.max(Math.max(activedDotRadius, dotRadius), cureentDotRadius) + shadowWidth
            // 绘制圆点的背景
            if (shadowWidth > 0) {
                for (index in stepTextList!!.indices) {
                    mPaint!!.style = Paint.Style.FILL
                    mPaint!!.isAntiAlias = true
                    mPaint!!.color = shadowColor
                    if (index < activedPosition) {
                        canvas.drawCircle(halfWidth + itemWidth * index, dotViewHeight, activedDotRadius + shadowWidth, mPaint)
                    } else if (index == activedPosition) {
                        canvas.drawCircle(halfWidth + itemWidth * index, dotViewHeight, cureentDotRadius + shadowWidth, mPaint)
                    }
                }
            }
            // 绘制背景线条
            mPaint!!.style = Paint.Style.FILL
            mPaint!!.color = lineColor
            val bgWidth = halfWidth + itemWidth * (stepTextList!!.size - 1)
            canvas.drawRect(halfWidth.toFloat(), dotViewHeight - lineHeight / 2, bgWidth, dotViewHeight + lineHeight / 2, mPaint)
            // 绘制激活的线条
            mRecfF = if (activedPosition == stepTextList!!.size - 1) {
                RectF(halfWidth - lineHeight / 2, dotViewHeight - lineHeight / 2,
                        halfWidth + itemWidth * activedPosition + lineHeight / 2, dotViewHeight + lineHeight / 2)
            } else {
                RectF(halfWidth - lineHeight / 2, dotViewHeight - lineHeight / 2,
                        halfWidth + itemWidth * activedPosition + itemWidth / 2 + lineHeight / 2, dotViewHeight + lineHeight / 2)
            }
            mPaint!!.color = activitedLineColor
            canvas.drawRoundRect(mRecfF, lineHeight / 2, lineHeight / 2, mPaint)
            // 绘制Dot
            for (index in stepTextList!!.indices) {
                if (index < activedPosition) {
                    mPaint!!.color = activedDotColor
                    canvas.drawCircle(halfWidth + itemWidth * index, dotViewHeight, activedDotRadius, mPaint)
                } else if (index == activedPosition) {
                    mPaint!!.color = activedDotColor
                    canvas.drawCircle(halfWidth + itemWidth * index, dotViewHeight, cureentDotRadius, mPaint)
                } else {
                    mPaint!!.color = dotColor
                    canvas.drawCircle(halfWidth + itemWidth * index, dotViewHeight, dotRadius, mPaint)
                    val rectfWidth = dotRadius / 2
                    val mRecfF = RectF(halfWidth + itemWidth * index - rectfWidth, dotViewHeight - rectfWidth,
                            halfWidth + itemWidth * index + rectfWidth, dotViewHeight + rectfWidth)
                    if (dotBitmap != null && !dotBitmap!!.isRecycled) {
                        val rect = Rect(0, 0, dotBitmap!!.width, dotBitmap!!.height)
                        canvas.drawBitmap(dotBitmap, rect, mRecfF, mPaint)
                    }
                }
            }
            // 绘制文字
            for (i in stepTextList!!.indices) {
                mPaint!!.isAntiAlias = true
                mPaint!!.textAlign = Paint.Align.CENTER
                if (i == selectedPosition) {
                    mPaint!!.textSize = selectedTextSize
                } else {
                    mPaint!!.textSize = textSize
                }
                if (i <= activedPosition) {
                    mPaint!!.color = activitedTextColor
                } else {
                    mPaint!!.color = textColor
                }
                val fontMetrics = mPaint!!.fontMetricsInt
                val baseline = (getFontHeight(Math.max(selectedTextSize, textSize)) - fontMetrics.bottom - fontMetrics.top) / 2 + dotViewHeight * 2
                canvas.drawText(stepTextList!![i], itemWidth * i + halfWidth, baseline, mPaint)
            }
        }
        super.onDraw(canvas)
    }

    fun scrollTo(position: Int) {
        if (position >= 0 && position < stepTextList!!.size) {
            if (position != selectedPosition) {
                val scrollDistance = (position - selectedPosition) * itemWidth
                val scrollInt = scrollDistance.toInt()
                scroller!!.startScroll(currentX, 0, scrollInt, 0)
                selectedPosition = position
                currentX += scrollDistance.toInt()
                invalidate() //进行下段位移
            }
        }
    }

    override fun computeScroll() {
        super.computeScroll()
        if (scroller != null) {
            if (scroller!!.computeScrollOffset()) { //判断scroll是否完成
                scrollTo(scroller!!.currX, scroller!!.currY) //执行本段位移
                invalidate() //进行下段位移
            }
        }
    }

    fun getFontHeight(fontSize: Float): Float {
        val paint = Paint()
        paint.textSize = fontSize
        val fm = paint.fontMetrics
        return Math.ceil(fm.descent - fm.ascent.toDouble()).toFloat()
    }

    init {
        initAttr(context, attrs)
    }
}