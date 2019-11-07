package com.cmput3owo1.moodlet.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cmput3owo1.moodlet.R;
import com.cmput3owo1.moodlet.adapters.FeedListAdapter;
import com.cmput3owo1.moodlet.models.EmotionalState;
import com.cmput3owo1.moodlet.models.MoodEvent;
import com.cmput3owo1.moodlet.models.MoodEventAssociation;
import com.cmput3owo1.moodlet.models.SocialSituation;
import com.cmput3owo1.moodlet.services.DatabaseUtil;
import com.cmput3owo1.moodlet.services.MoodEventService;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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

        feedListView = rootView.findViewById(R.id.feed_list);
        feedDataList = new ArrayList<>();

        service.getFeedUpdates(this);

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