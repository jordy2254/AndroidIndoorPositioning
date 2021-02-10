package com.jordan.ips.view.recyclerAdapters.listeners;

import android.view.View;

import com.jordan.ips.model.data.MapWrapper;

public interface MapRecyclerAdapterListener {

    void mapRecyclerOnItemDelete(View v, int i, MapWrapper map);

    void mapRecyclerOnItemClick(View view, int adapterPosition, MapWrapper map);

}
