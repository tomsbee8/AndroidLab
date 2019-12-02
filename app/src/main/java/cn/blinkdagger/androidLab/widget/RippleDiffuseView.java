package cn.blinkdagger.androidLab.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.View;


import cn.blinkdagger.androidLab.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：波纹扩散的View
 * 创建人：ls
 * 创建时间：2017/10/23
 * 修改人：
 * 修改时间：
 * 修改备注：
 */

public class RippleDiffuseView extends View {

    /**
     * 扩散圆颜色
     */
    private int mColor = getResources().getColor(R.color.main_color);
    /**
     * 圆圈中心图片
     */
    private Bitmap mBitmap;
    /**
     * 圆圈中心图片宽度
     */
    private int imageWidth;
    /**
     * 圆圈中心图片高度
     */
    private int imageHeight;

    /**
     * 扩散圆最小半径
     */
    private int minRadius = 0;

    /**
     * 扩散圆最大半径
     */
    private int maxRadius = 0;

    /**
     * 扩散圆个数
     */
    private int maxCircleCount = 3;

    /**
     * 圆环宽度
     */
    private int mDiffuseStrokeWidth = 1;

    /**
     * 是否正在扩散中
     */
    private boolean mIsDiffuse = false;

    // 扩散圆透明度集合【颜色会逐渐变淡】
    private List<Integer> mAlphaArray = new ArrayList<>();
    // 扩散圆半径集合【半径会逐渐变大】
    private List<Integer> mRadiusArray = new ArrayList<>();
    // 扩散圆宽度集合【宽度会逐渐变小】
    private List<Float> mStrokeWidthArray = new ArrayList<>();
    private Paint mPaint;

    private Rect srcRec;
    private Rect dstRec;

    public RippleDiffuseView(Context context) {
        this(context, null);
    }

    public RippleDiffuseView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public RippleDiffuseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RippleDiffuseView, defStyleAttr, 0);
        mColor = a.getColor(R.styleable.RippleDiffuseView_color, mColor);
        imageHeight = a.getDimensionPixelSize(R.styleable.RippleDiffuseView_imageHeight, 0);
        imageWidth = a.getDimensionPixelSize(R.styleable.RippleDiffuseView_imageWidth, 0);
        minRadius = a.getDimensionPixelSize(R.styleable.RippleDiffuseView_minRadius, imageHeight > imageWidth ? imageHeight / 2 : imageWidth / 2);
        maxRadius = a.getDimensionPixelSize(R.styleable.RippleDiffuseView_maxRadius, 100);
        maxCircleCount = a.getInteger(R.styleable.RippleDiffuseView_maxCircleCount, maxCircleCount);
        mDiffuseStrokeWidth = a.getDimensionPixelSize(R.styleable.RippleDiffuseView_strokeWidth, mDiffuseStrokeWidth);
        int imageId = a.getResourceId(R.styleable.RippleDiffuseView_imageSrc, -1);
        if (imageId != -1) {
            mBitmap = BitmapFactory.decodeResource(getResources(), imageId);
        }
        a.recycle();

        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);

        mAlphaArray.add(255);
        mRadiusArray.add(minRadius);
        mStrokeWidthArray.add(0.0f + mDiffuseStrokeWidth);
    }

    @Override
    public void invalidate() {
        if (hasWindowFocus()) {
            super.invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 扩散圆半径最大不超过控件宽高度
        if (maxRadius == 0 || maxRadius > getMeasuredHeight() / 2 || maxRadius > getMeasuredWidth() / 2) {
            maxRadius = getMeasuredHeight() > getMeasuredWidth() ? getMeasuredWidth() / 2 : getMeasuredHeight() / 2;
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (mIsDiffuse) {
            // 绘制扩散圆
            mPaint.setColor(mColor);
            for (int i = 0; i < mAlphaArray.size(); i++) {
                // 设置透明度
                Integer alpha = mAlphaArray.get(i);
                mPaint.setAlpha(alpha);
                // 设置宽度
                Float strokeWidth = mStrokeWidthArray.get(i);
                mPaint.setStrokeWidth(strokeWidth);

                // 绘制扩散圆
                Integer radius = mRadiusArray.get(i);
                canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, radius, mPaint);

                // 扩散速度在此处控制，暂时不可在XML控制.
                if (alpha > 1 && radius < maxRadius) {
                    mAlphaArray.set(i, alpha - 2);
                    mRadiusArray.set(i, radius + 1);
                    mStrokeWidthArray.set(i, strokeWidth - 0.02f > 0 ? strokeWidth - 0.02f : 1f);
                }
            }
            // 判断当扩散圆扩散到指定宽度时添加新扩散圆
            if (mRadiusArray.get(mRadiusArray.size() - 1) == ((maxRadius - minRadius) / maxCircleCount + minRadius)) {
                mRadiusArray.add(minRadius);
                mAlphaArray.add(255);
                mStrokeWidthArray.add(0f + mDiffuseStrokeWidth);
            }
            // 超过最大扩散圆个数，删除最外层
            if (mRadiusArray.size() > maxCircleCount) {
                mRadiusArray.remove(0);
                mAlphaArray.remove(0);
                mStrokeWidthArray.remove(0);
            }
        }

        // 绘制中心图片
        mPaint.setAlpha(255);
        if (mBitmap != null) {
            srcRec = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
            dstRec = new Rect((getWidth() - imageWidth) / 2, (getHeight() - imageHeight) / 2, (getWidth() + imageWidth) / 2,
                    (getHeight() + imageHeight) / 2);
            canvas.drawBitmap(mBitmap, srcRec, dstRec, mPaint);
        }

        if (mIsDiffuse) {
            invalidate();
        }
    }

    /**
     * 开始扩散
     */
    public void start() {
        mIsDiffuse = true;
        invalidate();
    }

    /**
     * 停止扩散
     */
    public void stop() {
        mIsDiffuse = false;
    }

    /**
     * 是否扩散中
     */
    public boolean isDiffuse() {
        return mIsDiffuse;
    }

    /**
     * 设置扩散圆颜色
     */
    public void setColor(@ColorRes int colorId) {
        mColor = colorId;
    }

    /**
     * 设置中心图片
     *
     * @param imageId
     */
    public void setImageSrc(@DrawableRes int imageId) {
        mBitmap = BitmapFactory.decodeResource(getResources(), imageId);
    }
}
