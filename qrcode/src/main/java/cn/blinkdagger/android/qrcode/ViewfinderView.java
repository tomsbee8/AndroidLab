package cn.blinkdagger.android.qrcode;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.google.zxing.ResultPoint;

import java.util.ArrayList;
import java.util.List;

import cn.blinkdagger.android.qrcode.camera.CameraManager;

/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder rectangle and partial
 * transparency outside it, as well as the laser scanner animation and result points.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class ViewfinderView extends View {

  private static final int[] SCANNER_ALPHA = {0, 64, 128, 192, 255, 192, 128, 64};
  private static final long ANIMATION_DELAY = 80L;
  private static final int CURRENT_POINT_OPACITY = 0xA0;
  private static final int MAX_RESULT_POINTS = 20;
  private static final int POINT_SIZE = 6;

  private CameraManager cameraManager;
  private Bitmap resultBitmap;

  private final Paint paint;
  private Paint maskPaint;
  private Paint linePaint;
  private Paint traAnglePaint;
  private Paint textPaint;

  private final int resultColor;
  private final int maskColor = Color.parseColor("#60000000");                //蒙在摄像头上面区域的半透明颜色
  private final int triAngleColor = Color.parseColor("#4ea45d");              //边角的颜色
  private final int lineColor = Color.parseColor("#FF0000");                  //中间线的颜色
  private final int textColor = Color.parseColor("#CCCCCC");                  //文字的颜色
  private final int triAngleLength = dp2px(15);                                         //每个角的点距离
  private final int triAngleWidth = dp2px(3);                                           //每个角的点宽度
  private final int textMarinTop = dp2px(30);                                           //文字距离识别框的距离
  private int lineOffsetCount = 0;

  private final int resultPointColor;
  private int scannerAlpha;
  private List<ResultPoint> possibleResultPoints;
  private List<ResultPoint> lastPossibleResultPoints;

  // This constructor is used when the class is built from an XML resource.
  public ViewfinderView(Context context, AttributeSet attrs) {
    super(context, attrs);

    // Initialize these once for performance rather than calling them every time in onDraw().
    paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Resources resources = getResources();
    resultPointColor = resources.getColor(R.color.possible_result_points);
    resultColor = resources.getColor(R.color.result_view);

    maskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    maskPaint.setColor(maskColor);

    traAnglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    traAnglePaint.setColor(triAngleColor);
    traAnglePaint.setStrokeWidth(triAngleWidth);
    traAnglePaint.setStyle(Paint.Style.STROKE);

    linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    linePaint.setColor(lineColor);

    textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    textPaint.setColor(textColor);
    textPaint.setTextSize(dp2px(14));

    scannerAlpha = 0;
    possibleResultPoints = new ArrayList<>(5);
    lastPossibleResultPoints = null;
  }

  public void setCameraManager(CameraManager cameraManager) {
    this.cameraManager = cameraManager;
  }

  @SuppressLint("DrawAllocation")
  @Override
  public void onDraw(Canvas canvas) {
    if (cameraManager == null) {
      return; // not ready yet, early draw before done configuring
    }
    Rect frame = cameraManager.getFramingRect();
    Rect previewFrame = cameraManager.getFramingRectInPreview();
    if (frame == null || previewFrame == null) {
      return;
    }
    int width = canvas.getWidth();
    int height = canvas.getHeight();

// 除了中间的识别区域，其他区域都将蒙上一层半透明的图层
    canvas.drawRect(0, 0, width, frame.top, maskPaint);
    canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, maskPaint);
    canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, maskPaint);
    canvas.drawRect(0, frame.bottom + 1, width, height, maskPaint);

    String text = "将条码放入框内，即可自动扫描";
    canvas.drawText(text, (width - textPaint.measureText(text)) / 2, frame.bottom + textMarinTop, textPaint);

    // 四个角落的三角
    Path leftTopPath = new Path();
    leftTopPath.moveTo(frame.left + triAngleLength, frame.top + triAngleWidth / 2);
    leftTopPath.lineTo(frame.left + triAngleWidth / 2, frame.top + triAngleWidth / 2);
    leftTopPath.lineTo(frame.left + triAngleWidth / 2, frame.top + triAngleLength);
    canvas.drawPath(leftTopPath, traAnglePaint);

    Path rightTopPath = new Path();
    rightTopPath.moveTo(frame.right - triAngleLength, frame.top + triAngleWidth / 2);
    rightTopPath.lineTo(frame.right - triAngleWidth / 2, frame.top + triAngleWidth / 2);
    rightTopPath.lineTo(frame.right - triAngleWidth / 2, frame.top + triAngleLength);
    canvas.drawPath(rightTopPath, traAnglePaint);

    Path leftBottomPath = new Path();
    leftBottomPath.moveTo(frame.left + triAngleWidth / 2, frame.bottom - triAngleLength);
    leftBottomPath.lineTo(frame.left + triAngleWidth / 2, frame.bottom - triAngleWidth / 2);
    leftBottomPath.lineTo(frame.left + triAngleLength, frame.bottom - triAngleWidth / 2);
    canvas.drawPath(leftBottomPath, traAnglePaint);

    Path rightBottomPath = new Path();
    rightBottomPath.moveTo(frame.right - triAngleLength, frame.bottom - triAngleWidth / 2);
    rightBottomPath.lineTo(frame.right - triAngleWidth / 2, frame.bottom - triAngleWidth / 2);
    rightBottomPath.lineTo(frame.right - triAngleWidth / 2, frame.bottom - triAngleLength);
    canvas.drawPath(rightBottomPath, traAnglePaint);

    //循环划线，从上到下
    if (lineOffsetCount > frame.bottom - frame.top - dp2px(10)) {
      lineOffsetCount = 0;
    } else {
      lineOffsetCount = lineOffsetCount + 6;
//            canvas.drawLine(frame.left, frame.top + lineOffsetCount, frame.right, frame.top + lineOffsetCount, linePaint);    //画一条红色的线
      Rect lineRect = new Rect();
      lineRect.left = frame.left;
      lineRect.top = frame.top + lineOffsetCount;
      lineRect.right = frame.right;
      lineRect.bottom = frame.top + dp2px(3) + lineOffsetCount;
      canvas.drawBitmap(((BitmapDrawable)(getResources().getDrawable(R. mipmap.scan_line))).getBitmap(), null, lineRect, linePaint);
    }



    if (resultBitmap != null) {
      // Draw the opaque result bitmap over the scanning rectangle
      paint.setAlpha(CURRENT_POINT_OPACITY);
      canvas.drawBitmap(resultBitmap, null, frame, paint);
    } else {

      float scaleX = frame.width() / (float) previewFrame.width();
      float scaleY = frame.height() / (float) previewFrame.height();

      List<ResultPoint> currentPossible = possibleResultPoints;
      List<ResultPoint> currentLast = lastPossibleResultPoints;
      int frameLeft = frame.left;
      int frameTop = frame.top;
      if (currentPossible.isEmpty()) {
        lastPossibleResultPoints = null;
      } else {
        possibleResultPoints = new ArrayList<>(5);
        lastPossibleResultPoints = currentPossible;
        paint.setAlpha(CURRENT_POINT_OPACITY);
        paint.setColor(resultPointColor);
        synchronized (currentPossible) {
          for (ResultPoint point : currentPossible) {
            canvas.drawCircle(frameLeft + (int) (point.getX() * scaleX),
                              frameTop + (int) (point.getY() * scaleY),
                              POINT_SIZE, paint);
          }
        }
      }
      if (currentLast != null) {
        paint.setAlpha(CURRENT_POINT_OPACITY / 2);
        paint.setColor(resultPointColor);
        synchronized (currentLast) {
          float radius = POINT_SIZE / 2.0f;
          for (ResultPoint point : currentLast) {
            canvas.drawCircle(frameLeft + (int) (point.getX() * scaleX),
                              frameTop + (int) (point.getY() * scaleY),
                              radius, paint);
          }
        }
      }

      postInvalidateDelayed(10L, frame.left, frame.top, frame.right, frame.bottom);

    }
  }

  public void drawViewfinder() {
    Bitmap resultBitmap = this.resultBitmap;
    this.resultBitmap = null;
    if (resultBitmap != null) {
      resultBitmap.recycle();
    }
    invalidate();
  }

  /**
   * Draw a bitmap with the result points highlighted instead of the live scanning display.
   *
   * @param barcode An image of the decoded barcode.
   */
  public void drawResultBitmap(Bitmap barcode) {
    resultBitmap = barcode;
    invalidate();
  }

  public void addPossibleResultPoint(ResultPoint point) {
    List<ResultPoint> points = possibleResultPoints;
    synchronized (points) {
      points.add(point);
      int size = points.size();
      if (size > MAX_RESULT_POINTS) {
        // trim it
        points.subList(0, size - MAX_RESULT_POINTS / 2).clear();
      }
    }
  }

  private int dp2px(int dp) {
    float density = getContext().getResources().getDisplayMetrics().density;
    return (int) (dp * density + 0.5f);
  }

}
