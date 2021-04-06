package com.jordan.ips.model.locationTracking.calculators;

import com.jordan.ips.model.data.map.persisted.Map;
import com.jordan.ips.model.data.map.persisted.Sensor;
import com.jordan.ips.model.locationTracking.scanners.BluetoothScanService;
import com.jordan.ips.model.locationTracking.scanners.ScanAndDistanceService;
import com.jordan.renderengine.data.Point2d;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class LocationService {

    private static LocationService INSTANCE = null;

    private List<ScanAndDistanceService> scanners = Arrays.asList(BluetoothScanService.getInstance());
    private Map map;
    private int selectedFloor = 1;

    public double getDistanceFromSensor(String sensorId){
        for(ScanAndDistanceService s : scanners){
            double d = s.getDistance(sensorId);
            if(d != Double.POSITIVE_INFINITY){
                return d;
            }
        }
        return Double.POSITIVE_INFINITY;
    }

    public Point2d calculateCurrentLocation(){
        //TODO remove hard coded floor value
        List<Sensor> sensors = map.getBuildings().get(0).getFloors().get(0).getSensors();


        return new Point2d(10,10);
    }

    public void start(){
        for (ScanAndDistanceService s: scanners) {
            s.start();
        }
    }

    public void stop(){
        for (ScanAndDistanceService s: scanners) {
            s.stop();
        }
    }

    public synchronized static LocationService getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new LocationService();
        }
        return INSTANCE;
    }

    public List<ScanAndDistanceService> getScanners() {
        return scanners;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public java.util.Map<String, Double> getAllMeasurements() {
        java.util.Map<String, Double> sensorData = new HashMap<>();

        for (ScanAndDistanceService s: scanners) {
            sensorData.putAll(s.getAllMesurements());
        }
        return sensorData;
    }
}
