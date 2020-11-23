package com.jordan.ips.model.data.map.persisted;

import com.jordan.ips.model.data.Point2d;

import java.io.Serializable;

public class RoomIndent implements Serializable {

    private long id;
    private long roomId;
    private String wallKeyA, wallKeyB;

    private Point2d dimensions;
    private double location;

    public String getWallKeyA() {
        return wallKeyA;
    }

    public void setWallKeyA(String wallKeyA) {
        this.wallKeyA = wallKeyA;
    }

    public String getWallKeyB() {
        return wallKeyB;
    }

    public void setWallKeyB(String wallKeyB) {
        this.wallKeyB = wallKeyB;
    }

    public Point2d getDimensions() {
        return dimensions;
    }

    public void setDimensions(Point2d dimensions) {
        this.dimensions = dimensions;
    }

    public double getLocation() {
        return location;
    }

    public void setLocation(double location) {
        this.location = location;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }
}
