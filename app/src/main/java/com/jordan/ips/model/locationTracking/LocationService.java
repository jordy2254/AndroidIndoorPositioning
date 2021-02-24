package com.jordan.ips.model.locationTracking;

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


    public synchronized static LocationService getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new LocationService();
        }
        return INSTANCE;
    }
}
