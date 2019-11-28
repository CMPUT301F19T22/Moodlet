package com.cmput3owo1.moodlet.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cmput3owo1.moodlet.R;
import com.cmput3owo1.moodlet.models.FollowRequest;
import com.google.firebase.firestore.Query;

public class FollowRequestActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_request);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //      Manually replace navigation icon with custom icon.
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_24px);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//              TODO: Create intent to go to the follow requests fragment.
                Toast.makeText(FollowRequestActivity.this, "Go back", Toast.LENGTH_SHORT).show();

                finish();

            }
        });
    }

}
