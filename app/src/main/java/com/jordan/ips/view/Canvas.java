package com.jordan.ips.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.annotation.Nullable;

import com.jordan.ips.model.data.map.persisted.Building;
import com.jordan.ips.model.data.pathfinding.persisted.PathNode;
import com.jordan.ips.model.locationTracking.BluetoothScanner;
import com.jordan.ips.view.renderable.RenderableBuilding;
import com.jordan.renderengine.data.Point2d;
import com.jordan.ips.model.data.map.persisted.Map;
import com.jordan.renderengine.android.RenderView;
import com.jordan.renderengine.Screen;
import com.jordan.renderengine.data.Point2i;
import com.jordan.renderengine.graphics.Drawable;
import com.jordan.renderengine.graphics.Renderable;
import com.jordan.renderengine.utils.RenderUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Canvas extends RenderView implements View.OnTouchListener, ScaleGestureDetector.OnScaleGestureListener{

    Screen screen = Screen.getInstance();
    private Map map;


    private double xOff = 0;
    private double yOff = 0;
    private double scale = 1;

    //used for motion detection.
    private float initX = 0;
    private float initY = 0;
    private double initXOff = 0;
    private double initYOff = 0;
    private boolean scaling = false;
    ScaleGestureDetector mScaleDetector;

    Bitmap renderOuput;
    List<Renderable> renderables = new ArrayList<>();

    List<Drawable> drawables = new ArrayList<>();

    public Canvas(Context context) {
        super(context);
        init(context);

        screen.setClearColor(0x888888);
    }

    public Canvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);

        screen.setClearColor(0x888888);
    }

    @Override
    public void update() {

    }

    @Override
    public void render() {
        for(Renderable r : renderables){
            r.render(screen, new Point2d(xOff, yOff), scale);
        }

        List<PathNode> flattenedNodes = map.getPathRoot().flattenNodes();

        for(PathNode n : map.getPathRoot().flattenNodes()){
            int size = 10;
            int dx = (int)((n.getLocation().x * scale) + xOff - (size / 2));
            int dy = (int)((n.getLocation().y * scale) + yOff - (size / 2));
            screen.renderRect(dx, dy, size, size, 0xffeeff);
        }


        //draw our path points
        List<PathNode> nonComplete = new ArrayList<>(Arrays.asList(map.getPathRoot()));
        List<PathNode> complete = new ArrayList<>();

        while(!nonComplete.isEmpty()){
            PathNode node = nonComplete.get(0);
            if(node.getChildNodes() != null){
                for(PathNode child : node.getChildNodes()){
                    Point2i p1 = RenderUtils.calculateRenderLocation(node.getLocation(), new Point2d(xOff, yOff), scale);
                    Point2i p2 = RenderUtils.calculateRenderLocation(child.getLocation(), new Point2d(xOff, yOff), scale);

                    screen.drawLine(p1.x, p1.y, p2.x, p2.y, 3, 0xfefefe);
                    if(!complete.contains(child) && !nonComplete.contains(child)){
                        nonComplete.add(child);
                    }
                }
            }

            nonComplete.remove(node);
            complete.add(node);
        }

//        screen.renderRect((int)xOff, (int)yOff, (int) (100 * scale), (int) (100 * scale), 0xff0e0e);
//        int xPos = 100, yPos = 100, size = 100;
//        List<Point2d> points = Arrays.asList(
//                new Point2d(0,0),
//                new Point2d(100,0),
//                new Point2d(100,100),
//                new Point2d(200,200),
//                new Point2d(0,200)
//        );
//        for(Point2d p : points){
//            p.y += 150;
//            p.x += 50;
//        }
//        screen.drawPolygonUpdated(points, 2, 0x000000, 0xfe593c, true);
//        for(Point2d p : points){
//            p.x += 180;
//        }
//        screen.drawPolygon(points, 3, 0x000000, 0xfe593c, true);
//        screen.drawLine(xPos,yPos,  xPos + size,yPos,  2,0x000000);
//        screen.drawLine(xPos + size, yPos,  xPos + size,yPos + size,  2,0x000000);
//        screen.drawLine(xPos + size,yPos + size,  xPos,yPos + size,  2,0x000000);
//        screen.drawLine(xPos,yPos + size,  xPos,yPos,  2,0x000000);
//        screen.fill(xPos + (size / 2),xPos + (size / 2),0xff00ff);
        int i = 0;
    }

    @Override
    public void drawFrame(android.graphics.Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        paint.setColor(Color.WHITE);
        canvas.drawRect(new Rect(0,150,250,225), paint);
        paint.setColor(Color.BLACK);
        canvas.drawText("FPS | UPS " + fpsUP1 + " | " + upsUp1, 10,200, paint);
        for (Drawable d: drawables) {
            d.draw(canvas, new Point2d(xOff, yOff), scale);
        }
    }

    @Override
    protected void secondTimer() {
        java.util.Map<String, Integer[]> sensors = BluetoothScanner.sensorData;
        for (String key : sensors.keySet()) {
            double distance = BluetoothScanner.calculateDistanceInCm(key);

            Log.i("Distance", String.format("Sensor: %s, Distance: " + distance, key));
        }
    }


    public void setMap(Map map) {
        this.map = map;
        Building building = map.getBuildings().get(0);
        RenderableBuilding r = new RenderableBuilding(building);
        r.setFloor(building.getFloors().get(0));

        this.renderables.add(r);
        this.drawables.add(r);
    }


    public void init(Context context){
        setOnTouchListener(this);
        setFocusable(true);
        mScaleDetector = new ScaleGestureDetector(context, this);
    }

    public void addRenderable(Renderable renderable){
        this.renderables.add(renderable);
    }

    public void removeRenderable(Renderable renderable){
        this.renderables.remove(renderable);
    }

    public void clearRenderables(){
        this.renderables.clear();
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        requestFocusFromTouch();

        mScaleDetector.onTouchEvent(event);
        if(scaling){
            return true;
        }
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            initX = event.getX();
            initY = event.getY();
            initXOff = xOff;
            initYOff = yOff;
            return true;
        }

        if(event.getAction() != MotionEvent.ACTION_MOVE){
            return false;
        }

        xOff = initXOff + (event.getX() - initX);
        yOff = initYOff + (event.getY() - initY);
        return true;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        scale *= detector.getScaleFactor();
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        scaling = true;
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        scaling = false;
    }
}
