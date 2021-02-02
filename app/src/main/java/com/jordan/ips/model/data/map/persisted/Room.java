package com.jordan.ips.model.data.map.persisted;

import java.io.Serializable;
import java.util.List;

import com.jordan.renderengine.data.Point2d;
import com.jordan.ips.model.utils.IndentLocationFinder;

import com.jordan.ips.model.exceptions.InvalidLocationException;


public class Room implements Serializable {

    long id;
    long floorId;
    private String name;

    Point2d location;
    Point2d dimensions;

    private List<RoomIndent> indents;


    public boolean isPointInRoom(Point2d point){

        //if we're not in initial wall bounds
        if(!(point.x >= location.x && point.x <= location.x+dimensions.x && point.y >= location.y && point.y <= location.y+dimensions.y)){
            return false;
        }

        //if we're within an indent, we're outside of room bounds
        for(RoomIndent indent : indents){
            try {
                double[] startLocations = IndentLocationFinder.findStartPointsOfIndent(this, indent, false);
                if((point.x >= startLocations[0] && point.x <=  startLocations[0]+indent.getDimensions().x && point.y >=  startLocations[1] && point.y <=  startLocations[1]+indent.getDimensions().y)){
                    return false;
                }
            } catch (InvalidLocationException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

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
}
