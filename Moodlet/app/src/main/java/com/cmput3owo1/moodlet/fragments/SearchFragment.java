package com.cmput3owo1.moodlet.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.cmput3owo1.moodlet.R;
import com.cmput3owo1.moodlet.adapters.UserListAdapter;
import com.cmput3owo1.moodlet.models.User;
import com.cmput3owo1.moodlet.services.IUserServiceProvider;
import com.cmput3owo1.moodlet.services.UserService;

import java.util.ArrayList;

/**
 * Placeholder for the user search. Not implemented.
         */
public class SearchFragment extends Fragment implements
        SearchView.OnQueryTextListener, IUserServiceProvider.OnUserSearchListener {
    private SearchView userSearchView;
    private ListView userListView;
    private ArrayList<User> userDataList;
    private UserListAdapter userAdapter;
    private IUserServiceProvider service;
    private boolean isSearchBarEmpty;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        service = new UserService();

        userSearchView = rootView.findViewById(R.id.user_search_view);
        userListView = rootView.findViewById(R.id.user_list_view);
        userDataList = new ArrayList<>();

        userAdapter = new UserListAdapter(getContext(), userDataList);
        userListView.setAdapter(userAdapter);

        userSearchView.setIconifiedByDefault(false);
        userSearchView.setOnQueryTextListener(this);

        return rootView;
    }

    @Override
    public boolean onQueryTextSubmit(String newString) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newString) {
        if (newString.isEmpty()) {
            isSearchBarEmpty = true;
            userDataList.clear();
            userAdapter.notifyDataSetChanged();
        } else {
            isSearchBarEmpty = false;
            service.getUsers(newString, this);
        }
        return true;
    }

    @Override
    public void onSearchResult(ArrayList<User> searchResult) {
        if (!isSearchBarEmpty) {
            userDataList.clear();
            userDataList.addAll(searchResult);
            userAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onUserUpdate() {
        userAdapter.notifyDataSetChanged();
    }

}