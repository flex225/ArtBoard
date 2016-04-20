package com.yantur.artboard.shapes;

import android.graphics.Bitmap;
import android.graphics.Paint;

public abstract class CanvasObject {
    float x, y;
    int radius;
    Bitmap bitmap;
    Paint paint;
    boolean isMoved;

    public CanvasObject(Bitmap bitmap, float x, float y) {
        this.x = x;
        this.y = y;
        radius = 200;
        this.bitmap = bitmap;
        isMoved = false;
    }

    public CanvasObject(float x, float y, int radius, Paint paint) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.paint = paint;
        isMoved = false;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public boolean isMoved() {
        return isMoved;
    }

    public void setIsMoved(boolean isMoved) {
        this.isMoved = isMoved;
    }

    public boolean touchInObject(float x, float y) {
        float deltaX = this.x - x;
        float deltaY = this.y - y;

        return radius * radius >= deltaX * deltaX + deltaY * deltaY;
    }

}
