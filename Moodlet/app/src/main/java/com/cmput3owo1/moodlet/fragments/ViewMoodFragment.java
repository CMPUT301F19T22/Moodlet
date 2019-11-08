package com.cmput3owo1.moodlet.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.cmput3owo1.moodlet.R;
import com.cmput3owo1.moodlet.models.MoodEvent;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A fragment that displays the details of a specific MoodEvent and optionally
 * allows for users to swap between an editable mode.
 */
public class ViewMoodFragment extends Fragment {

    private boolean editMode;

    private ImageView bg;
    private TextView moodDisplay;
    private TextView socialDisplay;
    private TextView date;
    private TextView reasonDisplay;
    private ImageView imageUpload;
    private Button toggleEdit;

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

        date = view.findViewById(R.id.date);
        bg = view.findViewById(R.id.bg_vector);
        String pattern = "MMMM d, yyyy \nh:mm a";
        imageUpload = view.findViewById(R.id.imageToUpload);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        moodDisplay = view.findViewById(R.id.moodDisplay);
        socialDisplay = view.findViewById(R.id.socialDisplay);
        reasonDisplay = view.findViewById(R.id.reasonDisplay);
        toggleEdit = view.findViewById(R.id.toggle_edit);

        Bundle args = getArguments();
        final MoodEvent moodObj = (MoodEvent) args.getSerializable("MoodEvent");
        final Date argDate = (Date) args.getSerializable("date");

        moodDisplay.setText(moodObj.getEmotionalState().getDisplayName());
        socialDisplay.setText(moodObj.getSocialSituation().getDisplayName());
        reasonDisplay.setText(moodObj.getReasoning());
        date.setText(sdf.format(argDate));
        bg.setColorFilter(moodObj.getEmotionalState().getColor());

        toggleEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddMoodFragment fragment = new AddMoodFragment ();
                Bundle args = new Bundle();
                args.putSerializable("MoodEvent",moodObj);
                args.putSerializable("date",argDate);
                args.putBoolean("edit",true);
                fragment.setArguments(args);

                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();
            }
        });
        return view;
    }

}
