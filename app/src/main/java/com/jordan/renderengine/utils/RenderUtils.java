package com.jordan.renderengine.utils;

import com.jordan.renderengine.data.Point2d;
import com.jordan.renderengine.data.Point2i;

public class RenderUtils {

    private RenderUtils(){}

    public static final Point2i calculateRenderLocation(Point2d mapPosition, Point2d offsets, double scale){
        Point2d x = mapPosition.multiply(new Point2d(scale, scale));
        x = x.add(offsets);
        return new Point2i(x);
    }

    public static final Point2i calculateMapLocationFromScreen(Point2d touchLocation, Point2d offsets, double scale){
        Point2d x = touchLocation.subtract(offsets);
        x = x.divide(new Point2d(scale, scale));
        return new Point2i(x);
    }
}
