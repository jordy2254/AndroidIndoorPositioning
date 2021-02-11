package com.jordan.ips.model.data.map.persisted;

import com.google.gson.annotations.SerializedName;
import com.jordan.renderengine.data.Point2d;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Building implements Serializable {

    long id;
    long mapId;
    String buildingName;
    Point2d location;
    List<Floor> floors;

    public void addFloor(Floor floor){
        if(this.floors == null){
            floors = new ArrayList<>();
        }
        floors.add(floor);
    }

    public List<Floor> getFloors() {
        return floors;
    }

    public void setFloors(List<Floor> floors) {
        this.floors = floors;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMapId() {
        return mapId;
    }

    public void setMapId(long mapId) {
        this.mapId = mapId;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public Point2d getLocation() {
        return location;
    }

    public void setLocation(Point2d location) {
        this.location = location;
    }
}
