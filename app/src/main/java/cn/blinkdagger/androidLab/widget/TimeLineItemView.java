package cn.blinkdagger.androidLab.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import cn.blinkdagger.androidLab.R;
import cn.blinkdagger.androidLab.Utils.DensityUtil;


/**
 * 类描述：时间线Item(冰糖葫芦)
 * 创建人：ls
 * 创建时间：2016/11/22
 * 修改人：
 * 修改时间：
 * 修改备注：
 */

public class TimeLineItemView extends View {

    //点的半径
    private float dotRadius;
    //线的宽度（不可以超过点的直径）
    private float lineWidth;
    //点的颜色
    private int dotColor;
    //线的颜色
    private int lineColor;

    //顶部线的高度
    private float topLineHeight = 0;
    //底部线的高度
    private float bottomLineHeight = 0;
    //底部线是否可见
    private boolean bottomLineVisiable =true;
    //顶部线是否可见
    private boolean topLineVisiable =true;


    public TimeLineItemView(Context context) {
        super(context);
    }

    public TimeLineItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
    }

    public TimeLineItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TimeLineItemView);
        dotRadius = a.getDimension(R.styleable.TimeLineItemView_dot_radius, DensityUtil.dp2px(context, 8));
        dotColor = a.getColor(R.styleable.TimeLineItemView_dot_color, Color.parseColor("#E1E1E1"));
        lineWidth = a.getDimension(R.styleable.TimeLineItemView_line_width, DensityUtil.dp2px(context, 6));
        lineColor = a.getColor(R.styleable.TimeLineItemView_line_color, Color.parseColor("#E1E1E1"));
        topLineHeight = a.getDimension(R.styleable.TimeLineItemView_top_line_height, 0);
        bottomLineHeight = a.getDimension(R.styleable.TimeLineItemView_bottom_line_height, 0);
        a.recycle();
        if (lineWidth > dotRadius * 2) {
            lineWidth = dotRadius * 2;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST) {
            width = (int) (dotRadius * 2 + getPaddingLeft() + getPaddingRight());
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            height = (int) (topLineHeight + bottomLineHeight + dotRadius * 2);
        } else if (heightMode == MeasureSpec.EXACTLY) {
            if ((topLineHeight + bottomLineHeight + dotRadius * 2) < height) {
                bottomLineHeight = height - topLineHeight - dotRadius * 2;
            }
        }
        setMeasuredDimension(width, height);

    }


    @Override
    protected void onDraw(Canvas canvas) {


        Paint dotPaint = new Paint();
        Paint linePaint = new Paint();
        dotPaint.setColor(dotColor);
        dotPaint.setAntiAlias(true);
        linePaint.setColor(lineColor);
        linePaint.setAntiAlias(true);

        int startHeight = (int) Math.sqrt(dotRadius * dotRadius - lineWidth * lineWidth / 4.0);
        double sinValue = lineWidth * 0.5 / dotRadius;
        double halfAngle = Math.asin(sinValue) / Math.PI * 180;
        RectF topRectF = new RectF(getPaddingLeft(), topLineHeight, getPaddingLeft() + dotRadius * 2, topLineHeight + dotRadius * 2);

        //画上部分线
        Path path = new Path();
        if(topLineVisiable) {
            path.moveTo(getPaddingLeft() + dotRadius - lineWidth / 2, 0);
            path.lineTo(getPaddingLeft() + dotRadius + lineWidth / 2, 0);
            path.lineTo(getPaddingLeft() + dotRadius + lineWidth / 2, topLineHeight + dotRadius - startHeight);
            path.arcTo(topRectF, 270 + (float) halfAngle, (float) halfAngle * -2);
            path.lineTo(getPaddingLeft() + dotRadius - lineWidth / 2, 0);
            path.close();
            canvas.drawPath(path, linePaint);
        }
        //画圆
        canvas.drawCircle(getPaddingLeft() + dotRadius, topLineHeight + dotRadius, dotRadius, dotPaint);
        //画下部分线
        if(bottomLineVisiable) {
            path.moveTo(getPaddingLeft() + dotRadius - lineWidth / 2, dotRadius * 2 + topLineHeight + startHeight);
            path.arcTo(topRectF, 90 + (float) halfAngle, (float) halfAngle * -2);
            path.lineTo(getPaddingLeft() + dotRadius + lineWidth / 2, getMeasuredHeight());
            path.lineTo(getPaddingLeft() + dotRadius - lineWidth / 2, getMeasuredHeight());
            path.lineTo(getPaddingLeft() + dotRadius - lineWidth / 2, dotRadius * 2 + topLineHeight + startHeight);
            path.close();
            canvas.drawPath(path, linePaint);
        }

    }

    public void setDotColor(int dotColor) {
        this.dotColor = dotColor;
        invalidate();
    }

    public void setTopLineHeight(int topLineHeight) {
        this.topLineHeight = topLineHeight;
        invalidate();
    }
    public void setBottomLineHeight(int bottomLineHeight) {
        this.bottomLineHeight = bottomLineHeight;
        invalidate();
    }
    public void setBottomLineVisiable(boolean bottomLineVisiable) {
        this.bottomLineVisiable = bottomLineVisiable;
        invalidate();
    }
    public void setTopLineVisiable(boolean topLineVisiable) {
        this.topLineVisiable = topLineVisiable;
        invalidate();
    }

}