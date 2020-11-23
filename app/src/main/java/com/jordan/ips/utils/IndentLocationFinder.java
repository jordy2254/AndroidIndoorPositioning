package com.jordan.ips.utils;

import com.jordan.ips.model.data.map.persisted.Room;

import com.jordan.ips.model.data.map.persisted.RoomIndent;
import com.jordan.ips.model.exceptions.InvalidLocationException;

public class IndentLocationFinder {

    /**
     * Finds the true location of an indent, to draw tro the screen or check bounds of user
     * @param room
     * @param indent
     * @return
     * @throws InvalidLocationException
     */
    public static final double[] findStartPointsOfIndent(Room room, RoomIndent indent, boolean ignoreLocation) throws InvalidLocationException {
        if (!indent.getWallKeyA().isEmpty() && !indent.getWallKeyB().isEmpty()) {

            double xStart = 0;
            double yStart = 0;

            if(indent.getWallKeyA().equals("BOTTOM")){
                yStart = room.getDimensions().y - indent.getDimensions().y;
            }
            if(indent.getWallKeyB().equals("RIGHT")){
                xStart = room.getDimensions().x - indent.getDimensions().x;
            }
            if(!ignoreLocation){
                xStart += room.getLocation().x;
                yStart += room.getLocation().y;
            }
            return new double[]{xStart, yStart};
        } else if (!indent.getWallKeyA().isEmpty()) {
            double xStart = 0;
            double yStart = 0;
            boolean valid = true;
            switch (indent.getWallKeyA()) {
                case "TOP":
                    xStart = indent.getLocation();
                    yStart = 0;
                    break;
                case "BOTTOM":
                    xStart = indent.getLocation();
                    yStart = room.getDimensions().y - indent.getDimensions().y;
                    break;
                case "LEFT":
                    xStart = 0;
                    yStart = indent.getLocation();
                    break;
                case "RIGHT":
                    yStart = indent.getLocation();
                    xStart = room.getDimensions().x - indent.getDimensions().x;
                    break;
                default:
                    throw new InvalidLocationException(indent.getWallKeyA() +" Is an invalid wall location");
            }
            if(!ignoreLocation){
                xStart += room.getLocation().x;
                yStart += room.getLocation().y;
            }
            return new double[]{xStart, yStart};
        }
        throw new InvalidLocationException("No indent location found");
    }

}
