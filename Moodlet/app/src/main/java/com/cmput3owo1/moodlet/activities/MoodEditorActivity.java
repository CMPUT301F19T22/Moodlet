package com.cmput3owo1.moodlet.activities;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.cmput3owo1.moodlet.R;
import com.cmput3owo1.moodlet.models.EmotionalState;
import com.cmput3owo1.moodlet.models.SocialSituation;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MoodEditorActivity extends AppCompatActivity {

    Spinner moodSpinner;
    Spinner socialSpinner;
    ImageView bg;
    TextView moodLabel;
    TextView reasonLabel;
    ArrayAdapter<EmotionalState> moodAdapter;
    ArrayAdapter<SocialSituation> socialAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_add);




        moodSpinner = findViewById(R.id.moodSelected);
        socialSpinner= findViewById(R.id.socialSelected);
        moodLabel = findViewById(R.id.moodLabel);
        reasonLabel = findViewById(R.id.reasonLabel);
        bg = findViewById(R.id.bg_vector);

        moodAdapter = new ArrayAdapter<EmotionalState>(this, R.layout.mood_spinner_style, EmotionalState.values());
        moodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        socialAdapter = new ArrayAdapter<SocialSituation>(this, android.R.layout.simple_spinner_item, SocialSituation.values());
        socialAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        moodSpinner.setAdapter(moodAdapter);
        socialSpinner.setAdapter(socialAdapter);


        moodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                EmotionalState selected = EmotionalState.values()[i];
                String displayName = selected.getDisplayName();
                int color = selected.getColor();
                bg.setColorFilter(color);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                moodLabel.setText("Mood");
            }
        });
    }

}
