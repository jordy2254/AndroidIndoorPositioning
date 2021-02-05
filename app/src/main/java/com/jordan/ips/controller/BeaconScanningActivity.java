package com.jordan.ips.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.ips.R;
import com.jordan.ips.model.locationTracking.Test;

public class BeaconScanningActivity extends AppCompatActivity {

    private Test test = new Test();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_scanning);
        test.test();
    }
}