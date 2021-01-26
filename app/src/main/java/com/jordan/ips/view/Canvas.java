package com.jordan.ips.view;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.annotation.Nullable;

import com.jordan.ips.model.data.MapWrapper;
import com.jordan.ips.view.renders.MapRenderer;
import com.jordan.ips.view.renders.Renderable;

import java.util.Random;

public class Canvas extends View implements View.OnTouchListener, View.OnClickListener, ScaleGestureDetector.OnScaleGestureListener{
    Random random = new Random();
    Paint paint;
    MapWrapper map;

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

    Renderable mapRenderable;

    public Canvas(Context context) {
        super(context);
        init(context);
    }

    public Canvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context){
        paint = new Paint();
        paint.setColor(0xffff00ff);

        setOnClickListener(this);
        setOnTouchListener(this);
        mScaleDetector = new ScaleGestureDetector(context, this);
    }

    @Override
    protected void onDraw(android.graphics.Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(0xffCCCCCC);
        if (mapRenderable != null) {
            mapRenderable.render(canvas, xOff, yOff, scale);
        }
    }

    public MapWrapper getMap() {
        return map;
    }

    public void setMap(MapWrapper map) {
        this.map = map;
        this.mapRenderable = new MapRenderer(map.getMap());
    }

    @Override
    public void onClick(View v) {
        Log.i("Canvas", "Clicked");
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {


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
        invalidate();
        return true;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        Log.i("CANVASSCALE", "Onscale");
        scale *= detector.getScaleFactor();
        invalidate();
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        Log.i("CANVASSCALE", "OnscaleBegin");
        scaling = true;
        invalidate();
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        scaling = false;
        Log.i("CANVASSCALE", "OnscaleEnd");
        invalidate();
    }
}
