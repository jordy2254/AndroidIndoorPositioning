package com.jordan.ips.model.data.testdata;

import com.jordan.ips.model.data.Point2d;
import com.jordan.ips.model.data.map.persisted.Building;
import com.jordan.ips.model.data.map.persisted.Floor;
import com.jordan.ips.model.data.map.persisted.Map;
import com.jordan.ips.model.data.map.persisted.Room;

public class TestData {

    private static Map map = null;

    public static Map getTestMap(){
        if (map == null) {
            map = generateMap();
        }
        return map;
    }

    private static Map generateMap() {
        Room room = new Room();
        room.setLocation(new Point2d(0,0));
        room.setDimensions(new Point2d(200,100));

        Floor floor = new Floor();
        floor.addRoom(room);

        Building building = new Building();
        building.addFloor(floor);
        map = new Map();
        map.addBuilding(building);
        map.setName("Flat map");
        return map;
    }
}
