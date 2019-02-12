package com.wowapp.contactslist.ui.contactslist;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.wowapp.contactslist.domain.models.Group;
import com.wowapp.contactslist.domain.repositories.ContactsRepository;

import java.io.InputStream;
import java.util.List;

class MainViewModel extends ViewModel {
    private ContactsRepository repository;
    private final MutableLiveData<String> searchQuery;

    MainViewModel(ContactsRepository repository) {
        this.repository = repository;
        searchQuery = new MutableLiveData<>();
    }

    public LiveData<List<Group>> getContactsGroupList(InputStream inputStream) {
        return repository.getContactsGroupList(inputStream);
    }

    public String getCurrentSearch(){
        return searchQuery.getValue();
    }

    public void updateSearchQuery(String query){
        searchQuery.setValue(query);
    }

    public MutableLiveData<String> getSearchQuery(){
        return searchQuery;
    }
}
