package cn.blinkdagger.androidLab.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import cn.blinkdagger.androidLab.R
import cn.blinkdagger.androidLab.utils.DensityUtil
import java.util.*

/**
 * 类描述：
 * 创建人：ls
 * 创建时间：2017/11/23
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
class TipMenuView : View {
    //背景颜色
    private var bgColor = 0
    //文字颜色
    private var textColor = 0
    //文字大小
    private var textSize = 0f
    //Item矩形高度
    private var itemHeight = 0f
    //Item按压后的颜色
    private var itemPressedColor = 0
    //分割线高度
    private var separateLineHeight = 0f
    //分割线颜色
    private var separateLineColor = 0
    //圆角半径
    private var borderRadius = 0f
    //三角形高度
    private var trangleHeight = 0f
    //三角形底边长度
    private var trangleBottomLength = 0f
    //三角形边距
    private var trangleMargin = 0f
    //三角形相对矩形的方向位置
    private var trangleGravity = 0
    // 列表矩形的高度
    private var rectHeight = 0f
    //小三角形默认高度
    private val DEFAULT_TRANGLE_HEIGHT = 10f
    //小三角形底边默认长度
    private val DEFAULT_TRANGLE_BOTTOM_LENGTH = 10f
    private var borderPaint: Paint? = null
    private var rectF: RectF? = null
    private var path: Path? = null
    private var items: Array<String>? = arrayOf() //条目的个数
    private var onItemClickListener //点击事件
            : OnItemClickListener? = null

    constructor(context: Context?) : super(context) {}
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initAttr(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttr(context, attrs)
    }

    private fun initAttr(context: Context, attrs: AttributeSet) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.TipMenuView)
        textColor = a.getColor(R.styleable.TipMenuView_tipMenuTextColor, Color.parseColor("#FFFFFF"))
        bgColor = a.getColor(R.styleable.TipMenuView_tipMenuBgColor, Color.parseColor("#333333"))
        textSize = a.getDimension(R.styleable.TipMenuView_tipMenuTextSize, DensityUtil.dp2px(context, 14f).toFloat())
        itemHeight = a.getDimension(R.styleable.TipMenuView_tipMenuItemHeight, 30f)
        itemPressedColor = a.getColor(R.styleable.TipMenuView_tipMenuItemPressedColor, Color.parseColor("#333333"))
        separateLineHeight = a.getDimensionPixelOffset(R.styleable.TipMenuView_tipMenuSeparateLineHeight, 0).toFloat()
        separateLineColor = a.getColor(R.styleable.TipMenuView_tipMenuSeparateLineColor, Color.WHITE)
        borderRadius = a.getDimension(R.styleable.TipMenuView_tipMenuBorderRadius, 0f)
        trangleHeight = a.getDimension(R.styleable.TipMenuView_tipMenuTrangleHeight, DEFAULT_TRANGLE_HEIGHT)
        trangleBottomLength = a.getDimension(R.styleable.TipMenuView_tipMenuTrangleBottomLength, DEFAULT_TRANGLE_BOTTOM_LENGTH)
        trangleMargin = a.getDimension(R.styleable.TipMenuView_tipMenuTrangleMargin, 0f)
        trangleGravity = a.getInt(R.styleable.TipMenuView_tipMenuTrangleGravity, 0)
        a.recycle()
        borderPaint = Paint()
        rectF = RectF()
        path = Path()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        rectHeight = if (items!!.size > 1) {
            itemHeight * items!!.size + separateLineHeight * (items!!.size - 1)
        } else {
            itemHeight * items!!.size
        }
        // 高度由trangleHeight和rectHeight 确定，设置本身height将无效
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = (trangleHeight + rectHeight).toInt()
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        val width = measuredWidth
        val height = measuredHeight
        // 从新计算矩形高度
        rectHeight = if (items!!.size > 1) {
            itemHeight * items!!.size + separateLineHeight * (items!!.size - 1)
        } else {
            itemHeight * items!!.size
        }
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
        // 画文字和分割线
        if (items != null && items!!.size > 0) {
            for (i in items!!.indices) {
                borderPaint!!.reset()
                borderPaint!!.isAntiAlias = true
                borderPaint!!.color = textColor
                borderPaint!!.textSize = textSize
                borderPaint!!.textAlign = Paint.Align.CENTER
                val fontMetrics = borderPaint!!.fontMetricsInt
                // 画文字
                val targetTop = (itemHeight + separateLineHeight) * i
                val targetBottom = (itemHeight + separateLineHeight) * i + itemHeight
                if (trangleGravity == LeftTop || trangleGravity == RightTop) {
                    val baseline = (targetTop + targetBottom - fontMetrics.bottom - fontMetrics.top) / 2f + trangleHeight
                    canvas.drawText(items!![i], width / 2.toFloat(), baseline, borderPaint)
                } else if (trangleGravity == LeftBottom || trangleGravity == RightBottom) {
                    val baseline = (targetTop + targetBottom - fontMetrics.bottom - fontMetrics.top) / 2f
                    canvas.drawText(items!![i], width / 2.toFloat(), baseline, borderPaint)
                }
                // 画分割线
                if (i < items!!.size - 1) {
                    if (trangleGravity == LeftTop || trangleGravity == RightTop) {
                        rectF!![0f, (itemHeight + separateLineHeight) * i + itemHeight + trangleHeight, width.toFloat()] = (itemHeight + separateLineHeight) * (i + 1) + trangleHeight
                    } else if (trangleGravity == LeftBottom || trangleGravity == RightBottom) {
                        rectF!![0f, (itemHeight + separateLineHeight) * i + itemHeight, width.toFloat()] = (itemHeight + separateLineHeight) * (i + 1)
                    }
                    borderPaint!!.reset()
                    borderPaint!!.style = Paint.Style.FILL
                    borderPaint!!.color = separateLineColor
                    canvas.drawRect(rectF, borderPaint)
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val width = measuredWidth
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                run {
                    var i = 0
                    while (i < items!!.size) {
                        val rect = RectF(0f, (itemHeight + separateLineHeight) * i, width.toFloat(), (itemHeight + separateLineHeight) * i + itemHeight)
                        if (onItemClickListener != null && isPointInRect(PointF(event.x, event.y), RectF(rect))) { // 被按下时，更新背景视图
                            postInvalidate(rect.left.toInt(), rect.top.toInt(), rect.right.toInt(), rect.bottom.toInt())
                        }
                        i++
                    }
                }
                var i = 0
                while (i < items!!.size) {
                    val rect = RectF(0f, (itemHeight + separateLineHeight) * i, width.toFloat(), (itemHeight + separateLineHeight) * i + itemHeight)
                    if (onItemClickListener != null && isPointInRect(PointF(event.x, event.y), rect)) { // 触发点击事件
                        onItemClickListener!!.onItemClick(items!![i], i)
                    }
                    i++
                }
            }
            MotionEvent.ACTION_UP -> {
                var i = 0
                while (i < items!!.size) {
                    val rect = RectF(0f, (itemHeight + separateLineHeight) * i, width.toFloat(), (itemHeight + separateLineHeight) * i + itemHeight)
                    if (onItemClickListener != null && isPointInRect(PointF(event.x, event.y), rect)) {
                        onItemClickListener!!.onItemClick(items!![i], i)
                    }
                    i++
                }
            }
        }
        return super.onTouchEvent(event)
    }

    /**
     * 判断点是否处于矩形区域
     *
     * @param pointF
     * @param targetRect
     * @return
     */
    private fun isPointInRect(pointF: PointF, targetRect: RectF): Boolean {
        if (pointF.x < targetRect.left) {
            return false
        }
        if (pointF.x > targetRect.right) {
            return false
        }
        if (pointF.y < targetRect.top) {
            return false
        }
        return pointF.y <= targetRect.bottom
    }

    /**
     * 设置列表数据
     *
     * @param items
     */
    fun setItems(vararg items: String) {
        this.items = Arrays.copyOf(items,items.size)
        invalidate()
    }

    /**
     * 设置选项点击事件
     *
     * @param itemClickListener
     */
    fun setOnItemClickListener(itemClickListener: OnItemClickListener?) {
        onItemClickListener = itemClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(name: String?, position: Int)
    }

    companion object {
        private const val LeftTop = 1
        private const val LeftBottom = 2
        private const val RightTop = 3
        private const val RightBottom = 4
    }
}