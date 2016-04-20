package com.yantur.artboard.shapes;

import android.graphics.Bitmap;

public class MyImage extends CanvasObject {


    public MyImage(Bitmap bitmap, float x, float y) {
        super(bitmap, x, y);
    }

    @Override
    public boolean touchInObject(float x, float y) {
        if (x >= getX() && x <= getX() + getRadius() && y >= getY() && y <= getY() + getRadius()) {
            return true;
        }
        return false;
    }

}
