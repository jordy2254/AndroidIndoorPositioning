package com.jordan.ips.model.utils;

import com.jordan.ips.model.data.map.persisted.Map;
import com.jordan.ips.model.data.pathfinding.PathNode;
import com.jordan.renderengine.data.Point2d;

public class PathFindingUtils {

    private PathFindingUtils(){}

    public static PathNode createDynamicPathNode(Map map, Point2d point, int floorIndex){
        PathNode nNode = new PathNode(point);

        map.getRootNode().flattenNodes().stream().forEach(pathNode -> nNode.addChild(false, pathNode));
        return nNode;
    }
}
