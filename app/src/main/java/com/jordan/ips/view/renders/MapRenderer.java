package com.jordan.ips.view.renders;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.jordan.ips.model.data.MapWrapper;
import com.jordan.ips.model.data.map.persisted.Building;
import com.jordan.ips.model.data.map.persisted.Floor;
import com.jordan.ips.model.data.map.persisted.Map;
import com.jordan.ips.model.data.map.persisted.Room;

public class MapRenderer implements Renderable{
    private Map map;

    public MapRenderer(Map map) {
        this.map = map;
    }

    @Override
    public void render(Canvas canvas, double xOff, double yOff, double scale) {
        Paint paint = new Paint();
        paint.setColor(0xffff00ff);

        //Actual Map Rendering
        if (map.getBuildings() != null) {
            for(Building building : map.getBuildings()){
                for(Floor floor : building.getFloors()){
                    for(Room room : floor.getRooms()){
                        double xLoc = room.getLocation().x + xOff;
                        double yLoc = room.getLocation().y + yOff;

                        double width = room.getDimensions().x * scale;
                        double height = room.getDimensions().y * scale;
                        canvas.drawRect((int)xLoc,(int)yLoc,(int)(xLoc + width),(int)(yLoc + height),paint);
                    }
                }
            }
        }
    }
}
