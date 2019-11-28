package com.cmput3owo1.moodlet.fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.cmput3owo1.moodlet.R;
import com.cmput3owo1.moodlet.adapters.MapMarkerInfoWindowAdapter;
import com.cmput3owo1.moodlet.models.MoodEvent;
import com.cmput3owo1.moodlet.models.MoodEventAssociation;
import com.cmput3owo1.moodlet.services.IMoodEventServiceProvider;
import com.cmput3owo1.moodlet.services.MoodEventService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

/**
 * A fragment that holds a Google API MapView that displays the location of the user's mood event
 * history and optionally displays the location of the user's followers' most recent mood event.
 */
public class MapFragment extends Fragment implements
        OnMapReadyCallback,
        IMoodEventServiceProvider.OnMoodHistoryUpdateListener,
        IMoodEventServiceProvider.OnFeedUpdateListener {

    private static final String TAG = "Moodlet";
    private static final LatLng EDMONTON = new LatLng(53.5444,-113.4909);
    private static final float MARKER_COLOR = 207.61f; // Hue
    private static final float FOLLOWER_MARKER_COLOR = 164f;

    private LayoutInflater inflater;

    private IMoodEventServiceProvider moodEventService;

    private CheckBox showFollowersCheckbox;

    private ArrayList<MoodEvent> myMoodEvents;
    private ArrayList<MoodEventAssociation> followerMoodEvents;

    private MapView mapView;
    private GoogleMap map;

    /**
     * This function is called to have the fragment instantiate its user interface view.
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container  If non-null, this is the parent view that the fragment's UI should be attached to. The fragment should not add the view itself, but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Set the layout inflater
        this.inflater = inflater;

        // Obtain the view to inflate
        View view = this.inflater.inflate(R.layout.fragment_map, container, false);

        // Get checkbox
        showFollowersCheckbox = view.findViewById(R.id.showFollowers);

        // Mood event lists setup
        myMoodEvents = new ArrayList<>();
        followerMoodEvents = new ArrayList<>();

        // Setup mood event service
        moodEventService = new MoodEventService();

        // MapView setup
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        return view;
    }

    /**
     * Forwarding the MapView's onStart() lifecycle method
     */
    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    /**
     * Forwarding the MapView's onResume() lifecycle method
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * Forwarding the MapView's onPause() lifecycle method
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * Forwarding the MapView's onStop() lifecycle method
     */
    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    /**
     * Forwarding the MapView's onDestroy() lifecycle method
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    /**
     * Forwarding the MapView's onSaveInstanceState() lifecycle method
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * Forwarding the MapView's onLowMemory() lifecycle method
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    /**
     * Called when the map is ready to be used after calling getMapAsync().
     * @param map A non-null instance of a GoogleMap
     */
    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        this.map.getUiSettings().setMapToolbarEnabled(false);
        this.map.setInfoWindowAdapter(new MapMarkerInfoWindowAdapter(this.inflater));
        // TODO: Set camera to last known/recorded location
        // this.map.setMyLocationEnabled(true);
        // this.map.setOnMyLocationButtonClickListener(mapView.getContext());
        // this.map.setOnMyLocationClickListener(mapView.getContext());

        // Set the style of the map
        try {
            boolean success = this.map.setMapStyle(MapStyleOptions.loadRawResourceStyle(mapView.getContext(), R.raw.map_style_json));
            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        // TODO: change this to the last known location
        this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(EDMONTON, 12));

        // Set the update callbacks
        moodEventService.getMoodHistoryUpdates(this);
        moodEventService.getFeedUpdates(this);

        // Set a click listener for the checkbox to show followers' mood events
        showFollowersCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateMap(showFollowersCheckbox.isChecked());
            }
        });
    }

    /**
     * Gets called when there is an update to the user's Mood Event History
     * @param newHistory The updated Mood Event History
     */
    @Override
    public void onMoodHistoryUpdate(ArrayList<MoodEvent> newHistory) {
        myMoodEvents.clear();
        myMoodEvents.addAll(newHistory);
        updateMap(showFollowersCheckbox.isChecked());
    }

    /**
     * Gets called when there is an update to the user's Feed
     * @param newFeed The updated feed
     */
    @Override
    public void onFeedUpdate(ArrayList<MoodEventAssociation> newFeed) {
        followerMoodEvents.clear();
        followerMoodEvents.addAll(newFeed);
        updateMap(showFollowersCheckbox.isChecked());
    }

    /**
     * This function updates the markers shown on the map
     * @param showFollowers The flag to show/hide the follower markers
     */
    private void updateMap(boolean showFollowers) {
        // Clear all markers
        map.clear();

        // Add markers for user's mood events
        for (MoodEvent moodEvent : myMoodEvents) {
            GeoPoint location = moodEvent.getLocation();
            if (location != null) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                Marker marker = map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.defaultMarker(MARKER_COLOR))
                        .alpha(0.8f)
                );
                marker.setTag(moodEvent);
            }
        }

        // Add markers for follower(s)' mood events
        if (showFollowers) {
            for (MoodEventAssociation moodEventAssociation : followerMoodEvents) {
                GeoPoint location = moodEventAssociation.getMoodEvent().getLocation();
                if (location != null) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    Marker marker = map.addMarker(new MarkerOptions()
                            .position(latLng)
                            .icon(BitmapDescriptorFactory.defaultMarker(FOLLOWER_MARKER_COLOR))
                            .alpha(0.8f)
                    );
                    marker.setTag(moodEventAssociation);
                }
            }
        }
    }
}