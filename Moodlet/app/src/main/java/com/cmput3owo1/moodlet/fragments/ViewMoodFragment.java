package com.cmput3owo1.moodlet.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.cmput3owo1.moodlet.R;
import com.cmput3owo1.moodlet.models.MoodEvent;
import com.google.firebase.firestore.GeoPoint;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A fragment that displays the details of a specific MoodEvent and optionally
 * allows for users to swap between an editable mode.
 */
public class ViewMoodFragment extends Fragment {

    private ImageView bg;
    private TextView moodDisplay;
    private TextView socialDisplay;
    private TextView date;
    private TextView reasonDisplay;
    private TextView locationDisplay;
    private ImageView imageDisplay;
    private MoodEvent moodObj;
    private Date argDate;
    private GeoPoint argLocation;
    /**
     * Default constructor for the Fragment
     */
    public ViewMoodFragment(){

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
        View view = inflater.inflate(R.layout.fragment_view_mood, container, false);
        setHasOptionsMenu(true);

        //Setup date pattern and SimpleDateFormat.
        date = view.findViewById(R.id.date);
        String pattern = "MMMM d, yyyy \nh:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        //Setup view references
        imageDisplay = view.findViewById(R.id.imageDisplay);
        moodDisplay = view.findViewById(R.id.moodDisplay);
        socialDisplay = view.findViewById(R.id.socialDisplay);
        reasonDisplay = view.findViewById(R.id.reasonDisplay);
        locationDisplay = view.findViewById(R.id.locationDisplay);
        bg = view.findViewById(R.id.bg_vector);

        //Get parameters from Mood
        Bundle args = getArguments();
        moodObj = (MoodEvent) args.getSerializable("MoodEvent");
        argDate = (Date) args.getSerializable("date");
        double lat = args.getDouble("location_lat", -99999);
        double lon = args.getDouble("location_lon", -99999);
        if (lat != -99999 && lon != -99999) {
            argLocation = new GeoPoint(lat, lon);
        }

        //Set text after obtaining data
        moodDisplay.setText(moodObj.getEmotionalState().getDisplayName());
        socialDisplay.setText(moodObj.getSocialSituation().getDisplayName());
        String reasoning = moodObj.getReasoning();
        if (!reasoning.isEmpty()) {
            reasonDisplay.setText(moodObj.getReasoning());
        }
        String locationDescription = moodObj.getLocationDescription();
        String locationAddress = moodObj.getLocationAddress();
        if (locationDescription != null) {
            if (locationAddress != null) {
                locationDisplay.setText(String.format("%s,\n%s", locationDescription, locationAddress));
            } else {
                locationDisplay.setText(locationDescription);
            }
        } else {
            if (argLocation != null) {
                locationDisplay.setText(String.format(
                        Locale.US,
                        "[%.3f, %.3f]",
                        argLocation.getLatitude(),
                        argLocation.getLongitude()
                ));
            }
        }
        // TODO: set the text for location

        date.setText(sdf.format(argDate));
        bg.setColorFilter(moodObj.getEmotionalState().getColor());

        if(moodObj.getPhotographPath() != null){
            Picasso.get().load(moodObj.getPhotographPath()).into(imageDisplay);
        }

        return view;
    }

    /** Initialize the contents of the fragment's options menu.
     * @param menu The options menu in which you place your items.
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.mood_view_fragment_menu, menu);
        getActivity().setTitle("View Mood");
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * A hook that is called whenever an item in the options menu is selected.
     * This method changes the fragment to allow for the user to edit the selected mood
     * when the option is clicked.
     * @param item The menu item that was selected
     * @return boolean indicating state of whether option item was selected
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.editToggle:
                AddMoodFragment fragment = new AddMoodFragment ();
                Bundle args = new Bundle();
                args.putSerializable("MoodEvent",moodObj);
                args.putSerializable("date",argDate);
                args.putSerializable("location_lat", argLocation.getLatitude());
                args.putSerializable("location_lon", argLocation.getLongitude());

                args.putBoolean("edit",true);
                fragment.setArguments(args);
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();
        }

        return super.onOptionsItemSelected(item);
    }

}
