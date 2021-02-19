package com.jordan.ips.model.utils;

import com.jordan.ips.model.data.map.persisted.Building;
import com.jordan.ips.model.data.map.persisted.Floor;
import com.jordan.ips.model.data.map.persisted.Map;
import com.jordan.ips.model.data.map.persisted.Room;
import com.jordan.ips.model.data.map.persisted.RoomIndent;
import com.jordan.ips.model.exceptions.InvalidLocationException;
import com.jordan.renderengine.data.Point2d;

import java.util.Optional;

public class MapUtils {

    private MapUtils(){}

    public static Optional<Building> getBuildingById(Map map, long buildingId){
        return map.getBuildings().stream().filter(building -> building.getId() == buildingId).findFirst();
    }

    public static Optional<Floor> getFloorById(Map map, long floorId){
        return map.getBuildings().stream()
                .flatMap(building -> building.getFloors().stream())
                .filter(floor -> floor.getId() == floorId)
                .findFirst();
    }

    public static Optional<Room> getRoomById(Map map, long roomId){
        return map.getBuildings().stream()
                .flatMap(building -> building.getFloors().stream())
                .flatMap(floor -> floor.getRooms().stream())
                .filter(room -> room.getId() == roomId)
                .findFirst();
    }

    public static boolean isPointInRoom(Map map, Room room, Point2d point){
        if(room == null){
            return false;
        }

        Point2d location = room.getLocation();

        //if we're not in initial wall bounds
        if(!(point.x >= location.x && point.x <= location.x+room.getDimensions().x && point.y >= location.y && point.y <= location.y+room.getDimensions().y)){
            return false;
        }

        //if we're within an indent, we're outside of room bounds
        for(RoomIndent indent : room.getIndents()){
            try {
                Point2d startLocations = IndentLocationFinder.findStartPointsOfIndent(room, indent, true);
                startLocations = startLocations.add(location);

                if((point.x >= startLocations.x && point.x <=  startLocations.x+indent.getDimensions().x && point.y >=  startLocations.y && point.y <=  startLocations.y+indent.getDimensions().y)){
                    return false;
                }
            } catch (InvalidLocationException e) {
                e.printStackTrace();
            }
        }

        return true;

    }
}
