package com.cmput3owo1.moodlet.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.cmput3owo1.moodlet.R;
import com.cmput3owo1.moodlet.fragments.LoginFragment;

/**
 *  Activity that holds the login and registration fragments
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Called when the activity is starting.
     * @param savedInstanceState Used to restore an activity's previous state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
            LoginFragment loginFragment = new LoginFragment();
            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            loginFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, loginFragment).commit();
        }
    }

}
