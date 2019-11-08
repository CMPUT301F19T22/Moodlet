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

public class ViewMoodFragment extends Fragment {

    boolean editMode;

    ImageView bg;
    TextView moodDisplay;
    TextView socialDisplay;
    TextView date;
    TextView reasonDisplay;
    ImageView imageUpload;
    String dateText;
    Button toggleEdit;
    Button confirmEdit;

    public ViewMoodFragment(){

    }


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

//        try{
//            Date dateObj = sdf.parse(argDate);
//            date.setText(sdf.format(dateObj));
//        }catch(ParseException e){
//            e.printStackTrace();
//        }
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
