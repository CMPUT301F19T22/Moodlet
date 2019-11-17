package com.cmput3owo1.moodlet.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.cmput3owo1.moodlet.R;

/**
 * Placeholder for the user search. Not implemented.
 */
public class SearchFragment extends Fragment {
    private SearchView userSearchView;
    private ListView userListView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        userSearchView = rootView.findViewById(R.id.user_search_view);
        userListView = rootView.findViewById(R.id.user_list_view);

        userSearchView.setIconifiedByDefault(false);
        userSearchView.setQueryHint(getResources().getString(R.string.search_hint));

        return rootView;
    }
}