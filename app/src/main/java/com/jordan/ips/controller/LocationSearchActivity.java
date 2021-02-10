package com.jordan.ips.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.jordan.ips.R;
import com.jordan.ips.model.data.MapWrapper;
import com.jordan.ips.model.data.map.persisted.Room;
import com.jordan.ips.view.recyclerAdapters.SearchResultRecyclerAdapter;
import com.jordan.ips.view.recyclerAdapters.listeners.SearchResultRecyclerListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LocationSearchActivity extends AppCompatActivity {

    public static final String EXTRA_RESULT = "SELECTEDROOM";

    MapWrapper map;

    EditText txtSearch;
    RecyclerView lstResults;
    SearchResultRecyclerAdapter searchResultRecyclerAdapter;
    SearchResultRecyclerListener searchResultRecyclerListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search);
        Intent intent = getIntent();

        MapWrapper map = (MapWrapper) intent.getSerializableExtra(MapActivity.INTENT_MAP);
        this.map = map;
        searchResultRecyclerAdapter = new SearchResultRecyclerAdapter(this, new ArrayList<>());

        searchResultRecyclerListener = room -> {
            Intent intent1 = new Intent();
            intent1.putExtra(EXTRA_RESULT, room);
            setResult(Activity.RESULT_OK, intent1);
            finish();
        };

        searchResultRecyclerAdapter.setSearchResultRecyclerListener(searchResultRecyclerListener);
        lstResults = findViewById(R.id.lstResults);

        lstResults.setLayoutManager(new LinearLayoutManager(this));
        lstResults.setAdapter(searchResultRecyclerAdapter);

        txtSearch = findViewById(R.id.txtSearch);
        txtSearch.requestFocusFromTouch();
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
                    searchResultRecyclerAdapter.setData(new ArrayList<>());
                    return;
                }
                Log.i("Search", "Found: ");
                List<Room> results = map.getMap().getBuildings()
                        .stream().flatMap(building -> building.getFloors().stream())
                        .flatMap(floor -> floor.getRooms().stream())
                        .filter(room -> room.getName().toLowerCase().contains(s))
                        .collect(Collectors.toList());

                searchResultRecyclerAdapter.setData(results);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}