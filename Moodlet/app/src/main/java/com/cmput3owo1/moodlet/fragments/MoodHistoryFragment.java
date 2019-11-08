package com.cmput3owo1.moodlet.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput3owo1.moodlet.R;
import com.cmput3owo1.moodlet.activities.MoodEditorActivity;
import com.cmput3owo1.moodlet.adapters.MoodEventAdapter;
import com.cmput3owo1.moodlet.models.MoodEvent;
import com.cmput3owo1.moodlet.services.MoodEventService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MoodHistoryFragment extends Fragment
        implements MoodEventAdapter.OnItemClickListener, MoodEventService.OnMoodHistoryUpdateListener {

    private RecyclerView recyclerView;
    private MoodEventAdapter recyclerAdapter;
    private ArrayList<MoodEvent> moodEventList;
    private MoodEventService moodEventService;
    private FloatingActionButton addMood;


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

        addMood = view.findViewById(R.id.add_mood_fab);
        addMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MoodEditorActivity.class);
                intent.putExtra("add",true);
                startActivity(intent);
            }
        });

        return view;
    }


    @Override
    public void onItemClick(int pos) {
        // Implement for editing a mood event
        MoodEvent selected = moodEventList.get(pos);
        Intent intent = new Intent(getActivity(), MoodEditorActivity.class);
        intent.putExtra("MoodEvent",selected);
        intent.putExtra("date",selected.getDate());
        intent.putExtra("view",true);
        startActivity(intent);
    }

    @Override
    public void onMoodHistoryUpdate(ArrayList<MoodEvent> newHistory) {
        moodEventList.clear();
        moodEventList.addAll(newHistory);
        recyclerAdapter.notifyDataSetChanged();
    }
}

