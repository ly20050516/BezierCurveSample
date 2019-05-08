package ly.com.bezier.curve.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author：ly on 2019/5/7 22:40
 * @mail：liuyan@zhimei.ai
 */
public class PointView extends TextView {

    public PointView(Context context) {
        super(context);
    }

    public PointView(Context context,String pointText) {
        super((context));
        setText(pointText);
        setTextColor(Color.BLACK);
        setBackgroundColor(Color.TRANSPARENT);
        setGravity(Gravity.CENTER);
    }

    public int centerX() {
        return getLeft() + getWidth() / 2;
    }

    public int centerY() {
        return getTop() + getHeight() / 2;
    }

    private float lastX;
    private float lastY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                actionMove(x,y);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    private void actionMove(float x,float y) {
        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) getLayoutParams();
        int leftMargin = (int) (getLeft() + (x - lastX));
        int topMargin = (int) (getTop() + (y - lastY));

        final int gap = 5;
        if(Math.abs(leftMargin - mlp.leftMargin) < gap && Math.abs(topMargin - mlp.topMargin) < gap) {
            return;
        }

        mlp.leftMargin = leftMargin;
        mlp.topMargin = topMargin;
        setLayoutParams(mlp);
    }

    private Paint paint = new Paint();
    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(Color.YELLOW);
        canvas.drawCircle(getWidth() / 2.0f,getHeight() / 2.0f,getWidth() / 2,paint);
        super.onDraw(canvas);
    }
}
