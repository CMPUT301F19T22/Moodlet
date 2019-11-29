package com.cmput3owo1.moodlet.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cmput3owo1.moodlet.R;
import com.cmput3owo1.moodlet.models.MoodEvent;
import com.cmput3owo1.moodlet.models.MoodEventAssociation;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.cmput3owo1.moodlet.utils.Utils.getMoodEmoticon;
import static com.cmput3owo1.moodlet.utils.Utils.getTimeDifference;

/**
 * This class provides customized rendering of info windows for markers on the Google Map object. The
 * methods of this provider are called when it is time to show an info window for a marker.
 */
public class MapMarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private String pattern = "MMMM d, yyyy - h:mm a";
    private SimpleDateFormat sdf = new SimpleDateFormat(pattern);

    private LayoutInflater inflater;

    private LinearLayout infoWindowTitle;
    private TextView usernameInfo;
    private TextView locationInfo;
    private TextView emotionInfo;
    private TextView dateInfo;
    private TextView timeInfo;
    private ImageView emoticonInfo;
    private ImageView infoWindowBar;

    /**
     * Constructor for the MapMarkerInfoWindowAdapter
     * @param inflater The LayoutInflater object that can be used to inflate any views in the parent fragment.
     */
    public MapMarkerInfoWindowAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    /**
     * This function customizes what is displayed in the map marker's info window.
     * @param marker The marker for which an info window is being populated.
     * @return A custom info window for marker, or null to use the default info window frame with
     * custom contents.
     */
    @Override
    public View getInfoWindow(Marker marker) {
        View infoWindow = inflater.inflate(R.layout.marker_info_window, null);

        infoWindowTitle = infoWindow.findViewById(R.id.infoWindowTitle);
        usernameInfo = infoWindow.findViewById(R.id.usernameInfo);
        locationInfo = infoWindow.findViewById(R.id.locationInfo);
        emotionInfo = infoWindow.findViewById(R.id.emotionInfo);
        dateInfo = infoWindow.findViewById(R.id.dateInfo);
        timeInfo = infoWindow.findViewById(R.id.timeInfo);
        emoticonInfo = infoWindow.findViewById(R.id.emoticonInfo);
        infoWindowBar = infoWindow.findViewById(R.id.infoWindowBar);

        Object tagObject = marker.getTag();

        if (tagObject != null) {
            if (tagObject.getClass().equals(MoodEvent.class)) {
                MoodEvent moodEvent = (MoodEvent) tagObject;
                setInfo(moodEvent, false);

            } else if (tagObject.getClass().equals(MoodEventAssociation.class)) {
                MoodEventAssociation moodEventAssociation = (MoodEventAssociation) tagObject;
                MoodEvent moodEvent = moodEventAssociation.getMoodEvent();
                if (moodEventAssociation.getMoodEvent().getLocationDescription() == null) {
                    usernameInfo.setText(String.format("@%s", moodEventAssociation.getUsername()));
                } else {
                    usernameInfo.setText(String.format("@%s - ", moodEventAssociation.getUsername()));
                }
                setInfo(moodEvent, true);
            }
            return infoWindow;
        }

        return null;
    }

    /**
     * This function customizes what is displayed in the map marker's info window but also
     * keeps the default info window frame and background. The implementation for this method is
     * not needed since {@link MapMarkerInfoWindowAdapter#getInfoWindow(Marker)} will be called.
     * @param marker The marker for which an info window is being populated.
     * @return A custom view to display as contents in the info window for marker, or null
     * to use the default content rendering instead.
     */
    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    /**
     * Sets the information on the info window from the given mood event.
     * @param moodEvent The mood event to obtain the information from.
     * @param isFollower A flag to indicate whether the mood event information is from a follower
     */
    private void setInfo(MoodEvent moodEvent, boolean isFollower) {
        String locationDescription = moodEvent.getLocationDescription();
        locationInfo.setText(locationDescription);
        if (!isFollower && locationDescription == null) {
            infoWindowTitle.setVisibility(View.GONE);
        }
        emotionInfo.setText(moodEvent.getEmotionalState().getDisplayName());
        Date date = moodEvent.getDate();
        dateInfo.setText(sdf.format(date));
        timeInfo.setText(getTimeDifference(date));
        emoticonInfo.setImageResource(getMoodEmoticon(moodEvent.getEmotionalState()));
        infoWindowBar.setColorFilter(moodEvent.getEmotionalState().getColor());
    }

}
