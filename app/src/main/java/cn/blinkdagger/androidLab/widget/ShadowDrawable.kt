package cn.blinkdagger.androidLab.widget

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat


/**
 * @Author ls
 * @Date  2020/9/1
 * @Description 支持渐变阴影的Drawable
 * @Version
 */
class ShadowDrawable private constructor(shapeRadius: Float, shadowRadius: Int, bgColor: Int, shadowColor: Int, offsetX: Int, offsetY: Int) : Drawable() {

    private var mShadowPaint: Paint = Paint()
    private var mBgPaint: Paint = Paint()

    private val mShadowRadius = shadowRadius
    private val mShapeRadius = shapeRadius
    private val mBgColor: Int = bgColor
    private var mShadowColor: Int = shadowColor
    private var mRect: RectF = RectF()

    init {
        /**
         * 阴影paint
         */
        mShadowPaint.color = Color.TRANSPARENT
        mShadowPaint.isAntiAlias = true
        mShadowPaint.setStyle(Paint.Style.FILL)
        mShadowPaint.setShadowLayer(mShadowRadius.toFloat(), offsetX.toFloat(), offsetY.toFloat(), mShadowColor)
        /**
         * 背景paint
         */
        mBgPaint.isAntiAlias = true
        mBgPaint.color = mBgColor
    }

    override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {

        val newLeft = left + mShadowRadius - 0
        val newTop = top + mShadowRadius - 0
        val newRight = right - mShadowRadius - 2
        val newBottom = bottom - mShadowRadius - 2
        super.setBounds(left, top, right, bottom)

        mRect = RectF(newLeft.toFloat(), newTop.toFloat(), newRight.toFloat(), newBottom.toFloat())
    }

    override fun draw(canvas: Canvas) {

        canvas.drawRoundRect(mRect, mShapeRadius, mShapeRadius, mShadowPaint)
        canvas.drawRoundRect(mRect, mShapeRadius, mShapeRadius, mBgPaint)
    }

    override fun setAlpha(alpha: Int) {
        mShadowPaint.alpha = alpha;
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        mShadowPaint.colorFilter = colorFilter
    }

    class Builder(context: Context) {
        private val mContext: Context = context

        private var mShapeRadius = 0f
        private var mBgColor: Int = Color.WHITE

        private var mShadowRadius = 0
        private var mShadowColor: Int = Color.LTGRAY

        private var mOffsetX: Int = 0
        private var mOffsetY: Int = 0

        fun setBackgroundRadius(shapeRadius: Float): Builder {
            this.mShapeRadius = shapeRadius
            return this
        }

        fun setBackgroundColor(@ColorRes bgColor: Int): Builder {
            this.mBgColor = ContextCompat.getColor(mContext,bgColor)
            return this
        }

        fun setShadowRadius(shadowRadius: Int): Builder {
            this.mShadowRadius = shadowRadius
            return this
        }

        fun setShadowColor(@ColorRes shadowColor: Int): Builder {
            this.mShadowColor = ContextCompat.getColor(mContext,shadowColor)
            return this
        }

        fun offsetX(offsetX: Int): Builder {
            this.mOffsetX = offsetX
            return this
        }

        fun offsetY(offsetY: Int): Builder {
            this.mOffsetY = offsetY
            return this
        }

        fun build(): ShadowDrawable {
            return ShadowDrawable(mShapeRadius, mShadowRadius, mBgColor, mShadowColor, mOffsetX, mOffsetY)
        }

        fun into(view: View) {
            view.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
            ViewCompat.setBackground(view, ShadowDrawable(mShapeRadius, mShadowRadius, mBgColor, mShadowColor, mOffsetX, mOffsetY))
        }
    }
}