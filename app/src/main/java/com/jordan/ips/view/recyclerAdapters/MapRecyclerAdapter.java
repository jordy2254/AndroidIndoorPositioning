package com.jordan.ips.view.recyclerAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jordan.ips.R;
import com.jordan.ips.model.api.MapSyncronisationUtil;
import com.jordan.ips.model.api.MapSyncronsiedCallBack;
import com.jordan.ips.model.data.FileManager;
import com.jordan.ips.model.data.MapWrapper;
import com.jordan.ips.model.data.map.persisted.Map;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MapRecyclerAdapter extends RecyclerView.Adapter<MapRecyclerAdapter.ViewHolder> {

    private List<MapWrapper> mData;
    private LayoutInflater mInflater;
    private MapRecyclerAdapterListeners mapRecyclerAdapterListeners;
    private Context context;

    // data is passed into the constructor
    public MapRecyclerAdapter(Context context, List<MapWrapper> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycler_map_view, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MapWrapper map = mData.get(position);
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

        holder.btnSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapSyncronisationUtil.syncroniseMapAndUpdateWrapper(map, (int)map.getMap().getId(), map.getMap().getPassword(), context, new MapSyncronsiedCallBack() {
                    @Override
                    public void syncronisationComplete(Map m) {
                        notifyDataSetChanged();
                    }

                    @Override
                    public void syncronisationFailed() {
                        notifyDataSetChanged();
                    }
                });

                map.setSyncing(true);
                notifyDataSetChanged();
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileManager.deleteWrapper(context, map);
                mData.remove(map);
                notifyDataSetChanged();
                if(mapRecyclerAdapterListeners != null){
                    mapRecyclerAdapterListeners.mapRecyclerOnItemDelete(v, -1, map);
                }
            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addMap(MapWrapper map) {
        mData.add(map);
        notifyDataSetChanged();
    }

    public void removeMap(MapWrapper map) {
        mData.remove(map);
        notifyDataSetChanged();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mapName, mapDescription, lastSyncDate;
        ImageView mapIcon;
        ProgressBar pb;
        ImageButton btnDelete;
        ImageButton btnSync;

        boolean syncronising;


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
                MapWrapper map = mData.get(getAdapterPosition());
                if(map.isSyncing() || map.getLastSyncedDate() == null){
                    return;
                }
                mapRecyclerAdapterListeners.mapRecyclerOnItemClick(view, getAdapterPosition(), map);
            }
        }
    }

    public List<MapWrapper> getmData() {
        return mData;
    }

    // allows clicks events to be caught
    public void setMapRecyclerAdapterListeners(MapRecyclerAdapterListeners mapRecyclerAdapterListeners) {
        this.mapRecyclerAdapterListeners = mapRecyclerAdapterListeners;
    }

    // parent activity will implement this method to respond to click events
    public interface MapRecyclerAdapterListeners {
        void mapRecyclerOnItemClick(View view, int position, MapWrapper map);
        void mapRecyclerOnItemDelete(View view, int position, MapWrapper map);

    }
}