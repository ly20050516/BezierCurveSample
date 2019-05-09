package ly.com.bezier.curve.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * @author：ly on 2019/5/9 09:02
 * @mail：liuyan@zhimei.ai
 */
public class WaveView extends View {

    public static final String TAG = "WaveView";
    public static final int WAVE_AMPLITTUDE = 100;
    private Path path = new Path();
    private Paint paint = new Paint();
    private float offset = 0;
    private Matrix matrix = new Matrix();
    private ObjectAnimator objectAnimator;
    public WaveView(Context context) {
        this(context,null);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context) {

        paint.setAntiAlias(true);
        paint.setColor(Color.CYAN);
        paint.setStyle(Paint.Style.FILL);

        postDelayed(new Runnable() {
            @Override
            public void run() {
                objectAnimator = ObjectAnimator.ofFloat(WaveView.this,"offset",0,getWidth());
                objectAnimator.setDuration(2000L);
                objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
                objectAnimator.setInterpolator(new LinearInterpolator());
                objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        Log.d(TAG, "onAnimationUpdate: " + animation.toString());
                        postInvalidate();
                    }
                });
                objectAnimator.start();
            }
        },1000L);

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if(objectAnimator != null) {
            objectAnimator.cancel();
        }
    }

    public void setOffset(float offset) {
        this.offset = offset;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int w = getWidth();
        int h = getHeight() / 2;

        if(w == 0 || h == 0) {
            return;
        }

        path.reset();
        path.moveTo(0,h);
        path.quadTo(w / 4,h + WAVE_AMPLITTUDE,w / 2,h);
        path.quadTo(w * 3 / 4,h - WAVE_AMPLITTUDE,w,h);
        path.quadTo(w * 5 / 4,h + WAVE_AMPLITTUDE,w * 3 / 2,h);
        path.quadTo(w * 7 / 4,h - WAVE_AMPLITTUDE,w * 2,h);
        path.lineTo(w * 2,getHeight());
        path.lineTo(0,getHeight());
        path.close();

        matrix.reset();
        matrix.postTranslate(-offset,0);
        path.transform(matrix);

        canvas.drawPath(path,paint);
    }
}
