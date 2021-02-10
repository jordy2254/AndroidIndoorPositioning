package com.jordan.ips.controller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.jordan.ips.R;
import com.jordan.ips.model.data.MapWrapper;
import com.jordan.ips.model.data.map.persisted.Room;
import com.jordan.ips.model.locationTracking.BluetoothScanner;
import com.jordan.ips.view.Canvas;

import java.util.Optional;

public class MapActivity extends AppCompatActivity {

    public static final String INTENT_MAP = "MAP";
    public static final int TARGET_SEARCH = 1;

    Canvas canvas;

    EditText txtTarget;

    MapWrapper map;
    Room selectedRoom = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        setSupportActionBar(null);

        Intent intent = getIntent();
        map = (MapWrapper) intent.getSerializableExtra(INTENT_MAP);

        txtTarget = findViewById(R.id.txtTarget);
        txtTarget.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    Intent i = new Intent(getApplicationContext(), LocationSearchActivity.class);
                    i.putExtra(INTENT_MAP, map);

                    startActivityForResult(i, TARGET_SEARCH);
                }
            }
        });
        canvas = findViewById(R.id.mapCanvas);
        canvas.setMap(map.getMap());


        new Thread(new Runnable() {
                    @Override
                    public void run() {
                        BluetoothScanner.test();
                    }
                }, "Sensor Thread").start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != Activity.RESULT_OK){
            return;
        }
        switch (requestCode){
            case TARGET_SEARCH:
                Room selRoom = (Room) data.getSerializableExtra(LocationSearchActivity.EXTRA_RESULT);
                long selectedId = selRoom.getId();

                Optional<Room> room = map.findRoomById(selectedId);
                if(!room.isPresent()){
                    Log.e("Search", "Room with id not found on map: " + selectedId);
                    return;
                }
                if(this.selectedRoom != null){
                    this.selectedRoom.setSelected(false);
                }
                room.get().setSelected(true);
                this.selectedRoom = room.get();
                txtTarget.setText(room.get().getName());
                canvas.requestFocusFromTouch();
                break;
            default:
                Log.w("SEARCH", "No handler for request code: " + requestCode);
        }
    }
}