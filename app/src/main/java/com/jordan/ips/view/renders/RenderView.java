package com.jordan.ips.view.renders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jordan.ips.model.data.map.persisted.Map;
import com.jordan.renderengine.Screen;

public abstract class RenderView extends SurfaceView implements Runnable, SurfaceHolder.Callback {

    private static final int fpsLOCK = 30;
    private Thread thread;
    private Screen screen = Screen.getInstance();
    private boolean running = false;

    protected int fps = 0, ups = 0, fpsUP1, upsUp1;

    private int width, height;
    private Bitmap renderOuput;

    Map map;

    public RenderView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }


    public RenderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
    }

    public abstract void update();
    public abstract void render();
    public abstract void drawFrame(Canvas canvas);

    @Override
    public synchronized void run() {
        long lasttime = System.nanoTime();
        final double ns = 1000000000 / fpsLOCK;
        double delta = 0;
        long timer = System.currentTimeMillis();

        while (running) {
            long now = System.nanoTime();
            delta += (now - lasttime) / ns;

            lasttime = now;
            while (delta >= 1) {
                delta--;
                ups++;
                update();

                //if we've dropped too many frames let's skipp rendering
                if(delta > 10){
                    Log.w("IPS", "Dropping frames");
                    continue;
                }

                fps++;
                Canvas canvas = getHolder().lockCanvas();
                if(canvas == null){
                    continue;
                }
                screen.clear();

                render();

                renderOuput.setPixels(screen.getPixels(), 0, width, 0,0,width,height);
                canvas.drawBitmap(renderOuput, 0,0, null);
                drawFrame(canvas);
                getHolder().unlockCanvasAndPost(canvas);
            }

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                fpsUP1 = fps;
                upsUp1 = ups;
                fps = 0;
                ups = 0;
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.width = w;
        this.height = h;

        screen.setSize(w,h);

        Bitmap.Config conf = Bitmap.Config.RGB_565;
        renderOuput = Bitmap.createBitmap(w, h, conf);
    }

    public void start(){
        if(running){
            return;
        }
        //TODO
        if (thread == null) {
            thread = new Thread(this, "Render thread");
        }
        thread.start();
        running = true;
    }

    public void stop(){
        if(!running){
            return;
        }
        try {
            running = false;
            thread.join();
            Log.d("Render Engine", "Stopped thread");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        stop();
    }
}