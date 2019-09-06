package com.nytimes.ui.base;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {


    public RecyclerViewHolder(@NonNull ViewDataBinding itemView) {
        super(itemView.getRoot());
    }
}
