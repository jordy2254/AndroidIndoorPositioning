package com.jordan.ips.view.renderable;

import com.jordan.ips.model.data.map.persisted.Map;
import com.jordan.ips.model.data.pathfinding.PathNode;
import com.jordan.renderengine.Screen;
import com.jordan.renderengine.data.Pair;
import com.jordan.renderengine.data.Point2d;
import com.jordan.renderengine.data.Point2i;
import com.jordan.renderengine.graphics.Renderable;
import com.jordan.renderengine.utils.RenderUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MapRenderer implements Renderable {

    private int selectedFloorIndex = 0;
    private final Map selectedMap;

    java.util.Map<Long, List<Point2d>> roomPolygonCache = new HashMap<>();

    public MapRenderer(Map selectedMap) {
        this.selectedMap = selectedMap;
    }

    @Override
    public void render(Screen screen, Point2d offset, double scale) {
        selectedMap.getBuildings().forEach(building -> {
            building.getFloors()
                    .stream()
                    .filter(floor -> floor.getFloorNumber() == selectedFloorIndex)
                    .forEach(floor -> {
                        floor.getRooms().forEach(room -> {
                            if(!roomPolygonCache.containsKey(room.getId())){
                                roomPolygonCache.put(room.getId(), RoomPolygonGenerator.createPolygon(room));
                            }
                            Point2d translation = floor.getLocation()
                                    .add(building.getLocation())
                                    .add(room.getLocation())
                                    .multiply(new Point2d(scale, scale))
                                    .add(offset);


//                            List<Point2d> renderPoints = RoomPolygonGenerator.translate(roomPolygonCache.get(room.getId()), translation);
//                            renderPoints = RoomPolygonGenerator.scale(renderPoints, scale);
                            List<Point2d> renderPoints = RoomPolygonGenerator.scale(roomPolygonCache.get(room.getId()), scale);
                            renderPoints = RoomPolygonGenerator.translate(renderPoints, translation);
                            int color = 0xf0ae5d;
                            if(room.isSelected()){
                                color = 0xff00ff;
                            }
                            screen.drawPolygonUpdated(renderPoints, 2, 0x0, color, true);
                            screen.drawPolygon(renderPoints, 2, 0x0, true);
                        });
                    });
        });
    }

    public int getSelectedFloorIndex() {
        return selectedFloorIndex;
    }

    public void setSelectedFloorIndex(int selectedFloorIndex) {
        if(selectedFloorIndex == this.selectedFloorIndex){
            return;
        }
        roomPolygonCache.clear();
        this.selectedFloorIndex = selectedFloorIndex;
    }

//PATH NODE RENDERING
//    List<PathNode> flattenedNodes = map.getRootNode().flattenNodes();
//
//        for(PathNode n : flattenedNodes){
//        int size = 10;
//        int dx = (int)((n.getLocation().x * scale) + xOff - (size / 2));
//        int dy = (int)((n.getLocation().y * scale) + yOff - (size / 2));
//        screen.renderRect(dx, dy, size, size, 0xffeeff);
//    }
//
//
//    //draw our path points
//    List<PathNode> nonComplete = new ArrayList<>(Arrays.asList(map.getRootNode()));
//    List<PathNode> complete = new ArrayList<>();
//
//        while(!nonComplete.isEmpty()){
//        PathNode node = nonComplete.get(0);
//        if(node.getChildNodes() != null){
//            for(PathNode child : node.getChildNodes()){
//                Point2i p1 = RenderUtils.calculateRenderLocation(node.getLocation(), new Point2d(xOff, yOff), scale);
//                Point2i p2 = RenderUtils.calculateRenderLocation(child.getLocation(), new Point2d(xOff, yOff), scale);
//
//                screen.drawLine(p1.x, p1.y, p2.x, p2.y, 3, 0xfefefe);
//                if(!complete.contains(child) && !nonComplete.contains(child)){
//                    nonComplete.add(child);
//                }
//            }
//        }
//
//        nonComplete.remove(node);
//        complete.add(node);
}
