package com.wowapp.contactslist.ui.widgets.expandablerecyclerview;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wowapp.contactslist.ui.widgets.expandablerecyclerview.model.Parent;

public class ParentViewHolder<P extends Parent<C>, C> extends RecyclerView.ViewHolder implements View.OnClickListener {
    @Nullable
    private ParentViewHolderExpandCollapseListener parentViewHolderExpandCollapseListener;
    private boolean isExpanded;
    P parent;
    ExpandableRecyclerAdapter expandableAdapter;

    interface ParentViewHolderExpandCollapseListener {

        @UiThread
        void onParentExpanded(int flatParentPosition);

        @UiThread
        void onParentCollapsed(int flatParentPosition);
    }

    @UiThread
    protected ParentViewHolder(@NonNull View itemView) {
        super(itemView);
        isExpanded = false;
    }

    @UiThread
    public void setMainItemClickToExpand() {
        itemView.setOnClickListener(this);
    }

    @UiThread
    public void setExpanded(boolean expanded) {
        this.isExpanded = expanded;
    }

    @UiThread
    public void onExpansionToggled(boolean expanded) {

    }

    @UiThread
    void setParentViewHolderExpandCollapseListener(ParentViewHolderExpandCollapseListener parentViewHolderExpandCollapseListener) {
        this.parentViewHolderExpandCollapseListener = parentViewHolderExpandCollapseListener;
    }

    @Override
    @UiThread
    public void onClick(View v) {
        if (isExpanded) {
            collapseView();
        } else {
            expandView();
        }
    }

    @UiThread
    public boolean shouldItemViewClickToggleExpansion() {
        return true;
    }

    @UiThread
    private void expandView() {
        setExpanded(true);
        onExpansionToggled(false);

        if (parentViewHolderExpandCollapseListener != null) {
            parentViewHolderExpandCollapseListener.onParentExpanded(getAdapterPosition());
        }
    }

    @UiThread
    private void collapseView() {
        setExpanded(false);
        onExpansionToggled(true);

        if (parentViewHolderExpandCollapseListener != null) {
            parentViewHolderExpandCollapseListener.onParentCollapsed(getAdapterPosition());
        }
    }
}
