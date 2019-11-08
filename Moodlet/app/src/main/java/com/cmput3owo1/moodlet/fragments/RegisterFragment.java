package com.cmput3owo1.moodlet.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput3owo1.moodlet.R;
import com.cmput3owo1.moodlet.activities.LoginActivity;
import com.cmput3owo1.moodlet.services.IUserServiceProvider;
import com.cmput3owo1.moodlet.services.UserService;


/**
 * A fragment that handles user registration. It takes in all the necessary data
 * and performs checks to make sure that the inputs are valid. Once the inputs
 * have been verified, the register button starts the user creation process.
 * The fragment also contains a clickable TextView that changes from a register
 * fragment to a login fragment
 */
public class RegisterFragment extends Fragment implements IUserServiceProvider.RegistrationListener {

    private EditText fullname, username, email, password, confirmPassword;
    private TextView loginText;
    private Button registerButton;

    private UserService userService = new UserService();

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

        //  Click listener to change to login fragment
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new LoginFragment());
                fragmentTransaction.commit();

            }
        });


        //  Click listener to start user registration
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_username = username.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                String txt_fullname = fullname.getText().toString();
                String txt_confirm_password = confirmPassword.getText().toString();

                if(txt_username.isEmpty()|| txt_fullname.isEmpty() || txt_email.isEmpty() || txt_password.isEmpty() || txt_confirm_password.isEmpty()) {
                    Toast.makeText(getActivity(), R.string.all_fields_required, Toast.LENGTH_SHORT).show();
                } else if (txt_password.length() < 6) {
                    Toast.makeText(getActivity(), R.string.password_too_short, Toast.LENGTH_SHORT).show();
                } else if (!txt_password.equals(txt_confirm_password)) {
                    Toast.makeText(getActivity(), R.string.password_does_not_match, Toast.LENGTH_SHORT).show();
                } else {
                    userService.validateUsernameAndCreateUser(txt_username, txt_email, txt_password, txt_fullname, RegisterFragment.this);
                }
            }
        });

        return registerFragmentView;
    }

    /**
     * Interface function to switch to Login activity upon successful user registration.
     */
    @Override
    public void onRegistrationSuccess() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    /**
     *   Interface function to show toast message for an existing account
     */
    @Override
    public void onRegistrationFailure() {
        Toast.makeText(getActivity(), R.string.account_already_exists, Toast.LENGTH_SHORT).show();
    }

    /**
     *   Interface function to show toast message when there is a problem accessing database
     */
    public void onDatabaseAccessFailure() {
        Toast.makeText(getActivity(), R.string.please_try_again_later, Toast.LENGTH_SHORT).show();
    }

    /**
     *  Interface function to show toast message when username is already taken
     */
    @Override
    public void onUsernameIsTaken() {
        Toast.makeText(getActivity(), R.string.username_taken, Toast.LENGTH_SHORT).show();
    }
}

