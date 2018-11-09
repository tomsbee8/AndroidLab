package cn.blinkdagger.androidLab.Widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.blinkdagger.androidLab.R;
import cn.blinkdagger.androidLab.Utils.DensityUtil;
import cn.blinkdagger.androidLab.Utils.DeviceUtil;
import cn.blinkdagger.androidLab.Utils.ScreenUtil;

/**
 * @Author ls
 * @Date 2018/11/9
 * @Description
 * @Version
 */
public class HorizontalStepView extends View {

    private List<CharSequence> stepTextList;// Step 数据列表

    // 点的选中状态影响文字样式
    private int selectedPosition;          // 选中的点的位置
    private float selectedTextSize;         // 选中的文字的大小
    private int selectedTextColor;         // 选中的文字的颜色
    private float textSize;                  // 未选中的文字的大小
    private int textColor;                 // 未选中的文字的颜色

    // 点的激活状态影响圆点View的样式
    private int activedPosition;           // 激活的圆点的位置
    private int activedDotColor;           // 激活的圆点的背景颜色
    private float activedDotRadius;          // 激活的圆点的半径
    private float dotRadius;                 // 未激活的圆点的半径
    private int dotColor;                  // 未激活的圆点的背景颜色
    private Bitmap dotBitmap;                  // 未激活的圆点的背景

    private int lineColor;                 // 线条颜色
    private int activitedLineColor;          // 线条颜色
    private float lineHeight;                // 线条高度
    private float itemWidth;               // item 宽度

    private Paint mPaint;
    private RectF mRecfF;
    

    public HorizontalStepView(Context context) {
        super(context);
    }

    public HorizontalStepView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
    }

    public HorizontalStepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HorizontalStepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttr(context, attrs);
    }

    private void initAttr(Context context, AttributeSet attrs) {

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HorizontalStepView);
        selectedTextSize = a.getDimension(R.styleable.HorizontalStepView_selectedTextSize, DensityUtil.dp2px(context, 12));
        selectedTextColor = a.getColor(R.styleable.HorizontalStepView_selectedTextColor, Color.parseColor("#333333"));
        textSize = a.getDimension(R.styleable.HorizontalStepView_normalTextSize, DensityUtil.dp2px(context, 12));
        textColor = a.getColor(R.styleable.HorizontalStepView_normalTextColor, Color.parseColor("#666666"));
        activedDotColor = a.getColor(R.styleable.HorizontalStepView_activedDotColor, Color.parseColor("#000000"));
        activedDotRadius = a.getDimension(R.styleable.HorizontalStepView_activedDotRadius, DensityUtil.dp2px(context, 6));
        dotRadius = a.getDimension(R.styleable.HorizontalStepView_normalDotRadius, DensityUtil.dp2px(context, 4));
        dotColor = a.getColor(R.styleable.HorizontalStepView_normalDotColor, Color.parseColor("#000000"));
        lineColor = a.getColor(R.styleable.HorizontalStepView_lineColor, Color.parseColor("#F1F1F1"));
        activitedLineColor = a.getColor(R.styleable.HorizontalStepView_activatedLineColor, Color.parseColor("#666666"));
        lineHeight = a.getDimension(R.styleable.HorizontalStepView_lineHeight, DensityUtil.dp2px(context, 2));
        itemWidth = a.getDimension(R.styleable.HorizontalStepView_itemWidth, DensityUtil.dp2px(context, 100));
        int dotImageResId = a.getResourceId(R.styleable.HorizontalStepView_dotImage, -1);

        if (dotImageResId != -1) {
            dotBitmap = BitmapFactory.decodeResource(getResources(), dotImageResId);
        }
        a.recycle();

    }

    public void setStepTextList(List<CharSequence> stepTextList) {
        this.stepTextList = stepTextList;
        invalidate();
    }


    /**
     * 设置 激活的阶段位置
     *
     * @param activedPosition
     */
    public void setActivedPosition(int activedPosition) {
        this.activedPosition = activedPosition;
        invalidate();
    }

    /**
     * 设置 选中的圆点位置
     *
     * @param selectedPosition
     */
    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        
        setMeasuredDimension(width, height);

    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {

        if (stepTextList != null && !stepTextList.isEmpty()) {

            int halfWidth = getWidth() / 2;
            float dotViewHeight = Math.max(activedDotRadius, dotRadius);

            // 绘制背景线条
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(lineColor);
            canvas.drawRect(halfWidth, dotViewHeight - lineHeight / 2, getMeasuredWidth() - halfWidth, dotViewHeight - lineHeight / 2, mPaint);

            // 绘制激活的线条
            mRecfF = new RectF(halfWidth - lineHeight / 2, dotViewHeight - lineHeight / 2,
                    getMeasuredWidth() - halfWidth + lineHeight / 2, dotViewHeight - lineHeight / 2);
            mPaint.setColor(activitedLineColor);
            canvas.drawRoundRect(mRecfF, lineHeight / 2, lineHeight / 2, mPaint);

            // 绘制Dot
            for (int index = 0; index < stepTextList.size() - 1; index++) {
                if (index <= activedPosition) {
                    mPaint.setColor(activedDotColor);
                    canvas.drawCircle(halfWidth + itemWidth * index, dotViewHeight, activedDotRadius, mPaint);
                } else {
                    mPaint.setColor(dotColor);
                    canvas.drawCircle(halfWidth + itemWidth * index, dotViewHeight, dotRadius, mPaint);
                    float rectfWidth = (float) Math.sqrt(2) / 2 * dotRadius;
                    RectF mRecfF = new RectF(halfWidth + itemWidth * index - rectfWidth, dotViewHeight - rectfWidth,
                            halfWidth + itemWidth * index + rectfWidth, dotViewHeight + rectfWidth);
                    Rect rect = new Rect(0,0,dotBitmap.getWidth(),dotBitmap.getHeight());
                    canvas.drawBitmap(dotBitmap, rect, mRecfF,mPaint);

                }
            }

            // 绘制文字
        }
        super.onDraw(canvas);
    }


    @Override
    public void computeScroll() {
        super.computeScroll();
    }
}
