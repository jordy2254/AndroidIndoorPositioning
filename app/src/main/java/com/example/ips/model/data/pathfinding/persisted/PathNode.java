package com.example.ips.model.data.pathfinding.persisted;

import com.example.ips.model.data.map.persisted.Room;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PathNode {

    public double x, y;
    public List<Room> rooms;
    public List<PathNode> childNodes;

    public PathNode(double x, double y, List<Room> rooms) {
        this.x = x;
        this.y = y;
        this.rooms = rooms;
        childNodes = new ArrayList<>();
    }

    public PathNode(double x, double y) {
        this.x = x;
        this.y = y;
        childNodes = new ArrayList<>();
    }

    public List<PathNode> getChildNodes() {
        return childNodes;
    }

    public String generateKey(){
        return x + "," + y;
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
            for(PathNode child : node.getChildNodes()){
                if(!complete.contains(child) && !nonComplete.contains(child)){
                    nonComplete.add(child);
                }
            }
            nonComplete.remove(node);
            complete.add(node);
        }

        return complete;
    }
}
