package com.example.ips;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.ips.model.data.Map;

import java.util.Random;

public class Canvas extends View {
    Random random = new Random();
    Paint paint;
    Map map;


    public Canvas(Context context) {
        super(context);
        paint = new Paint();
        paint.setColor(0xffff00ff);

    }

    public Canvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(0xffff00ff);
    }

    @Override
    protected void onDraw(android.graphics.Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(0xffCCCCCC);
        canvas.drawRect(0,0,100,100,paint);
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }
}
