package com.jordan.ips.controller;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jordan.ips.R;
import com.jordan.ips.model.api.MapSyncronisationUtil;
import com.jordan.ips.model.api.MapSyncronsiedCallBack;
import com.jordan.ips.model.data.FileManager;
import com.jordan.ips.view.mapSyncronisation.MapSyncronisationDialog;
import com.jordan.ips.view.mapSyncronisation.MapSyncronisationDialogConfirmListener;

import com.jordan.ips.model.data.map.persisted.Map;
import com.jordan.ips.model.data.MapWrapper;
import com.jordan.ips.view.recyclerAdapters.MapRecyclerAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        List<MapWrapper> wrappers = FileManager.loadMapWrappers(this.getApplicationContext());
        for(MapWrapper wrapper : wrappers){
            mapRecyclerAdapter.addMap(wrapper);
        }
        checkMapState();
//        Intent intent = new Intent(this, BeaconScanningActivity.class);
//        startActivity(intent);

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
        if(mapId == null || mapId.isEmpty()){
            Toast.makeText(getApplicationContext(), "Map Id must be filled in", Toast.LENGTH_SHORT).show();
            return;
        }
        int mapIdParsed = 0;
        try {
            mapIdParsed = Integer.parseInt(mapId);
        }catch (NumberFormatException e){
            Toast.makeText(getApplicationContext(), "Map Id's contain number's only", Toast.LENGTH_SHORT).show();
            return;
        }

        MapWrapper mapWrapper = new MapWrapper();
        mapWrapper.setSyncing(true);


        MapSyncronisationUtil.syncroniseMapAndUpdateWrapper(mapWrapper,mapIdParsed, mapPass, this.getApplicationContext(), new MapSyncronsiedCallBack() {
            @Override
            public void syncronisationComplete(Map map) {
               mapRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void syncronisationFailed() {
                mapRecyclerAdapter.removeMap(mapWrapper);
                mapRecyclerAdapter.notifyDataSetChanged();
            }
        });

        //TODO optimise this with a DTO, holding only the contents needed for the list rather than the whole map
        mapRecyclerAdapter.addMap(mapWrapper);
        checkMapState();
    }

    @Override
    public void mapRecyclerOnItemClick(View view, int position, MapWrapper map) {
        Log.i("MAIN ACTIVITY LIST PRES", "Position: " + position);
        Intent intent = new Intent(this, MapActivity.class);
        if(map.getMap() == null){
            Toast.makeText(getApplicationContext(), "This map hasn't successfully synced yet", Toast.LENGTH_SHORT).show();
            return;
        }
        //TODO when implementing db send over map wrapper ID and load the map.
        intent.putExtra(MapActivity.INTENT_MAP, map);
        startActivity(intent);
    }

    @Override
    public void mapRecyclerOnItemDelete(View view, int position, MapWrapper map) {
        checkMapState();
    }
}