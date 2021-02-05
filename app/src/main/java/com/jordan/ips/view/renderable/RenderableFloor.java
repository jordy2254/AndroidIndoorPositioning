package com.jordan.ips.view.renderable;

import com.jordan.ips.model.data.map.persisted.Floor;
import com.jordan.ips.model.data.map.persisted.Room;
import com.jordan.ips.model.data.map.persisted.Sensor;
import com.jordan.renderengine.Screen;
import com.jordan.renderengine.data.Point2d;
import com.jordan.renderengine.graphics.Renderable;

import java.util.ArrayList;
import java.util.List;

public class RenderableFloor  implements Renderable {

    Floor floor;
    List<RenderableRoom> rooms;

    public RenderableFloor(Floor floor) {
        this.floor = floor;
        rooms = new ArrayList<>();

        for(Room room : floor.getRooms()){
            rooms.add(new RenderableRoom(room));
        }


    }

    @Override
    public void render(Screen screen, Point2d offset, double scale) {
                for(RenderableRoom room : rooms){
            room.render(screen, offset, scale);
            for(Sensor sensor : floor.getSensors()){
                screen.renderRect((int)(sensor.getLocation().x + offset.x) - 10, (int)(sensor.getLocation().y + offset.y) - 10, 20,20, 0xff00ff);
            }
        }
    }

}
