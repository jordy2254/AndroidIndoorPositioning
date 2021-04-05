package com.jordan.ips.model.locationTracking.scanners;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.os.ParcelUuid;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BluetoothScanService extends ScanCallback implements ScanAndDistanceService {

    private static BluetoothScanService INSTANCE = null;

    private static final int RSSI_INDEX = 0;
    private static final int MESURED_POWER_INDEX = 1;

    static final ParcelUuid serviceUid = ParcelUuid.fromString("0000FEAA-0000-1000-8000-00805F9B34FB");

    static final BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
    private final Map<String, Integer[]> sensorData = new HashMap<>();

    private boolean running = false;

    private BluetoothScanService() {

    }

    @Override
    public void start() {
        if(running){
            return;
        }
        List<ScanFilter> filters = new ArrayList<>();
        filters.add(
                new ScanFilter.Builder()
                        .setServiceUuid(serviceUid)
                        .build());

        ScanSettings settings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build();
        mAdapter.getBluetoothLeScanner().startScan(filters, settings, this);
        running = true;
    }

    @Override
    public void stop() {
        if(!running){
            return;
        }
        mAdapter.getBluetoothLeScanner().stopScan(this);
        running = false;
    }

    @Override
    public double getDistance(String id) {
        return 0;
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    public void onScanResult(int callbackType, ScanResult result) {
        super.onScanResult(callbackType, result);
        int rssi = result.getRssi(); // rssi of the device
        byte[] data = result.getScanRecord().getServiceData(serviceUid);

        if (data[0] == 0x00){
            int power = (data[1] & 0xFF) - 256;
            byte[] namespaceId = Arrays.copyOfRange(data, 2, 12);
            byte[] instanceId = Arrays.copyOfRange(data, 12, 18);

            //create hex string of byte array
            String hex = convertToHex(namespaceId);
            if(!sensorData.containsKey(hex)){
                Log.i("Sensor", "found new sensor: " + hex);
            }
            sensorData.put(hex, new Integer[]{rssi, power});
        }
    }

    @Override
    public void onBatchScanResults(List<ScanResult> results) {
        super.onBatchScanResults(results);
    }

    @Override
    public void onScanFailed(int errorCode) {
        super.onScanFailed(errorCode);
    }

    public double calculateDistanceInCm(String sensor){
        Integer[] data = sensorData.get(sensor);
        if(data == null){
            return Double.POSITIVE_INFINITY;
        }
        double distance  = Math.pow(10, (((double)-61) - ((double)data[RSSI_INDEX])) / (10.0 * 2));
        return distance * 100;
    }

    public static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte datum : data) {
            int halfbyte = (datum >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9))
                    buf.append((char) ('0' + halfbyte));
                else
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = datum & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public synchronized static BluetoothScanService getInstance(){
        if (INSTANCE == null) {
            INSTANCE = new BluetoothScanService();
        }
        return INSTANCE;
    }
}
