package cn.blinkdagger.androidLab.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Scroller;

import java.util.List;

import cn.blinkdagger.androidLab.R;
import cn.blinkdagger.androidLab.utils.DensityUtil;


/**
 * @Author ls
 * @Date 2018/11/9
 * @Description
 * @Version
 */
public class HorizontalStepView extends View {

    private List<String> stepTextList;// Step 数据列表

    // 点的选中状态影响文字样式
    private int selectedPosition;          // 选中的点的位置
    private float selectedTextSize;         // 选中的文字的大小
    private float textSize;                  // 未选中的文字的大小
    private int textColor;                 // 未激活的文字的颜色
    private int activitedTextColor;        // 激活的文字的颜色

    // 点的激活状态影响圆点View的样式
    private int activedPosition;           // 激活的圆点的位置
    private int activedDotColor;           // 激活的圆点的背景颜色
    private float activedDotRadius;          // 激活的圆点的半径
    private float cureentDotRadius;        // 激活的最后的圆点的半径
    private float dotRadius;                 // 未激活的圆点的半径
    private int dotColor;                  // 未激活的圆点的背景颜色
    private Bitmap dotBitmap;                  // 未激活的圆点的背景
    private int shadowColor;                  // 圆点背景颜色
    private float shadowWidth;                // 圆点背景宽度

    private int lineColor;                 // 线条颜色
    private int activitedLineColor;          // 线条颜色
    private float lineHeight;                // 线条高度
    private float itemWidth;               // item 宽度

    private Paint mPaint;
    private RectF mRecfF;

    private Scroller scroller;
    // 当前 X坐标位置
    private int currentX = 0;

    public HorizontalStepView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
    }

    private void initAttr(Context context, AttributeSet attrs) {

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HorizontalStepView);
        selectedTextSize = a.getDimension(R.styleable.HorizontalStepView_selectedTextSize, DensityUtil.dp2px(context, 12));
        activitedTextColor = a.getColor(R.styleable.HorizontalStepView_activedTextColor, Color.parseColor("#333333"));
        textSize = a.getDimension(R.styleable.HorizontalStepView_normalTextSize, DensityUtil.dp2px(context, 12));
        textColor = a.getColor(R.styleable.HorizontalStepView_normalTextColor, Color.parseColor("#666666"));
        activedDotColor = a.getColor(R.styleable.HorizontalStepView_activedDotColor, Color.parseColor("#000000"));
        activedDotRadius = a.getDimension(R.styleable.HorizontalStepView_activedDotRadius, DensityUtil.dp2px(context, 3));
        cureentDotRadius = a.getDimension(R.styleable.HorizontalStepView_currentDotRadius, DensityUtil.dp2px(context, 3));
        dotRadius = a.getDimension(R.styleable.HorizontalStepView_normalDotRadius, DensityUtil.dp2px(context, 4));
        dotColor = a.getColor(R.styleable.HorizontalStepView_normalDotColor, Color.parseColor("#000000"));
        lineColor = a.getColor(R.styleable.HorizontalStepView_lineColor, Color.parseColor("#F1F1F1"));
        activitedLineColor = a.getColor(R.styleable.HorizontalStepView_activatedLineColor, Color.parseColor("#666666"));
        lineHeight = a.getDimension(R.styleable.HorizontalStepView_lineHeight, DensityUtil.dp2px(context, 2));
        itemWidth = a.getDimension(R.styleable.HorizontalStepView_itemWidth, DensityUtil.dp2px(context, 100));
        shadowColor = a.getColor(R.styleable.HorizontalStepView_shadowColor, Color.parseColor("#666666"));
        shadowWidth = a.getDimension(R.styleable.HorizontalStepView_shadowWidth, DensityUtil.dp2px(context, 0));
        int dotImageResId = a.getResourceId(R.styleable.HorizontalStepView_dotImage, -1);

        if (dotImageResId != -1) {
            dotBitmap = BitmapFactory.decodeResource(getResources(), dotImageResId);
        }
        a.recycle();

        mPaint = new Paint();
        scroller = new Scroller(context);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = 0;

        if (heightMode == MeasureSpec.EXACTLY) {
            heightSize = MeasureSpec.getSize(heightMeasureSpec);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            float maxDotHeight = Math.max(Math.max(activedDotRadius, dotRadius), cureentDotRadius) * 2 + shadowWidth * 2;
            int minHeight = (int) (getFontHeight(Math.max(selectedTextSize, textSize)) + maxDotHeight);
            heightSize = Math.min(MeasureSpec.getSize(heightMeasureSpec), minHeight);
        } else if (heightMode == MeasureSpec.UNSPECIFIED) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        setMeasuredDimension(widthSize, heightSize);
//        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
    }

    public void setStepTextList(List<String> stepTextList) {
        this.stepTextList = stepTextList;
        invalidate();
    }


    /**
     * 设置 激活的阶段位置
     *
     * @param activedPosition
     */
    public void setActivedPosition(int activedPosition) {
        if (activedPosition > 0 && activedPosition < stepTextList.size()) {
            this.activedPosition = activedPosition;
            invalidate();
        }
    }

    /**
     * 设置 选中的圆点位置
     *
     * @param position
     */
    public void setSelectedPosition(int position) {
        if (stepTextList.isEmpty() || selectedPosition == position) {
            return;
        }
        if (position > 0 && position < stepTextList.size()) {
            scrollTo(position);
        }
    }


    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {

        if (stepTextList != null && !stepTextList.isEmpty()) {

            int halfWidth = getWidth() / 2;

            float dotViewHeight = Math.max(Math.max(activedDotRadius, dotRadius), cureentDotRadius) + shadowWidth;
            // 绘制圆点的背景
            if (shadowWidth > 0) {
                for (int index = 0; index < stepTextList.size(); index++) {
                    mPaint.setStyle(Paint.Style.FILL);
                    mPaint.setAntiAlias(true);
                    mPaint.setColor(shadowColor);
                    if (index < activedPosition) {
                        canvas.drawCircle(halfWidth + itemWidth * index, dotViewHeight, activedDotRadius + shadowWidth, mPaint);
                    } else if (index == activedPosition) {
                        canvas.drawCircle(halfWidth + itemWidth * index, dotViewHeight, cureentDotRadius + shadowWidth, mPaint);
                    }
                }
            }

            // 绘制背景线条
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(lineColor);
            float bgWidth = halfWidth + itemWidth * (stepTextList.size() - 1);
            canvas.drawRect(halfWidth, dotViewHeight - lineHeight / 2, bgWidth, dotViewHeight + lineHeight / 2, mPaint);

            // 绘制激活的线条
            if (activedPosition == stepTextList.size() - 1) {
                mRecfF = new RectF(halfWidth - lineHeight / 2, dotViewHeight - lineHeight / 2,
                        halfWidth + itemWidth * activedPosition + lineHeight / 2, dotViewHeight + lineHeight / 2);
            } else {
                mRecfF = new RectF(halfWidth - lineHeight / 2, dotViewHeight - lineHeight / 2,
                        halfWidth + itemWidth * activedPosition + itemWidth / 2 + lineHeight / 2, dotViewHeight + lineHeight / 2);
            }
            mPaint.setColor(activitedLineColor);
            canvas.drawRoundRect(mRecfF, lineHeight / 2, lineHeight / 2, mPaint);

            // 绘制Dot
            for (int index = 0; index < stepTextList.size(); index++) {
                if (index < activedPosition) {
                    mPaint.setColor(activedDotColor);
                    canvas.drawCircle(halfWidth + itemWidth * index, dotViewHeight, activedDotRadius, mPaint);
                } else if (index == activedPosition) {
                    mPaint.setColor(activedDotColor);
                    canvas.drawCircle(halfWidth + itemWidth * index, dotViewHeight, cureentDotRadius, mPaint);
                } else {
                    mPaint.setColor(dotColor);
                    canvas.drawCircle(halfWidth + itemWidth * index, dotViewHeight, dotRadius, mPaint);
                    float rectfWidth = (float) dotRadius / 2;
                    RectF mRecfF = new RectF(halfWidth + itemWidth * index - rectfWidth, dotViewHeight - rectfWidth,
                            halfWidth + itemWidth * index + rectfWidth, dotViewHeight + rectfWidth);
                    if (dotBitmap != null && !dotBitmap.isRecycled()) {
                        Rect rect = new Rect(0, 0, dotBitmap.getWidth(), dotBitmap.getHeight());
                        canvas.drawBitmap(dotBitmap, rect, mRecfF, mPaint);
                    }

                }
            }

            // 绘制文字
            for (int i = 0; i < stepTextList.size(); i++) {
                mPaint.setAntiAlias(true);
                mPaint.setTextAlign(Paint.Align.CENTER);
                if (i == selectedPosition) {
                    mPaint.setTextSize(selectedTextSize);
                } else {
                    mPaint.setTextSize(textSize);
                }
                if (i <= activedPosition) {
                    mPaint.setColor(activitedTextColor);
                } else {
                    mPaint.setColor(textColor);
                }
                Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
                float baseline = (getFontHeight(Math.max(selectedTextSize, textSize)) - fontMetrics.bottom - fontMetrics.top) / 2 + dotViewHeight * 2;
                canvas.drawText(stepTextList.get(i), itemWidth * i + halfWidth, baseline, mPaint);
            }
        }
        super.onDraw(canvas);
    }


    public void scrollTo(int position) {

        if (position >= 0 && position < stepTextList.size()) {

            if (position != selectedPosition) {
                float scrollDistance = (position - selectedPosition) * itemWidth;
                int scrollInt = (int) scrollDistance;

                scroller.startScroll(currentX, 0, scrollInt, 0);
                selectedPosition = position;
                currentX += scrollDistance;
                invalidate();//进行下段位移
            }
        }
    }


    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller != null) {
            if (scroller.computeScrollOffset()) {//判断scroll是否完成
                scrollTo(scroller.getCurrX(), scroller.getCurrY());//执行本段位移

                invalidate();//进行下段位移
            }
        }
    }


    public float getFontHeight(float fontSize) {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (float) Math.ceil(fm.descent - fm.ascent);
    }
}
