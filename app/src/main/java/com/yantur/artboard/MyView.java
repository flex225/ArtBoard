package com.yantur.artboard;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;

import com.yantur.artboard.shapes.CanvasObject;
import com.yantur.artboard.shapes.MyCircle;
import com.yantur.artboard.shapes.MyImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyView extends View {

    private float width;
    private float height;
    private List<CanvasObject> canvasObjects;
    List<Integer> colors;
    private CanvasObject currObject;
    private boolean draw;
    Paint linesColor;
    Paint backgroundColor;
    float scaleSize =1;

    public MyView(Context context) {
        super(context);
        init();
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        RectF rectF = new RectF(0, 0, width, height);
        canvas.drawRect(rectF, backgroundColor);

        if (draw) {
            for (int i = 0; i <= height; i += 50) {
                canvas.drawLine(0, i, width, i, linesColor);
            }
            for (int i = 0; i <= width; i += 50) {
                canvas.drawLine(i, 0, i, height, linesColor);
            }
        }
        for (CanvasObject curr :
                canvasObjects) {
            if (curr.getBitmap() != null) {
                canvas.drawBitmap(curr.getBitmap(), curr.getX(), curr.getY(), null);
            } else {
                if (draw || curr.getPaint().getColor() != Color.YELLOW) {
                    canvas.drawCircle(curr.getX(), curr.getY(), curr.getRadius(), curr.getPaint());
//                    Matrix scale = new Matrix();
//                    scale.setScale(scaleSize, scaleSize, curr.getX(), curr.getY());
//                    Path path = new Path();
//                    path.addCircle(curr.getX(), curr.getY(), curr.getRadius(), Path.Direction.CCW);
//                    path.transform(scale);
//                    canvas.drawPath(path, curr.getPaint());
                }
            }
        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                for (int i = canvasObjects.size() - 1; i >= 0; i--) {
                    if (canvasObjects.get(i).touchInObject(event.getX(), event.getY())) {
                        currObject = canvasObjects.get(i);
                        return true;
                    }
                }
                return false;
            case MotionEvent.ACTION_MOVE:
                currObject.setX(event.getX());
                currObject.setY(event.getY());
                currObject.setIsMoved(true);
                invalidate();
                return false;

            case MotionEvent.ACTION_UP:
                if (currObject.isMoved() == false && event.getEventTime() - event.getDownTime() >= 1000) {
                    canvasObjects.remove(currObject);
                    invalidate();
                    return false;
                }
                currObject.setIsMoved(false);
                return false;
        }
        return true;
    }

    public Bitmap getBitmap() {
        draw = false;
        invalidate();
        setDrawingCacheEnabled(true);
        buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(getDrawingCache());
        setDrawingCacheEnabled(false);
        draw = true;
        invalidate();
        return bitmap;
    }

    public void addPhotoToCanvas(Bitmap bitmap) {
        bitmap = cropBitmap(bitmap);
        MyImage image = new MyImage(bitmap, width / 2, height / 2);
        canvasObjects.add(image);
        invalidate();
    }

    public void addCircle() {
        Paint circlePaint = new Paint();
        Random random = new Random();
        int radius = random.nextInt(150) + 50;
        circlePaint.setColor(colors.get(random.nextInt(colors.size() - 1)));

        final MyCircle circle = new MyCircle(width / 2, height / 2, 0, circlePaint);
        canvasObjects.add(circle);

        ValueAnimator animator = ValueAnimator.ofInt(0, radius);
        animator.setDuration(1000);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                circle.setRadius((int) animation.getAnimatedValue());
                invalidate();
            }
        });
        animator.start();
        invalidate();
    }


    public Bitmap cropBitmap(Bitmap source) {
        Bitmap result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Rect rect = new Rect(0, 0, source.getWidth(), source.getHeight());
        RectF rectF = new RectF(rect);
        Paint paint = new Paint();

        paint.setAntiAlias(true);
        canvas.drawArc(rectF, -270, 180, true, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, null, rect, paint);

        return result;
    }

    private void init() {
        canvasObjects = new ArrayList<>();
        colors = new ArrayList<>();
        colors.add(Color.BLUE);
        colors.add(Color.YELLOW);
        colors.add(Color.GREEN);
        colors.add(Color.RED);
        draw = true;
        linesColor = new Paint();
        backgroundColor = new Paint();
        linesColor.setColor(Color.BLACK);
        backgroundColor.setColor(Color.WHITE);
    }
}
