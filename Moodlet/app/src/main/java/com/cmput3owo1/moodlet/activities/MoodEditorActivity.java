package com.cmput3owo1.moodlet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cmput3owo1.moodlet.R;
import com.cmput3owo1.moodlet.fragments.AddMoodFragment;
import com.cmput3owo1.moodlet.fragments.ViewMoodFragment;
/**
 * An activity used to handle fragments for adding, viewing and editing MoodEvents
 * depending on where the activity is invoked.
 */
public class MoodEditorActivity extends AppCompatActivity {

    private AddMoodFragment addMoodFragment;
    private ViewMoodFragment viewMoodFragment;
    private Toolbar toolbar;

    /**
     * Called when the activity is started and sets up the corresponding fragment to display.
     * @param savedInstanceState Used to restore an activity's previous state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        toolbar = findViewById(R.id.add_toolbar);
        setSupportActionBar(toolbar);

        // Manually replace navigation icon with custom icon.
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_24px);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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
                addMoodFragment = new AddMoodFragment();
                Bundle args = new Bundle();
                args.putBoolean("add",true);
                addMoodFragment.setArguments(args);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, addMoodFragment).commit();
            } else if(intent.hasExtra("view")){
                viewMoodFragment = new ViewMoodFragment();
                viewMoodFragment.setArguments(getIntent().getExtras());

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, viewMoodFragment).commit();
            }

        }
    }

    /**
     * This function is used to forward the onActivityResult call to the AddMoodFragment when
     * requesting location settings so that the results can be handled in the fragment.
     * @param requestCode The request code initially supplied to identify where the result is obtained from
     * @param resultCode The result code from the calling activity
     * @param data The intent to return if additional data exists
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AddMoodFragment.REQUEST_CHECK_SETTINGS) {
            // Check to see if the fragment is null, if not get the edit fragment using tag
            if (addMoodFragment == null) {
                addMoodFragment = (AddMoodFragment) getSupportFragmentManager().findFragmentByTag("EDIT_FRAGMENT");
            }
            addMoodFragment.onActivityResult(requestCode, resultCode, data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
