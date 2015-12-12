package draughts.com.draughts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class DraughtsView extends View{
    private Context mContext;
    private Paint redPaint;

    public DraughtsView(Context context) {
        super(context);
        init(context);
    }
    public DraughtsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DraughtsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private void init(Context context) {
        mContext = context;
        redPaint = new Paint();
        redPaint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(100, 100, 500, redPaint);
    }
}
