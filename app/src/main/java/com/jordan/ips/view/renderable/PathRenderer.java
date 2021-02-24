package com.jordan.ips.view.renderable;

import com.jordan.ips.model.data.pathfinding.PathNode;
import com.jordan.renderengine.Screen;
import com.jordan.renderengine.data.Point2d;
import com.jordan.renderengine.graphics.Renderable;

import java.util.List;

public class PathRenderer implements Renderable {
    List<PathNode> nodes;

    public PathRenderer(List<PathNode> nodes) {
        this.nodes = nodes;
    }

    @Override
    public void render(Screen screen, Point2d offset, double scale) {
        Point2d prevDraw = null;
        for (int i = 0; i < nodes.size(); i++) {
            PathNode node = nodes.get(i);
            Point2d nodeLocation = node.location;
            Point2d drawLocation = nodeLocation.multiply(new Point2d(scale, scale));
            drawLocation = drawLocation.add(offset);

            int color = 0x0000ff;
            if(i == 0){
                color = 0xff0000;
            }
            if(i == nodes.size() - 1){
                color = 0x00ff00;
            }
            if (prevDraw != null) {
                screen.drawLine(((int) drawLocation.x), ((int) drawLocation.y), ((int) prevDraw.x), ((int) prevDraw.y), 3, 0x0000ff);
            }
            screen.renderRect(((int) drawLocation.x - 10), ((int) drawLocation.y - 10), 20, 20, color);
            prevDraw = drawLocation;
        }
    }

    public void setNodes(List<PathNode> nodes) {
        this.nodes = nodes;
    }
}
