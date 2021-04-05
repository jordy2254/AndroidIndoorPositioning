package com.jordan.ips.model.locationTracking.scanners;

import java.util.Map;

public interface ScanAndDistanceService {

    void start();
    void stop();

    Map<String, Double> getAllMesurements();

    double getDistance(String id);
    boolean isRunning();
}
