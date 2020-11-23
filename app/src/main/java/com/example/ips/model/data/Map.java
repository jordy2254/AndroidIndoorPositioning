package com.example.ips.model.data;

import android.media.Image;

import java.io.Serializable;
import java.util.Date;

public class Map implements Serializable {

    private String mapName;
    private String description;
    private Image mapIcon;
    private Date lastSyncedDate = new Date();
    private boolean syncing = false;

    public Map(String mapName, String description, Image mapIcon) {
        this.mapName = mapName;
        this.description = description;
        this.mapIcon = mapIcon;
    }

    public String getMapName() {
        return mapName;
    }

    public String getDescription() {
        return description;
    }

    public Image getMapIcon() {
        return mapIcon;
    }

    public Date getLastSyncedDate() {
        return lastSyncedDate;
    }

    public boolean isSyncing() {
        return syncing;
    }

    public void setSyncing(boolean syncing) {
        this.syncing = syncing;
    }
}
