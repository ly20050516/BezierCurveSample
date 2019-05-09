package ly.com.bezier.curve.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

/**
 * @author：ly on 2019/5/9 17:42
 * @mail：liuyan@zhimei.ai
 */
public class TipsView extends TextView {

    private Paint paint;
    public TipsView(Context context) {
        this(context,null);
    }

    public TipsView(Context context,  @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context) {

        setBackgroundColor(Color.TRANSPARENT);
        setTextColor(Color.WHITE);
        setGravity(Gravity.CENTER);
        paint = new Paint();
        paint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        // 绘制圆形背景
        canvas.drawCircle(getWidth() / 2,getHeight() / 2,getWidth() / 2,paint);

        super.onDraw(canvas);
    }
}
