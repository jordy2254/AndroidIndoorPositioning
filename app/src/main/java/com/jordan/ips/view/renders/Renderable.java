package com.jordan.ips.view.renders;

import android.graphics.Canvas;

public interface Renderable {

    void render(Canvas canvas, double xOff, double yOff, double scale);

}
