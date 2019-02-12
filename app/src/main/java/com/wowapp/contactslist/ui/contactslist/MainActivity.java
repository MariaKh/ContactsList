package com.wowapp.contactslist.ui.contactslist;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.wowapp.contactslist.R;
import com.wowapp.contactslist.di.InjectorUtils;
import com.wowapp.contactslist.domain.models.Group;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MainViewModel viewModel;
    private RecyclerView contactsList;
    private EditText searchField;
    private ContactsAdapter adapter;
    private Bundle savedInstanceState;
    private Observer<List<Group>> contactsObserver = new Observer<List<Group>>() {
        @Override
        public void onChanged(@Nullable List<Group> groups) {
            adapter.updateList(groups, viewModel.getCurrentSearch());
            adapter.onRestoreInstanceState(savedInstanceState);
        }
    };
    private Observer<String> searchQueryObserver = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String query) {
            adapter.getFilter().filter(query);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.activity_main);
        contactsList = findViewById(R.id.contacts_list);
        searchField = findViewById(R.id.search_contacts);
        viewModel = ViewModelProviders.of(this, InjectorUtils.provideMainViewModelFactory()).get(MainViewModel.class);
        viewModel.getContactsGroupList(getResources().openRawResource(R.raw.contacts)).observe(this, contactsObserver);
        viewModel.getSearchQuery().observe(this, searchQueryObserver);
        setUpContactsList();
        setUpSearchField();
    }

    private void setUpSearchField() {
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                viewModel.updateSearchQuery(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setUpContactsList() {
        adapter = new ContactsAdapter(this, new ArrayList<Group>());
        contactsList.setAdapter(adapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        adapter.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        adapter.onRestoreInstanceState(savedInstanceState);

    }
}
