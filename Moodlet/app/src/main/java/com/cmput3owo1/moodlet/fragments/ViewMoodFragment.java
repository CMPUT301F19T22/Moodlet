package com.cmput3owo1.moodlet.fragments;

import android.os.Bundle;
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

import java.text.SimpleDateFormat;
import java.util.Date;

public class ViewMoodFragment extends Fragment {

    boolean editMode;

    ImageView bg;
    TextView moodDisplay;
    TextView socialDisplay;
    TextView date;
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
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        dateText = sdf.format(new Date());
        date.setText(dateText);
        imageUpload = view.findViewById(R.id.imageToUpload);

        moodDisplay = view.findViewById(R.id.moodDisplay);
        socialDisplay = view.findViewById(R.id.socialDisplay);

        toggleEdit = view.findViewById(R.id.toggle_edit);
        confirmEdit = view.findViewById(R.id.confirm_edit);

        toggleEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new AddMoodFragment());
                fragmentTransaction.commit();
            }
        });




        return view;
    }

}
