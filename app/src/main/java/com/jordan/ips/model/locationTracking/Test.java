package com.jordan.ips.model.locationTracking;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.ParcelUuid;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.jordan.ips.model.data.map.persisted.Sensor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static androidx.core.app.ActivityCompat.requestPermissions;

public class Test {


    static final ParcelUuid serviceUid = ParcelUuid.fromString("0000FEAA-0000-1000-8000-00805F9B34FB");
    static final BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();

    public static Map<String, Integer[]> sensorData = new HashMap<>();

    private static final int RSSI_INDEX = 0;
    private static final int MESURED_POWER_INDEX = 1;

    public static final void test(){
        List<ScanFilter> filters = new ArrayList<>();
        filters.add(
                new ScanFilter.Builder()
                        .setServiceUuid(serviceUid)
                        .build());

        ScanSettings settings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build();

        ScanCallback callBack = new ScanCallback() {
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

                    // ... now you can do anything you want with this data
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
        };


        mAdapter.getBluetoothLeScanner().startScan(filters, settings, callBack);
    }

    public static double calculateDistanceInCm(String sensor){
        Integer[] data = sensorData.get(sensor);
        if(data == null){
            return Double.POSITIVE_INFINITY;
        }
        double distance  = Math.pow(10, (((double)-61) - ((double)data[RSSI_INDEX])) / (10.0 * 2));
        return distance * 100;
    }

    public static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9))
                    buf.append((char) ('0' + halfbyte));
                else
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while(two_halfs++ < 1);
        }
        return buf.toString();
    }
}
