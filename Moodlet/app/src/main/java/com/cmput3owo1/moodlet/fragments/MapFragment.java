package com.cmput3owo1.moodlet.fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cmput3owo1.moodlet.R;
import com.cmput3owo1.moodlet.activities.LoginActivity;
import com.cmput3owo1.moodlet.services.UserService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;

/**
 * A fragment that holds a Google API MapView that displays the location of the user's mood event
 * history and optionally displays the location of the user's followers' most recent mood event.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "Moodlet";
    private static final LatLng EDMONTON = new LatLng(53.5444,-113.4909);

    private MapView mapView;
    private GoogleMap map;
    private UserService userService;

    /**
     * Called when the fragment is starting.
     * @param savedInstanceState Used to restore a fragment's previous state
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

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
        // Obtain the view to inflate
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        userService = new UserService();

        // MapView setup
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.logout:
                userService.logoutUser();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Initialize the contents of the Activity's standard options menu.
     * @param menu The options menu in which you place your items.
     * @param inflater The MenuInflater object that can be used to inflate any itmes in the menu
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.general_fragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
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
    }
}