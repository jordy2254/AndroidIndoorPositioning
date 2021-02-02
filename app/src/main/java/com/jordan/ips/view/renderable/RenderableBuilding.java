package com.jordan.ips.view.renderable;


import com.jordan.ips.model.data.map.persisted.Building;
import com.jordan.ips.model.data.map.persisted.Floor;
import com.jordan.renderengine.Screen;
import com.jordan.renderengine.data.Point2d;
import com.jordan.renderengine.graphics.Renderable;

public class RenderableBuilding  implements Renderable {

    Building building;
    RenderableFloor renderableFloor;


    public RenderableBuilding(Building building) {
        this.building = building;
    }

    public void setFloor(Floor floor){
        this.renderableFloor = new RenderableFloor(floor);
    }

    @Override
    public void render(Screen screen, Point2d offset, double scale) {
        if (renderableFloor != null){
           renderableFloor.render(screen, offset, scale);
       }
    }

//    @Override
//    public void draw(Canvas canvas, Graphics g, int offX, int offY, float scale) {
//       if (renderableFloor != null){
//           renderableFloor.draw(canvas, g, offX, offY, scale);
//       }
//    }
}
