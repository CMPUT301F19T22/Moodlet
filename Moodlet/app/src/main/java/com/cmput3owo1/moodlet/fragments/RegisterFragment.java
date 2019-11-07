package com.cmput3owo1.moodlet.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput3owo1.moodlet.R;
import com.cmput3owo1.moodlet.activities.LoginActivity;
import com.cmput3owo1.moodlet.services.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterFragment extends Fragment {

    EditText fullname, username, email, password, confirmPassword;
    TextView loginText;
    Button registerButton;

    FirebaseAuth auth;
    FirebaseFirestore db;

    /**
     * Callback function that redirects to login fragment when registration is successful
     */
    UserService.CallbackFunction registrationSuccessCallback = new UserService.CallbackFunction() {
        @Override
        public void callback() {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }
    };

    /**
     * Callback function that displays a toast message when login is unsuccessful
     */
    UserService.CallbackFunction registrationFailureCallback = new UserService.CallbackFunction() {
        @Override
        public void callback() {
            Toast.makeText(getActivity(), "You can't register with this email or password", Toast.LENGTH_SHORT).show();
        }
    };

    public RegisterFragment() {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        View registerFragmentView = inflater.inflate(R.layout.fragment_register, container, false);

        fullname = registerFragmentView.findViewById(R.id.full_name);
        username = registerFragmentView.findViewById(R.id.username);
        email = registerFragmentView.findViewById(R.id.email);
        password = registerFragmentView.findViewById(R.id.password);
        confirmPassword = registerFragmentView.findViewById(R.id.confirm_password);
        registerButton = registerFragmentView.findViewById(R.id.btn_register);
        loginText = registerFragmentView.findViewById(R.id.swap_to_login_text_view);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        final CollectionReference collectionReference = db.collection("users");

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new LoginFragment());
                fragmentTransaction.commit();

            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_username = username.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                String txt_fullname = fullname.getText().toString();
                String txt_confirm_password = confirmPassword.getText().toString();

                if(txt_username.isEmpty()|| txt_email.isEmpty() || txt_password.isEmpty() || txt_confirm_password.isEmpty()) {
                    Toast.makeText(getActivity(), "All fields are required", Toast.LENGTH_SHORT).show();
                } else if (txt_password.length() < 6) {
                    Toast.makeText(getActivity(), "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
                } else if (!txt_password.equals(txt_confirm_password)) {
                    Toast.makeText(getActivity(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else {
                    UserService.registerUser(auth, collectionReference, txt_username, txt_email, txt_password, txt_fullname, registrationSuccessCallback, registrationFailureCallback);
                }
            }
        });

        return registerFragmentView;
    }
}

