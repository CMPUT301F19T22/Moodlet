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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    private TextView clearLocation;
    private String dateText;
    private EditText reasonEdit;
    private EditText locationEdit;
    private CheckBox currentLocationCheckbox;
    private CheckBox usePreviousLocationCheckbox;
    private ArrayAdapter<EmotionalState> moodAdapter;
    private ArrayAdapter<SocialSituation> socialAdapter;

    private EmotionalState selectedMood;
    private SocialSituation selectedSocial;

    //Add
    private String moodDisplayName;
    private String socialDisplayName;
    private String[] words;
    private MoodEvent mood;
    private IMoodEventServiceProvider mes;

    //Image
    private ImageView imageUpload;
    private Uri selectedImage;
    private static final int image_loaded = 1;
    private ProgressDialog progressDialog;

    //Location
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private Location currentLocation;
    private GeoPoint placesLocation;
    private String placesLocationDescription;
    private String placesLocationAddress;
    private GeoPoint previousLocation;
    private String previousLocationDescription;
    private String previousLocationAddress;
    private static final String[] PERMISSIONS = { Manifest.permission.ACCESS_FINE_LOCATION };
    private static final int LOCATION_REQUEST_CODE = 1;
    public static final int REQUEST_CHECK_SETTINGS = 2; // Keep public to access in parent activity
    private static final int PLACES_REQUEST_CODE = 3;

    /**
     * Default public constructor
     */
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
        setHasOptionsMenu(true);

        mes = new MoodEventService();

        //Generate current date/time
        String pattern = "MMMM d, yyyy \nh:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        date = view.findViewById(R.id.date);
        dateText = sdf.format(new Date());
        date.setText(dateText);

        //Setup view references
        moodSpinner = view.findViewById(R.id.moodSelected);
        socialSpinner= view.findViewById(R.id.socialSelected);
        reasonEdit = view.findViewById(R.id.reasonEdit);
        locationEdit = view.findViewById(R.id.locationEdit);
        clearLocation = view.findViewById(R.id.locationClear);
        currentLocationCheckbox = view.findViewById(R.id.currentLocationCheckbox);
        usePreviousLocationCheckbox = view.findViewById(R.id.usePreviousLocationCheckbox);
        imageUpload = view.findViewById(R.id.imageToUpload);
        bg = view.findViewById(R.id.bg_vector);

        //Set up spinners
        moodAdapter = new ArrayAdapter<EmotionalState>(getActivity(), R.layout.mood_spinner_style, EmotionalState.values());
        moodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        moodSpinner.setAdapter(moodAdapter);
        socialAdapter = new ArrayAdapter<SocialSituation>(getActivity(), android.R.layout.simple_spinner_item, SocialSituation.values());
        socialAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        socialSpinner.setAdapter(socialAdapter);

        //Fills in fields if editMode is active
        Bundle args = getArguments();
        if(args!=null){
            if(args.getBoolean("edit") == true) {
                editMode = true;
                mood = (MoodEvent) args.getSerializable("MoodEvent");
                final Date argDate = (Date) args.getSerializable("date");

                double lat = args.getDouble("location_lat", -99999);
                double lon = args.getDouble("location_lon", -99999);
                if (lat != -99999 && lon != -99999) {
                    previousLocation = new GeoPoint(lat, lon);
                }
                previousLocationDescription = mood.getLocationDescription();
                previousLocationAddress = mood.getLocationAddress();

                //Fill in fields
                moodSpinner.setSelection(moodAdapter.getPosition(mood.getEmotionalState()));
                socialSpinner.setSelection(socialAdapter.getPosition(mood.getSocialSituation()));
                reasonEdit.setText(mood.getReasoning());

                // Set the previous location checkbox to be initially checked
                usePreviousLocationCheckbox.setChecked(true);
                // Disable the checkbox and places autocomplete edittext
                currentLocationCheckbox.setEnabled(false);
                locationEdit.setEnabled(false);

                if (previousLocationDescription == null && previousLocation == null) {
                    // Disable the checkbox if there was no previous location
                    usePreviousLocationCheckbox.setEnabled(false);
                    // Uncheck the checkbox
                    usePreviousLocationCheckbox.setChecked(false);
                    // Re-enable the checkbox and places autocomplete edittext
                    currentLocationCheckbox.setEnabled(true);
                    locationEdit.setEnabled(true);
                } else {
                    // Set the location description or its coordinates if possible
                    setPreviousLocationInfo();
                }

                date.setText(sdf.format(argDate));
                mood.setDate(argDate);
                bg.setColorFilter(mood.getEmotionalState().getColor());
            }
        }


        // Set a click listener for the textview to clear the selected place
        clearLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do not clear using previous location
                if (editMode && usePreviousLocationCheckbox.isChecked() && usePreviousLocationCheckbox.isEnabled()) {
                    return;
                }
                // Clear the selected places autocomplete location
                placesLocation = null;
                placesLocationDescription = null;
                placesLocationAddress = null;
                // Clear the current location and uncheck the checkbox
                currentLocation = null;
                currentLocationCheckbox.setChecked(false);
                // Reset the location edittext display
                locationEdit.setText("");
            }
        });

        // Initialize the SDK
        if (!Places.isInitialized()) {
            Places.initialize(getContext(), getResources().getString(R.string.GOOGLE_API_KEY));
        }

        // Set click/focus change listeners to launch the places autocomplete widget
        locationEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchAutocomplete();
            }
        });

        locationEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    launchAutocomplete();
                }
            }
        });

        // Initialize the fused location provider
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        // Set the location callback
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    currentLocation = location;
                }
                // Stop receiving location requests (only need current location)
                fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                // Set the edittext to display the coordinates of the location
                locationEdit.setText(String.format(
                        Locale.US,
                        "[%.3f, %.3f]",
                        currentLocation.getLatitude(),
                        currentLocation.getLongitude()
                ));
            };
        };

        // Set a click listener to check if user wishes to use current location
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
                    // Restore the places autocomplete location description, or empty if there is none
                    if (placesLocation != null && placesLocationDescription != null) {
                        if (placesLocationAddress != null) {
                            locationEdit.setText(String.format("%s, %s", placesLocationDescription, placesLocationAddress));
                        } else {
                            locationEdit.setText(String.format(placesLocationDescription));
                        }
                    } else {
                        locationEdit.setText("");
                    }
                }
            }
        });

        // Set a click listener to for using previous location if in edit mode, else don't show it
        if (editMode) {
            usePreviousLocationCheckbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (usePreviousLocationCheckbox.isChecked()) {
                        // Disable the checkbox and places autocomplete edittext
                        locationEdit.setEnabled(false);
                        currentLocationCheckbox.setEnabled(false);
                        currentLocationCheckbox.setChecked(false);
                        // Set the previous location's information to the edit fields
                        setPreviousLocationInfo();
                    } else {
                        // Restore the selected (if any) edit fields
                        currentLocationCheckbox.setEnabled(true);
                        locationEdit.setEnabled(true);
                        // If there is a location description, restore the description
                        if (placesLocationDescription != null) {
                            if (placesLocationAddress != null) {
                                locationEdit.setText(String.format("%s, %s", placesLocationDescription, placesLocationAddress));
                            } else {
                                locationEdit.setText(String.format(placesLocationDescription));
                            }
                        } else {
                            locationEdit.setText("");
                        }
                    }
                }
            });
        } else {
            usePreviousLocationCheckbox.setVisibility(View.GONE);
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
                socialDisplayName = selectedSocial.getDisplayName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        return view;
    }

    /** Initialize the contents of the fragment's options menu.
     * @param menu The options menu in which you place your items.
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        if(editMode){
            inflater.inflate(R.menu.mood_edit_fragment_menu, menu);
            getActivity().setTitle("Edit Mood");
        }else{
            inflater.inflate(R.menu.mood_add_fragment_menu, menu);
            getActivity().setTitle("Add Mood");
        }
        super.onCreateOptionsMenu(menu, inflater);
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
        } else if (requestCode == PLACES_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                LatLng latLng = place.getLatLng();
                placesLocation = new GeoPoint(latLng.latitude, latLng.longitude);
                placesLocationDescription = place.getName();
                placesLocationAddress = place.getAddress();
                locationEdit.setText(String.format("%s, %s", placesLocationDescription, placesLocationAddress));
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
        Toast.makeText(getActivity(), R.string.upload_failure, Toast.LENGTH_SHORT).show();
    }

    /**
     * Callback function that is triggered upon editing or adding a mood to return the fragment back to
     * to the previous location.
     */
    @Override
    public void onMoodUpdateSuccess(){
        getActivity().finish();
    }

    /**
     * Sets the location edit fields to the previous location's information (if it exists)
     */
    private void setPreviousLocationInfo() {
        if (previousLocationDescription != null) {
            locationEdit.setText(String.format("%s, %s", previousLocationDescription, previousLocationAddress));
        } else {
            if (previousLocation != null) {
                locationEdit.setText(String.format(
                        Locale.US,
                        "[%.3f, %.3f]",
                        previousLocation.getLatitude(),
                        previousLocation.getLongitude()
                ));
            }
        }
    }

    /**
     * A function that launches the autocomplete widget to prompt the user to search for a place
     */
    private void launchAutocomplete() {
        List<Place.Field> fields = Arrays.asList(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG
        );
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(getContext());
        startActivityForResult(intent, PLACES_REQUEST_CODE);
    }

    /**
     * A function that calls the fused location provider to obtain updates on the user's location
     */
    private void getCurrentLocation() {
        fusedLocationProviderClient.requestLocationUpdates(createLocationRequest(), locationCallback, Looper.getMainLooper());
    }

    /**
     * Creates a location request to obtain the user's current location
     * @return The created location request
     */
    private LocationRequest createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    /**
     * A function that checks if the location settings on the Android device is enabled.
     * If location settings are not enabled, it prompts the user to enable them. If the
     * user grants location settings, the function will attempt to obtain the current location.
     *
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
    /**
     * A hook that is called whenever an item in the options menu is selected.
     * This method handles the the addition and editing of moods when the corresponding
     * action item is clicked.
     * @param item The menu item that was selected
     * @return boolean indicating state of whether option item was selected
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.confirmEdit:
                mood.setSocialSituation(selectedSocial);
                mood.setEmotionalState(selectedMood);

                // Check for using previous location first (takes precedence)
                if (usePreviousLocationCheckbox.isChecked() && previousLocation != null) {
                    mood.setLocation(previousLocation);
                    if (previousLocationDescription != null) {
                        mood.setLocationDescription(previousLocationDescription);
                    }
                } else if (currentLocationCheckbox.isChecked() && currentLocation != null) {
                    mood.setLocation(new GeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude()));
                    // Clear the location description and address for current location setting
                    mood.setLocationDescription(null);
                    mood.setLocationAddress(null);
                } else if (placesLocation != null) {
                    mood.setLocation(placesLocation);
                    mood.setLocationDescription(placesLocationDescription);
                    mood.setLocationAddress(placesLocationAddress);
                }
            
                words = reasonEdit.getText().toString().split(" ");
                if(words.length <= 3) {
                    mood.setReasoning(reasonEdit.getText().toString());
                    if(selectedImage != null) {
                        progressDialog = new ProgressDialog(getActivity());
                        progressDialog.setTitle("Uploading...");
                        progressDialog.show();
                        mes.uploadImage(AddMoodFragment.this, selectedImage);
                    }else{
                        mes.editMoodEvent(mood,AddMoodFragment.this);
                    }
                }else{
                    Toast.makeText(getActivity(), R.string.word_count_exceeded, Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.confirmAdd:
                mood = new MoodEvent();
                mood.setEmotionalState(selectedMood);
                mood.setSocialSituation(selectedSocial);

                if (currentLocationCheckbox.isChecked() && currentLocation != null) {
                    mood.setLocation(new GeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude()));
                } else if (placesLocation != null) {
                    mood.setLocation(placesLocation);
                    mood.setLocationDescription(placesLocationDescription);
                    mood.setLocationAddress(placesLocationAddress);
                }

                words = reasonEdit.getText().toString().split(" ");
                if(words.length <= 3) {
                    mood.setReasoning(reasonEdit.getText().toString());
                    if(selectedImage != null) {
                        progressDialog = new ProgressDialog(getActivity());
                        progressDialog.setTitle("Uploading...");
                        progressDialog.show();
                        mes.uploadImage(AddMoodFragment.this, selectedImage);
                    }else{
                        mes.addMoodEvent(mood,AddMoodFragment.this);
                    }
                }else{
                    Toast.makeText(getActivity(), R.string.word_count_exceeded, Toast.LENGTH_SHORT).show();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
