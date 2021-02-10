package com.jordan.ips.controller;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.jordan.ips.R;
import com.jordan.ips.model.data.MapWrapper;
import com.jordan.ips.model.data.map.persisted.Building;
import com.jordan.ips.model.data.map.persisted.Room;
import com.jordan.ips.model.locationTracking.Test;
import com.jordan.ips.view.Canvas;
import com.jordan.ips.view.renders.RenderView;

import org.w3c.dom.Text;

import java.util.List;
import java.util.stream.Collectors;

import static androidx.core.app.ActivityCompat.requestPermissions;

public class MapActivity extends AppCompatActivity {

    public static final String INTENT_MAP = "MAP";

    Canvas canvas;

    EditText txtSearch;
    RecyclerView lstResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        setSupportActionBar(null);

        Intent intent = getIntent();
        MapWrapper map = (MapWrapper) intent.getSerializableExtra(INTENT_MAP);

        canvas = findViewById(R.id.mapCanvas);
        canvas.setMap(map.getMap());
        lstResults = findViewById(R.id.lstResults);

        txtSearch = findViewById(R.id.txtSearch);
        txtSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                lstResults.setVisibility(hasFocus ? View.VISIBLE : View.GONE);
            }
        });
        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0){
                    return;
                }
                Log.i("Search", "Found: ");
                List<Room> results = map.getMap().getBuildings()
                        .stream().flatMap(building -> building.getFloors().stream())
                        .flatMap(floor -> floor.getRooms().stream())
                        .filter(room -> room.getName().toLowerCase().contains(s))
                .collect(Collectors.toList());

                for (Room r: results) {
                    Log.i("Match", "Found: " + r.getName());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Test.test();
                    }
                }, "Sensor Thread").start();
    }
}