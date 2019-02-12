package com.wowapp.contactslist.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.squareup.moshi.JsonAdapter;
import com.wowapp.contactslist.di.InjectorUtils;
import com.wowapp.contactslist.domain.models.Group;
import com.wowapp.contactslist.domain.models.GroupsResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by maria on 2/12/19.
 */

public class ContactsDataSource {

    private static final Object LOCK = new Object();

    private static ContactsDataSource instance;

    private final MutableLiveData<List<Group>> groupsLiveData;


    private ContactsDataSource() {
        groupsLiveData = new MutableLiveData<>();
    }

    public static ContactsDataSource getInstance() {
        if (instance == null) {
            synchronized (LOCK) {
                instance = new ContactsDataSource();
            }
        }
        return instance;
    }

    public LiveData<List<Group>> getGroupContacts(final InputStream inputStream){
        final MutableLiveData<List<Group>> listLiveData = new MutableLiveData<>();
        InjectorUtils.provideExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String rawJson = inputStreamToString(inputStream);
                    JsonAdapter<GroupsResponse> adapter = InjectorUtils.provideMoshi().adapter(GroupsResponse.class);
                    listLiveData.postValue(adapter.fromJson(rawJson).getGroups());
                    groupsLiveData.postValue(adapter.fromJson(rawJson).getGroups());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return listLiveData;
    }


    private String inputStreamToString(InputStream inputStream) {
        try {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, bytes.length);
            return new String(bytes);
        } catch (IOException e) {
            return null;
        }
    }
}
