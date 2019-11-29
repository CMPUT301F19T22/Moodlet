package com.cmput3owo1.moodlet.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cmput3owo1.moodlet.R;
import com.cmput3owo1.moodlet.models.EmotionalState;
import com.cmput3owo1.moodlet.services.IMoodEventServiceProvider;
import com.cmput3owo1.moodlet.services.MoodEventService;

import java.util.ArrayList;

/**
 * An Activity that allows users to filter for specific emotional states.
 */
public class FilterActivity extends AppCompatActivity {

    Toolbar toolbar;
    private IMoodEventServiceProvider mes;
    private CheckBox happyBox;
    private CheckBox angryBox;
    private CheckBox sadBox;
    private CheckBox excitedBox;
    private CheckBox confusedBox;
    private CheckBox scaredBox;
    private CheckBox jealousBox;
    private Button applyButton;
    private ArrayList<String> selectedFilters;


    /**
     * Called when activity starts, and displays filtering layout
     * @param savedInstanceState Used to restore an activity's previous state
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        Intent intent = getIntent();

        if (intent.hasExtra("filter_checked")) {
            selectedFilters = intent.getStringArrayListExtra("filter_checked");
        } else {
            selectedFilters = new ArrayList<>();
        }

        mes = new MoodEventService();

        // set up view refs
        happyBox = findViewById(R.id.filter_happy);
        angryBox = findViewById(R.id.filter_angry);
        sadBox = findViewById(R.id.filter_sad);
        excitedBox = findViewById(R.id.filter_excited);
        confusedBox = findViewById(R.id.filter_confused);
        scaredBox = findViewById(R.id.filter_scared);
        jealousBox = findViewById(R.id.filter_jealous);
        applyButton = findViewById(R.id.apply_filter_button);

        for (int i = 0; i < selectedFilters.size(); i++) {
            switch (selectedFilters.get(i)) {
                case "HAPPY":
                    happyBox.setChecked(true);
                    break;
                case "SAD":
                    sadBox.setChecked(true);
                    break;
                case "ANGRY":
                    angryBox.setChecked(true);
                    break;
                case "EXCITED":
                    excitedBox.setChecked(true);
                    break;
                case "CONFUSED":
                    confusedBox.setChecked(true);
                    break;
                case "SCARED":
                    scaredBox.setChecked(true);
                    break;
                case "JEALOUS":
                    jealousBox.setChecked(true);
                    break;
            }
        }


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_24px);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        happyBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox cb = (CheckBox) view;
                if (cb.isChecked()) {
                    selectedFilters.add(EmotionalState.HAPPY.toString().toUpperCase());
                }
                else {
                    selectedFilters.remove(EmotionalState.HAPPY.toString().toUpperCase());
                }
            }
        });

        sadBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox cb = (CheckBox) view;
                if (cb.isChecked()) {
                    selectedFilters.add(EmotionalState.SAD.toString().toUpperCase());
                }
                else {
                    selectedFilters.remove(EmotionalState.SAD.toString().toUpperCase());
                }
            }
        });

        angryBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox cb = (CheckBox) view;
                if (cb.isChecked()) {
                    selectedFilters.add(EmotionalState.ANGRY.toString().toUpperCase());
                }
                else {
                    selectedFilters.remove(EmotionalState.ANGRY.toString().toUpperCase());
                }
            }
        });

        excitedBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox cb = (CheckBox) view;
                if (cb.isChecked()) {
                    selectedFilters.add(EmotionalState.EXCITED.toString().toUpperCase());
                }
                else {
                    selectedFilters.remove(EmotionalState.EXCITED.toString().toUpperCase());
                }
            }
        });

        confusedBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox cb = (CheckBox) view;
                if (cb.isChecked()) {
                    selectedFilters.add(EmotionalState.CONFUSED.toString().toUpperCase());
                }
                else {
                    selectedFilters.remove(EmotionalState.CONFUSED.toString().toUpperCase());
                }
            }
        });

        scaredBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox cb = (CheckBox) view;
                if (cb.isChecked()) {
                    selectedFilters.add(EmotionalState.SCARED.toString().toUpperCase());
                }
                else {
                    selectedFilters.remove(EmotionalState.SCARED.toString().toUpperCase());
                }
            }
        });

        jealousBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox cb = (CheckBox) view;
                if (cb.isChecked()) {
                    selectedFilters.add(EmotionalState.JEALOUS.toString().toUpperCase());
                }
                else {
                    selectedFilters.remove(EmotionalState.JEALOUS.toString().toUpperCase());
                }
            }
        });

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                if (selectedFilters.size() > 0) {
                    returnIntent.putExtra("filters", selectedFilters);
                }
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }

    /**
     * Initialize the contents of the Activity's standard options menu.
     * @param menu The options menu in which you place your items.
     * @return Return true if menu displayed, else nothing shown.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * When cleared icon is clicked on the toolbar, then all checkboxes are cleared
     * @param item The options menu item to clear
     * @return Return true if menu item is clicked, else false.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.clear) {
            happyBox.setChecked(false);
            sadBox.setChecked(false);
            angryBox.setChecked(false);
            excitedBox.setChecked(false);
            confusedBox.setChecked(false);
            scaredBox.setChecked(false);
            jealousBox.setChecked(false);
            selectedFilters.clear();
        }

        return super.onOptionsItemSelected(item);
    }
}
