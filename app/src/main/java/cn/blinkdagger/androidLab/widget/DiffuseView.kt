package cn.blinkdagger.androidLab.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import cn.blinkdagger.androidLab.R
import java.util.*

/**
 * 类描述：圆形扩散的View
 * 创建人：ls
 * 创建时间：2016/11/22
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
class DiffuseView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = -1) : View(context, attrs, defStyleAttr) {
    /** 扩散圆圈颜色  */
    private var mColor = resources.getColor(R.color.main_color)
    /** 圆圈中心颜色  */
    private var mCoreColor = resources.getColor(R.color.main_color)
    /** 圆圈中心图片  */
    private var mBitmap: Bitmap? = null
    /** 中心圆半径  */
    private var mCoreRadius = 150f
    /** 扩散圆宽度  */
    private var mDiffuseWidth = 3
    /** 最大宽度  */
    private var mMaxWidth = 255
    /**
     * 是否扩散中
     */
    /** 是否正在扩散中  */
    var isDiffuse = false
        private set
    // 透明度集合
    private val mAlphas: MutableList<Int> = ArrayList()
    // 扩散圆半径集合
    private val mWidths: MutableList<Int> = ArrayList()
    private var mPaint: Paint? = null
    private fun init() {
        mPaint = Paint()
        mPaint!!.isAntiAlias = true
        mAlphas.add(255)
        mWidths.add(0)
    }

    override fun invalidate() {
        if (hasWindowFocus()) {
            super.invalidate()
        }
    }

    public override fun onDraw(canvas: Canvas) {
        if (isDiffuse) { // 绘制扩散圆
            mPaint!!.color = mColor
            for (i in mAlphas.indices) { // 设置透明度
                val alpha = mAlphas[i]
                mPaint!!.alpha = alpha
                // 绘制扩散圆
                val width = mWidths[i]
                canvas.drawCircle(getWidth() / 2.toFloat(), height / 2.toFloat(), mCoreRadius + width, mPaint)
                if (alpha > 0 && width < mMaxWidth) {
                    mAlphas[i] = alpha - 1
                    mWidths[i] = width + 1
                }
            }
            // 判断当扩散圆扩散到指定宽度时添加新扩散圆
            if (mWidths[mWidths.size - 1] == mMaxWidth / mDiffuseWidth) {
                mAlphas.add(255)
                mWidths.add(0)
            }
            // 超过10个扩散圆，删除最外层
            if (mWidths.size >= 4) {
                mWidths.removeAt(0)
                mAlphas.removeAt(0)
            }
        }
        // 绘制中心圆及图片
        mPaint!!.alpha = 255
        mPaint!!.color = mCoreColor
        canvas.drawCircle(width / 2.toFloat(), height / 2.toFloat(), mCoreRadius, mPaint)
        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap, width / 2 - mBitmap!!.width / 2
                    .toFloat(), height / 2 - mBitmap!!.height / 2.toFloat(), mPaint)
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
    fun setColor(colorId: Int) {
        mColor = colorId
    }

    /**
     * 设置中心圆颜色
     */
    fun setCoreColor(colorId: Int) {
        mCoreColor = colorId
    }

    /**
     * 设置中心圆图片
     */
    fun setCoreImage(imageId: Int) {
        mBitmap = BitmapFactory.decodeResource(resources, imageId)
    }

    /**
     * 设置中心圆半径
     */
    fun setCoreRadius(radius: Int) {
        mCoreRadius = radius.toFloat()
    }

    /**
     * 设置扩散圆宽度(值越小宽度越大)
     */
    fun setDiffuseWidth(width: Int) {
        mDiffuseWidth = width
    }

    /**
     * 设置最大宽度
     */
    fun setMaxWidth(maxWidth: Int) {
        mMaxWidth = maxWidth
    }

    init {
        init()
        val a = context.obtainStyledAttributes(attrs, R.styleable.DiffuseView, defStyleAttr, 0)
        mColor = a.getColor(R.styleable.DiffuseView_diffuse_color, mColor)
        mCoreColor = a.getColor(R.styleable.DiffuseView_diffuse_coreColor, mCoreColor)
        mCoreRadius = a.getDimension(R.styleable.DiffuseView_diffuse_coreRadius, mCoreRadius)
        mDiffuseWidth = a.getDimensionPixelSize(R.styleable.DiffuseView_diffuse_width, mDiffuseWidth)
        mMaxWidth = a.getDimensionPixelSize(R.styleable.DiffuseView_diffuse_maxWidth, mMaxWidth)
        val imageId = a.getResourceId(R.styleable.DiffuseView_diffuse_coreImage, -1)
        if (imageId != -1) mBitmap = BitmapFactory.decodeResource(resources, imageId)
        a.recycle()
    }
}