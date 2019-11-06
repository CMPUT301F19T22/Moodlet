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
import com.cmput3owo1.moodlet.models.EmotionalState;
import com.cmput3owo1.moodlet.models.MoodEvent;
import com.cmput3owo1.moodlet.models.MoodEventAssociation;

import java.util.ArrayList;

public class FeedFragment extends Fragment {
    private ListView feedListView;
    private FeedListAdapter feedAdapter;
    private ArrayList<MoodEventAssociation> feedDataList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);

        feedListView = rootView.findViewById(R.id.feed_list);
        feedDataList = new ArrayList<MoodEventAssociation>();

        MoodEvent moodEvent = new MoodEvent(EmotionalState.HAPPY);
        MoodEventAssociation meAs = new MoodEventAssociation(moodEvent, "harrypotter");

        feedDataList.add(meAs);
        feedAdapter = new FeedListAdapter(getContext(), feedDataList);

        feedListView.setAdapter(feedAdapter);
        return rootView;
    }
}