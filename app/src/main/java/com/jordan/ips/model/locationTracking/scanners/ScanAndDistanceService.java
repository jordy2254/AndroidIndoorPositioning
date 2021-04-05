package com.jordan.ips.model.locationTracking.scanners;

public interface ScanAndDistanceService {

    void start();
    void stop();

    double getDistance(String id);
    boolean isRunning();
}
