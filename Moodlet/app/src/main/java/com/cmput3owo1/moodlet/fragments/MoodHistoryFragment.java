package com.cmput3owo1.moodlet.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput3owo1.moodlet.R;
import com.cmput3owo1.moodlet.adapters.MoodEventAdapter;
import com.cmput3owo1.moodlet.models.MoodEvent;
import com.cmput3owo1.moodlet.services.MoodEventService;

import java.util.ArrayList;

public class MoodHistoryFragment extends Fragment
        implements MoodEventAdapter.OnItemClickListener, MoodEventService.OnMoodHistoryUpdateListener {

    private RecyclerView recyclerView;
    private MoodEventAdapter recyclerAdapter;
    private ArrayList<MoodEvent> moodEventList;
    private MoodEventService moodEventService;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mood_history, container, false);

        recyclerView = view.findViewById(R.id.mood_event_rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        moodEventList = new ArrayList<>();

        recyclerAdapter = new MoodEventAdapter(moodEventList, this);
        recyclerView.setAdapter(recyclerAdapter);

        moodEventService = new MoodEventService();
        moodEventService.getMoodHistoryUpdates(this);

        return view;
    }

    @Override
    public void onItemClick(int pos) {
        // Implement for editing a mood event
    }

    @Override
    public void onMoodHistoryUpdate(ArrayList<MoodEvent> newHistory) {
        moodEventList.clear();
        moodEventList.addAll(newHistory);
        recyclerAdapter.notifyDataSetChanged();
    }
}
