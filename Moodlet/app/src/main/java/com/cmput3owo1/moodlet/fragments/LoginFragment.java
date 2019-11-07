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
import com.cmput3owo1.moodlet.activities.MainActivity;
import com.cmput3owo1.moodlet.services.UserService;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {

    EditText email, password;
    TextView signupText;
    Button loginButton;

    FirebaseAuth auth;


    /**
     * Callback function that redirects activity to the main activity when login is successful
     */
    UserService.CallbackFunction loginSuccessCallback = new UserService.CallbackFunction() {
        @Override
        public void callback() {

            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }
    };

    /**
     * Callback function that displays a toast message when login is unsuccessful
     */
    UserService.CallbackFunction loginFailureCallback = new UserService.CallbackFunction() {
        @Override
        public void callback() {
            Toast.makeText(getActivity(), "Authentication failed", Toast.LENGTH_SHORT).show();
        }
    };

    public LoginFragment() {
        // Required empty public constructor
    }
    /**
     * This function is called to do initial creation of a fragment.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return none.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser() != null){
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }
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
        View loginFragmentView = inflater.inflate(R.layout.fragment_login, container, false);

        signupText = loginFragmentView.findViewById(R.id.sign_up_text);
        email = loginFragmentView.findViewById(R.id.edit_text_email);
        password = loginFragmentView.findViewById(R.id.edit_text_password);
        loginButton = loginFragmentView.findViewById(R.id.btn_login);

        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new RegisterFragment());
                fragmentTransaction.commit();

            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                if(txt_email.isEmpty() || txt_password.isEmpty()) {
                    Toast.makeText(getActivity(), "All fields are required", Toast.LENGTH_SHORT).show();
                } else {
                    UserService.loginUser(auth, txt_email, txt_password, loginSuccessCallback, loginFailureCallback);
                }
            }
        });

        // Inflate the layout for this fragment
        return loginFragmentView;
    }
}
