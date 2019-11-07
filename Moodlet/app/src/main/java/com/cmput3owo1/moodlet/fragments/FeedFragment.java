package com.cmput3owo1.moodlet.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.cmput3owo1.moodlet.R;
import com.cmput3owo1.moodlet.adapters.FeedListAdapter;
import com.cmput3owo1.moodlet.models.MoodEventAssociation;
import com.cmput3owo1.moodlet.services.MoodEventService;

import java.util.ArrayList;

public class FeedFragment extends Fragment implements MoodEventService.OnFeedUpdateListener {
    private ListView feedListView;
    private FeedListAdapter feedAdapter;
    private ArrayList<MoodEventAssociation> feedDataList;
    private MoodEventService service;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);

        service = new MoodEventService();
        service.getFeedUpdates(this);

        feedListView = rootView.findViewById(R.id.feed_list);
        feedDataList = new ArrayList<>();

        feedAdapter = new FeedListAdapter(getContext(), feedDataList);
        feedListView.setAdapter(feedAdapter);

        return rootView;
    }

    @Override
    public void onFeedUpdate(ArrayList<MoodEventAssociation> newFeed) {
        feedDataList.clear();
        feedDataList.addAll(newFeed);
        feedAdapter.notifyDataSetChanged();
    }
}