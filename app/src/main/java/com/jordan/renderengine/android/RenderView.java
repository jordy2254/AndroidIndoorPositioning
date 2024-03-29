package com.jordan.renderengine.android;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jordan.renderengine.Screen;

public abstract class RenderView extends SurfaceView implements Runnable, SurfaceHolder.Callback, View.OnFocusChangeListener {

    private static final int fpsLOCK = 30;
    private Thread thread;
    private final Screen screen = Screen.getInstance();
    private boolean running = false;

    protected int fps = 0, ups = 0, fpsUP1, upsUp1;

    private int width, height;
    private Bitmap renderOutput;

    public RenderView(Context context) {
        super(context);
        getHolder().addCallback(this);
        setOnFocusChangeListener(this);
    }

    public RenderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        setOnFocusChangeListener(this);
    }

    public abstract void update();
    public abstract void render();
    public abstract void drawFrame(Canvas canvas);

    @Override
    public synchronized void run() {
        long lasttime = System.nanoTime();
        final double ns = 1000000000F / fpsLOCK;
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
                if(renderOutput.getHeight() != height){
                    getHolder().unlockCanvasAndPost(canvas);
                    continue;
                }
                renderOutput.setPixels(screen.getPixels(), 0, width, 0,0,width,height);
                canvas.drawBitmap(renderOutput, 0,0, null);

                drawFrame(canvas);
                getHolder().unlockCanvasAndPost(canvas);
            }

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                fpsUP1 = fps;
                upsUp1 = ups;
                fps = 0;
                ups = 0;
                secondTimer();
            }
        }
    }

    protected abstract void secondTimer();

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.width = w;
        this.height = h;

        screen.setSize(w,h);

        Bitmap.Config conf = Bitmap.Config.RGB_565;
        renderOutput = Bitmap.createBitmap(w, h, conf);
    }

    public void start(){
        if(running){
            return;
        }
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
            thread = null;
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

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        InputMethodManager ims = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        ims.hideSoftInputFromWindow(getWindowToken(), 0);
    }
}