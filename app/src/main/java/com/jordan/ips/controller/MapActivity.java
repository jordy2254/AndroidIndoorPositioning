package com.jordan.ips.controller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.jordan.ips.R;
import com.jordan.ips.model.data.MapWrapper;
import com.jordan.ips.model.data.map.persisted.Building;
import com.jordan.ips.model.data.map.persisted.Floor;
import com.jordan.ips.model.data.map.persisted.Room;
import com.jordan.ips.model.data.waypoints.RoomWaypoint;
import com.jordan.ips.model.data.waypoints.Waypoint;
import com.jordan.ips.model.locationTracking.BluetoothScanner;
import com.jordan.ips.view.Canvas;
import com.jordan.ips.view.renderable.MapRenderer;
import com.jordan.ips.view.renderable.WaypointRenderer;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MapActivity extends AppCompatActivity {

    public static final String INTENT_MAP = "MAP";
    public static final int START_POINT_SEARCH = 1;
    public static final int END_POINT_SEARCH = 2;

    Canvas canvas;

    EditText txtTarget;
    EditText txtStartPoint;

    Button btnDirections;

    MapWrapper map;

    Waypoint startWaypoint = null;
    Waypoint endWaypoint = null;

    WaypointRenderer startPointRenderer;
    WaypointRenderer endPointRenderer;

    LinearLayout lytDirectionPanel;

    List<Integer> floorIndexes;

    MapRenderer mapRenderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        setSupportActionBar(null);

        Intent intent = getIntent();
        map = (MapWrapper) intent.getSerializableExtra(INTENT_MAP);

        //Pull out unqique floor indexes
        floorIndexes = map.getMap().getBuildings()
                .stream()
                .flatMap(building -> building.getFloors().stream())
                .map(Floor::getFloorNumber)
                .distinct()
                .sorted(Integer::compareTo)
                .collect(Collectors.toList());

        lytDirectionPanel = findViewById(R.id.lytDirectionPanel);
        btnDirections = findViewById(R.id.btnDirections);

        txtTarget = findViewById(R.id.txtTarget);
        txtTarget.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                Intent i = new Intent(getApplicationContext(), LocationSearchActivity.class);
                i.putExtra(INTENT_MAP, map);

                startActivityForResult(i, END_POINT_SEARCH);
            }
        });

        txtStartPoint = findViewById(R.id.txtStartPoint);
        txtStartPoint.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                Intent i = new Intent(getApplicationContext(), LocationSearchActivity.class);
                i.putExtra(INTENT_MAP, map);

                startActivityForResult(i, START_POINT_SEARCH);
            }
        });

        canvas = findViewById(R.id.mapCanvas);

        mapRenderer = new MapRenderer(map.getMap());
        mapRenderer.setSelectedFloorIndex(floorIndexes.get(0));
        canvas.addRenderable(mapRenderer);

        updateLayout();

        new Thread(() -> BluetoothScanner.test(), "Sensor Thread").start();
    }

    private void updateLayout(){
        lytDirectionPanel.setVisibility(endWaypoint != null ? View.VISIBLE : View.GONE);
        btnDirections.setVisibility(startWaypoint != null && endWaypoint != null ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != Activity.RESULT_OK){
            return;
        }
        Optional<Waypoint<Room>> waypoint = extractRoomWaypointFromResult(data);
        switch (requestCode){
            case START_POINT_SEARCH:
                if(!waypoint.isPresent()){
                    return;
                }
                setStartWaypoint(waypoint.get());

                txtStartPoint.setText(waypoint.get().getPoint().getName());
                canvas.requestFocusFromTouch();
                break;
            case END_POINT_SEARCH:
                if(!waypoint.isPresent()){
                    return;
                }
                setEndWaypoint(waypoint.get());

                txtTarget.setText(waypoint.get().getPoint().getName());
                canvas.requestFocusFromTouch();
                break;
            default:
                Log.w("SEARCH", "No handler for request code: " + requestCode);
        }
    }

    private Optional<Waypoint<Room>> extractRoomWaypointFromResult(Intent data){
        Room selRoom = (Room) data.getSerializableExtra(LocationSearchActivity.EXTRA_RESULT);
        long selectedId = selRoom.getId();

        Optional<Room> room = map.findRoomById(selectedId);
        if(!room.isPresent()){
            Log.e("Search", "Room with id not found on map: " + selectedId);
            return Optional.empty();
        }
        return Optional.of(new RoomWaypoint(room.get()));
    }

    public void setStartWaypoint(Waypoint startWaypoint) {
        if(this.startWaypoint != null){
            this.startWaypoint.setSelected(false);
            canvas.removeRenderable(startPointRenderer);
        }
        this.startWaypoint = startWaypoint;
        this.startWaypoint.setSelected(true);
        startPointRenderer = new WaypointRenderer(startWaypoint);
        canvas.addRenderable(startPointRenderer);
        updateLayout();
    }

    public void setEndWaypoint(Waypoint endWaypoint) {
        if(this.endWaypoint != null){
            this.endWaypoint.setSelected(false);
            canvas.removeRenderable(endPointRenderer);
        }

        this.endWaypoint = endWaypoint;
        this.endWaypoint.setSelected(true);
        endPointRenderer = new WaypointRenderer(endWaypoint);
        canvas.addRenderable(endPointRenderer);
        updateLayout();
    }
}