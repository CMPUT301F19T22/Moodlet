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

    @Override
    public void onRequestsUpdate(ArrayList<FollowRequest> newRequests) {
        requestDataList.clear();
        requestDataList.addAll(newRequests);
        requestsAdapter.notifyDataSetChanged();
    }

    @Override
    public void OnAcceptClick(FollowRequest request) {
        userService.acceptFollowRequest(request, this);
    }

    @Override
    public void OnDeclineClick(FollowRequest request) {
        userService.deleteFollowRequest(request);
    }

    @Override
    public void onAcceptRequestSuccess(String newFollowerUsername) {
        moodEventService.updateFollowerWithMostRecentMood(newFollowerUsername);
    }
}
