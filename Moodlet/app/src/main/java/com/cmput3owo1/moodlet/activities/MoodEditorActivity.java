package com.cmput3owo1.moodlet.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cmput3owo1.moodlet.R;
import com.cmput3owo1.moodlet.fragments.AddMoodFragment;
import com.cmput3owo1.moodlet.fragments.LoginFragment;
import com.cmput3owo1.moodlet.fragments.ViewMoodFragment;
import com.cmput3owo1.moodlet.models.MoodEvent;

import java.io.Serializable;


public class MoodEditorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            Intent intent = getIntent();
            if(intent.hasExtra("add")){
                AddMoodFragment addMoodFragment = new AddMoodFragment();

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, addMoodFragment).commit();
            }
            else if(intent.hasExtra("view")){
                ViewMoodFragment viewMoodFragment = new ViewMoodFragment();
                viewMoodFragment.setArguments(getIntent().getExtras());

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, viewMoodFragment).commit();
            }

        }
    }

}
