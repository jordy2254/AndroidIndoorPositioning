package com.jordan.ips.model.data.map.persisted;

import com.jordan.ips.model.data.Point2d;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Floor implements Serializable {

    private long id;
    private long buildingId;
    private int floorNumber;
    private String floorname;
    private Point2d location;
    private List<Room> rooms;
    private List<Sensor> sensors;

    public Floor() {
        rooms = new ArrayList<>();
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(long buildingId) {
        this.buildingId = buildingId;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public String getFloorname() {
        return floorname;
    }

    public void setFloorname(String floorname) {
        this.floorname = floorname;
    }

    public Point2d getLocation() {
        return location;
    }

    public void setLocation(Point2d location) {
        this.location = location;
    }

    private void addSensor(Sensor sensor){
        if (sensors == null) {
            sensors = new ArrayList<>();
        }
        sensors.add(sensor);
    }
}
