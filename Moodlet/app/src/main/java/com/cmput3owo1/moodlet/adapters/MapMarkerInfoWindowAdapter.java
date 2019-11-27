package com.cmput3owo1.moodlet.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmput3owo1.moodlet.R;
import com.cmput3owo1.moodlet.models.EmotionalState;
import com.cmput3owo1.moodlet.models.MoodEvent;
import com.cmput3owo1.moodlet.models.MoodEventAssociation;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.text.SimpleDateFormat;

public class MapMarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private String pattern = "MMMM d, yyyy - h:mm a";
    private SimpleDateFormat sdf = new SimpleDateFormat(pattern);

    private LayoutInflater inflater;

    private TextView usernameInfo;
    private TextView locationInfo;
    private TextView emotionInfo;
    private TextView dateInfo;
    private TextView timeInfo;
    private ImageView emoticonInfo;
    private ImageView infoWindowBar;

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

        usernameInfo = infoWindow.findViewById(R.id.usernameInfo);
        locationInfo = infoWindow.findViewById(R.id.locationInfo);
        emotionInfo = infoWindow.findViewById(R.id.emotionInfo);
        dateInfo = infoWindow.findViewById(R.id.dateInfo);
        timeInfo = infoWindow.findViewById(R.id.timeInfo);
        emoticonInfo = infoWindow.findViewById(R.id.emoticonInfo);
        infoWindowBar = infoWindow.findViewById(R.id.infoWindowBar);

        Object tagObject = marker.getTag();

        if (tagObject != null) {
            Log.e("MOODLET", tagObject.getClass().toString());
            if (tagObject.getClass().equals(MoodEvent.class)) {
                Log.e("MOODLET", "My Mood Event");
                MoodEvent moodEvent = (MoodEvent) tagObject;
                locationInfo.setText("Location placeholder");
                emotionInfo.setText(moodEvent.getEmotionalState().getDisplayName());
                dateInfo.setText(sdf.format(moodEvent.getDate()));
                timeInfo.setText("? h");
                setEmoticon(moodEvent.getEmotionalState());
                infoWindowBar.setColorFilter(moodEvent.getEmotionalState().getColor());
            } else if (tagObject.getClass().equals(MoodEventAssociation.class)) {
                Log.e("MOODLET", "Follower Mood Event");
                MoodEventAssociation moodEventAssociation = (MoodEventAssociation) tagObject;
                MoodEvent moodEvent = moodEventAssociation.getMoodEvent();
                usernameInfo.setText(String.format("@%s - ", moodEventAssociation.getUsername()));
                locationInfo.setText("Location placeholder");
                emotionInfo.setText(moodEvent.getEmotionalState().getDisplayName());
                dateInfo.setText(sdf.format(moodEvent.getDate()));
                timeInfo.setText("? h");
                setEmoticon(moodEvent.getEmotionalState());
                infoWindowBar.setColorFilter(moodEvent.getEmotionalState().getColor());
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
     * Helper function to set the ImageView to the correct emoticon for the mood
     * @param emotionalState The emotional state to set the emoticon to.
     */
    private void setEmoticon(EmotionalState emotionalState) {
        switch(emotionalState) {
            case SAD:
                emoticonInfo.setImageResource(R.drawable.ic_mood_sad);
                break;
            case ANGRY:
                emoticonInfo.setImageResource(R.drawable.ic_mood_angry);
                break;
            case CONFUSED:
                emoticonInfo.setImageResource(R.drawable.ic_mood_confused);
                break;
            case EXCITED:
                emoticonInfo.setImageResource(R.drawable.ic_mood_excited);
                break;
            case HAPPY:
                emoticonInfo.setImageResource(R.drawable.ic_mood_happy);
                break;
            case JEALOUS:
                emoticonInfo.setImageResource(R.drawable.ic_mood_jealous);
                break;
            case SCARED:
                emoticonInfo.setImageResource(R.drawable.ic_mood_scared);
                break;
        }
    }
}
