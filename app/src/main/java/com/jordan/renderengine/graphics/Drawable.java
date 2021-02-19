package com.jordan.renderengine.graphics;

import android.graphics.Canvas;

import com.jordan.renderengine.data.Point2d;

public interface Drawable {

    void draw(Canvas canvas, Point2d offset, double scale);

}
