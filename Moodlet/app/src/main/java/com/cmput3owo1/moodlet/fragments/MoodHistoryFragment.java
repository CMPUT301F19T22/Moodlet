package com.cmput3owo1.moodlet.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.cmput3owo1.moodlet.R;
import com.cmput3owo1.moodlet.activities.MoodEditorActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MoodHistoryFragment extends Fragment {

    private FloatingActionButton addMood;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_mood_history, container, false);
        addMood = rootView.findViewById(R.id.add_fab);

        addMood.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                //onAddMoodClick
                Intent intent = new Intent(rootView.getContext(), MoodEditorActivity.class);
                startActivity(intent);
            }
        });

        return rootView;

    }

}