package com.jordan.ips.model.utils;

import android.util.Log;

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

    public static void unlinkDynamicPathNode(PathNode node){
        if(!dynamicPathNodes.contains(node)){
            throw new RuntimeException("Node is not dynamic");
        }

        for(PathNode n : node.childNodes){
            n.removeChildNode(node);
        }

        node.childNodes.clear();
        dynamicPathNodes.remove(node);
    }

    public static void clearAllDynamicPathNodes(){
        for (int i = dynamicPathNodes.size() - 1; i >= 0; i--) {
            PathNode node = dynamicPathNodes.get(i);
            unlinkDynamicPathNode(node);
        }
    }

    public static PathNode createDynamicPathNode(Map map, Point2d point, int floorIndex) {
        PathNode nNode = new PathNode(point);

        //Find the room the point is part of
        Room pointRoom = map.getBuildings().stream()
                .flatMap(building -> building.getFloors().stream())
                .filter(floor -> floor.getFloorNumber() == floorIndex)
                .flatMap(floor -> floor.getRooms().stream())
                .filter(room1 -> MapUtils.isPointInRoom(map, room1, point))
                .findFirst()
                .orElse(null);

        if (pointRoom == null) {
            return nNode;
        }

        //filter all relevent path nodes within that room
        List<PathNode> releventNodes = map.getRootNode()
                .flattenNodes()
                .stream()
                .filter(pathNode -> MapUtils.isPointInRoom(map, pointRoom, pathNode.getLocation()))
                .collect(Collectors.toList());

        for (PathNode node : releventNodes) {
            List<Pair<Point2d, Point2d>> walls = RoomPolygonGenerator.calculateWalls(pointRoom);

            boolean nodeOk = true;
            for (Pair<Point2d, Point2d> wall : walls) {
                wall.fst = wall.fst.add(pointRoom.getLocation());
                wall.snd = wall.snd.add(pointRoom.getLocation());

                if (LineIntersection.get_line_intersection(nNode.location.x, nNode.location.y,
                        node.location.x, node.location.y,
                        wall.fst.x, wall.fst.y,
                        wall.snd.x, wall.snd.y)) {
                    nodeOk = false;
                }

                if (!nodeOk) {
                    break;
                }
            }
            if (nodeOk) {
                nNode.addChild(true, node);
            }
        }

        dynamicPathNodes.add(nNode);
        return nNode;
    }
}
