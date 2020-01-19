package cn.blinkdagger.androidLab.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import cn.blinkdagger.androidLab.R
import cn.blinkdagger.androidLab.utils.DensityUtil

/**
 * 类描述：
 * 创建人：ls
 * 创建时间：2017/11/23
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
class TipView : View {
    //背景颜色
    private var bgColor = 0
    //文字颜色
    private var textColor = 0
    //文字大小
    private var textSize = 0f
    //
    private var rectHeight = 0f
    //圆角半径
    private var borderRadius = 0f
    //小三角形高度
    private var trangleHeight = 0f
    //小三角形底边长度
    private var trangleBottomLength = 0f
    //小三角形边距
    private var trangleMargin = 0f
    //小三角形相对位置
    private var trangleGravity = 0
    //小三角形默认高度
    private val DEFAULT_TRANGLE_HEIGHT = 10f
    //小三角形底边默认长度
    private val DEFAULT_TRANGLE_BOTTOM_LENGTH = 10f
    private var borderPaint: Paint? = null
    private var rectF: RectF? = null
    private var path: Path? = null
    private var textContent: String? = null

    constructor(context: Context?) : super(context) {}
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initAttr(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttr(context, attrs)
    }

    private fun initAttr(context: Context, attrs: AttributeSet) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.TipView)
        textColor = a.getColor(R.styleable.TipView_TipTextColor, Color.parseColor("#FFFFFF"))
        bgColor = a.getColor(R.styleable.TipView_TipBgColor, Color.parseColor("#333333"))
        textSize = a.getDimension(R.styleable.TipView_TipTextSize, DensityUtil.dp2px(context, 14f).toFloat())
        rectHeight = a.getDimension(R.styleable.TipView_TipRectHeight, 30f)
        borderRadius = a.getDimension(R.styleable.TipView_TipBorderRadius, 0f)
        trangleHeight = a.getDimension(R.styleable.TipView_trangleHeight, DEFAULT_TRANGLE_HEIGHT)
        trangleBottomLength = a.getDimension(R.styleable.TipView_trangleBottomLength, DEFAULT_TRANGLE_BOTTOM_LENGTH)
        trangleMargin = a.getDimension(R.styleable.TipView_trangleMargin, 0f)
        trangleGravity = a.getInt(R.styleable.TipView_trangleGravity, 0)
        textContent = a.getString(R.styleable.TipView_TipTextContent)
        a.recycle()
        borderPaint = Paint()
        rectF = RectF()
        path = Path()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) { // 高度由trangleHeight和rectHeight 确定，设置本身height将无效
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = (trangleHeight + rectHeight).toInt()
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        val width = measuredWidth
        val height = measuredHeight
        // 设置设置小三角形底边长度[限制其最大值]
        if (trangleBottomLength > width - 2 * borderRadius) {
            trangleBottomLength = width - 2 * borderRadius
        }
        // 设置小三角形边距[限制其最大值和最小值]
        if (trangleMargin < 0) {
            trangleMargin = 0f
        } else if (trangleMargin > width - 2 * borderRadius - trangleBottomLength) {
            trangleMargin = width - 2 * borderRadius - trangleBottomLength
        }
        borderPaint!!.style = Paint.Style.FILL
        borderPaint!!.isAntiAlias = true
        borderPaint!!.color = bgColor
        when (trangleGravity) {
            LeftTop -> {
                // 画圆角矩形
                rectF!![0f, trangleHeight, width.toFloat()] = height.toFloat()
                canvas.drawRoundRect(rectF, borderRadius, borderRadius, borderPaint)
                // 画三角形
                if (trangleBottomLength > 0 && trangleHeight > 0) {
                    path!!.moveTo(borderRadius + trangleMargin, trangleHeight)
                    path!!.lineTo(borderRadius + trangleMargin + trangleBottomLength / 2.0f, 0f)
                    path!!.lineTo(borderRadius + trangleMargin + trangleBottomLength, trangleHeight)
                    path!!.close()
                    canvas.drawPath(path, borderPaint)
                }
            }
            LeftBottom -> {
                // 画圆角矩形
                rectF!![0f, 0f, width.toFloat()] = rectHeight
                canvas.drawRoundRect(rectF, borderRadius, borderRadius, borderPaint)
                // 画三角形
                if (trangleBottomLength > 0 && trangleHeight > 0) {
                    path!!.moveTo(borderRadius + trangleMargin, rectHeight)
                    path!!.lineTo(borderRadius + trangleMargin + trangleBottomLength / 2.0f, height.toFloat())
                    path!!.lineTo(borderRadius + trangleMargin + trangleBottomLength, rectHeight)
                    path!!.close()
                    canvas.drawPath(path, borderPaint)
                }
            }
            RightTop -> {
                // 画圆角矩形
                rectF!![0f, trangleHeight, width.toFloat()] = height.toFloat()
                canvas.drawRoundRect(rectF, borderRadius, borderRadius, borderPaint)
                // 画三角形
                if (trangleBottomLength > 0 && trangleHeight > 0) {
                    path!!.moveTo(width - borderRadius - trangleMargin - trangleBottomLength, trangleHeight)
                    path!!.lineTo(width - borderRadius - trangleMargin - trangleBottomLength / 2.0f, 0f)
                    path!!.lineTo(width - borderRadius - trangleMargin, trangleHeight)
                    path!!.close()
                    canvas.drawPath(path, borderPaint)
                }
            }
            RightBottom -> {
                // 画圆角矩形
                rectF!![0f, 0f, width.toFloat()] = rectHeight
                canvas.drawRoundRect(rectF, borderRadius, borderRadius, borderPaint)
                // 画三角形
                if (trangleBottomLength > 0 && trangleHeight > 0) {
                    path!!.moveTo(width - borderRadius - trangleMargin - trangleBottomLength, rectHeight)
                    path!!.lineTo(width - borderRadius - trangleMargin - trangleBottomLength / 2.0f, height.toFloat())
                    path!!.lineTo(width - borderRadius - trangleMargin, rectHeight)
                    path!!.close()
                    canvas.drawPath(path, borderPaint)
                }
            }
        }
        // 画文字
        if (textContent != null) {
            borderPaint!!.isAntiAlias = true
            borderPaint!!.color = textColor
            borderPaint!!.textSize = textSize
            borderPaint!!.textAlign = Paint.Align.CENTER
            val fontMetrics = borderPaint!!.fontMetricsInt
            if (trangleGravity == LeftTop || trangleGravity == RightTop) {
                val baseline = (rectHeight - fontMetrics.bottom - fontMetrics.top) / 2 + trangleHeight
                canvas.drawText(textContent, width / 2.toFloat(), baseline, borderPaint)
            } else if (trangleGravity == LeftBottom || trangleGravity == RightBottom) {
                val baseline = (rectHeight - fontMetrics.bottom - fontMetrics.top) / 2
                canvas.drawText(textContent, width / 2.toFloat(), baseline, borderPaint)
            }
        }
    }

    companion object {
        private const val LeftTop = 1
        private const val LeftBottom = 2
        private const val RightTop = 3
        private const val RightBottom = 4
    }
}