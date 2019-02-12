package com.wowapp.contactslist.ui.widgets.expandablerecyclerview.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ExpandableWrapper<P extends Parent<C>, C> {

    private P parent;
    private C child;
    private boolean isWrappedParent;
    private boolean isExpanded;

    private List<ExpandableWrapper<P, C>> wrappedChildList;

    public ExpandableWrapper(@NonNull P parent) {
        this.parent = parent;
        isWrappedParent = true;
        isExpanded = false;
        wrappedChildList = generateChildItemList(parent);
    }

    private ExpandableWrapper(@NonNull C child) {
        this.child = child;
        isWrappedParent = false;
        isExpanded = false;
    }

    public P getParent() {
        return parent;
    }

    public void setParent(@NonNull P parent) {
        this.parent = parent;
        wrappedChildList = generateChildItemList(parent);
    }

    public C getChild() {
        return child;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public boolean isParent() {
        return isWrappedParent;
    }

    /**
     * @return The initial expanded state of a parent
     * @throws IllegalStateException If a parent isn't being wrapped
     */
    public boolean isParentInitiallyExpanded() {
        if (!isWrappedParent) {
            throw new IllegalStateException("Parent not wrapped");
        }
        return parent.isInitiallyExpanded();
    }

    /**
     * @return The list of children of a parent
     * @throws IllegalStateException If a parent isn't being wrapped
     */
    public List<ExpandableWrapper<P, C>> getWrappedChildList() {
        if (!isWrappedParent) {
            throw new IllegalStateException("Parent not wrapped");
        }
        return wrappedChildList;
    }

    private List<ExpandableWrapper<P, C>> generateChildItemList(P parentListItem) {
        List<ExpandableWrapper<P, C>> childItemList = new ArrayList<>();
        for (C child : parentListItem.getChildList()) {
            childItemList.add(new ExpandableWrapper<P, C>(child));
        }
        return childItemList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ExpandableWrapper<?, ?> that = (ExpandableWrapper<?, ?>) o;

        if (parent != null ? !parent.equals(that.parent) : that.parent != null)
            return false;
        return child != null ? child.equals(that.child) : that.child == null;

    }

    @Override
    public int hashCode() {
        int result = parent != null ? parent.hashCode() : 0;
        result = 31 * result + (child != null ? child.hashCode() : 0);
        return result;
    }
}
