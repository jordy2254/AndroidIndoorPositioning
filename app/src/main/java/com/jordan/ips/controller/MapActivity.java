package com.jordan.ips.controller;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.jordan.ips.R;
import com.jordan.ips.model.data.MapWrapper;
import com.jordan.ips.model.locationTracking.Test;
import com.jordan.ips.view.Canvas;
import com.jordan.ips.view.renders.RenderView;

import static androidx.core.app.ActivityCompat.requestPermissions;

public class MapActivity extends AppCompatActivity {

    public static final String INTENT_MAP = "MAP";

    Canvas canvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        setSupportActionBar(null);

        Intent intent = getIntent();
        MapWrapper map = (MapWrapper) intent.getSerializableExtra(INTENT_MAP);

        canvas = findViewById(R.id.mapCanvas);
        canvas.setMap(map.getMap());

        new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Test.test();
                    }
                }, "Sensor Thread").start();
    }
}