package com.jordan.ips.model.locationTracking;

import com.jordan.renderengine.data.Point2d;

import java.util.ArrayList;
import java.util.List;

public class LocationService {

    private static LocationService INSTANCE = null;

    private final BluetoothScanService bluetoothScanService = BluetoothScanService.getInstance();


    public double getDistanceFromSensor(String sensorId){
        return 0D;
    }

    public List<String> getSensorIdsInRange(){
        return new ArrayList<>();
    }

    public Point2d calculateCurrentLocation(){
        return new Point2d(10,10);
    }

    public synchronized static LocationService getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new LocationService();
        }
        return INSTANCE;
    }
}
