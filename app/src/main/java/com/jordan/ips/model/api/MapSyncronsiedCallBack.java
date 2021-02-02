package com.jordan.ips.model.api;

import com.jordan.ips.model.data.map.persisted.Map;

public interface MapSyncronsiedCallBack {

    void syncronisationComplete(Map map);
    void syncronisationFailed();
}
