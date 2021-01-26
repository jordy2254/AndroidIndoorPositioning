package com.jordan.ips.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.ips.R;
import com.jordan.ips.model.data.testdata.TestData;
import com.jordan.ips.view.mapSyncronisation.MapSyncronisationDialog;
import com.jordan.ips.view.mapSyncronisation.MapSyncronisationDialogConfirmListener;

import com.jordan.ips.model.data.map.persisted.Map;
import com.jordan.ips.model.data.MapWrapper;
import com.jordan.ips.view.recyclerAdapters.MapRecyclerAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MapSyncronisationDialogConfirmListener, MapRecyclerAdapter.MapRecyclerAdapterListeners {

    MapRecyclerAdapter mapRecyclerAdapter;
    MapSyncronisationDialog input;
    FragmentManager fm;

    TextView txtNoMapsNotice;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<MapWrapper> maps = new ArrayList<>();
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        txtNoMapsNotice = findViewById(R.id.txtNoMapsNotice);


        // set up the RecyclerView
        recyclerView = findViewById(R.id.lstMaps);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mapRecyclerAdapter = new MapRecyclerAdapter(this, maps);

        fm = getSupportFragmentManager();
        input = new MapSyncronisationDialog();
        input.setMapSyncronisationDialogConfirmListener(this);

        recyclerView.setAdapter(mapRecyclerAdapter);
        mapRecyclerAdapter.setMapRecyclerAdapterListeners(this);

        checkMapState();
    }

    public void checkMapState(){
        if(mapRecyclerAdapter.getmData().size() == 0){
            txtNoMapsNotice.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else{
            txtNoMapsNotice.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_sync_new_map:
                input.show(fm, "sync_new_map");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapInputDialogSuccessListener(String mapId, String mapPass) {
        Log.i("MAIN ACTIVITY FEEDBACK", "MapId: " + mapId + " MapPass: " + mapPass);
        MapWrapper mapWrapper = new MapWrapper();
        mapWrapper.setMap(TestData.getTestMap());

        //TODO optimise this with a DTO, holding only the contents needed for the list rather than the whole map
        mapRecyclerAdapter.addMap(mapWrapper);
        checkMapState();
    }

    @Override
    public void mapRecyclerOnItemClick(View view, int position, MapWrapper map) {
        Log.i("MAIN ACTIVITY LIST PRES", "Position: " + position);
        Intent intent = new Intent(this, MapActivity.class);

        //TODO when implementing db send over map wrapper ID and load the map.
        intent.putExtra(MapActivity.INTENT_MAP, map);
        startActivity(intent);
    }

    @Override
    public void mapRecyclerOnItemDelete(View view, int position, MapWrapper map) {
        checkMapState();
    }
}