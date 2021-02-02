package com.jordan.ips.model.data.map.persisted;

import com.jordan.renderengine.data.Point2d;

import java.io.Serializable;

public class Sensor implements Serializable {

    private Point2d position;
    private long floorId;
    private String sensorId;

    public Sensor(Point2d position, long floorId, String sensorId) {
        this.position = position;
        this.floorId = floorId;
        this.sensorId = sensorId;
    }

    public Point2d getPosition() {
        return position;
    }

    public long getFloorId() {
        return floorId;
    }

    public String getSensorId() {
        return sensorId;
    }
}
