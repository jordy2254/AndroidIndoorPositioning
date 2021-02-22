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

import com.jordan.renderengine.data.Point2d;
import com.jordan.renderengine.android.RenderView;
import com.jordan.renderengine.Screen;
import com.jordan.renderengine.graphics.Drawable;
import com.jordan.renderengine.graphics.Renderable;
import com.jordan.renderengine.graphics.Updatable;

import java.util.ArrayList;
import java.util.List;

public class Canvas extends RenderView implements LongTouchListener, View.OnTouchListener, ScaleGestureDetector.OnScaleGestureListener{

    final Screen screen = Screen.getInstance();

    private double xOff = 0;
    private double yOff = 0;
    private double scale = 1;

    //used for motion detection.
    private float initX = 0;
    private float initY = 0;
    private double initXOff = 0;
    private double initYOff = 0;
    private boolean scaling = false;

    ScaleGestureDetector scaleGestureDetector;
    LongTouchDetector longTouchDetector;
    LongTouchListener longTouchListener;

    Bitmap renderOuput;
    final List<Renderable> renderables = new ArrayList<>();
    final List<Updatable> updatables = new ArrayList<>();
    final List<Drawable> drawables = new ArrayList<>();

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
        for(Updatable u : updatables){
            u.update();
        }
    }

    @Override
    public void render() {
        synchronized (renderables){
            for(Renderable r : renderables){
                r.render(screen, new Point2d(xOff, yOff), scale);
            }
        }
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
        synchronized (drawables){
            for (Drawable d: drawables) {
                d.draw(canvas, new Point2d(xOff, yOff), scale);
            }
        }
    }

    @Override
    protected void secondTimer() {

    }

    public void init(Context context){
        setOnTouchListener(this);
        setFocusable(true);
        scaleGestureDetector = new ScaleGestureDetector(context, this);
        longTouchDetector = new LongTouchDetector();
        longTouchDetector.setLongTouchListener(this);
    }

    public void addRenderable(Renderable renderable){
        synchronized (this.renderables){
            this.renderables.add(renderable);
        }
    }

    public void removeRenderable(Renderable renderable){
        synchronized (this.renderables) {
            this.renderables.remove(renderable);
        }
    }

    public void clearRenderables(){
        synchronized (this.renderables){
            this.renderables.clear();
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        requestFocusFromTouch();

        scaleGestureDetector.onTouchEvent(event);

        //Prevent moving of view when scaling
        if(scaling){
            longTouchDetector.invalidate();
            return true;
        }

        longTouchDetector.onTouch(v, event);

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

        if(longTouchDetector.isValid()){
            return true;
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

    public void setLongTouchListener(LongTouchListener longTouchListener) {
        this.longTouchListener = longTouchListener;
    }

    @Override
    public void onLongTouchDetected(Point2d point) {
        if(longTouchListener != null){
            longTouchListener.onLongTouchDetected(point);
        }
    }
}
