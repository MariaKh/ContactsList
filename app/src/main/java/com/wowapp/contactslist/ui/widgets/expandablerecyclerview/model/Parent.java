package com.wowapp.contactslist.ui.widgets.expandablerecyclerview.model;

import java.util.List;

public interface Parent<C> {

    List<C> getChildList();

    boolean isInitiallyExpanded();
}