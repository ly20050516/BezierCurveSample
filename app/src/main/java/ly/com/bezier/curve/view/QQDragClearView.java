package ly.com.bezier.curve.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * @author：ly on 2019/5/9 17:05
 * @mail：liuyan@zhimei.ai
 */
public class QQDragClearView extends FrameLayout {

    public static final String TAG = "QQDragClearView";
    public static final float DEFAULT_RADIUS = 100;
    /**
     * Tips View
     */
    private TipsView tipsView;

    /**
     * Tips View 的固定位置
     */
    private int tipsViewX = 0;
    private int tipsViewY = 0;

    /**
     * Tips View 被移动的位置
     */
    private int tipsViewMoveX = 0;
    private int tipsViewMoveY = 0;

    /**
     * 中间控制点
     */
    private int controllerX = 0;
    private int controllerY = 0;

    private boolean isDragStart = false;
    private boolean isLimit = false;

    private Path path;
    private Paint paint;

    private float radius = DEFAULT_RADIUS;

    public QQDragClearView(@NonNull Context context) {
        this(context, null);
    }

    public QQDragClearView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context) {

        tipsView = new TipsView(context);
        tipsView.setText("99");
        addView(tipsView,new ViewGroup.LayoutParams((int)DEFAULT_RADIUS,(int)DEFAULT_RADIUS));

        path = new Path();
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
    }

    private boolean isTouchInTipsView(int rawX, int rawY) {

        Rect rect = new Rect();
        tipsView.getDrawingRect(rect);
        int[] screenLocation = new int[2];
        tipsView.getLocationOnScreen(screenLocation);

        rect.left = screenLocation[0];
        rect.top = screenLocation[1];
        rect.right = rect.right + screenLocation[0];
        rect.bottom = rect.bottom + screenLocation[1];

        return rect.contains(rawX, rawY);
    }

    private void calculateDrawPath() {

        // 计算 2 点之间的距离
        float distance = (float) Math.sqrt(Math.pow((tipsViewMoveX - tipsViewX), 2) + Math.pow((tipsViewMoveY - tipsViewY), 2));
        // 圆的半径随着距离越来越远变和越来越小
        radius = -distance / 15 + DEFAULT_RADIUS;

        if (radius <= 0) {
            isLimit = true;
            return;
        }

        if(tipsViewMoveX - tipsViewX == 0 || tipsViewMoveY - tipsViewY == 0) {
            return;
        }

        /**
         * 计算偏移量 offsetX 以及 offsetY
         *
         * 直线的斜率 k = (y2 - y1) / (x2 - x1) = tan𝞪，所以这里 Math.atan(k) 就是计算出来的角度，再根据角度分别计算出 offsetX 与 offsetY。
         *
         */
        float offsetX = (float) (radius * Math.sin(Math.atan((tipsViewMoveY - tipsViewY) / (tipsViewMoveX - tipsViewX))));
        float offsetY = (float) (radius * Math.cos(Math.atan((tipsViewMoveY - tipsViewY) / (tipsViewMoveX - tipsViewX))));

        float x1 = tipsViewX - offsetX;
        float y1 = tipsViewY + offsetY;

        float x2 = tipsViewMoveX - offsetX;
        float y2 = tipsViewMoveY + offsetY;

        float x3 = tipsViewMoveX + offsetX;
        float y3 = tipsViewMoveY - offsetY;

        float x4 = tipsViewX + offsetX;
        float y4 = tipsViewY - offsetY;

        // 重置 path
        path.reset();
        // 移到点 (x1,y1)
        path.moveTo(x1,y1);
        // 以 (x1,y1) 为起点，(x2,y2) 为终点，控制点为两圆心的中心点，画一条二阶贝塞尔曲线
        path.quadTo(controllerX,controllerY,x2,y2);
        // 画直线
        path.lineTo(x3,y3);
        // 再对称画一条二阶贝塞尔曲线
        path.quadTo(controllerX,controllerY,x4,y4);
        // 画直线，封闭区域
        path.lineTo(x1,y1);
    }

    private void reset() {
        isDragStart = false;
        isLimit = false;
        path.reset();
        tipsViewMoveX = tipsViewX;
        tipsViewMoveY = tipsViewY;
        tipsView.setX(tipsViewX - tipsView.getWidth() / 2);
        tipsView.setY(tipsViewY - tipsView.getHeight() / 2);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        tipsViewX = getWidth() / 2;
        tipsViewY = getHeight() / 2;
        tipsView.setX(tipsViewX - tipsView.getWidth() / 2);
        tipsView.setY(tipsViewY - tipsView.getHeight() / 2);

        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw: ");
        
        super.onDraw(canvas);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {

        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.OVERLAY);
        if (isDragStart && !isLimit) {
            calculateDrawPath();
            canvas.drawPath(path, paint);
            canvas.drawCircle(tipsViewX, tipsViewY, radius, paint);
            canvas.drawCircle(tipsViewMoveX, tipsViewMoveY, radius, paint);
        }
        
        super.dispatchDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (isLimit) {
            reset();
            return super.onTouchEvent(event);
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isTouchInTipsView((int) event.getRawX(), (int) event.getRawY())) {
                    isDragStart = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(!isDragStart) {
                    break;
                }
                tipsViewMoveX = (int) event.getX();
                tipsViewMoveY = (int) event.getY();
                controllerX = (tipsViewX + tipsViewMoveX) / 2;
                controllerY = (tipsViewY + tipsViewMoveY) / 2;

                tipsView.setX(tipsViewMoveX - tipsView.getWidth() / 2);
                tipsView.setY(tipsViewMoveY - tipsView.getHeight() / 2);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                reset();
                invalidate();
                break;
        }
        return true;
    }
}
