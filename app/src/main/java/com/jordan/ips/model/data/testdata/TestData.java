package com.jordan.ips.model.data.testdata;

import com.jordan.ips.model.data.map.persisted.RoomIndent;
import com.jordan.renderengine.data.Point2d;
import com.jordan.ips.model.data.map.persisted.Building;
import com.jordan.ips.model.data.map.persisted.Floor;
import com.jordan.ips.model.data.map.persisted.Map;
import com.jordan.ips.model.data.map.persisted.Room;

import java.util.Arrays;

public class TestData {

    private static Map map = null;

    public static Map getTestMap(){
        if (map == null) {
            map = generateMap();
        }
        return map;
    }

    private static Map generateMap() {
        Room room = createRoom(0,0);
        Room room2 = createRoom(400,0);
        Floor floor = new Floor();
        floor.addRoom(room);
        floor.addRoom(room2);
        Building building = new Building();
        building.addFloor(floor);
        map = new Map();
        map.addBuilding(building);
        map.setName("Flat map");
        return map;
    }

    private static Room createRoom(int x, int y) {
        Room room = new Room();
        room.setLocation(new Point2d(x,y));
        room.setDimensions(new Point2d(400,400));
        room.setIndents(Arrays.asList(
                new RoomIndent(
                        "TOP",
                        "LEFT",
                        new Point2d(50,50)
                ),
                new RoomIndent(
                        "TOP",
                        "RIGHT",
                        new Point2d(50,50)
                ),
                new RoomIndent(
                        "BOTTOM",
                        "LEFT",
                        new Point2d(50,50)
                ),
                new RoomIndent(
                        "BOTTOM",
                        "RIGHT",
                        new Point2d(50,50)
                )
        ));
        return room;
    }
}
