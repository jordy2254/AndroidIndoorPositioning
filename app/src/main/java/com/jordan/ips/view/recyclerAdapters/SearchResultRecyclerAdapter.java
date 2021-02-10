package com.jordan.ips.view.recyclerAdapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jordan.ips.R;
import com.jordan.ips.model.data.map.persisted.Room;
import com.jordan.ips.view.recyclerAdapters.listeners.SearchResultRecyclerListener;

import java.util.List;

public class SearchResultRecyclerAdapter extends BaseRecycler<Room, SearchResultRecyclerAdapter.ViewHolder> {

    SearchResultRecyclerListener searchResultRecyclerListener;

    public SearchResultRecyclerAdapter(Context context, List<Room> data) {
        super(context, data, R.layout.recycler_view_item);
    }

    @Override
    protected ViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String d = data.get(position).getName();
        holder.txtRecyclerData.setText(d);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtRecyclerData;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtRecyclerData = itemView.findViewById(R.id.txtRecyclerData);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(searchResultRecyclerListener != null){
                Room room = data.get(getAdapterPosition());
                searchResultRecyclerListener.selected(room);
            }
        }
    }

    public void setSearchResultRecyclerListener(SearchResultRecyclerListener searchResultRecyclerListener) {
        this.searchResultRecyclerListener = searchResultRecyclerListener;
    }
}
