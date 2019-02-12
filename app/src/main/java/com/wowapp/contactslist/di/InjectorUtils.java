package com.wowapp.contactslist.di;

import com.squareup.moshi.Moshi;
import com.wowapp.contactslist.data.ContactsDataSource;
import com.wowapp.contactslist.domain.repositories.ContactsRepository;
import com.wowapp.contactslist.ui.contactslist.MainViewModelFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by maria on 2/12/19.
 */

public class InjectorUtils {

    public static Moshi provideMoshi(){
        return new Moshi.Builder().build();
    }

    public static Executor provideExecutor(){
        return Executors.newSingleThreadExecutor();
    }

    public static MainViewModelFactory provideMainViewModelFactory() {
        return new MainViewModelFactory(ContactsRepository.
                getInstance(provideContactsDataSource()));
    }

    private static ContactsDataSource provideContactsDataSource() {
        return ContactsDataSource.getInstance();
    }
}
