package cn.blinkdagger.androidLab.Widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


import cn.blinkdagger.androidLab.R;
import cn.blinkdagger.androidLab.Utils.DensityUtil;


/**
 * 类描述：
 * 创建人：ls
 * 创建时间：2017/11/23
 * 修改人：
 * 修改时间：
 * 修改备注：
 */

public class TipMenuView extends View {

    private static final int LeftTop = 1;
    private static final int LeftBottom = 2;
    private static final int RightTop = 3;
    private static final int RightBottom = 4;


    //背景颜色
    private int bgColor;
    //文字颜色
    private int textColor;
    //文字大小
    private float textSize;
    //Item矩形高度
    private float itemHeight;
    //Item按压后的颜色
    private int itemPressedColor;
    //分割线高度
    private float separateLineHeight;
    //分割线颜色
    private int separateLineColor;
    //圆角半径
    private float borderRadius;
    //三角形高度
    private float trangleHeight;
    //三角形底边长度
    private float trangleBottomLength;
    //三角形边距
    private float trangleMargin;
    //三角形相对矩形的方向位置
    private int trangleGravity;
    // 列表矩形的高度
    private float rectHeight;


    //小三角形默认高度
    private final float DEFAULT_TRANGLE_HEIGHT = 10;
    //小三角形底边默认长度
    private final float DEFAULT_TRANGLE_BOTTOM_LENGTH = 10;

    private Paint borderPaint;
    private RectF rectF;
    private Path path;

    private String[] items = new String[]{};//条目的个数

    private OnItemClickListener onItemClickListener;//点击事件

    public TipMenuView(Context context) {
        super(context);
    }

    public TipMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
    }

    public TipMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TipMenuView);
        textColor = a.getColor(R.styleable.TipMenuView_tipMenuTextColor, Color.parseColor("#FFFFFF"));
        bgColor = a.getColor(R.styleable.TipMenuView_tipMenuBgColor, Color.parseColor("#333333"));
        textSize = a.getDimension(R.styleable.TipMenuView_tipMenuTextSize, DensityUtil.dp2px(context, 14));
        itemHeight = a.getDimension(R.styleable.TipMenuView_tipMenuItemHeight, 30);
        itemPressedColor = a.getColor(R.styleable.TipMenuView_tipMenuItemPressedColor, Color.parseColor("#333333"));
        separateLineHeight = a.getDimensionPixelOffset(R.styleable.TipMenuView_tipMenuSeparateLineHeight, 0);
        separateLineColor = a.getColor(R.styleable.TipMenuView_tipMenuSeparateLineColor, Color.WHITE);
        borderRadius = a.getDimension(R.styleable.TipMenuView_tipMenuBorderRadius, 0);
        trangleHeight = a.getDimension(R.styleable.TipMenuView_tipMenuTrangleHeight, DEFAULT_TRANGLE_HEIGHT);
        trangleBottomLength = a.getDimension(R.styleable.TipMenuView_tipMenuTrangleBottomLength, DEFAULT_TRANGLE_BOTTOM_LENGTH);
        trangleMargin = a.getDimension(R.styleable.TipMenuView_tipMenuTrangleMargin, 0);
        trangleGravity = a.getInt(R.styleable.TipMenuView_tipMenuTrangleGravity, 0);
        a.recycle();

        borderPaint = new Paint();
        rectF = new RectF();
        path = new Path();

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (items.length > 1) {
            rectHeight = itemHeight * items.length + separateLineHeight * (items.length - 1);
        } else {
            rectHeight = itemHeight * items.length;
        }

        // 高度由trangleHeight和rectHeight 确定，设置本身height将无效
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = (int) (trangleHeight + rectHeight);

        setMeasuredDimension(width, height);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        // 从新计算矩形高度
        if (items.length > 1) {
            rectHeight = itemHeight * items.length + separateLineHeight * (items.length - 1);
        } else {
            rectHeight = itemHeight * items.length;
        }

        // 设置设置小三角形底边长度[限制其最大值]
        if (trangleBottomLength > width - 2 * borderRadius) {
            trangleBottomLength = width - 2 * borderRadius;
        }

        // 设置小三角形边距[限制其最大值和最小值]
        if (trangleMargin < 0) {
            trangleMargin = 0;
        } else if (trangleMargin > width - 2 * borderRadius - trangleBottomLength) {
            trangleMargin = width - 2 * borderRadius - trangleBottomLength;
        }

        borderPaint.setStyle(Paint.Style.FILL);
        borderPaint.setAntiAlias(true);
        borderPaint.setColor(bgColor);

        switch (trangleGravity) {
            case LeftTop:
                // 画圆角矩形
                rectF.set(0, trangleHeight, width, height);
                canvas.drawRoundRect(rectF, borderRadius, borderRadius, borderPaint);

                // 画三角形
                if (trangleBottomLength > 0 && trangleHeight > 0) {
                    path.moveTo(borderRadius + trangleMargin, trangleHeight);
                    path.lineTo(borderRadius + trangleMargin + trangleBottomLength / 2.0f, 0);
                    path.lineTo(borderRadius + trangleMargin + trangleBottomLength, trangleHeight);
                    path.close();
                    canvas.drawPath(path, borderPaint);
                }
                break;
            case LeftBottom:
                // 画圆角矩形
                rectF.set(0, 0, width, rectHeight);
                canvas.drawRoundRect(rectF, borderRadius, borderRadius, borderPaint);

                // 画三角形
                if (trangleBottomLength > 0 && trangleHeight > 0) {
                    path.moveTo(borderRadius + trangleMargin, rectHeight);
                    path.lineTo(borderRadius + trangleMargin + trangleBottomLength / 2.0f, height);
                    path.lineTo(borderRadius + trangleMargin + trangleBottomLength, rectHeight);
                    path.close();
                    canvas.drawPath(path, borderPaint);
                }
                break;
            case RightTop:
                // 画圆角矩形
                rectF.set(0, trangleHeight, width, height);
                canvas.drawRoundRect(rectF, borderRadius, borderRadius, borderPaint);

                // 画三角形
                if (trangleBottomLength > 0 && trangleHeight > 0) {
                    path.moveTo(width - borderRadius - trangleMargin - trangleBottomLength, trangleHeight);
                    path.lineTo(width - borderRadius - trangleMargin - trangleBottomLength / 2.0f, 0);
                    path.lineTo(width - borderRadius - trangleMargin, trangleHeight);
                    path.close();
                    canvas.drawPath(path, borderPaint);
                }
                break;
            case RightBottom:
                // 画圆角矩形
                rectF.set(0, 0, width, rectHeight);
                canvas.drawRoundRect(rectF, borderRadius, borderRadius, borderPaint);

                // 画三角形
                if (trangleBottomLength > 0 && trangleHeight > 0) {
                    path.moveTo(width - borderRadius - trangleMargin - trangleBottomLength, rectHeight);
                    path.lineTo(width - borderRadius - trangleMargin - trangleBottomLength / 2.0f, height);
                    path.lineTo(width - borderRadius - trangleMargin, rectHeight);
                    path.close();
                    canvas.drawPath(path, borderPaint);
                }
                break;
        }

        // 画文字和分割线
        if (items != null && items.length > 0) {


            for (int i = 0; i < items.length; i++) {
                borderPaint.reset();
                borderPaint.setAntiAlias(true);
                borderPaint.setColor(textColor);
                borderPaint.setTextSize(textSize);
                borderPaint.setTextAlign(Paint.Align.CENTER);
                Paint.FontMetricsInt fontMetrics = borderPaint.getFontMetricsInt();

                // 画文字
                float targetTop = (itemHeight + separateLineHeight) * i;
                float targetBottom = (itemHeight + separateLineHeight) * i + itemHeight;
                if (trangleGravity == LeftTop || trangleGravity == RightTop) {
                    float baseline = (targetTop + targetBottom - fontMetrics.bottom - fontMetrics.top) / 2f + trangleHeight;
                    canvas.drawText(items[i], width / 2, baseline, borderPaint);
                } else if (trangleGravity == LeftBottom || trangleGravity == RightBottom) {
                    float baseline = (targetTop + targetBottom - fontMetrics.bottom - fontMetrics.top) / 2f;
                    canvas.drawText(items[i], width / 2, baseline, borderPaint);
                }

                // 画分割线
                if (i < items.length - 1) {
                    if (trangleGravity == LeftTop || trangleGravity == RightTop) {
                        rectF.set(0, (itemHeight + separateLineHeight) * i + itemHeight + trangleHeight,
                                width, (itemHeight + separateLineHeight) * (i + 1) + trangleHeight);
                    } else if (trangleGravity == LeftBottom || trangleGravity == RightBottom) {
                        rectF.set(0, (itemHeight + separateLineHeight) * i + itemHeight, width, (itemHeight + separateLineHeight) * (i + 1));
                    }
                    borderPaint.reset();
                    borderPaint.setStyle(Paint.Style.FILL);
                    borderPaint.setColor(separateLineColor);
                    canvas.drawRect(rectF, borderPaint);
                }

            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int width = getMeasuredWidth();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (int i = 0; i < items.length; i++) {
                    RectF rect =new RectF(0,(itemHeight + separateLineHeight) * i,width,(itemHeight + separateLineHeight) * i + itemHeight);
                    if (onItemClickListener != null && isPointInRect(new PointF(event.getX(), event.getY()), new RectF(rect))) {
                        // 被按下时，更新背景视图
                        postInvalidate((int)(rect.left), (int)rect.top, (int)rect.right, (int)rect.bottom);
                    }
                }
            case MotionEvent.ACTION_UP:
                for (int i = 0; i < items.length; i++) {
                    RectF rect =new RectF(0,(itemHeight + separateLineHeight) * i,width,(itemHeight + separateLineHeight) * i + itemHeight);
                    if (onItemClickListener != null && isPointInRect(new PointF(event.getX(), event.getY()), rect)) {
                        // 触发点击事件
                        onItemClickListener.onItemClick(items[i], i);
                    }
                }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 判断点是否处于矩形区域
     *
     * @param pointF
     * @param targetRect
     * @return
     */
    private boolean isPointInRect(PointF pointF, RectF targetRect) {
        if (pointF.x < targetRect.left) {
            return false;
        }
        if (pointF.x > targetRect.right) {
            return false;
        }
        if (pointF.y < targetRect.top) {
            return false;
        }
        if (pointF.y > targetRect.bottom) {
            return false;
        }
        return true;
    }


    /**
     * 设置列表数据
     *
     * @param items
     */
    public void setItems(String... items) {
        this.items = items;
        invalidate();
    }


    /**
     * 设置选项点击事件
     *
     * @param itemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.onItemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(String name, int position);
    }


}
