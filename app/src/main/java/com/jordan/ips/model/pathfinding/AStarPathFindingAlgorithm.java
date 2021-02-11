package com.jordan.ips.model.pathfinding;



import com.jordan.ips.model.data.pathfinding.AStarNode;
import com.jordan.ips.model.data.pathfinding.PathNode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AStarPathFindingAlgorithm {

    private List<AStarNode> openList;
    private List<AStarNode> closeList;

    private PathNode startNode, endNode;
    private AStarNode currentNode = null;

    public AStarPathFindingAlgorithm(PathNode startNode, PathNode endNode) {
        this.startNode = startNode;
        this.endNode = endNode;
        reset();
    }

    public List<PathNode> compute(){
        while(openList.size() > 0 && currentNode.pathNode != endNode){
            step();
        }

        List<PathNode> retVal = new ArrayList<>();
        retVal.add(currentNode.pathNode);
        while(currentNode.parent != null){
            currentNode = currentNode.parent;
            retVal.add(currentNode.pathNode);
        }

        return retVal;
    }

    public PathNode getEndNode(){
        return endNode;
    }

    public void setStartNode(PathNode startNode){
        this.startNode = startNode;
        reset();
    }


    public void reset(){
        openList = new ArrayList<>();
        closeList = new ArrayList<>();
        if(startNode != null){
            int[] cost = calculateCost(startNode, endNode);
            currentNode = new AStarNode(startNode, null, cost[0], cost[1]);
            openList.add(currentNode);
        }
    }

    private int[] calculateCost(PathNode currentNode, PathNode childNode) {
        int gCost = (int)(int)Math.abs(calculateEuclidian(endNode.getLocation().x, endNode.getLocation().y, childNode.getLocation().x, childNode.getLocation().y)*10000);
        int hCost = (int)Math.abs(calculateEuclidian(startNode.getLocation().x, startNode.getLocation().y, currentNode.getLocation().x, currentNode.getLocation().y) *10000);
        return new int[]{gCost, hCost};
    }

    private void setCost(AStarNode currentNode, AStarNode childNode) {
        int[] costs = calculateCost(currentNode.pathNode, childNode.pathNode);
        currentNode.setCost(costs[0], costs[1]);
    }

    public float calculateEuclidian(double x1, double y1, double x2, double y2) {
        float result = (float) Math.sqrt( ((y1 - y2) * (y1 - y2))+ ((x1 - x2) * (x1 - x2)));
        return result;
    }

    private Comparator<AStarNode> nodeSorter = new Comparator<AStarNode>() {
        @Override
        public int compare(AStarNode n0, AStarNode n1) {
            if(n1.fCost < n0.fCost){
                return 1;
            }else if(n1.fCost > n0.fCost){
                return -1;
            }else if(n1.fCost == n0.fCost){
                if(n1.gCost < n0.gCost){
                    return 1;
                }else if(n1.gCost > n0.gCost){
                    return -1;
                }
            }
            return 0;
        }
    };

    public void step() {
//        openList.sort(nodeSorter);
        currentNode = openList.get(0);

        openList.remove(currentNode);
        closeList.add(currentNode);

        for (PathNode child : currentNode.pathNode.childNodes){
            int[] cost = calculateCost(currentNode.pathNode, child);
            AStarNode existingClosedNode = getNodeFromList(closeList, child);

            if(existingClosedNode != null && cost[0] >= existingClosedNode.gCost){
                continue;
            }

            //we havn't discovered this node yet
            if(!listContains(openList, existingClosedNode) && !listContains(closeList, existingClosedNode)){
                openList.add(new AStarNode(child, currentNode, cost[0], cost[1]));
            }

            if(existingClosedNode != null && cost[0] < existingClosedNode.gCost){
                AStarNode existingNode = getNodeFromList(closeList, existingClosedNode);
                closeList.remove(existingNode);
                existingNode.parent = currentNode;
                existingNode.setCost(cost[0], cost[1]);
                openList.add(existingNode);
            }
        }
    }

    private AStarNode getNodeFromList(List<AStarNode> list, AStarNode node){
        for(AStarNode tmpNode : list){
            if(tmpNode.equals(node)){
                return tmpNode;
            }
        }
        return null;
    }

    private AStarNode getNodeFromList(List<AStarNode> list, PathNode node){
        for(AStarNode tmpNode : list){
            if(tmpNode.pathNode.equals(node)){
                return tmpNode;
            }
        }
        return null;
    }

    private boolean listContains(List<AStarNode> list, AStarNode node){
        for(AStarNode tmpNode : list){
            if(tmpNode.equals(node)){
                return true;
            }
        }
        return false;
    }
}
