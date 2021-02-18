package com.jordan.ips.view.recyclerAdapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jordan.ips.R;
import com.jordan.ips.model.data.map.persisted.Room;
import com.jordan.ips.view.recyclerAdapters.listeners.BasicRecyclerAdapterListener;
import com.jordan.ips.view.recyclerAdapters.listeners.SearchResultRecyclerListener;

import java.util.List;

public class BasicRecyclerAdapter extends BaseRecycler<String, BasicRecyclerAdapter.ViewHolder> {

    BasicRecyclerAdapterListener basicRecyclerAdapterListener;

    int selectedIndex = 0;

    public BasicRecyclerAdapter(Context context, List<String> data) {
        super(context, data, R.layout.recycler_view_floor_selection);
    }

    @Override
    protected ViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(position == selectedIndex){
            holder.txtFloorNumber.setBackgroundColor(Color.GRAY);
        }else{
            holder.txtFloorNumber.setBackgroundColor(Color.WHITE);
        }
        holder.txtFloorNumber.setText(data.get(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtFloorNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFloorNumber = itemView.findViewById(R.id.txtFloorNumber);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(basicRecyclerAdapterListener != null){
                selectedIndex = getAdapterPosition();
                basicRecyclerAdapterListener.selected(selectedIndex);
                notifyDataSetChanged();
            }
        }
    }

    public void setBasicRecyclerAdapterListener(BasicRecyclerAdapterListener basicRecyclerAdapterListener) {
        this.basicRecyclerAdapterListener = basicRecyclerAdapterListener;
    }
}
