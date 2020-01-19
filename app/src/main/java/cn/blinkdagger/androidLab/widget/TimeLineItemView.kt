package cn.blinkdagger.androidLab.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import cn.blinkdagger.androidLab.R
import cn.blinkdagger.androidLab.utils.DensityUtil

/**
 * 类描述：时间线Item(冰糖葫芦)
 * 创建人：ls
 * 创建时间：2016/11/22
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
class TimeLineItemView : View {
    //点的半径
    private var dotRadius = 0f
    //线的宽度（不可以超过点的直径）
    private var lineWidth = 0f
    //点的颜色
    private var dotColor = 0
    //线的颜色
    private var lineColor = 0
    //顶部线的高度
    private var topLineHeight = 0f
    //底部线的高度
    private var bottomLineHeight = 0f
    //底部线是否可见
    private var bottomLineVisiable = true
    //顶部线是否可见
    private var topLineVisiable = true

    constructor(context: Context?) : super(context) {}
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initAttr(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttr(context, attrs)
    }

    private fun initAttr(context: Context, attrs: AttributeSet) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.TimeLineItemView)
        dotRadius = a.getDimension(R.styleable.TimeLineItemView_dot_radius, DensityUtil.dp2px(context, 8f).toFloat())
        dotColor = a.getColor(R.styleable.TimeLineItemView_dot_color, Color.parseColor("#E1E1E1"))
        lineWidth = a.getDimension(R.styleable.TimeLineItemView_line_width, DensityUtil.dp2px(context, 6f).toFloat())
        lineColor = a.getColor(R.styleable.TimeLineItemView_line_color, Color.parseColor("#E1E1E1"))
        topLineHeight = a.getDimension(R.styleable.TimeLineItemView_top_line_height, 0f)
        bottomLineHeight = a.getDimension(R.styleable.TimeLineItemView_bottom_line_height, 0f)
        a.recycle()
        if (lineWidth > dotRadius * 2) {
            lineWidth = dotRadius * 2
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var width = MeasureSpec.getSize(widthMeasureSpec)
        var height = MeasureSpec.getSize(heightMeasureSpec)
        if (widthMode == MeasureSpec.AT_MOST) {
            width = (dotRadius * 2 + paddingLeft + paddingRight).toInt()
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            height = (topLineHeight + bottomLineHeight + dotRadius * 2).toInt()
        } else if (heightMode == MeasureSpec.EXACTLY) {
            if (topLineHeight + bottomLineHeight + dotRadius * 2 < height) {
                bottomLineHeight = height - topLineHeight - dotRadius * 2
            }
        }
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        val dotPaint = Paint()
        val linePaint = Paint()
        dotPaint.color = dotColor
        dotPaint.isAntiAlias = true
        linePaint.color = lineColor
        linePaint.isAntiAlias = true
        val startHeight = Math.sqrt(dotRadius * dotRadius - lineWidth * lineWidth / 4.0).toInt()
        val sinValue = lineWidth * 0.5 / dotRadius
        val halfAngle = Math.asin(sinValue) / Math.PI * 180
        val topRectF = RectF(paddingLeft.toFloat(), topLineHeight, paddingLeft + dotRadius * 2, topLineHeight + dotRadius * 2)
        //画上部分线
        val path = Path()
        if (topLineVisiable) {
            path.moveTo(paddingLeft + dotRadius - lineWidth / 2, 0f)
            path.lineTo(paddingLeft + dotRadius + lineWidth / 2, 0f)
            path.lineTo(paddingLeft + dotRadius + lineWidth / 2, topLineHeight + dotRadius - startHeight)
            path.arcTo(topRectF, 270 + halfAngle.toFloat(), halfAngle.toFloat() * -2)
            path.lineTo(paddingLeft + dotRadius - lineWidth / 2, 0f)
            path.close()
            canvas.drawPath(path, linePaint)
        }
        //画圆
        canvas.drawCircle(paddingLeft + dotRadius, topLineHeight + dotRadius, dotRadius, dotPaint)
        //画下部分线
        if (bottomLineVisiable) {
            path.moveTo(paddingLeft + dotRadius - lineWidth / 2, dotRadius * 2 + topLineHeight + startHeight)
            path.arcTo(topRectF, 90 + halfAngle.toFloat(), halfAngle.toFloat() * -2)
            path.lineTo(paddingLeft + dotRadius + lineWidth / 2, measuredHeight.toFloat())
            path.lineTo(paddingLeft + dotRadius - lineWidth / 2, measuredHeight.toFloat())
            path.lineTo(paddingLeft + dotRadius - lineWidth / 2, dotRadius * 2 + topLineHeight + startHeight)
            path.close()
            canvas.drawPath(path, linePaint)
        }
    }

    fun setDotColor(dotColor: Int) {
        this.dotColor = dotColor
        invalidate()
    }

    fun setTopLineHeight(topLineHeight: Int) {
        this.topLineHeight = topLineHeight.toFloat()
        invalidate()
    }

    fun setBottomLineHeight(bottomLineHeight: Int) {
        this.bottomLineHeight = bottomLineHeight.toFloat()
        invalidate()
    }

    fun setBottomLineVisiable(bottomLineVisiable: Boolean) {
        this.bottomLineVisiable = bottomLineVisiable
        invalidate()
    }

    fun setTopLineVisiable(topLineVisiable: Boolean) {
        this.topLineVisiable = topLineVisiable
        invalidate()
    }
}