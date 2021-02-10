package com.jordan.ips.view.renderable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.jordan.ips.model.data.map.persisted.Floor;
import com.jordan.ips.model.data.map.persisted.Room;
import com.jordan.ips.model.data.map.persisted.Sensor;
import com.jordan.ips.model.locationTracking.BluetoothScanner;
import com.jordan.renderengine.Screen;
import com.jordan.renderengine.data.Point2d;
import com.jordan.renderengine.graphics.Drawable;
import com.jordan.renderengine.graphics.Renderable;

import java.util.ArrayList;
import java.util.List;

public class RenderableFloor  implements Renderable, Drawable {

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

    @Override
    public void draw(Canvas canvas, Point2d offset, double scale) {
        Paint paint = new Paint();
         paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        paint.setTextSize(24);
        for(Sensor sensor : floor.getSensors()){
            sensor.setId("0e67470442d01406d584");
            double distance = BluetoothScanner.calculateDistanceInCm(sensor.getId());
            if(distance == Double.POSITIVE_INFINITY){
                continue;
            }
            int dx = ((int) ((sensor.getLocation().x * scale) + offset.x));
            int dy = ((int) ((sensor.getLocation().y * scale) + offset.y));

         canvas.drawCircle(dx, dy, (int)(distance * scale), paint);
            canvas.drawText(String.valueOf(distance), dx, dy, paint);
        }
    }
}
