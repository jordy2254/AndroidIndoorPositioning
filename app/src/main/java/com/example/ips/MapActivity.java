package com.example.ips;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.ips.model.data.Map;

public class MapActivity extends AppCompatActivity {

    Canvas canvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
       Intent intent = getIntent();
       Map map = (Map) intent.getSerializableExtra("map");

        canvas = findViewById(R.id.mapCanvas);
        canvas.setMap(map);
    }
}