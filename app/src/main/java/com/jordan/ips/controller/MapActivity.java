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
import com.jordan.ips.model.data.waypoints.CurrentLocationWayPoint;
import com.jordan.ips.model.data.waypoints.DynamicWaypoint;
import com.jordan.ips.model.data.waypoints.RoomWaypoint;
import com.jordan.ips.model.data.waypoints.Waypoint;
import com.jordan.ips.model.pathfinding.AStarPathFindingAlgorithm;
import com.jordan.ips.model.utils.MapUtils;
import com.jordan.ips.view.Canvas;
import com.jordan.ips.view.LongTouchListener;
import com.jordan.ips.view.recyclerAdapters.BasicRecyclerAdapter;
import com.jordan.ips.view.renderable.MapRenderer;
import com.jordan.ips.view.renderable.PathRenderer;
import com.jordan.ips.view.renderable.WaypointRenderer;
import com.jordan.renderengine.data.Point2d;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MapActivity extends AppCompatActivity implements LongTouchListener {

    public static final String INTENT_MAP = "MAP";
    public static final int START_POINT_SEARCH = 1;
    public static final int END_POINT_SEARCH = 2;

    Canvas canvas;

    EditText txtTarget;
    EditText txtStartPoint;

    Button btnDirections;

    MapWrapper mapWrapper;

    Waypoint<?> startWaypoint = null;
    Waypoint<?> endWaypoint = null;

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
        mapWrapper = (MapWrapper) intent.getSerializableExtra(INTENT_MAP);

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
                i.putExtra(INTENT_MAP, mapWrapper);

                startActivityForResult(i, END_POINT_SEARCH);
            }
        });

        txtStartPoint = findViewById(R.id.txtStartPoint);
        txtStartPoint.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                Intent i = new Intent(getApplicationContext(), LocationSearchActivity.class);
                i.putExtra(INTENT_MAP, mapWrapper);

                startActivityForResult(i, START_POINT_SEARCH);
            }
        });

        canvas = findViewById(R.id.mapCanvas);

        //Pull out unqique floor indexes
        floorIndexes = mapWrapper.getMap().getBuildings()
                .stream()
                .flatMap(building -> building.getFloors().stream())
                .map(Floor::getFloorNumber)
                .distinct()
                .sorted(Integer::compareTo)
                .collect(Collectors.toList());

        List<String> stringValues = floorIndexes.stream().map(String::valueOf).collect(Collectors.toList());
        floorsAdapter = new BasicRecyclerAdapter(getApplicationContext(), stringValues);

        lstFloors = findViewById(R.id.lstFloors);
        lstFloors.setLayoutManager(new LinearLayoutManager(this));
        lstFloors.setAdapter(floorsAdapter);

        mapRenderer = new MapRenderer(mapWrapper.getMap());
        mapRenderer.setSelectedFloorIndex(floorIndexes.get(0));

        floorsAdapter.setBasicRecyclerAdapterListener((position) -> {
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

        Optional<Waypoint<?>> waypoint = Optional.empty();

        switch (resultType){
            case LocationSearchActivity.RESPONSE_ROOM:
                waypoint = extractRoomWaypointFromResult(data);
                break;
            case LocationSearchActivity.RESPONSE_CLEAR:
                //TODO clear selection
                return;
            case LocationSearchActivity.RESPONSE_CURRENT_LOCATION:
                waypoint = Optional.of(new CurrentLocationWayPoint(null, mapWrapper.getMap()));
                break;
            case LocationSearchActivity.RESPONSE_POINT_ON_MAP:
                //TODO interact with the canvas to detect a long press!
                return;
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

        Optional<Room> room = mapWrapper.findRoomById(selectedId);
        if(!room.isPresent()){
            Log.e("Search", "Room with id not found on map: " + selectedId);
            return Optional.empty();
        }
        Waypoint<Room> waypoint = new RoomWaypoint(room.get());
        if(mapWrapper.getMap().getRootNode() == null){
            return Optional.empty();
        }
        Optional<PathNode> node = mapWrapper.getMap().getRootNode().flattenNodes()
                .stream()
                .filter(pathNode -> MapUtils.isPointInRoom(mapWrapper.getMap(), room.get(), pathNode.getLocation())).min((o1, o2) -> {
                    Point2d roomCenter = room.get().getDimensions().divide(new Point2d(2, 2)).add(room.get().getLocation());

                    float euclidian1 = (float) Math.sqrt(((o1.getLocation().y - roomCenter.y) * (o1.getLocation().y - roomCenter.y)) + ((o1.getLocation().x - roomCenter.x * (o1.getLocation().x - roomCenter.x))));
                    float euclidian2 = (float) Math.sqrt(((o2.getLocation().y - roomCenter.y) * (o2.getLocation().y - roomCenter.y)) + ((o2.getLocation().x - roomCenter.x * (o2.getLocation().x - roomCenter.x))));

                    return (int) (euclidian1 - euclidian2);
                });
        if(!node.isPresent()){
            return Optional.empty();
        }
        waypoint.setPathNode(node.get());
        return Optional.of(waypoint);
    }

    public void setStartWaypoint(Waypoint<?> startWaypoint) {
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

    public void setEndWaypoint(Waypoint<?> endWaypoint) {
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

    @Override
    public void onLongTouchDetected(Point2d point) {
        //TODO normalise point based on offset on canvas
        if(startWaypoint == null){
            startWaypoint = new DynamicWaypoint(point, mapWrapper.getMap());
        }else if(endWaypoint == null){
            endWaypoint = new DynamicWaypoint(point, mapWrapper.getMap());
        }
    }
}