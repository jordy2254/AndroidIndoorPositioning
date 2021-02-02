package com.jordan.renderengine.graphics;

import com.jordan.renderengine.Screen;
import com.jordan.renderengine.data.Point2d;

public interface Renderable {

    void render(Screen screen, Point2d offset, double scale);

}
