package com.jordan.ips.model.data;

import com.jordan.ips.model.data.map.persisted.Map;

import java.io.Serializable;
import java.util.Date;

public class MapWrapper implements Serializable {

    private Map map;
    private Date lastSyncedDate = new Date();
    private boolean syncing = false;

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public Date getLastSyncedDate() {
        return lastSyncedDate;
    }

    public void setLastSyncedDate(Date lastSyncedDate) {
        this.lastSyncedDate = lastSyncedDate;
    }

    public boolean isSyncing() {
        return syncing;
    }

    public void setSyncing(boolean syncing) {
        this.syncing = syncing;
    }
}
