package com.jordan.ips.model.data.pathfinding;

import com.jordan.ips.model.data.map.persisted.Room;
import com.jordan.renderengine.data.Point2d;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PathNode implements Serializable {

    public Point2d location;
    public List<Room> rooms;
    public List<PathNode> childNodes;
    int mapId;

    public PathNode(Point2d location, List<Room> rooms) {
        this.location = location;
        this.rooms = rooms;
        childNodes = new ArrayList<>();
    }

    public PathNode(Point2d location) {
        this.location = location;
        childNodes = new ArrayList<>();
    }

    public List<PathNode> getChildNodes() {
        return childNodes;
    }

    public String generateKey(){
        return location.x + "," + location.y;
    }

    public void addChild(boolean twoWay, PathNode child) {
        childNodes.add(child);
        if(twoWay){
            child.addChild(false, this);
        }
    }
    public void addChild(boolean twoWay, PathNode... children) {
        for(PathNode child : children){
            addChild(twoWay, child);
        }
    }

    public void addChild(boolean twoWay, List<PathNode> children) {
        for(PathNode child : children){
            addChild(twoWay, child);
        }
    }
    public void addRoom(Room room){
        this.rooms.add(room);
    }

    public List<PathNode> flattenNodes(){
        List<PathNode> nonComplete = new ArrayList<>(Arrays.asList(this));
        List<PathNode> complete = new ArrayList<>();

        while(!nonComplete.isEmpty()){
            PathNode node = nonComplete.get(0);
            if(node.getChildNodes() != null){
                for(PathNode child : node.getChildNodes()){
                    if(!complete.contains(child) && !nonComplete.contains(child)){
                        nonComplete.add(child);
                    }
                }
            }

            nonComplete.remove(node);
            complete.add(node);
        }

        return complete;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public void setChildNodes(List<PathNode> childNodes) {
        this.childNodes = childNodes;
    }

    public int getMapId() {
        return mapId;
    }

    public void setMapId(int mapId) {
        this.mapId = mapId;
    }

    public Point2d getLocation() {
        return location;
    }

    public void setLocation(Point2d location) {
        this.location = location;
    }
}
