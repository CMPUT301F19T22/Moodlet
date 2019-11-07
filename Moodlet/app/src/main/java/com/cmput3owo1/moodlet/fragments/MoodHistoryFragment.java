package com.cmput3owo1.moodlet.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.cmput3owo1.moodlet.R;
import com.cmput3owo1.moodlet.activities.MoodEditorActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

public class MoodHistoryFragment extends Fragment {

    private FloatingActionButton addMood;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_mood_history, container, false);
        addMood = rootView.findViewById(R.id.add_fab);
        TextView moodHistory = rootView.findViewById(R.id.text_history);


        addMood.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                //onAddMoodClick
                Intent intent = new Intent(rootView.getContext(), MoodEditorActivity.class);
                intent.putExtra("add", true);
                startActivity(intent);
            }
        });

        moodHistory.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View view) {
                //onAddMoodClick
                Intent intent = new Intent(rootView.getContext(), MoodEditorActivity.class);
                intent.putExtra("view", true);
                startActivity(intent);
            }
        });


        return rootView;

    }

}