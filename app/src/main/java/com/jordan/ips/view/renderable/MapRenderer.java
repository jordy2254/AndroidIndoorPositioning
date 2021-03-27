package com.jordan.ips.view.renderable;

import com.jordan.ips.model.data.map.persisted.Map;
import com.jordan.ips.model.data.pathfinding.PathNode;
import com.jordan.ips.model.utils.rendering.RoomPolygonGenerator;
import com.jordan.renderengine.Screen;
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


                        Point2d translation = floor.getLocation()
                                .add(building.getLocation())
                                .add(room.getLocation())
                                .multiply(new Point2d(scale, scale))
                                .add(offset);


//                            List<Point2d> renderPoints = RoomPolygonGenerator.translate(roomPolygonCache.get(room.getId()), translation);
//                            renderPoints = RoomPolygonGenerator.scale(renderPoints, scale);
                        List<Point2d> renderPoints = RoomPolygonGenerator.scale(room.getPolygon(), scale);
                        renderPoints = RoomPolygonGenerator.translate(renderPoints, translation);
                        int color = 0xf0ae5d;
//                            if(room.isSelected()){
//                                color = 0xff00ff;
//                            }
                        screen.drawPolygonUpdated(renderPoints, 2, 0x0, color, true);
                        screen.drawPolygon(renderPoints, 2, 0x0, true);

                    })));
//        renderPathNodes(screen, offset, scale);
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
