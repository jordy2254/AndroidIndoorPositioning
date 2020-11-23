package com.jordan.ips;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.ips.R;
import com.jordan.ips.model.data.map.persisted.MapWrapper;

public class MapActivity extends AppCompatActivity {

    Canvas canvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
       Intent intent = getIntent();
        MapWrapper map = (MapWrapper) intent.getSerializableExtra("map");

        canvas = findViewById(R.id.mapCanvas);
        canvas.setMap(map);
    }
}