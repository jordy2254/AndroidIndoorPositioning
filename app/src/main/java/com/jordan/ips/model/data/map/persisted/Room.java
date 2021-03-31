package com.jordan.ips.model.data.map.persisted;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.jordan.renderengine.data.Pair;
import com.jordan.renderengine.data.Point2d;


public class Room implements Serializable {

    long id;
    long floorId;
    double rotation;
    private String name;

    Point2d location;
    Point2d dimensions;

    private List<Pair<Point2d, Point2d>> walls;
    private List<Point2d> polygon;

    private List<RoomIndent> indents;

    private transient boolean selected = false;

    public List<RoomIndent> getIndents() {
        return indents;
    }

    public void setIndents(List<RoomIndent> indents) {
        this.indents = indents;
    }

    public void addIndent(RoomIndent indent) {
        indents.add(indent);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getFloorId() {
        return floorId;
    }

    public void setFloorId(long floorId) {
        this.floorId = floorId;
    }

    public Point2d getLocation() {
        return location;
    }

    public void setLocation(Point2d location) {
        this.location = location;
    }

    public Point2d getDimensions() {
        return dimensions;
    }

    public void setDimensions(Point2d dimensions) {
        this.dimensions = dimensions;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    public List<Pair<Point2d, Point2d>> getWalls() {
        return walls;
    }

    public List<Pair<Point2d, Point2d>> getWallsCopy() {
        List<Pair<Point2d, Point2d>> retVal = new ArrayList<>();
        walls.forEach(p ->{
            retVal.add(new Pair(new Point2d(p.fst), new Point2d(p.snd)));
        });
        return retVal;
    }

    public void setWalls(List<Pair<Point2d, Point2d>> walls) {
        this.walls = walls;
    }

    public List<Point2d> getPolygon() {
        return polygon;
    }

    public void setPolygon(List<Point2d> polygon) {
        this.polygon = polygon;
    }
}
