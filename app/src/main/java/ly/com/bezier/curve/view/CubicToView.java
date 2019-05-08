package ly.com.bezier.curve.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * @author：ly on 2019/5/8 23:00
 * @mail：liuyan@zhimei.ai
 */
public class CubicToView extends FrameLayout {

    public static final int POINT_RADIUS = 100;

    private PointView startPoint;
    private PointView endPoint;
    private PointView controllerPoint1;
    private PointView controllerPoint2;

    public CubicToView(@NonNull Context context) {

        this(context,null);
    }

    public CubicToView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context) {

        startPoint = new PointView(context,"S");
        endPoint = new PointView(context,"E");
        controllerPoint1 = new PointView(context,"C1");
        controllerPoint2 = new PointView(context,"C2");

        addView(startPoint,new LayoutParams(POINT_RADIUS,POINT_RADIUS));
        addView(endPoint,new LayoutParams(POINT_RADIUS,POINT_RADIUS));
        addView(controllerPoint1,new LayoutParams(POINT_RADIUS,POINT_RADIUS));
        addView(controllerPoint2,new LayoutParams(POINT_RADIUS,POINT_RADIUS));
        setBackgroundColor(Color.BLACK);
    }

    private Path path = new Path();
    private Paint paint = new Paint();

    @Override
    protected void dispatchDraw(Canvas canvas) {

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);

        canvas.drawLine(startPoint.centerX(),startPoint.centerY(),controllerPoint1.centerX(),controllerPoint1.centerY(),paint);
        canvas.drawLine(controllerPoint1.centerX(),controllerPoint1.centerY(),controllerPoint2.centerX(),controllerPoint2.centerY(),paint);
        canvas.drawLine(controllerPoint2.centerX(),controllerPoint2.centerY(),endPoint.centerX(),endPoint.centerY(),paint);

        path.reset();
        path.moveTo(startPoint.centerX(),startPoint.centerY());
        path.cubicTo(controllerPoint1.centerX(),controllerPoint1.centerY(),controllerPoint2.centerX(),controllerPoint2.centerY(),endPoint.centerX(),endPoint.centerY());
        paint.setColor(Color.GREEN);
        canvas.drawPath(path,paint);

        super.dispatchDraw(canvas);
    }

}
