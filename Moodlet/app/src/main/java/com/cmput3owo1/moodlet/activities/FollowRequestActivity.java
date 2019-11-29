package com.cmput3owo1.moodlet.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.cmput3owo1.moodlet.R;
import com.cmput3owo1.moodlet.adapters.RequestListAdapter;
import com.cmput3owo1.moodlet.models.FollowRequest;
import com.cmput3owo1.moodlet.services.IMoodEventServiceProvider;
import com.cmput3owo1.moodlet.services.IUserServiceProvider;
import com.cmput3owo1.moodlet.services.MoodEventService;
import com.cmput3owo1.moodlet.services.UserService;

import java.util.ArrayList;

/**
 * The follow request activity. Holds the user's {@link FollowRequest} objects from other users.
 * The user can accept or decline follow requests from this screen.
 */
public class FollowRequestActivity extends AppCompatActivity implements
        IUserServiceProvider.OnRequestsUpdateListener,
        RequestListAdapter.OnRequestClickListener,
        IUserServiceProvider.OnAcceptRequestListener {

    private Toolbar toolbar;
    private ListView requestsListView;
    private RequestListAdapter requestsAdapter;
    private ArrayList<FollowRequest> requestDataList;
    private IUserServiceProvider userService;
    private IMoodEventServiceProvider moodEventService;

    /**
     * Called when the activity is starting.
     * @param savedInstanceState Used to restore an activity's previous state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_request);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Manually replace navigation icon with custom icon.
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_24px);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        requestDataList = new ArrayList<>();
        requestsAdapter = new RequestListAdapter(this, requestDataList, this);

        requestsListView = findViewById(R.id.requests_list_view);
        requestsListView.setAdapter(requestsAdapter);

        userService = new UserService();
        moodEventService = new MoodEventService();

        userService.getRequestUpdates(this);
    }

    /**
     * Callback function to receive follow request updates from Firestore. Updates the list view
     * with the new follow requests.
     * @param newRequests The list of new {@link FollowRequest} objects.
     */
    @Override
    public void onRequestsUpdate(ArrayList<FollowRequest> newRequests) {
        requestDataList.clear();
        requestDataList.addAll(newRequests);
        requestsAdapter.notifyDataSetChanged();
    }

    /**
     * Click handler called when a follow request in the list is selected to be accepted. Passes
     * the request to the service in order to accept it.
     * @param request The follow request that accept was clicked on
     */
    @Override
    public void OnAcceptClick(FollowRequest request) {
        userService.acceptFollowRequest(request, this);
    }

    /**
     * Click handler called when a follow request in the list is selected to be decline. Passes
     * the request to the service in order to decline it.
     * @param request The follow request that decline was clicked on
     */
    @Override
    public void OnDeclineClick(FollowRequest request) {
        userService.deleteFollowRequest(request);
    }

    /**
     * Callback function when a request is successfully accepted on the database side. This function
     * passes the new follower's username to the {@link MoodEventService} in order to update the
     * follower's feed.
     * @param newFollowerUsername The username of the new follower
     */
    @Override
    public void onAcceptRequestSuccess(String newFollowerUsername) {
        moodEventService.updateFollowerWithMostRecentMood(newFollowerUsername);
    }
}
