package com.jordan.ips.model;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class CompassService implements SensorEventListener {

    private static CompassService INSTANCE;

    private SensorManager sensorManager;
    private float currentDegree =0;

    private CompassService(SensorManager manager){
        this.sensorManager = manager;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void start(){
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_GAME);
    }
    public void stop(){

    }

    public static synchronized CompassService getInstance(SensorManager manager) {
        if(INSTANCE == null){
            INSTANCE = new CompassService(manager);
        }

        return INSTANCE;
    }
}
