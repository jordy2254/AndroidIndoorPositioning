package com.jordan.ips.model.locationTracking.calculators;

import android.util.Log;

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
import java.util.Optional;
import java.util.stream.Collectors;

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
        List<String> sensorIds = sensors.stream().map(Sensor::getSensorId).collect(Collectors.toList());

        java.util.Map<String, Double> mesurements = getAllMeasurements();

        Double cShort = -1d;
        String cKey = "";
        for (String key : mesurements.keySet()) {
            if(!sensorIds.contains(key)){
                continue;
            }
            if(cKey == ""){
                cKey = key;
                cShort = mesurements.get(key);
                continue;
            }

            if(mesurements.get(key) < cShort){
                cKey = key;
                cShort = mesurements.get(key);
            }
        }

        if(cKey == ""){
            return new Point2d(10,10);
        }
        Log.i("Location", cKey);
        String finalCKey = cKey;
        List<Sensor> collected = sensors.stream().filter(s->s.getSensorId().equals(finalCKey)).collect(Collectors.toList());
        Optional<Sensor> sensor = sensors.stream().filter(s->s.getSensorId().equals(finalCKey)).findFirst();
        if(!sensor.isPresent()){
            Log.i("Location", "Sensor not present");
            return new Point2d(40,40);
        }
        return sensor.get().getLocation();
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
