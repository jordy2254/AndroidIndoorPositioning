package com.example.ips.model.data.pathfinding;

import com.example.ips.model.data.pathfinding.persisted.PathNode;

public class AStarNode {

    public PathNode pathNode;
    public AStarNode parent;
    public int gCost, hCost, fCost;

    public AStarNode(PathNode pathNode, AStarNode parent, int gCost, int hCost) {
        this.pathNode = pathNode;
        this.parent = parent;
        this.gCost = gCost;
        this.hCost = hCost;
        this.fCost = gCost + hCost;
    }

    public void setCost(int gCost, int hCost){
        this.gCost = gCost;
        this.hCost = hCost;
        this.fCost = gCost + hCost;
    }

    public void setParent(AStarNode parent){
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AStarNode aStarNode = (AStarNode) o;
        return aStarNode.pathNode.equals(this.pathNode);
    }
}
