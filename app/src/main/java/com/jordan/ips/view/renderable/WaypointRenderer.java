package com.jordan.ips.view.renderable;

import com.jordan.ips.model.data.waypoints.Waypoint;
import com.jordan.renderengine.Screen;
import com.jordan.renderengine.data.Point2d;
import com.jordan.renderengine.data.Point2i;
import com.jordan.renderengine.graphics.Renderable;
import com.jordan.renderengine.utils.RenderUtils;

public class WaypointRenderer implements Renderable {

    private Waypoint waypoint;

    public WaypointRenderer(Waypoint waypoint) {
        this.waypoint = waypoint;
    }

    @Override
    public void render(Screen screen, Point2d offset, double scale) {
        Point2i renderLocation = RenderUtils.calculateRenderLocation(waypoint.getLocation(), offset, scale);
        screen.renderRect(renderLocation.x, renderLocation.y, 10,10,0xaaaaaa);
    }
}
