package com.jordan.ips.controller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.jordan.ips.model.data.map.persisted.Floor;
import com.jordan.ips.model.data.map.persisted.Room;
import com.jordan.ips.model.data.pathfinding.PathNode;
import com.jordan.ips.model.data.waypoints.RoomWaypoint;
import com.jordan.ips.model.data.waypoints.Waypoint;
import com.jordan.ips.model.pathfinding.AStarPathFindingAlgorithm;
import com.jordan.ips.view.Canvas;
import com.jordan.ips.view.recyclerAdapters.BasicRecyclerAdapter;
import com.jordan.ips.view.renderable.MapRenderer;
import com.jordan.ips.view.renderable.PathRenderer;
import com.jordan.ips.view.renderable.WaypointRenderer;
import com.jordan.renderengine.data.Point2d;

import java.security.cert.PKIXRevocationChecker;
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

    RecyclerView lstFloors;
    BasicRecyclerAdapter floorsAdapter;
    AStarPathFindingAlgorithm aStarPathFindingAlgorithm;
    PathRenderer pathRenderer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        setSupportActionBar(null);

        Intent intent = getIntent();
        map = (MapWrapper) intent.getSerializableExtra(INTENT_MAP);

        lytDirectionPanel = findViewById(R.id.lytDirectionPanel);
        btnDirections = findViewById(R.id.btnDirections);

        btnDirections.setOnClickListener(v -> {
            if(pathRenderer != null){
                canvas.removeRenderable(pathRenderer);
                pathRenderer = null;
                btnDirections.setText("Directions");
                return;
            }
            aStarPathFindingAlgorithm = new AStarPathFindingAlgorithm(startWaypoint.getPathNode(), endWaypoint.getPathNode());
            List<PathNode> nodes = aStarPathFindingAlgorithm.compute();
            if (pathRenderer == null) {
                pathRenderer = new PathRenderer(nodes);
                canvas.addRenderable(pathRenderer);
                return;
            }
            pathRenderer.setNodes(nodes);

            btnDirections.setText("Cancel Directions");
        });

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

        //Pull out unqique floor indexes
        floorIndexes = map.getMap().getBuildings()
                .stream()
                .flatMap(building -> building.getFloors().stream())
                .map(Floor::getFloorNumber)
                .distinct()
                .sorted(Integer::compareTo)
                .collect(Collectors.toList());

        List<String> stringValues = floorIndexes.stream().map(integer -> String.valueOf(integer)).collect(Collectors.toList());
        floorsAdapter = new BasicRecyclerAdapter(getApplicationContext(), stringValues);

        lstFloors = findViewById(R.id.lstFloors);
        lstFloors.setLayoutManager(new LinearLayoutManager(this));
        lstFloors.setAdapter(floorsAdapter);

        mapRenderer = new MapRenderer(map.getMap());
        mapRenderer.setSelectedFloorIndex(floorIndexes.get(0));

        floorsAdapter.setBasicRecyclerAdapterListener((position) -> {
            Log.d("abcd", "Setting floor position to index: " + position);
            mapRenderer.setSelectedFloorIndex(floorIndexes.get(position));
        });
        canvas.addRenderable(mapRenderer);

        updateLayout();

//        new Thread(() -> BluetoothScanner.test(), "Sensor Thread").start();
    }

    private void updateLayout(){
        lytDirectionPanel.setVisibility(endWaypoint != null ? View.VISIBLE : View.GONE);
        btnDirections.setVisibility(startWaypoint != null && endWaypoint != null ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        canvas.requestFocusFromTouch();

        if(resultCode != Activity.RESULT_OK){
            return;
        }

        int resultType = data.getIntExtra(LocationSearchActivity.EXTRA_RESULT_TYPE, -1);

        if(resultType == -1){
            Log.d("Result", "Unexprect result type from search activity");
            return;
        }

        Optional<Waypoint<?>> waypoint;

        switch (resultType){
            case LocationSearchActivity.RESPONSE_ROOM:
                waypoint = extractRoomWaypointFromResult(data);
                break;
            default:
                Log.d("Result", "Unexprect result type from search activity");
                return;
        }

        if(!waypoint.isPresent()){
            return;
        }

        switch (requestCode){
            case START_POINT_SEARCH:
                if(!waypoint.isPresent()){
                    return;
                }
                setStartWaypoint(waypoint.get());

                txtStartPoint.setText(waypoint.get().getName());
                canvas.requestFocusFromTouch();
                break;
            case END_POINT_SEARCH:
                if(!waypoint.isPresent()){
                    return;
                }
                setEndWaypoint(waypoint.get());

                txtTarget.setText(waypoint.get().getName());

                break;
            default:
                Log.w("SEARCH", "No handler for request code: " + requestCode);
        }
    }

    private Optional<Waypoint<?>> extractRoomWaypointFromResult(Intent data){
        Room selRoom = (Room) data.getSerializableExtra(LocationSearchActivity.EXTRA_ROOM_RESULT);
        long selectedId = selRoom.getId();

        Optional<Room> room = map.findRoomById(selectedId);
        if(!room.isPresent()){
            Log.e("Search", "Room with id not found on map: " + selectedId);
            return Optional.empty();
        }
        Waypoint<Room> waypoint = new RoomWaypoint(room.get());
        if(map.getMap().getRootNode() == null){
            return Optional.empty();
        }
        Optional<PathNode> node = map.getMap().getRootNode().flattenNodes()
                .stream()
                .filter(pathNode -> room.get().isPointInRoom(pathNode.getLocation()))
                .sorted(
                        (o1, o2) -> {
                            Point2d roomCenter = room.get().getDimensions().divide(new Point2d(2,2)).add(room.get().getLocation());

                            float euclidian1 = (float) Math.sqrt( ((o1.getLocation().y - roomCenter.y) * (o1.getLocation().y - roomCenter.y))+ ((o1.getLocation().x - roomCenter.x * (o1.getLocation().x - roomCenter.x))));
                            float euclidian2 = (float) Math.sqrt( ((o2.getLocation().y - roomCenter.y) * (o2.getLocation().y - roomCenter.y))+ ((o2.getLocation().x - roomCenter.x * (o2.getLocation().x - roomCenter.x))));

                            return (int) (euclidian1 - euclidian2);
                        }
                )
                .findFirst();
        if(!node.isPresent()){
            return Optional.empty();
        }
        waypoint.setPathNode(node.get());
        return Optional.of(waypoint);
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