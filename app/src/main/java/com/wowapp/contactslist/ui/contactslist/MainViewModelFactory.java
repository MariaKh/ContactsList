package com.wowapp.contactslist.ui.contactslist;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.wowapp.contactslist.domain.repositories.ContactsRepository;

public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final ContactsRepository contactsRepository;

    public MainViewModelFactory(ContactsRepository repository) {
        this.contactsRepository = repository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new MainViewModel(contactsRepository);
    }
}
