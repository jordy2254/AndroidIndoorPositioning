package com.jordan.ips.model.data;

import com.jordan.ips.model.data.map.persisted.Map;
import com.jordan.ips.model.data.map.persisted.Room;

import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

public class MapWrapper implements Serializable {

    private Map map;
    private Date lastSyncedDate;
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

    public Optional<Room> findRoomById(long roomId) {
       return map.getBuildings().stream()
                .flatMap(building -> building.getFloors().stream())
                .flatMap(floor -> floor.getRooms().stream())
                .filter(room -> room.getId() == roomId)
                .findFirst();
    }


}
