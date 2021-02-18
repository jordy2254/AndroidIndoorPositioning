package com.jordan.ips.model.utils;

import com.jordan.ips.model.data.map.persisted.Map;
import com.jordan.ips.model.data.map.persisted.Room;
import com.jordan.ips.model.data.pathfinding.PathNode;
import com.jordan.ips.model.utils.rendering.RoomPolygonGenerator;
import com.jordan.renderengine.data.Pair;
import com.jordan.renderengine.data.Point2d;

import java.util.List;
import java.util.stream.Collectors;

public class PathFindingUtils {

    private PathFindingUtils() {
    }

    public static PathNode createDynamicPathNode(Map map, Point2d point, int floorIndex) {
        PathNode nNode = new PathNode(point);

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
                nNode.addChild(false, node);
            }
        }
//        if(room == null){
//            return;
//        }
//        List<Pair<Point2d, Point2d>> walls = RoomPolygonGenerator.calculateWalls(room);
//
//        LineIntersection.get_line_intersection()
//        nNode.addChild(false, pathNode);
        return nNode;
    }
}
