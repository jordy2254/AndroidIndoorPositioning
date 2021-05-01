package com.jordan.ips.model.data.map.persisted;

import com.jordan.renderengine.data.Point2d;

import java.io.Serializable;

public class Sensor implements Serializable {

    private String id;
    private String sensorId;
    private int floorId;
    private Point2d location;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getFloorId() {
        return floorId;
    }

    public void setFloorId(int floorId) {
        this.floorId = floorId;
    }

    public Point2d getLocation() {
        return location;
    }

    public void setLocation(Point2d location) {
        this.location = location;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }
}
