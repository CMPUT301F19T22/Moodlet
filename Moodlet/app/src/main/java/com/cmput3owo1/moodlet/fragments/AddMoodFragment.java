package com.cmput3owo1.moodlet.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cmput3owo1.moodlet.R;
import com.cmput3owo1.moodlet.activities.MoodEditorActivity;
import com.cmput3owo1.moodlet.models.EmotionalState;
import com.cmput3owo1.moodlet.models.MoodEvent;
import com.cmput3owo1.moodlet.models.SocialSituation;
import com.cmput3owo1.moodlet.services.MoodEventService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddMoodFragment extends Fragment implements MoodEventService.OnImageUploadListener {

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

    public AddMoodFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_mood,container,false);

        date = view.findViewById(R.id.date);
        bg = view.findViewById(R.id.bg_vector);
        String pattern = "MMMM d, yyyy \nh:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        dateText = sdf.format(new Date());
        date.setText(dateText);

        moodSpinner = view.findViewById(R.id.moodSelected);
        socialSpinner= view.findViewById(R.id.socialSelected);
        reasonEdit = view.findViewById(R.id.reasonEdit);
        imageUpload = view.findViewById(R.id.imageToUpload);
        mes = new MoodEventService();
        //set time when press fab, fix
        mood = new MoodEvent();

        addMood = view.findViewById(R.id.add_mood);


        moodAdapter = new ArrayAdapter<EmotionalState>(getActivity(), R.layout.mood_spinner_style, EmotionalState.values());
        moodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        moodSpinner.setAdapter(moodAdapter);

        socialAdapter = new ArrayAdapter<SocialSituation>(getActivity(), android.R.layout.simple_spinner_item, SocialSituation.values());
        socialAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        socialSpinner.setAdapter(socialAdapter);

        imageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery,image_loaded);

            }
        });


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
                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setTitle("Uploading...");
                    progressDialog.show();
                    mes.uploadImage(AddMoodFragment.this, selectedImage);
                }else{
                    mes.addMoodEvent(mood);
                }

            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null) {
            selectedImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), selectedImage);
                imageUpload.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
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
        Toast.makeText(getActivity(), "Upload Failed.", Toast.LENGTH_SHORT).show();
    }


}
