package com.jordan.ips.model.utils;

import com.jordan.ips.model.data.map.persisted.Map;
import com.jordan.ips.model.data.map.persisted.Room;
import com.jordan.ips.model.data.pathfinding.PathNode;
import com.jordan.ips.model.utils.rendering.RoomPolygonGenerator;
import com.jordan.renderengine.data.Pair;
import com.jordan.renderengine.data.Point2d;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PathFindingUtils {

    private static final List<PathNode> dynamicPathNodes = new ArrayList<>();

    private PathFindingUtils() {

    }

    public static void unlinkDynamicPathNode(PathNode node, boolean remove){
        if(!dynamicPathNodes.contains(node)){
            throw new RuntimeException("Node is not dynamic");
        }

        for(PathNode n : node.childNodes){
            n.removeChildNode(node);
        }

        node.childNodes.clear();

        if(remove){
            dynamicPathNodes.remove(node);
        }
    }

    public static void clearAllDynamicPathNodes(){
        for (int i = dynamicPathNodes.size() - 1; i >= 0; i--) {
            PathNode node = dynamicPathNodes.get(i);
            unlinkDynamicPathNode(node, true);
        }
    }

    private static void updateDynamicPathNode(PathNode node, Map map, Point2d point, int floorIndex){
        if(point.equals(node.location)){
            return;
        }
        unlinkDynamicPathNode(node, true);
        node.setLocation(point);
        linkDynamicPathNode(map, node, floorIndex);
    }

    public static PathNode createDynamicPathNode(Map map, Point2d point, int floorIndex) {
        PathNode nNode = new PathNode(point);
        linkDynamicPathNode(map, nNode, floorIndex);
        return nNode;
    }

    public static void linkDynamicPathNode(Map map, PathNode node, int floorIndex){

        //Find the room the point is part of
        Room pointRoom = map.getBuildings().stream()
                .flatMap(building -> building.getFloors().stream())
                .filter(floor -> floor.getFloorNumber() == floorIndex)
                .flatMap(floor -> floor.getRooms().stream())
                .filter(room1 -> MapUtils.isPointInRoom(map, room1, node.getLocation()))
                .findFirst()
                .orElse(null);

        if (pointRoom == null) {
            return;
        }

        //filter all relevent path nodes within that room
        List<PathNode> releventNodes = map.getRootNode()
                .flattenNodes()
                .stream()
                .filter(pathNode -> MapUtils.isPointInRoom(map, pointRoom, pathNode.getLocation()))
                .collect(Collectors.toList());

        for (PathNode n : releventNodes) {


            boolean nodeOk = true;
            for (Pair<Point2d, Point2d> wall : pointRoom.getWalls()) {
                wall.fst = wall.fst.add(pointRoom.getLocation());
                wall.snd = wall.snd.add(pointRoom.getLocation());

                if (LineIntersection.get_line_intersection(node.location.x, node.location.y,
                        n.location.x, n.location.y,
                        wall.fst.x, wall.fst.y,
                        wall.snd.x, wall.snd.y)) {
                    nodeOk = false;
                }

                if (!nodeOk) {
                    break;
                }
            }
            if (nodeOk) {
                node.addChild(true, n);
            }
        }

        dynamicPathNodes.add(node);
    }
}
