package com.cmput3owo1.moodlet.fragments;

import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FeedFragment extends Fragment {
    private ListView feedListView;
    private FeedListAdapter feedAdapter;
    private ArrayList<MoodEventAssociation> feedDataList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);

        feedListView = rootView.findViewById(R.id.feed_list);
        feedDataList = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference following = db.collection("users/tbojovic/following");

        following.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                feedDataList.clear();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    EmotionalState emotionalState = EmotionalState.valueOf(String.valueOf(doc.getData().get("emotionalState")));
                    String reasoning = String.valueOf(doc.getData().get("reasoning"));
                    SocialSituation socialSituation = SocialSituation.valueOf(String.valueOf(doc.getData().get("socialSituation")));
                    Timestamp timestamp = (Timestamp) doc.getData().get("dateTime");
                    //photograph
                    //location
                    MoodEvent moodEvent = new MoodEvent(emotionalState, timestamp.toDate());

                    moodEvent.setReasoning(reasoning);
                    moodEvent.setSocialSituation(socialSituation);
                    feedDataList.add(new MoodEventAssociation(moodEvent, doc.getId()));
                }
                feedAdapter.notifyDataSetChanged();
            }
        });

        feedAdapter = new FeedListAdapter(getContext(), feedDataList);

        feedListView.setAdapter(feedAdapter);
        return rootView;
    }
}