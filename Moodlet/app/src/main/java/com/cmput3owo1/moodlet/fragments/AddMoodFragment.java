package com.cmput3owo1.moodlet.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.cmput3owo1.moodlet.R;
import com.cmput3owo1.moodlet.models.EmotionalState;
import com.cmput3owo1.moodlet.models.MoodEvent;
import com.cmput3owo1.moodlet.models.SocialSituation;
import com.cmput3owo1.moodlet.services.IMoodEventServiceProvider;
import com.cmput3owo1.moodlet.services.MoodEventService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A fragment that has editable fields that allow for a user to fill in and add a MoodEvent to
 * a database or edit an existing MoodEvent in a database.
 */
public class AddMoodFragment extends Fragment implements
        IMoodEventServiceProvider.OnImageUploadListener,
        IMoodEventServiceProvider.OnMoodUpdateListener {

    private boolean editMode;
    private Spinner moodSpinner;
    private Spinner socialSpinner;
    private ImageView bg;
    private TextView date;
    private String dateText;
    private EditText reasonEdit;
    private EditText locationEdit;
    private CheckBox currentLocationCheckbox;
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

    //Location
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private Location currentLocation;
    private static final String[] PERMISSIONS = { Manifest.permission.ACCESS_FINE_LOCATION };
    private static final int LOCATION_REQUEST_CODE = 1;
    public static final int REQUEST_CHECK_SETTINGS = 2;

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
        locationEdit = view.findViewById(R.id.locationEdit);
        currentLocationCheckbox = view.findViewById(R.id.currentLocationCheckbox);
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

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...
                    currentLocation = location;
                    locationEdit.setText(String.format("Lat: %s, Lon: %s", location.getLatitude(), location.getLongitude()));
                }
                // Stop receiving location requests (only need current location)
                fusedLocationProviderClient.removeLocationUpdates(locationCallback);
            };
        };

        currentLocationCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLocationCheckbox.isChecked()) {
                    locationEdit.setEnabled(false);
                    // Request location permissions if they are not yet granted
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(PERMISSIONS, LOCATION_REQUEST_CODE);
                    } else {
                        // Check if location settings are enabled
                        checkLocationSettings();
                    }
                } else {
                    locationEdit.setEnabled(true);
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
     * Callback for the result from requesting permissions. This function verifies that the user
     * has accepted the location permissions and prompts the user to turn on Google location
     * services if not already on.
     * @param requestCode The permission request code passed in the requestPermissions function
     * @param permissions The requested permissions
     * @param grantResults The grant results for corresponding permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Check if location settings are enabled
                checkLocationSettings();
            } else {
                // Deselect checkbox and re-enable location edit text
                currentLocationCheckbox.setChecked(false);
                locationEdit.setEnabled(true);
                Toast.makeText(getContext(), getResources().getString(R.string.location_permissions_denied), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Callback for handling the result of the location settings response.
     * Otherwise...
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

        // Check location settings result
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    // User has enabled location settings
                    getCurrentLocation();
                    return;
                case Activity.RESULT_CANCELED:
                    // Deselect checkbox and re-enable location edit text
                    currentLocationCheckbox.setChecked(false);
                    locationEdit.setEnabled(true);
                    Toast.makeText(getContext(), getResources().getString(R.string.location_settings_denied), Toast.LENGTH_SHORT).show();
                    return;
                default:
                    // Ignore
            }
        } else {
            // Handle importing image
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


    private void getCurrentLocation() {
        fusedLocationProviderClient.requestLocationUpdates(createLocationRequest(), locationCallback, Looper.getMainLooper());
    }

    private LocationRequest createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    /**
     * Source: https://developer.android.com/training/location/change-location-settings
     */
    private void checkLocationSettings() {
        // Create a locations settings request prompt for the user to enable location settings
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(createLocationRequest())
                .setAlwaysShow(true);
        Task<LocationSettingsResponse> task = LocationServices.getSettingsClient(getContext()).checkLocationSettings(builder.build());

        task.addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...
                getCurrentLocation();
            }
        });

        task.addOnFailureListener(getActivity(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().

                        // Note that upon resolution, the onActivityResult callback of the activity
                        // is called and will need to be forwarded to this fragment
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                        Toast.makeText(getContext(), "Error!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
