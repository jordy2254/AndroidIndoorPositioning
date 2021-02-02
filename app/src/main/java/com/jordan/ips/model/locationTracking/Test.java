package com.jordan.ips.model.locationTracking;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.os.ParcelUuid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test {

    ParcelUuid serviceUid = ParcelUuid.fromString("0000feaa-0000-1000-8000-00805f9b34fb");
    BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();

    private void test(){
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
}
