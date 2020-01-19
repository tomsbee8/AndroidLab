package cn.blinkdagger.androidLab.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import cn.blinkdagger.androidLab.R
import java.util.*

/**
 * 类描述：波纹扩散的View
 * 创建人：ls
 * 创建时间：2017/10/23
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
class RippleDiffuseView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = -1) : View(context, attrs, defStyleAttr) {
    /**
     * 扩散圆颜色
     */
    private var mColor = resources.getColor(R.color.main_color)
    /**
     * 圆圈中心图片
     */
    private var mBitmap: Bitmap? = null
    /**
     * 圆圈中心图片宽度
     */
    private val imageWidth: Int
    /**
     * 圆圈中心图片高度
     */
    private val imageHeight: Int
    /**
     * 扩散圆最小半径
     */
    private var minRadius = 0
    /**
     * 扩散圆最大半径
     */
    private var maxRadius = 0
    /**
     * 扩散圆个数
     */
    private var maxCircleCount = 3
    /**
     * 圆环宽度
     */
    private var mDiffuseStrokeWidth = 1
    /**
     * 是否扩散中
     */
    /**
     * 是否正在扩散中
     */
    var isDiffuse = false
        private set
    // 扩散圆透明度集合【颜色会逐渐变淡】
    private val mAlphaArray: MutableList<Int> = ArrayList()
    // 扩散圆半径集合【半径会逐渐变大】
    private val mRadiusArray: MutableList<Int> = ArrayList()
    // 扩散圆宽度集合【宽度会逐渐变小】
    private val mStrokeWidthArray: MutableList<Float> = ArrayList()
    private var mPaint: Paint? = null
    private var srcRec: Rect? = null
    private var dstRec: Rect? = null
    private fun init() {
        mPaint = Paint()
        mPaint!!.isAntiAlias = true
        mPaint!!.style = Paint.Style.STROKE
        mAlphaArray.add(255)
        mRadiusArray.add(minRadius)
        mStrokeWidthArray.add(0.0f + mDiffuseStrokeWidth)
    }

    override fun invalidate() {
        if (hasWindowFocus()) {
            super.invalidate()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // 扩散圆半径最大不超过控件宽高度
        if (maxRadius == 0 || maxRadius > measuredHeight / 2 || maxRadius > measuredWidth / 2) {
            maxRadius = if (measuredHeight > measuredWidth) measuredWidth / 2 else measuredHeight / 2
        }
    }

    public override fun onDraw(canvas: Canvas) {
        if (isDiffuse) { // 绘制扩散圆
            mPaint!!.color = mColor
            for (i in mAlphaArray.indices) { // 设置透明度
                val alpha = mAlphaArray[i]
                mPaint!!.alpha = alpha
                // 设置宽度
                val strokeWidth = mStrokeWidthArray[i]
                mPaint!!.strokeWidth = strokeWidth
                // 绘制扩散圆
                val radius = mRadiusArray[i]
                canvas.drawCircle(measuredWidth / 2.toFloat(), measuredHeight / 2.toFloat(), radius.toFloat(), mPaint)
                // 扩散速度在此处控制，暂时不可在XML控制.
                if (alpha > 1 && radius < maxRadius) {
                    mAlphaArray[i] = alpha - 2
                    mRadiusArray[i] = radius + 1
                    mStrokeWidthArray[i] = if (strokeWidth - 0.02f > 0) strokeWidth - 0.02f else 1f
                }
            }
            // 判断当扩散圆扩散到指定宽度时添加新扩散圆
            if (mRadiusArray[mRadiusArray.size - 1] == (maxRadius - minRadius) / maxCircleCount + minRadius) {
                mRadiusArray.add(minRadius)
                mAlphaArray.add(255)
                mStrokeWidthArray.add(0f + mDiffuseStrokeWidth)
            }
            // 超过最大扩散圆个数，删除最外层
            if (mRadiusArray.size > maxCircleCount) {
                mRadiusArray.removeAt(0)
                mAlphaArray.removeAt(0)
                mStrokeWidthArray.removeAt(0)
            }
        }
        // 绘制中心图片
        mPaint!!.alpha = 255
        if (mBitmap != null) {
            srcRec = Rect(0, 0, mBitmap!!.width, mBitmap!!.height)
            dstRec = Rect((width - imageWidth) / 2, (height - imageHeight) / 2, (width + imageWidth) / 2,
                    (height + imageHeight) / 2)
            canvas.drawBitmap(mBitmap, srcRec, dstRec, mPaint)
        }
        if (isDiffuse) {
            invalidate()
        }
    }

    /**
     * 开始扩散
     */
    fun start() {
        isDiffuse = true
        invalidate()
    }

    /**
     * 停止扩散
     */
    fun stop() {
        isDiffuse = false
    }

    /**
     * 设置扩散圆颜色
     */
    fun setColor(@ColorRes colorId: Int) {
        mColor = colorId
    }

    /**
     * 设置中心图片
     *
     * @param imageId
     */
    fun setImageSrc(@DrawableRes imageId: Int) {
        mBitmap = BitmapFactory.decodeResource(resources, imageId)
    }

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.RippleDiffuseView, defStyleAttr, 0)
        mColor = a.getColor(R.styleable.RippleDiffuseView_color, mColor)
        imageHeight = a.getDimensionPixelSize(R.styleable.RippleDiffuseView_imageHeight, 0)
        imageWidth = a.getDimensionPixelSize(R.styleable.RippleDiffuseView_imageWidth, 0)
        minRadius = a.getDimensionPixelSize(R.styleable.RippleDiffuseView_minRadius, if (imageHeight > imageWidth) imageHeight / 2 else imageWidth / 2)
        maxRadius = a.getDimensionPixelSize(R.styleable.RippleDiffuseView_maxRadius, 100)
        maxCircleCount = a.getInteger(R.styleable.RippleDiffuseView_maxCircleCount, maxCircleCount)
        mDiffuseStrokeWidth = a.getDimensionPixelSize(R.styleable.RippleDiffuseView_strokeWidth, mDiffuseStrokeWidth)
        val imageId = a.getResourceId(R.styleable.RippleDiffuseView_imageSrc, -1)
        if (imageId != -1) {
            mBitmap = BitmapFactory.decodeResource(resources, imageId)
        }
        a.recycle()
        init()
    }
}