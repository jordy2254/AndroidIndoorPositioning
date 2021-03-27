package com.jordan.ips.model.utils.rendering;


import com.jordan.ips.model.data.map.persisted.Room;
import com.jordan.ips.model.data.map.persisted.RoomIndent;
import com.jordan.ips.model.exceptions.InvalidLocationException;
import com.jordan.ips.model.utils.IndentLocationFinder;
import com.jordan.renderengine.data.Pair;
import com.jordan.renderengine.data.Point2d;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RoomPolygonGenerator {


    private RoomPolygonGenerator(){}

    public static List<Point2d>  translate(List<Point2d> points, Point2d translation){
        List<Point2d> nPoints = new ArrayList<>();
        points.forEach(point2d -> nPoints.add(point2d.add(translation)));
        return nPoints;
    }

    public static List<Point2d> scale(List<Point2d> points, double scale) {
        List<Point2d> nPoints = new ArrayList<>();
        points.forEach(point2d -> nPoints.add(point2d.multiply(new Point2d(scale, scale))));
        return nPoints;
    }
}
