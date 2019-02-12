package com.wowapp.contactslist.domain.models;

import com.wowapp.contactslist.ui.widgets.expandablerecyclerview.model.Parent;

import java.util.List;

public class Group implements Parent<Contact> /*implements Parent*/ {
    private String groupName;
    private List<Contact> people;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setPeople(List<Contact> people) {
        this.people = people;
    }

    @Override
    public List<Contact> getChildList() {
        return people;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
