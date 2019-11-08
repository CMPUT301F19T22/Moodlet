package com.cmput3owo1.moodlet.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput3owo1.moodlet.R;
import com.cmput3owo1.moodlet.models.EmotionalState;
import com.cmput3owo1.moodlet.models.MoodEvent;
import com.cmput3owo1.moodlet.models.SocialSituation;
import com.cmput3owo1.moodlet.services.MoodEventService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class MoodEditorActivity extends AppCompatActivity implements MoodEventService.OnImageUploadListener {

    boolean editMode;
    Spinner moodSpinner;
    Spinner socialSpinner;
    ImageView bg;
    TextView moodDisplay;
    TextView socialDisplay;
    TextView date;
    String dateText;
    EditText reasonEdit;
    ArrayAdapter<EmotionalState> moodAdapter;
    ArrayAdapter<SocialSituation> socialAdapter;

    EmotionalState selectedMood;
    SocialSituation selectedSocial;

    //Add
    String moodDisplayName;
    String socialDisplayName;
    MoodEvent mood;
    MoodEventService mes;

    //Image
    ImageView imageUpload;
    Uri selectedImage;
    private static final int image_loaded = 1;
    ProgressDialog progressDialog;

    Button addMood;
    Button toggleEdit;
    Button confirmEdit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_add);

        editMode = false;
        moodSpinner = findViewById(R.id.moodSelected);
        socialSpinner= findViewById(R.id.socialSelected);
        moodDisplay = findViewById(R.id.moodDisplay);
        socialDisplay = findViewById(R.id.socialDisplay);
        reasonEdit = findViewById(R.id.reasonEdit);
        date = findViewById(R.id.date);
        bg = findViewById(R.id.bg_vector);
        imageUpload = findViewById(R.id.imageToUpload);
        mes = new MoodEventService();
        mood = new MoodEvent();


        String pattern = "MMMM d, yyyy \nh:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        dateText = sdf.format(new Date());

        date.setText(dateText);

        //temp buttons
        addMood = findViewById(R.id.add_mood);
        toggleEdit = findViewById(R.id.toggle_edit);
        confirmEdit = findViewById(R.id.confirm_edit);

        Intent intent = getIntent();
        if(intent.hasExtra("add")){
            editMode = true;
            addMode();
        }
        else if(intent.hasExtra("view")){
            editMode = false;
            viewMode();
        }


        toggleEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editMode = !editMode;
                if(editMode){
                    editMode();
                }else{
                    viewMode();
                }
            }
        });

        imageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery,image_loaded);

            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data != null) {
            selectedImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                imageUpload.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }


    public void addMode(){
        addMood.setVisibility(View.VISIBLE);
        moodSpinner.setVisibility(View.VISIBLE);
        socialSpinner.setVisibility(View.VISIBLE);
        moodDisplay.setVisibility(View.INVISIBLE);
        socialDisplay.setVisibility(View.INVISIBLE);
        reasonEdit.setClickable(true);
        reasonEdit.setFocusable(true);

        moodAdapter = new ArrayAdapter<EmotionalState>(this, R.layout.mood_spinner_style, EmotionalState.values());
        moodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        moodSpinner.setAdapter(moodAdapter);

        socialAdapter = new ArrayAdapter<SocialSituation>(this, android.R.layout.simple_spinner_item, SocialSituation.values());
        socialAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        socialSpinner.setAdapter(socialAdapter);

        moodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedMood = EmotionalState.values()[i];
                mood.setEmotionalState(selectedMood);

                moodDisplayName = selectedMood.getDisplayName();
                int color = selectedMood.getColor();
                bg.setColorFilter(color);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        socialSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedSocial = SocialSituation.values()[i];
                mood.setSocialSituation(selectedSocial);
                socialDisplayName = selectedSocial.getDisplayName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        addMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(reasonEdit.getText().toString() != null){
                    mood.setReasoning(reasonEdit.getText().toString());
                }

                if(selectedImage != null) {
                    progressDialog = new ProgressDialog(MoodEditorActivity.this);
                    progressDialog.setTitle("Uploading...");
                    progressDialog.show();
                    mes.uploadImage(MoodEditorActivity.this, selectedImage);
                }else{
                    mes.addMoodEvent(mood);
                }

            }
        });

    }

    @Override
    public void onImageUploadSuccess(String filepath) {
        progressDialog.dismiss();
        mood.setPhotographPath(filepath);
        mes.addMoodEvent(mood);
    }

    @Override
    public void onImageUploadFailure() {
        progressDialog.dismiss();
        Toast.makeText(this, "Upload Failed.", Toast.LENGTH_SHORT).show();
    }


    public void editMode(){
        toggleEdit.setVisibility(View.INVISIBLE);
        confirmEdit.setVisibility(View.VISIBLE);
        moodSpinner.setVisibility(View.VISIBLE);
        socialSpinner.setVisibility(View.VISIBLE);
        moodDisplay.setVisibility(View.INVISIBLE);
        socialDisplay.setVisibility(View.INVISIBLE);
        reasonEdit.setClickable(true);
        reasonEdit.setFocusable(true);

        moodAdapter = new ArrayAdapter<EmotionalState>(this, R.layout.mood_spinner_style, EmotionalState.values());
        moodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        moodSpinner.setAdapter(moodAdapter);

        socialAdapter = new ArrayAdapter<SocialSituation>(this, android.R.layout.simple_spinner_item, SocialSituation.values());
        socialAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
            }
        });
    }

    public void viewMode(){
        toggleEdit.setVisibility(View.VISIBLE);
        reasonEdit.setClickable(false);
        reasonEdit.setFocusable(false);
        bg.setColorFilter(Color.GRAY);
        moodSpinner.setVisibility(View.INVISIBLE);
        socialSpinner.setVisibility(View.INVISIBLE);
        moodDisplay.setVisibility(View.VISIBLE);
        socialDisplay.setVisibility(View.VISIBLE);
    }



}
