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
import com.cmput3owo1.moodlet.models.EmotionalState;
import com.cmput3owo1.moodlet.models.MoodEvent;
import com.cmput3owo1.moodlet.models.SocialSituation;
import com.cmput3owo1.moodlet.services.IMoodEventServiceProvider;
import com.cmput3owo1.moodlet.services.MoodEventService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A fragment that has editable fields that allow for a user to fill in and add a MoodEvent to
 * a database or edit an existing MoodEvent in a database.
 */
public class AddMoodFragment extends Fragment
        implements IMoodEventServiceProvider.OnImageUploadListener, IMoodEventServiceProvider.OnMoodUpdateListener {

    private boolean editMode;
    private Spinner moodSpinner;
    private Spinner socialSpinner;
    private ImageView bg;
    private TextView date;
    private String dateText;
    private EditText reasonEdit;
    private ArrayAdapter<EmotionalState> moodAdapter;
    private ArrayAdapter<SocialSituation> socialAdapter;

    private EmotionalState selectedMood;
    private SocialSituation selectedSocial;

    //Add
    private String moodDisplayName;
    private String socialDisplayName;
    private MoodEvent mood;
    private IMoodEventServiceProvider mes;

    //Image
    private ImageView imageUpload;
    private Uri selectedImage;
    private static final int image_loaded = 1;
    private ProgressDialog progressDialog;

    private Button addMood;
    private Button confirmEdit;

    public AddMoodFragment(){
    }

    /**
     * This function is called to have the fragment instantiate its user interface view.
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container  If non-null, this is the parent view that the fragment's UI should be attached to. The fragment should not add the view itself, but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
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

        //Temporary debug buttons
        addMood = view.findViewById(R.id.add_mood);
        confirmEdit = view.findViewById(R.id.confirm_edit);

        moodAdapter = new ArrayAdapter<EmotionalState>(getActivity(), R.layout.mood_spinner_style, EmotionalState.values());
        moodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        moodSpinner.setAdapter(moodAdapter);
        socialAdapter = new ArrayAdapter<SocialSituation>(getActivity(), android.R.layout.simple_spinner_item, SocialSituation.values());
        socialAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        socialSpinner.setAdapter(socialAdapter);


        //fix logic up later
        try {
            Bundle args = getArguments();
            if(!args.isEmpty()){
                editMode = true;
                mood = (MoodEvent) args.getSerializable("MoodEvent");
                final Date argDate = (Date) args.getSerializable("date");
                moodSpinner.setSelection(moodAdapter.getPosition(mood.getEmotionalState()));
                socialSpinner.setSelection(socialAdapter.getPosition(mood.getSocialSituation()));
                reasonEdit.setText(mood.getReasoning());
                date.setText(sdf.format(argDate));
                mood.setDate(argDate);

                bg.setColorFilter(mood.getEmotionalState().getColor());

                //REMOVE LATER, debugging Proof of Concept
                if(args.getBoolean("edit")){
                    addMood.setVisibility(View.INVISIBLE);
                    confirmEdit.setVisibility(View.VISIBLE);

                    confirmEdit.setOnClickListener(new View.OnClickListener() {
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
                                mes.editMoodEvent(mood,AddMoodFragment.this);
                            }
                        }
                    });

                }
            }
        }
        catch(Exception e){
        }

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
                    mes.addMoodEvent(mood,AddMoodFragment.this);
                }

            }
        });

        return view;
    }

    /**
     * Callback for when an image is imported into the Fragment. This function verifies if data is returned
     * from the call and updates the user's currently selected image, and displays the imported
     * image.
     * @param requestCode The request code initially supplied to identify where the result is obtained from
     * @param resultCode The result code from the calling activity
     * @param data The intent to return if additional data exists
     */
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
    /**
     * Callback function that is triggered upon image upload success to give the user the corresponding filepath
     * where it is located in the database and to then execute the appropriate following action.
     * @param filepath A string value of the location in which the image was uploaded to in FireBase.
     */
    @Override
    public void onImageUploadSuccess(String filepath) {
        //progressDialog.dismiss();
        mood.setPhotographPath(filepath);
        if(!editMode){
            mes.addMoodEvent(mood,AddMoodFragment.this);
        }else{
            mes.editMoodEvent(mood,AddMoodFragment.this);
        }
    }

    /**
     * Callback function that is triggered upon image upload failure to inform the user of the exception.
     */
    @Override
    public void onImageUploadFailure() {
        progressDialog.dismiss();
        Toast.makeText(getActivity(), "Upload Failed.", Toast.LENGTH_SHORT).show();
    }

    /**
     * Callback function that is triggered upon editing or adding a mood to return the fragment back to
     * to the previous location.
     */
    @Override
    public void onMoodUpdateSuccess(){
        getActivity().finish();
    }

}
