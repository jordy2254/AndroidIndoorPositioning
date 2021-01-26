package com.jordan.ips.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.ips.R;
import com.jordan.ips.model.data.MapWrapper;
import com.jordan.ips.view.Canvas;

public class MapActivity extends AppCompatActivity {

    public static final String INTENT_MAP = "MAP";

    Canvas canvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
       Intent intent = getIntent();
        MapWrapper map = (MapWrapper) intent.getSerializableExtra(INTENT_MAP);

        canvas = findViewById(R.id.mapCanvas);
        canvas.setMap(map);
    }
}