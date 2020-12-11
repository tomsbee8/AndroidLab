package cn.blinkdagger.androidLab.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import cn.blinkdagger.androidLab.R;


/**
 * @Author tomsbee8
 * @Date 2019-06-28
 * @Description 带小三角形的FrameLayout
 * @Version
 */
public class TriangleFrameLayout extends ViewGroup {

    private static final int DEFAULT_CHILD_GRAVITY = Gravity.TOP | Gravity.START;

    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int TOP = 2;
    public static final int BOTTOM= 3;
    public static final int CENTER = 4;

    // 三角形箭头的方向
    private int triangleDirection;
    // 三角形箭头的位置
    private int triangleGravity;
    // 三角形箭头的高度
    private int triangleHeight;
    // 三角形箭头的底部宽度
    private int triangleWidth;
    // 三角形箭头的偏移距离
    private int triangleOffset;
    // 矩形的颜色
    private int retangleColor;
    // 矩形的圆角半径
    private int retangleRadius;


    public TriangleFrameLayout(Context context) {
        super(context);
        initAttr(context, null, 0);
    }

    public TriangleFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs, 0);
    }

    public TriangleFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs, defStyleAttr);
    }


    private void initAttr(Context context, AttributeSet attrs, int defStyleAttr) {

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TriangleFrameLayout, defStyleAttr, 0);
        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.TriangleFrameLayout_triangleHeight:
                    triangleHeight = a.getDimensionPixelSize(attr, 20);
                    break;
                case R.styleable.TriangleFrameLayout_triangleWidth:
                    triangleWidth = a.getDimensionPixelSize(attr, 40);
                    break;
                case R.styleable.TriangleFrameLayout_triangleDirection:
                    triangleDirection = a.getInt(attr, TOP);
                    break;
                case R.styleable.TriangleFrameLayout_triangleGravity:
                    triangleGravity = a.getInt(attr, LEFT);
                    break;
                case R.styleable.TriangleFrameLayout_rectangleColor:
                    retangleColor = a.getColor(attr, Color.GREEN);
                    break;
                case R.styleable.TriangleFrameLayout_rectangleRadius:
                    retangleRadius = a.getDimensionPixelSize(attr, 0);
                    break;
                case R.styleable.TriangleFrameLayout_offset:
                    triangleOffset = a.getDimensionPixelSize(attr, 0);
                    break;
            }
        }
        a.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int count = getChildCount();
        int maxWidth = 0;
        int maxHeight = 0;
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            final FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) child.getLayoutParams();
            if (child.getVisibility() != GONE) {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
                maxWidth = Math.max(maxWidth, child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
                maxHeight = Math.max(maxHeight, child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
            }
        }

        maxWidth = maxWidth + getPaddingLeft() + getPaddingRight() + retangleRadius * 2;
        maxHeight = maxHeight + getPaddingTop() + getPaddingBottom() + retangleRadius * 2;

        if(triangleDirection == TOP || triangleDirection == BOTTOM){
            maxHeight = maxHeight + triangleHeight;
        }else{
            maxWidth = maxWidth + triangleHeight;
        }

        setMeasuredDimension(maxWidth, maxHeight);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        layoutChildren(l, t, r, b, false);
    }

    void layoutChildren(int left, int top, int right, int bottom, boolean forceLeftGravity) {

        int parentLeft = retangleRadius;
        int parentRight = right - left - retangleRadius;

        int parentTop = retangleRadius;
        int parentBottom = bottom - top - retangleRadius;

        if ( triangleDirection == LEFT) {
            parentLeft = parentLeft + triangleHeight;
        } else if ( triangleDirection == TOP) {
            parentTop = parentTop + triangleHeight;
        } else if(triangleDirection == RIGHT) {
            parentRight = parentRight - triangleHeight;
        } else if(triangleDirection == BOTTOM) {
            parentBottom = parentBottom - triangleHeight;
        }


        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) child.getLayoutParams();

                final int width = child.getMeasuredWidth();
                final int height = child.getMeasuredHeight();

                int childLeft;
                int childTop;

                int gravity = lp.gravity;
                if (gravity == -1) {
                    gravity = DEFAULT_CHILD_GRAVITY;
                }

                final int layoutDirection = getLayoutDirection();
                final int absoluteGravity = Gravity.getAbsoluteGravity(gravity, layoutDirection);
                final int verticalGravity = gravity & Gravity.VERTICAL_GRAVITY_MASK;

                switch (absoluteGravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
                    case Gravity.CENTER_HORIZONTAL:
                        childLeft = parentLeft + (parentRight - parentLeft - width) / 2 +
                                lp.leftMargin - lp.rightMargin;
                        break;
                    case Gravity.RIGHT:
                        if (!forceLeftGravity) {
                            childLeft = parentRight - width - lp.rightMargin;
                            break;
                        }
                    case Gravity.LEFT:
                    default:
                        childLeft = parentLeft + lp.leftMargin;
                }

                switch (verticalGravity) {
                    case Gravity.TOP:
                        childTop = parentTop + lp.topMargin;
                        break;
                    case Gravity.CENTER_VERTICAL:
                        childTop = parentTop + (parentBottom - parentTop - height) / 2 +
                                lp.topMargin - lp.bottomMargin;
                        break;
                    case Gravity.BOTTOM:
                        childTop = parentBottom - height - lp.bottomMargin;
                        break;
                    default:
                        childTop = parentTop + lp.topMargin;
                }

                child.layout(childLeft , childTop , childLeft + width, childTop + height);
            }
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(retangleColor);
        paint.setStyle(Paint.Style.FILL);

        // set Xfermode for source and shadow overlap
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));

        // draw round corner rectangle
        paint.setColor(retangleColor);

        Path path = new Path();
        triangleOffset = Math.max(triangleOffset, retangleRadius);

        if (triangleDirection == TOP) {
            canvas.drawRoundRect(new RectF(0, triangleHeight, getMeasuredWidth(), getMeasuredHeight()), retangleRadius, retangleRadius, paint);

            if(triangleGravity == RIGHT){
                path.moveTo(getMeasuredWidth() - triangleOffset - triangleWidth, triangleHeight);
                path.lineTo(getMeasuredWidth() - triangleOffset, triangleHeight);
                path.lineTo(getMeasuredWidth() - triangleOffset - triangleWidth / 2f, 0);
                path.lineTo(getMeasuredWidth() - triangleOffset - triangleWidth, triangleHeight);
            }else if(triangleGravity == CENTER){
                path.moveTo((getMeasuredWidth() - triangleWidth) / 2f, triangleHeight);
                path.lineTo((getMeasuredWidth() + triangleWidth) / 2f, triangleHeight);
                path.lineTo(getMeasuredWidth() / 2f, 0);
                path.lineTo((getMeasuredWidth() - triangleWidth) / 2f, triangleHeight);
            }else{
                path.moveTo(triangleOffset, triangleHeight);
                path.lineTo(triangleOffset + triangleWidth, triangleHeight);
                path.lineTo(triangleOffset + triangleWidth / 2f, 0);
                path.lineTo(triangleOffset, triangleHeight);
            }
        } else if (triangleDirection == BOTTOM) {
            canvas.drawRoundRect(new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight() - triangleHeight), retangleRadius, retangleRadius, paint);

            if(triangleGravity == RIGHT){
                path.moveTo(getMeasuredWidth() - triangleOffset - triangleWidth, getMeasuredHeight() - triangleHeight);
                path.lineTo(getMeasuredWidth() - triangleOffset, getMeasuredHeight() - triangleHeight);
                path.lineTo(getMeasuredWidth() - triangleOffset - triangleWidth / 2f, getMeasuredHeight());
                path.lineTo(getMeasuredWidth() - triangleOffset - triangleWidth, getMeasuredHeight() - triangleHeight);
            }else if(triangleGravity == CENTER){
                path.moveTo((getMeasuredWidth() - triangleWidth) / 2f, getMeasuredHeight() - triangleHeight);
                path.lineTo((getMeasuredWidth() + triangleWidth) / 2f, getMeasuredHeight() - triangleHeight);
                path.lineTo(getMeasuredWidth() / 2f, getMeasuredHeight());
                path.lineTo((getMeasuredWidth() - triangleWidth) / 2f, getMeasuredHeight() - triangleHeight);
            }else{
                path.moveTo(triangleOffset, getMeasuredHeight() - triangleHeight);
                path.lineTo(triangleOffset + triangleWidth, getMeasuredHeight() - triangleHeight);
                path.lineTo(triangleOffset + triangleWidth / 2f, getMeasuredHeight());
                path.lineTo(triangleOffset, getMeasuredHeight() - triangleHeight);
            }
        } else if ( triangleDirection == LEFT){
            canvas.drawRoundRect(new RectF(triangleHeight, 0, getMeasuredWidth(), getMeasuredHeight()), retangleRadius, retangleRadius, paint);

            if(triangleGravity == BOTTOM){
                path.moveTo(triangleHeight, getMeasuredHeight() - triangleOffset - triangleWidth);
                path.lineTo(triangleHeight, getMeasuredHeight() - triangleOffset);
                path.lineTo(0, getMeasuredHeight() - triangleOffset - triangleWidth / 2f);
                path.lineTo(triangleHeight, getMeasuredHeight() - triangleOffset - triangleWidth);
            }else if(triangleGravity == CENTER){
                path.moveTo(triangleHeight, (getMeasuredHeight() - triangleWidth) / 2f);
                path.lineTo(triangleHeight, (getMeasuredHeight() + triangleWidth) / 2f);
                path.lineTo(0, getMeasuredHeight() / 2f);
                path.lineTo(triangleHeight, (getMeasuredHeight() - triangleWidth) / 2f);
            }else{
                path.moveTo(triangleHeight, triangleOffset);
                path.lineTo(triangleHeight, triangleOffset + triangleWidth);
                path.lineTo(0, triangleOffset + triangleWidth / 2f);
                path.lineTo(triangleHeight, triangleOffset);
            }
        } else if ( triangleDirection == RIGHT){
            canvas.drawRoundRect(new RectF(0, 0, getMeasuredWidth() - triangleHeight, getMeasuredHeight()), retangleRadius, retangleRadius, paint);

            if(triangleGravity == BOTTOM){
                path.moveTo(getMeasuredWidth() - triangleHeight, getMeasuredHeight() - triangleWidth - triangleOffset);
                path.lineTo(getMeasuredWidth() - triangleHeight, getMeasuredHeight() - triangleOffset);
                path.lineTo(getMeasuredWidth(), getMeasuredHeight() - triangleOffset - triangleWidth / 2f);
                path.lineTo(getMeasuredWidth() - triangleHeight, getMeasuredHeight() - triangleWidth - triangleOffset);
            }else if(triangleGravity == CENTER){
                path.moveTo(getMeasuredWidth() - triangleHeight, (getMeasuredHeight() - triangleWidth) / 2f);
                path.lineTo(getMeasuredWidth() - triangleHeight, (getMeasuredHeight() + triangleWidth) / 2f);
                path.lineTo(getMeasuredWidth(), getMeasuredHeight() / 2f);
                path.lineTo(getMeasuredWidth() - triangleHeight, (getMeasuredHeight() - triangleWidth) / 2f);
            }else{
                path.moveTo(getMeasuredWidth() - triangleHeight, triangleOffset);
                path.lineTo(getMeasuredWidth() - triangleHeight, triangleOffset + triangleWidth);
                path.lineTo(getMeasuredWidth(), triangleOffset + triangleWidth /2f );
                path.lineTo(getMeasuredWidth() - triangleHeight, triangleOffset);
            }
        }

        path.close();
        canvas.drawPath(path, paint);

        super.dispatchDraw(canvas);
    }


    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new FrameLayout.LayoutParams(getContext(), attrs);
    }

    public void setTriangleGravity(int triangleGravity) {
        if(triangleDirection > -1 && triangleDirection < 5) {
            this.triangleGravity = triangleGravity;
            requestLayout();
            invalidate();
        }
    }
    public void setTriangleDirection(int triangleDirection) {
        if(triangleDirection > -1 && triangleDirection < 4) {
            this.triangleDirection = triangleDirection;
            requestLayout();
            invalidate();
        }
    }

    public void setGravityAndOffset(int triangleGravity,int offset) {
        this.triangleGravity = triangleGravity;
        this.triangleOffset = offset;
        requestLayout();
        invalidate();
    }
}
