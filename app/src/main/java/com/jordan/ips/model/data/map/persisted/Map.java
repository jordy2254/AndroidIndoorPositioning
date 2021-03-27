package com.jordan.ips.model.data.map.persisted;

import com.jordan.ips.model.data.pathfinding.PathNode;
import com.jordan.ips.model.data.pathfinding.persisted.MapEdge;
import com.jordan.ips.model.data.pathfinding.persisted.MapNode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class Map implements Serializable {

    private long id;
    private String identifier;
    private String password;
    private String name;
    private double northAngle;

    private List<Building> buildings;

    private List<MapNode> nodes;
    private List<MapEdge> edges;

    private PathNode rootNode;

    public PathNode getRootNode(){
        return createPathgraph(false);
    }

    public PathNode createPathgraph(boolean forceUpdate){
        if(!forceUpdate && rootNode != null){
            return rootNode;
        }

        Optional<MapNode> rootNode = nodes.stream().filter(MapNode::isRootNode).findFirst();

        if(!rootNode.isPresent()){
            return null;
        }

        PathNode root = new PathNode(rootNode.get().getLocation());

        HashMap<Integer, MapNode> indexedNodes = new HashMap<>();
        for(MapNode node : nodes){
            indexedNodes.put(node.getId(), node);
        }


        HashMap<Integer, List<MapEdge>> indexedEdges = new HashMap<>();
        for(MapEdge edge : edges){
            if (indexedEdges.get(edge.getNode1Id()) == null) {
                indexedEdges.put(edge.getNode1Id(), new ArrayList<>());
            }
            indexedEdges.get(edge.getNode1Id()).add(edge);
        }

        List<Integer> visitedNodes = new ArrayList<>();
        List<Integer> nonVisitedNodes = new ArrayList<>();
        nonVisitedNodes.add(rootNode.get().getId());

        HashMap<Integer, PathNode> indexedPathNodes = new HashMap<>();
        indexedPathNodes.put(rootNode.get().getId(), root);

        while (!nonVisitedNodes.isEmpty()){
            Integer cId = nonVisitedNodes.get(nonVisitedNodes.size() - 1);
            nonVisitedNodes.remove(nonVisitedNodes.size() - 1);
            visitedNodes.add(cId);

            PathNode pathNode = getOrCreatePathNode(cId, indexedNodes, indexedPathNodes);

            if(pathNode == null){
                continue;
            }

            if(indexedEdges.get(cId) == null){
                continue;
            }
            for(MapEdge edge : indexedEdges.get(cId)){
                PathNode cp = getOrCreatePathNode(edge.getNode2Id(), indexedNodes, indexedPathNodes);
                if(!visitedNodes.contains(edge.getNode2Id())){
                    nonVisitedNodes.add(edge.getNode2Id());
                }
                if(cp == null){
                    continue;
                }
                pathNode.addChild(true, cp);
            }

        }
        this.rootNode = root;
        return this.rootNode;
    }

    private PathNode getOrCreatePathNode(Integer cId, HashMap<Integer, MapNode> indexedNodes, HashMap<Integer, PathNode> indexedPathNodes) {
        PathNode pathNode = null;
        if(indexedPathNodes.get(cId) == null){
            MapNode node = indexedNodes.get(cId);
            if(node == null){
                return null;
            }
            pathNode = new PathNode(node.getLocation());
            indexedPathNodes.put(cId, pathNode);
        }else{
            pathNode = indexedPathNodes.get(cId);
        }
        return pathNode;
    }


    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Building> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<Building> buildings) {
        this.buildings = buildings;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void addBuilding(Building building) {
        if (buildings == null) {
            buildings = new ArrayList<>();
        }
        this.buildings.add(building);
    }

    public List<MapNode> getNodes() {
        return nodes;
    }

    public List<MapEdge> getEdges() {
        return edges;
    }

    public double getNorthAngle() {
        return northAngle;
    }

    public void setNorthAngle(double northAngle) {
        this.northAngle = northAngle;
    }
}
