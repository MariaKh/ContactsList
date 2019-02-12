package com.wowapp.contactslist.ui.widgets.expandablerecyclerview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.wowapp.contactslist.ui.widgets.expandablerecyclerview.model.ExpandableWrapper;
import com.wowapp.contactslist.ui.widgets.expandablerecyclerview.model.Parent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ExpandableRecyclerAdapter<P extends Parent<C>, C, PVH extends ParentViewHolder, CVH extends ChildViewHolder>
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String EXPANDED_STATE_MAP = "ExpandableRecyclerAdapter.ExpandedStateMap";
    private static final int TYPE_PARENT = 0;
    private static final int TYPE_CHILD = 1;

    @NonNull
    private List<ExpandableWrapper<P, C>> flatItemList;

    @NonNull
    private List<P> parentList;

    @Nullable
    private ExpandCollapseListener expandCollapseListener;

    @NonNull
    private List<RecyclerView> attachedRecyclerViewPool;

    private Map<P, Boolean> expansionStateMap;

    public interface ExpandCollapseListener {

        @UiThread
        void onParentExpanded(int parentPosition);

        @UiThread
        void onParentCollapsed(int parentPosition);
    }

    protected ExpandableRecyclerAdapter(@NonNull List<P> parentList) {
        super();
        this.parentList = parentList;
        flatItemList = generateFlattenedParentChildList(parentList);
        attachedRecyclerViewPool = new ArrayList<>();
        expansionStateMap = new HashMap<>(this.parentList.size());
    }

    @NonNull
    @Override
    @UiThread
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (isParentViewType(viewType)) {
            PVH pvh = onCreateParentViewHolder(viewGroup, viewType);
            pvh.setParentViewHolderExpandCollapseListener(mParentViewHolderExpandCollapseListener);
            pvh.expandableAdapter = this;
            return pvh;
        } else {
            CVH cvh = onCreateChildViewHolder(viewGroup, viewType);
            cvh.setExpandableAdapter(this);
            return cvh;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    @UiThread
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int flatPosition) {
        if (flatPosition > flatItemList.size()) {
            throw new IllegalStateException("Trying to bind item out of bounds, size " + flatItemList.size()
                    + " flatPosition " + flatPosition + ". Was the data changed without a call to notify...()?");
        }

        ExpandableWrapper<P, C> listItem = flatItemList.get(flatPosition);
        if (listItem.isParent()) {
            PVH parentViewHolder = (PVH) holder;

            if (parentViewHolder.shouldItemViewClickToggleExpansion()) {
                parentViewHolder.setMainItemClickToExpand();
            }

            parentViewHolder.setExpanded(listItem.isExpanded());
            parentViewHolder.parent = listItem.getParent();
            onBindParentViewHolder(parentViewHolder, getNearestParentPosition(flatPosition), listItem.getParent());
        } else {
            CVH childViewHolder = (CVH) holder;
            childViewHolder.setChild(listItem.getChild());
            onBindChildViewHolder(childViewHolder, getNearestParentPosition(flatPosition), getChildPosition(flatPosition), listItem.getChild());
        }
    }

    @NonNull
    @UiThread
    protected abstract PVH onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType);


    @NonNull
    @UiThread
    protected abstract CVH onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType);


    @UiThread
    protected abstract void onBindParentViewHolder(@NonNull PVH parentViewHolder, int parentPosition, @NonNull P parent);


    @UiThread
    protected abstract void onBindChildViewHolder(@NonNull CVH childViewHolder, int parentPosition, int childPosition, @NonNull C child);


    @Override
    @UiThread
    public int getItemCount() {
        return flatItemList.size();
    }


    @Override
    @UiThread
    public int getItemViewType(int flatPosition) {
        ExpandableWrapper<P, C> listItem = flatItemList.get(flatPosition);
        if (listItem.isParent()) {
            return getParentViewType(getNearestParentPosition(flatPosition));
        } else {
            return getChildViewType(getNearestParentPosition(flatPosition), getChildPosition(flatPosition));
        }
    }

    private int getParentViewType(int parentPosition) {
        return TYPE_PARENT;
    }


    private int getChildViewType(int parentPosition, int childPosition) {
        return TYPE_CHILD;
    }


    public boolean isParentViewType(int viewType) {
        return viewType == TYPE_PARENT;
    }

    @UiThread
    protected void setParentList(@NonNull List<P> parentList, boolean preserveExpansionState) {
        this.parentList = parentList;
        notifyParentDataSetChanged(preserveExpansionState);
    }


    @Override
    @UiThread
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        attachedRecyclerViewPool.add(recyclerView);
    }


    @Override
    @UiThread
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        attachedRecyclerViewPool.remove(recyclerView);
    }

    @UiThread
    private void parentExpandedFromViewHolder(int flatParentPosition) {
        ExpandableWrapper<P, C> parentWrapper = flatItemList.get(flatParentPosition);
        updateExpandedParent(parentWrapper, flatParentPosition, true);
    }


    @UiThread
    private void parentCollapsedFromViewHolder(int flatParentPosition) {
        ExpandableWrapper<P, C> parentWrapper = flatItemList.get(flatParentPosition);
        updateCollapsedParent(parentWrapper, flatParentPosition, true);
    }

    private ParentViewHolder.ParentViewHolderExpandCollapseListener mParentViewHolderExpandCollapseListener = new ParentViewHolder.ParentViewHolderExpandCollapseListener() {

        @Override
        @UiThread
        public void onParentExpanded(int flatParentPosition) {
            parentExpandedFromViewHolder(flatParentPosition);
        }

        @Override
        @UiThread
        public void onParentCollapsed(int flatParentPosition) {
            parentCollapsedFromViewHolder(flatParentPosition);
        }
    };


    @UiThread
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        savedInstanceState.putSerializable(EXPANDED_STATE_MAP, generateExpandedStateMap());
    }


    @SuppressWarnings("unchecked")
    @UiThread
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null
                || !savedInstanceState.containsKey(EXPANDED_STATE_MAP)) {
            return;
        }

        HashMap<Integer, Boolean> expandedStateMap = (HashMap<Integer, Boolean>) savedInstanceState.getSerializable(EXPANDED_STATE_MAP);
        if (expandedStateMap == null) {
            return;
        }

        List<ExpandableWrapper<P, C>> itemList = new ArrayList<>();
        int parentsCount = parentList.size();
        for (int i = 0; i < parentsCount; i++) {
            ExpandableWrapper<P, C> parentWrapper = new ExpandableWrapper<>(parentList.get(i));
            itemList.add(parentWrapper);

            if (expandedStateMap.containsKey(i)) {
                boolean expanded = expandedStateMap.get(i);
                parentWrapper.setExpanded(expanded);

                if (expanded) {
                    List<ExpandableWrapper<P, C>> wrappedChildList = parentWrapper.getWrappedChildList();
                    int childrenCount = wrappedChildList.size();
                    for (int j = 0; j < childrenCount; j++) {
                        ExpandableWrapper<P, C> childWrapper = wrappedChildList.get(j);
                        itemList.add(childWrapper);
                    }
                }
            }
        }

        flatItemList = itemList;

        notifyDataSetChanged();
    }

    @UiThread
    private void updateExpandedParent(@NonNull ExpandableWrapper<P, C> parentWrapper, int flatParentPosition, boolean expansionTriggeredByListItemClick) {
        if (parentWrapper.isExpanded()) {
            return;
        }

        parentWrapper.setExpanded(true);
        expansionStateMap.put(parentWrapper.getParent(), true);

        List<ExpandableWrapper<P, C>> wrappedChildList = parentWrapper.getWrappedChildList();
        if (wrappedChildList != null) {
            int childCount = wrappedChildList.size();
            for (int i = 0; i < childCount; i++) {
                flatItemList.add(flatParentPosition + i + 1, wrappedChildList.get(i));
            }

            notifyItemRangeInserted(flatParentPosition + 1, childCount);
        }

        if (expansionTriggeredByListItemClick && expandCollapseListener != null) {
            expandCollapseListener.onParentExpanded(getNearestParentPosition(flatParentPosition));
        }
    }

    @UiThread
    private void updateCollapsedParent(@NonNull ExpandableWrapper<P, C> parentWrapper, int flatParentPosition, boolean collapseTriggeredByListItemClick) {
        if (!parentWrapper.isExpanded()) {
            return;
        }

        parentWrapper.setExpanded(false);
        expansionStateMap.put(parentWrapper.getParent(), false);

        List<ExpandableWrapper<P, C>> wrappedChildList = parentWrapper.getWrappedChildList();
        if (wrappedChildList != null) {
            int childCount = wrappedChildList.size();
            for (int i = childCount - 1; i >= 0; i--) {
                flatItemList.remove(flatParentPosition + i + 1);
            }

            notifyItemRangeRemoved(flatParentPosition + 1, childCount);
        }

        if (collapseTriggeredByListItemClick && expandCollapseListener != null) {
            expandCollapseListener.onParentCollapsed(getNearestParentPosition(flatParentPosition));
        }
    }

    @UiThread
    int getNearestParentPosition(int flatPosition) {
        if (flatPosition == 0) {
            return 0;
        }

        int parentCount = -1;
        for (int i = 0; i <= flatPosition; i++) {
            ExpandableWrapper<P, C> listItem = flatItemList.get(i);
            if (listItem.isParent()) {
                parentCount++;
            }
        }
        return parentCount;
    }

    @UiThread
    int getChildPosition(int flatPosition) {
        if (flatPosition == 0) {
            return 0;
        }

        int childCount = 0;
        for (int i = 0; i < flatPosition; i++) {
            ExpandableWrapper<P, C> listItem = flatItemList.get(i);
            if (listItem.isParent()) {
                childCount = 0;
            } else {
                childCount++;
            }
        }
        return childCount;
    }

    @UiThread
    private void notifyParentDataSetChanged(boolean preserveExpansionState) {
        if (preserveExpansionState) {
            flatItemList = generateFlattenedParentChildList(parentList, expansionStateMap);
        } else {
            flatItemList = generateFlattenedParentChildList(parentList);
        }
        notifyDataSetChanged();
    }

    protected List<ExpandableWrapper<P, C>> generateFlattenedParentChildList(List<P> parentList) {
        List<ExpandableWrapper<P, C>> flatItemList = new ArrayList<>();

        int parentCount = parentList.size();
        for (int i = 0; i < parentCount; i++) {
            P parent = parentList.get(i);
            generateParentWrapper(flatItemList, parent, parent.isInitiallyExpanded());
        }

        return flatItemList;
    }

    private List<ExpandableWrapper<P, C>> generateFlattenedParentChildList(List<P> parentList, Map<P, Boolean> savedLastExpansionState) {
        List<ExpandableWrapper<P, C>> flatItemList = new ArrayList<>();

        int parentCount = parentList.size();
        for (int i = 0; i < parentCount; i++) {
            P parent = parentList.get(i);
            Boolean lastExpandedState = savedLastExpansionState.get(parent);
            boolean shouldExpand = lastExpandedState == null ? parent.isInitiallyExpanded() : lastExpandedState;

            generateParentWrapper(flatItemList, parent, shouldExpand);
        }

        return flatItemList;
    }

    private void generateParentWrapper(List<ExpandableWrapper<P, C>> flatItemList, P parent, boolean shouldExpand) {
        ExpandableWrapper<P, C> parentWrapper = new ExpandableWrapper<>(parent);
        flatItemList.add(parentWrapper);
        if (shouldExpand) {
            generateExpandedChildren(flatItemList, parentWrapper);
        }
    }

    private void generateExpandedChildren(List<ExpandableWrapper<P, C>> flatItemList, ExpandableWrapper<P, C> parentWrapper) {
        parentWrapper.setExpanded(true);

        List<ExpandableWrapper<P, C>> wrappedChildList = parentWrapper.getWrappedChildList();
        int childCount = wrappedChildList.size();
        for (int j = 0; j < childCount; j++) {
            ExpandableWrapper<P, C> childWrapper = wrappedChildList.get(j);
            flatItemList.add(childWrapper);
        }
    }

    @NonNull
    @UiThread
    private HashMap<Integer, Boolean> generateExpandedStateMap() {
        HashMap<Integer, Boolean> parentHashMap = new HashMap<>();
        int childCount = 0;

        int listItemCount = flatItemList.size();
        for (int i = 0; i < listItemCount; i++) {
            if (flatItemList.get(i) != null) {
                ExpandableWrapper<P, C> listItem = flatItemList.get(i);
                if (listItem.isParent()) {
                    parentHashMap.put(i - childCount, listItem.isExpanded());
                } else {
                    childCount++;
                }
            }
        }

        return parentHashMap;
    }
}
