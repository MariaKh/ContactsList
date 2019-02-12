package com.wowapp.contactslist.domain.repositories;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.wowapp.contactslist.data.ContactsDataSource;
import com.wowapp.contactslist.domain.models.Group;

import java.io.InputStream;
import java.util.List;

public class ContactsRepository {

    // For Singleton instantiation
    private static final Object LOCK = new Object();

    private static ContactsRepository instance;
    private final ContactsDataSource contactsDataSource;

    private ContactsRepository(@NonNull ContactsDataSource contactsDataSource) {
        this.contactsDataSource = contactsDataSource;
    }

    public synchronized static ContactsRepository getInstance(@NonNull ContactsDataSource contactsDataSource) {
        if (instance == null) {
            synchronized (LOCK) {
                instance = new ContactsRepository(contactsDataSource);
            }
        }
        return instance;
    }

    public LiveData<List<Group>> getContactsGroupList(final InputStream inputStream) {
        return contactsDataSource.getGroupContacts(inputStream);
    }
}
