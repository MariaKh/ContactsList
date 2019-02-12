package com.wowapp.contactslist.ui.widgets.expandablerecyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ChildViewHolder<C> extends RecyclerView.ViewHolder {
    private C child;
    private ExpandableRecyclerAdapter expandableAdapter;

    protected ChildViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void setExpandableAdapter(ExpandableRecyclerAdapter expandableAdapter) {
        this.expandableAdapter = expandableAdapter;
    }

    public void setChild(C child) {
        this.child = child;
    }
}