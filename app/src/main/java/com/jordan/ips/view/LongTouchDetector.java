package com.jordan.ips.view;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.jordan.renderengine.data.Point2d;

public class LongTouchDetector implements View.OnTouchListener {


    private boolean valid;
    private long startTime;
    private long endTime;
    Point2d location;
    LongTouchListener longTouchListener;


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            //record the start time
            valid = true;
            startTime = event.getEventTime();
            location = new Point2d(event.getX(), event.getY());
            return true;
        }else if(event.getAction() == MotionEvent.ACTION_UP){
            //record the end time
            endTime = event.getEventTime();
        }else if(event.getAction() == MotionEvent.ACTION_MOVE){
            valid = false;
            return true;
        }

        if(valid && endTime - startTime > 1000){
            if(longTouchListener != null){
                longTouchListener.onLongTouchDetected(location);
            }
        }
        return true;
    }

    public void setLongTouchListener(LongTouchListener longTouchListener) {
        this.longTouchListener = longTouchListener;
    }

    public void invalidate() {
        valid = false;
    }

    public boolean isValid() {
        return valid;
    }
}
