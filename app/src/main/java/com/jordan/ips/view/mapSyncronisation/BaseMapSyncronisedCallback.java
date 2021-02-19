package com.jordan.ips.view.mapSyncronisation;

import com.jordan.ips.model.api.MapSyncronsiedCallBack;
import com.jordan.ips.model.data.map.persisted.Map;
import com.jordan.ips.view.recyclerAdapters.BaseRecycler;

public class BaseMapSyncronisedCallback implements MapSyncronsiedCallBack {

    private final BaseRecycler<?,?> recycler;

    public BaseMapSyncronisedCallback(BaseRecycler<?,?> recycler) {
        this.recycler = recycler;
    }

    @Override
    public void syncronisationComplete(Map map) {
        recycler.notifyDataSetChanged();
    }

    @Override
    public void syncronisationFailed() {
        recycler.notifyDataSetChanged();
    }
}
