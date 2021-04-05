package com.jordan.ips.view.renderable;

import com.jordan.ips.model.data.map.persisted.Map;
import com.jordan.ips.model.data.pathfinding.PathNode;
import com.jordan.ips.model.utils.rendering.RoomPolygonGenerator;
import com.jordan.renderengine.Screen;
import com.jordan.renderengine.data.Pair;
import com.jordan.renderengine.data.Point2d;
import com.jordan.renderengine.data.Point2i;
import com.jordan.renderengine.graphics.Renderable;
import com.jordan.renderengine.utils.RenderUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MapRenderer implements Renderable {

    public static final int ROOM_COLOR = 0xf0ae5d;
    public static final int WALL_COLOR = 0x000000;

    private int selectedFloorIndex = 0;
    private final Map selectedMap;


    public MapRenderer(Map selectedMap) {
        this.selectedMap = selectedMap;
    }

    @Override
    public void render(Screen screen, Point2d offset, double scale) {
        selectedMap.getBuildings().forEach(building -> building.getFloors()
                .stream()
                .filter(floor -> floor.getFloorNumber() == selectedFloorIndex)
                .forEach(floor -> floor.getRooms().forEach(room -> {

                        if(room.getId() != 1){
//                            return;
                        }

                        Point2d translation = floor.getLocation()
                                .add(building.getLocation())
                                .add(room.getLocation())
                                .multiply(new Point2d(scale, scale))
                                .add(offset);

                        List<Point2d> renderPoints = RoomPolygonGenerator.scale(room.getPolygon(), scale);
                        renderPoints = RoomPolygonGenerator.translate(renderPoints, translation);

                    screen.drawPolygonUpdated(renderPoints, 2, 0x0, ROOM_COLOR, true);

                        for (Pair<Point2d, Point2d> x :room.getWalls()) {
                            Point2d scaled1 = x.fst.multiply(new Point2d(scale, scale)).add(translation);
                            Point2d scaled2 = x.snd.multiply(new Point2d(scale, scale)).add(translation);

                            screen.drawLine((int)scaled1.x, (int)scaled1.y, (int)scaled2.x, (int)scaled2.y, 2, WALL_COLOR);
                        }

                    })));
        renderPathNodes(screen, offset, scale);
    }

    public int getSelectedFloorIndex() {
        return selectedFloorIndex;
    }

    public void setSelectedFloorIndex(int selectedFloorIndex) {
        if(selectedFloorIndex == this.selectedFloorIndex){
            return;
        }
        this.selectedFloorIndex = selectedFloorIndex;
    }

public void renderPathNodes(Screen screen, Point2d offset, double scale){
        if(selectedMap.getRootNode() == null){
            return;
        }
        List<PathNode> flattenedNodes = selectedMap.getRootNode().flattenNodes();

        for(PathNode n : flattenedNodes){
        int size = 10;
        int dx = (int)((n.getLocation().x * scale) + offset.x - (size / 2));
        int dy = (int)((n.getLocation().y * scale) + offset.y - (size / 2));
        screen.renderRect(dx, dy, size, size, 0xffeeff);
    }


    //draw our path points
    List<PathNode> nonComplete = new ArrayList<>(Collections.singletonList(selectedMap.getRootNode()));
    List<PathNode> complete = new ArrayList<>();

        while(!nonComplete.isEmpty()) {
            PathNode node = nonComplete.get(0);
            if (node.getChildNodes() != null) {
                for (PathNode child : node.getChildNodes()) {
                    Point2i p1 = RenderUtils.calculateRenderLocation(node.getLocation(), offset, scale);
                    Point2i p2 = RenderUtils.calculateRenderLocation(child.getLocation(), offset, scale);

                    screen.drawLine(p1.x, p1.y, p2.x, p2.y, 3, 0xfefefe);
                    if (!complete.contains(child) && !nonComplete.contains(child)) {
                        nonComplete.add(child);
                    }
                }
            }

            nonComplete.remove(node);
            complete.add(node);
        }
}

}
