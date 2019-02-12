package com.wowapp.contactslist.ui.contactslist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.wowapp.contactslist.R;
import com.wowapp.contactslist.domain.models.Contact;
import com.wowapp.contactslist.domain.models.Group;
import com.wowapp.contactslist.ui.widgets.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.wowapp.contactslist.ui.widgets.expandablerecyclerview.model.ExpandableWrapper;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends ExpandableRecyclerAdapter<Group, Contact, GroupHeaderViewHolder, ContactViewHolder> implements Filterable {

    private LayoutInflater inflater;
    private List<Group> groups;
    private final ContactsFilter contactsFilter;

    ContactsAdapter(Context context, @NonNull List<Group> groups) {
        super(groups);
        this.groups = groups;
        inflater = LayoutInflater.from(context);
        contactsFilter = new ContactsFilter();
    }

    @UiThread
    @NonNull
    @Override
    public GroupHeaderViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        return new GroupHeaderViewHolder(inflater.inflate(R.layout.layout_contact_list_header, parentViewGroup, false));
    }

    @UiThread
    @NonNull
    @Override
    public ContactViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        return new ContactViewHolder(inflater.inflate(R.layout.layout_contact_list_item, childViewGroup, false));
    }

    @UiThread
    @Override
    public void onBindParentViewHolder(@NonNull GroupHeaderViewHolder recipeViewHolder, int parentPosition, @NonNull Group group) {
        recipeViewHolder.bind(group.getGroupName());
    }

    @UiThread
    @Override
    public void onBindChildViewHolder(@NonNull ContactViewHolder contactViewHolder, int parentPosition, int childPosition, @NonNull Contact contact) {
        contactViewHolder.bind(contact);
    }

    public void updateList(List<Group> contacts, String filter) {
        this.groups = contacts;
        setParentList(contacts, true);
        if (!contacts.isEmpty()&& !TextUtils.isEmpty(filter)) {
            getFilter().filter(filter);
        }
    }

    @Override
    public Filter getFilter() {
        return contactsFilter;
    }

    private final class ContactsFilter extends Filter {
        @Override
        protected FilterResults performFiltering(final CharSequence constraint) {
            final String filterString = constraint.toString().toLowerCase();
            final FilterResults results = new FilterResults();
            if (filterString.isEmpty()) {
                results.values = groups;
                results.count = groups.size();
            }else {
                final ArrayList<Group> filteredParentObjects = new ArrayList<>();
                for (ExpandableWrapper<Group, Contact> expandableWrapper : generateFlattenedParentChildList(groups)) {
                    final ArrayList<Contact> filteredChildObjects = new ArrayList<>();
                    final Group parentObject = expandableWrapper.getParent();
                    if (parentObject != null) {
                        for (final Contact childObject : parentObject.getChildList()) {
                            if (childObject.getFirstName().toLowerCase().contains(filterString)) {
                                filteredChildObjects.add(childObject);
                            }
                        }
                    }
                    if (filteredChildObjects.size() > 0) {
                        final Group filteredParentObject = new Group();
                        filteredParentObject.setGroupName(parentObject.getGroupName());
                        filteredParentObject.setPeople(filteredChildObjects);
                        filteredParentObjects.add(filteredParentObject);
                    }
                }
                results.values = filteredParentObjects;
                results.count = filteredParentObjects.size();
            }
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(final CharSequence constraint, final FilterResults results) {
            final ArrayList<Group> parentObjectFilteredList = (ArrayList<Group>) results.values;
            setParentList(parentObjectFilteredList, true);
        }
    }

}
