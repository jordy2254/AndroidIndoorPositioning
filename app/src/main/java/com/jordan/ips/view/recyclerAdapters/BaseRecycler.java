package com.jordan.ips.view.recyclerAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jordan.ips.controller.MainActivity;

import java.util.List;

public abstract class BaseRecycler<T, R extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<R> {

    protected List<T> data;
    protected LayoutInflater inflater;
    protected Context context;

    private final int viewId;

    protected abstract R createViewHolder(View view);

    // data is passed into the constructor
    public BaseRecycler(Context context, List<T> data, int viewId) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
        this.viewId = viewId;
    }

    @NonNull
    @Override
    public R onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(viewId, parent, false);
        return createViewHolder(view);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return data.size();
    }

    public void add(T data) {
        this.data.add(data);
        notifyDataSetChanged();
    }

    public void remove(T data) {
        this.data.remove(data);
        notifyDataSetChanged();
    }

    public void setData(List<T> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public List<T> getData() {
        return data;
    }


}