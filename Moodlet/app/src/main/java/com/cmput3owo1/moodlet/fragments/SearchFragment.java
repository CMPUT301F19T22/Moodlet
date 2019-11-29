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
import com.cmput3owo1.moodlet.adapters.UserListAdapter;
import com.cmput3owo1.moodlet.models.User;
import com.cmput3owo1.moodlet.services.IUserServiceProvider;
import com.cmput3owo1.moodlet.services.UserService;

import java.util.ArrayList;

/**
 * A fragment that holds the Search/Discover functionality. App users can search for other app
 * users to follow, and can send them a follow request. This fragment displays a search bar, and
 * the user can type in text to search for. A list of {@link User} objects with a prefix matching
 * the search text is displayed under.
 */
public class SearchFragment extends Fragment implements
        SearchView.OnQueryTextListener, UserListAdapter.OnFollowClickListener,
        IUserServiceProvider.OnUserSearchListener, IUserServiceProvider.OnFollowRequestListener {

    private SearchView userSearchView;
    private ListView userListView;
    private ArrayList<User> userDataList;
    private UserListAdapter userAdapter;
    private IUserServiceProvider service;

    /**
     * Default constructor for the Fragment
     */
    public SearchFragment(){

    }

    /**
     * This function is called to have the fragment instantiate its user interface view.
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container  If non-null, this is the parent view that the fragment's UI should be attached to. The fragment should not add the view itself, but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        service = new UserService();

        userSearchView = rootView.findViewById(R.id.user_search_view);
        userListView = rootView.findViewById(R.id.user_list_view);
        userDataList = new ArrayList<>();

        userAdapter = new UserListAdapter(getContext(), userDataList, this);
        userListView.setAdapter(userAdapter);

        userSearchView.setOnQueryTextListener(this);

        return rootView;
    }

    /**
     * Gets called when the user submits the query test. Overridden since it is required by the
     * interface. Searching occurs on every text change so search does not need to happen here.
     * @param newString The text in the search field
     * @return Returns true if the event was handled; false otherwise
     */
    @Override
    public boolean onQueryTextSubmit(String newString) {
        return false;
    }

    /**
     * Gets called when the text in the search field changes. Runs a search with the text every
     * time the text is changed.
     * @param newString The text in the search field
     * @return Returns true if the event was handled; false otherwise
     */
    @Override
    public boolean onQueryTextChange(String newString) {
        if (newString.isEmpty()) {
            userDataList.clear();
            userAdapter.notifyDataSetChanged();
        } else {
            service.searchForUsers(newString, this);
        }
        return true;
    }

    /**
     * Gets called when the search query returns a result.
     * @param searchResult A list of the search results
     * @param searchText The text that was searched for
     */
    @Override
    public void onSearchResult(ArrayList<User> searchResult, String searchText) {
        if (searchText.equals(userSearchView.getQuery().toString())) {
            userDataList.clear();
            userDataList.addAll(searchResult);
            userAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Gets called when the follow button next to a user is pressed. Uses the service to send a
     * follow request to the user.
     * @param user The user who the current user wants to send a follow request to
     */
    @Override
    public void onFollowClick(User user) {
        service.sendFollowRequest(user, this);
    }

    /**
     * Gets called when the follow request is successfully sent to the user. Used to update the
     * button to say "Requested" instead of "Follow".
     * @param user The user who the request was sent to
     */
    @Override
    public void onRequestSuccess(User user) {
        user.setRequested(true);
        userAdapter.notifyDataSetChanged();
    }

}