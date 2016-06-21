package chaitanya.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;


public class DrawCircle extends View{
    private Paint paint;
    public DrawCircle(Context context) {

        super(context);
        paint = new Paint();
        paint.setColor(Color.BLUE);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.RED);
        canvas.drawCircle(200,200,100,paint);
    }
}
