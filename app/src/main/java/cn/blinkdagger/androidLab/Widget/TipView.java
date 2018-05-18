package cn.blinkdagger.androidLab.Widget;

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
 * 类描述：
 * 创建人：ls
 * 创建时间：2017/11/23
 * 修改人：
 * 修改时间：
 * 修改备注：
 */

public class TipView extends View {

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
    //
    private float rectHeight;
    //圆角半径
    private float borderRadius;
    //小三角形高度
    private float trangleHeight;
    //小三角形底边长度
    private float trangleBottomLength;
    //小三角形边距
    private float trangleMargin;
    //小三角形相对位置
    private int trangleGravity;

    //小三角形默认高度
    private final float DEFAULT_TRANGLE_HEIGHT = 10;
    //小三角形底边默认长度
    private final float DEFAULT_TRANGLE_BOTTOM_LENGTH = 10;

    private Paint borderPaint;
    private RectF rectF;
    private Path path;

    private String textContent;


    public TipView(Context context) {
        super(context);
    }

    public TipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
    }

    public TipView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TipView);
        textColor = a.getColor(R.styleable.TipView_TipTextColor, Color.parseColor("#FFFFFF"));
        bgColor = a.getColor(R.styleable.TipView_TipBgColor, Color.parseColor("#333333"));
        textSize = a.getDimension(R.styleable.TipView_TipTextSize, DensityUtil.dp2px(context, 14));
        rectHeight = a.getDimension(R.styleable.TipView_TipRectHeight, 30);
        borderRadius = a.getDimension(R.styleable.TipView_TipBorderRadius, 0);
        trangleHeight = a.getDimension(R.styleable.TipView_trangleHeight, DEFAULT_TRANGLE_HEIGHT);
        trangleBottomLength = a.getDimension(R.styleable.TipView_trangleBottomLength, DEFAULT_TRANGLE_BOTTOM_LENGTH);
        trangleMargin = a.getDimension(R.styleable.TipView_trangleMargin, 0);
        trangleGravity = a.getInt(R.styleable.TipView_trangleGravity, 0);
        textContent = a.getString(R.styleable.TipView_TipTextContent);
        a.recycle();

        borderPaint = new Paint();
        rectF = new RectF();
        path = new Path();

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        // 高度由trangleHeight和rectHeight 确定，设置本身height将无效
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = (int) (trangleHeight + rectHeight);

        setMeasuredDimension(width, height);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

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

        // 画文字
        if (textContent != null) {
            borderPaint.setAntiAlias(true);
            borderPaint.setColor(textColor);
            borderPaint.setTextSize(textSize);
            borderPaint.setTextAlign(Paint.Align.CENTER);
            Paint.FontMetricsInt fontMetrics = borderPaint.getFontMetricsInt();

            if (trangleGravity == LeftTop || trangleGravity == RightTop) {
                float baseline = (rectHeight - fontMetrics.bottom - fontMetrics.top) / 2 + trangleHeight;
                canvas.drawText(textContent, width / 2, baseline, borderPaint);
            } else if (trangleGravity == LeftBottom || trangleGravity == RightBottom) {
                float baseline = (rectHeight - fontMetrics.bottom - fontMetrics.top) / 2;
                canvas.drawText(textContent, width / 2, baseline, borderPaint);
            }
        }

    }
}
