package cn.blinkdagger.androidLab.widget;

import android.graphics.Rect;
import android.view.View;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;

import cn.blinkdagger.androidLab.R;


/**
 * 类描述：水平分段选择器
 * 创建人：ls
 * 创建时间：2016/11/22
 * 修改人：
 * 修改时间：
 * 修改备注：
 */

public class SelectBarView extends View {

    private Context context;

    private int borderColor; //边框颜色
    private float borderWidth; //边框宽度
    private float borderRadius = 0; //边框圆角半径
    private int bgColor; //默认背景颜色
    private int checkedBgColor; //选中条目的背景颜色
    private int textColor;//默认字体颜色
    private int checkedTextColor;//选中条目的字体颜色
    private float textSize;//默认字体大小
    private float checkedTextSize;//选中条目的字体大小
    private int splitLineColor;//分割线的颜色
    private float splitLineWidth;//分割线的宽度
    private int dotColor;//分割线的颜色
    private float dotRadius;//分割线的宽度
    private String[] items = new String[]{};//条目的个数
    private Boolean[] dotsInItem = new Boolean[]{};//条目中是否有圆点
    private OnItemCheckedChangeListener itemClickListener; //点击事件
    private int checkedIndex = 0; //选中的索引
    private float dotMarginLeft = 10f; //小圆点和文字的水平距离


    public Boolean[] getDotsInItem() {
        return dotsInItem;
    }


    public SelectBarView(Context context) {
        super(context);
        this.context = context;
    }

    public SelectBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SelectBarView);
        borderColor = a.getColor(R.styleable.SelectBarView_borderColor, context.getResources().getColor(R.color.main_color));
        borderWidth = a.getDimension(R.styleable.SelectBarView_borderWidth, 1);
        borderRadius = a.getDimension(R.styleable.SelectBarView_borderRadius, 0);
        bgColor = a.getColor(R.styleable.SelectBarView_bgColor, context.getResources().getColor(R.color.main_color));
        checkedBgColor = a.getColor(R.styleable.SelectBarView_checkedBgColor, context.getResources().getColor(R.color.main_color));
        textColor = a.getColor(R.styleable.SelectBarView_textColor, context.getResources().getColor(R.color.main_color));
        checkedTextColor = a.getColor(R.styleable.SelectBarView_checkedTextColor, context.getResources().getColor(R.color.main_color));
        textSize = a.getDimension(R.styleable.SelectBarView_textSize, 14);
        checkedTextSize = a.getDimension(R.styleable.SelectBarView_checkedTextSize, textSize);
        splitLineColor = a.getColor(R.styleable.SelectBarView_splitLineColor, context.getResources().getColor(R.color.main_color));
        splitLineWidth = a.getDimension(R.styleable.SelectBarView_splitLineWidth, 1);
        dotColor = a.getColor(R.styleable.SelectBarView_dotColor, context.getResources().getColor(R.color.main_color));
        dotRadius = a.getDimension(R.styleable.SelectBarView_dotRadius, 1);
        //最后记得将TypedArray对象回收
        a.recycle();
    }

    public SelectBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }


    public void setItems(String[] items) {
        this.items = items;
        dotsInItem = new Boolean[items.length];
        for (int i = 0; i < items.length; i++) {
            dotsInItem[i] = false;
        }
        invalidate();
    }

    public void setItemGroup(String... items) {
        this.items = items;
        dotsInItem = new Boolean[items.length];
        for (int i = 0; i < items.length; i++) {
            dotsInItem[i] = false;
        }
        invalidate();
    }

    public void setOnItemCheckedChangeListener(OnItemCheckedChangeListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {


        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = 0;
        int height = 0;

        if (widthMode == MeasureSpec.EXACTLY) {
            // Parent has told us how big to be. So be it.
            width = widthSize;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {


        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        //画边线
        Paint borderPaint = new Paint();
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(borderWidth);
        borderPaint.setAntiAlias(true);
        borderPaint.setColor(borderColor);
        RectF rectF = new RectF(borderWidth / 2, borderWidth / 2, width - borderWidth / 2, height - borderWidth / 2);

        if (borderRadius > 0) {
            canvas.drawRoundRect(rectF, borderRadius, borderRadius, borderPaint);
        } else {
            canvas.drawRect(rectF, borderPaint);
        }

        if (items != null && items.length > 1) {
            float itemWidth = (width - 2 * borderWidth - (items.length - 1) * splitLineWidth) / items.length;
            //画分割线
            for (int i = 0; i < items.length - 1; i++) {
                Paint linePaint = new Paint();
                linePaint.setColor(splitLineColor);
                linePaint.setStyle(Paint.Style.FILL);
                linePaint.setAntiAlias(true);
                canvas.drawRect((itemWidth + splitLineWidth) * i + itemWidth + borderWidth, borderWidth,
                        (itemWidth + splitLineWidth) * (i + 1) + borderWidth, height - borderWidth, linePaint);
            }
            //画背景
            for (int i = 0; i < items.length; i++) {

                Paint bgPaint = new Paint();
                if (checkedIndex == i) {
                    bgPaint.setColor(checkedBgColor);
                } else {
                    bgPaint.setColor(bgColor);
                }
//                bgPaint.setStyle(Paint.Style.FILL);
                bgPaint.setAntiAlias(true);
                if (i == 0) {
                    Path leftPath = new Path();

                    leftPath.moveTo(borderWidth + itemWidth, borderWidth);
                    leftPath.lineTo(borderWidth + borderRadius, borderWidth);
                    //内圆角直径
                    float radius = borderRadius * 2 > borderWidth ? borderRadius * 2 - borderWidth * 2 : 0;

                    RectF leftTopRectF = new RectF(borderWidth, borderWidth, borderWidth + (radius), borderWidth + (radius));
                    leftPath.arcTo(leftTopRectF, 270, -90);
                    leftPath.lineTo(borderWidth, height - borderWidth - borderRadius);
                    RectF leftBotomRectF = new RectF(borderWidth, height - (radius) - borderWidth, borderWidth + (radius), height - borderWidth);
                    leftPath.arcTo(leftBotomRectF, 180, -90);
                    leftPath.lineTo(borderWidth + itemWidth, height - borderWidth);
                    leftPath.close();


                    canvas.drawPath(leftPath, bgPaint);
                } else if (i == items.length - 1) {
                    Path rightPath = new Path();
                    rightPath.moveTo(width - borderWidth - itemWidth, borderWidth);
                    rightPath.lineTo(width - borderRadius - borderWidth, borderWidth);
                    //内圆角直径
                    float radius = borderRadius * 2 > borderWidth ? borderRadius * 2 - borderWidth * 2 : 0;

                    RectF rightTopRectF = new RectF(width - radius - borderWidth, borderWidth, width - borderWidth, radius + borderWidth);
                    rightPath.arcTo(rightTopRectF, 270, 90);
                    rightPath.lineTo(width - borderWidth, height - borderRadius - borderWidth);
                    RectF rightBottomRectF = new RectF(width - radius - borderWidth, height - radius - borderWidth, width - borderWidth, height - borderWidth);
                    rightPath.arcTo(rightBottomRectF, 0, 90);
                    rightPath.lineTo(width - borderWidth - itemWidth, height - borderWidth);
                    rightPath.close();
                    canvas.drawPath(rightPath, bgPaint);
                } else {
                    RectF centRectF = new RectF(i * (splitLineWidth + itemWidth) + borderWidth, borderWidth, i * (splitLineWidth + itemWidth) + borderWidth + itemWidth, height - borderWidth);
                    canvas.drawRect(centRectF, bgPaint);


                }
            }
            //画文字和小圆点
            for (int i = 0; i < items.length; i++) {
                //画文字
                Paint textPaint = new Paint();
                textPaint.setAntiAlias(true);
                if (checkedIndex == i) {
                    textPaint.setColor(checkedTextColor);
                    textPaint.setTextSize(checkedTextSize);
                } else {
                    textPaint.setColor(textColor);
                    textPaint.setTextSize(textSize);
                }

                textPaint.setTextAlign(Paint.Align.CENTER);
                Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();


                int baseline = (height - fontMetrics.bottom - fontMetrics.top) / 2;
                canvas.drawText(items[i].toString(), itemWidth / 2 + itemWidth * i + splitLineWidth * i + borderWidth, baseline, textPaint);

                //画小圆点
                if (dotsInItem[i]) {
                    Paint dotPaint = new Paint();
                    dotPaint.setAntiAlias(true);
                    dotPaint.setColor(dotColor);
                    dotPaint.setStyle(Paint.Style.FILL);
                    int textWidth = getTextWidth(textPaint, items[i]);

                    float dotX = itemWidth / 2 + itemWidth * i + splitLineWidth * i + borderWidth + textWidth / 2 + dotRadius + dotMarginLeft;
                    float dotY = baseline + fontMetrics.top + dotRadius;

                    canvas.drawCircle(dotX, dotY, dotRadius, dotPaint);

                }

            }

        }
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int width = getMeasuredWidth();
        float itemWidth = (width - 2 * borderWidth - (items.length - 1) * splitLineWidth) / items.length;

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            for (int i = 0; i < items.length; i++) {
                //判断X坐标的位置
                boolean xInField = x >= borderWidth + splitLineWidth * i + itemWidth * i && x <= borderWidth + splitLineWidth * i + itemWidth * (i + 1);
                if (xInField && checkedIndex != i) {
                    checkedIndex = i;
                    invalidate();
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(i);
                    }
                }
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 设置小圆点是否可见
     *
     * @param index    分段Item的索引
     * @param visiable 小圆点是否可见
     */
    public void setItemDotVisiable(int index, boolean visiable) {
        dotsInItem[index] = visiable;
        invalidate();
    }

    /**
     * 获取小圆点可见性
     *
     * @param index 分段Item的索引
     * @return visiable 小圆点是否可见
     */
    public boolean getItemDotVisiablity(int index) {
        return dotsInItem[index];
    }

    /**
     * 获取文字的宽度
     *
     * @param paint
     * @param str
     * @return
     */
    public static int getTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }

    /**
     * 获取文字的高度
     *
     * @param
     * @return
     */
    public static float getTextHeight(float textSize, String sample) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(textSize);
        Rect bounds = new Rect();
        paint.getTextBounds(sample, 0, sample.length(), bounds);
        return bounds.height();
    }

    /**
     * 分段选中改变事件
     */
    public interface OnItemCheckedChangeListener {
        /**
         * 改变后的索引
         *
         * @param index
         */
        void onItemClick(int index);
    }
}
