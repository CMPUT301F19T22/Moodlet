package com.cmput3owo1.moodlet.fragments;

import android.content.Intent;
import android.os.Bundle;
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
import com.cmput3owo1.moodlet.services.IMoodEventServiceProvider;
import com.cmput3owo1.moodlet.services.MoodEventService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * A fragment that hold the list of user's mood events while displaying the emotion, date,
 * and time sorted in reverse chronological order of each mood event.
 */
public class MoodHistoryFragment extends Fragment
        implements MoodEventAdapter.OnItemClickListener, IMoodEventServiceProvider.OnMoodHistoryUpdateListener {

    private RecyclerView recyclerView;
    private MoodEventAdapter recyclerAdapter;
    private ArrayList<MoodEvent> moodEventList;
    private IMoodEventServiceProvider moodEventService;
    private FloatingActionButton addMood;


    /**
     * This function is called to have the fragment instantiate its user interface view.
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
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

    /**
     * Called when the user is clicking on a mood event to edit
     * @param pos Gets position of the item clicked in the RecyclerView
     */
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

    /**
     * Gets called when there is an update to the user's Mood Event History
     * @param newHistory The updated Mood Event History
     */
    @Override
    public void onMoodHistoryUpdate(ArrayList<MoodEvent> newHistory) {
        moodEventList.clear();
        moodEventList.addAll(newHistory);
        recyclerAdapter.notifyDataSetChanged();
    }
}

