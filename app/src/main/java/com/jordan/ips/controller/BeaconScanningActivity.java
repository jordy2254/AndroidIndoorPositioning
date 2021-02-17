package com.jordan.ips.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.jordan.ips.R;
import com.jordan.ips.model.locationTracking.BluetoothScanner;

public class BeaconScanningActivity extends AppCompatActivity {

    private final BluetoothScanner bluetoothScanner = new BluetoothScanner();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_scanning);
        BluetoothScanner.test();
    }
}