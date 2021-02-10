package com.jordan.ips.view.recyclerAdapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jordan.ips.R;
import com.jordan.ips.controller.MainActivity;
import com.jordan.ips.model.api.MapSyncronisationUtil;
import com.jordan.ips.model.api.MapSyncronsiedCallBack;
import com.jordan.ips.model.data.FileManager;
import com.jordan.ips.model.data.MapWrapper;
import com.jordan.ips.model.data.map.persisted.Map;
import com.jordan.ips.view.mapSyncronisation.BaseMapSyncronisedCallback;
import com.jordan.ips.view.recyclerAdapters.listeners.MapRecyclerAdapterListener;

import java.text.SimpleDateFormat;
import java.util.List;

public class MapRecyclerAdapter extends BaseRecycler<MapWrapper, MapRecyclerAdapter.ViewHolder>{

    private MapRecyclerAdapterListener mapRecyclerAdapterListeners;

    public MapRecyclerAdapter(Context context, List<MapWrapper> data) {
        super(context, data, R.layout.recycler_map_view);
    }

    @Override
    protected ViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MapWrapper map = data.get(position);
        String mapName = map.getMap() == null ? "unkown" : map.getMap().getName();
        holder.mapName.setText(mapName);
        holder.mapDescription.setText("Description not yet implemented");
        holder.mapIcon.setImageResource(R.drawable.map);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd@hh:mm");

        if(map.getLastSyncedDate() == null){
            holder.lastSyncDate.setText("Never");
        }else{
            holder.lastSyncDate.setText(dateFormat.format(map.getLastSyncedDate()));
        }

        if(map.isSyncing()){
            holder.mapIcon.setVisibility(View.INVISIBLE);
            holder.pb.setVisibility(View.VISIBLE);
        }else{
            holder.mapIcon.setVisibility(View.VISIBLE);
            holder.pb.setVisibility(View.INVISIBLE);
        }

        holder.btnSync.setOnClickListener(c ->{
            MapSyncronisationUtil.syncroniseMapAndUpdateWrapper(map, (int)map.getMap().getId(), map.getMap().getPassword(), context, new BaseMapSyncronisedCallback(this));
            map.setSyncing(true);
            notifyDataSetChanged();
        });

        holder.btnDelete.setOnClickListener(v -> {
            FileManager.deleteWrapper(context, map);
            data.remove(map);
            notifyDataSetChanged();
            if(mapRecyclerAdapterListeners != null){
                mapRecyclerAdapterListeners.mapRecyclerOnItemDelete(v, -1, map);
            }
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mapName, mapDescription, lastSyncDate;
        ImageView mapIcon;
        ProgressBar pb;
        ImageButton btnDelete;
        ImageButton btnSync;

        ViewHolder(View itemView) {
            super(itemView);
            mapName = itemView.findViewById(R.id.txtMapName);
            mapDescription = itemView.findViewById(R.id.txtMapDescription);
            mapIcon = itemView.findViewById(R.id.imgMapIcon);
            lastSyncDate = itemView.findViewById(R.id.lblLastSyncedDate);
            pb = itemView.findViewById(R.id.pbSyncing);
            btnDelete = itemView.findViewById(R.id.btnDeleteMap);
            btnSync = itemView.findViewById(R.id.btnSyncMap);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mapRecyclerAdapterListeners != null){
                MapWrapper map = data.get(getAdapterPosition());
                if(map.isSyncing() || map.getLastSyncedDate() == null){
                    return;
                }
                mapRecyclerAdapterListeners.mapRecyclerOnItemClick(view, getAdapterPosition(), map);
            }
        }
    }

    public void setMapRecyclerAdapterListeners(MapRecyclerAdapterListener mapRecyclerAdapterListeners){
        this.mapRecyclerAdapterListeners = mapRecyclerAdapterListeners;
    }
}
