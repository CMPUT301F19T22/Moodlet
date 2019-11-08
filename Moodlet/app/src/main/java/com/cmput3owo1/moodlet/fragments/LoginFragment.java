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
import com.cmput3owo1.moodlet.services.IUserServiceProvider;
import com.cmput3owo1.moodlet.services.UserService;

/**
 * A fragment that handles user login. It takes in email and password in EditTexts
 * and makes sure that the inputs are valid. Once the inputs have been verified,
 * the login button attempts to log the user in. The fragment also contains a clickable
 * TextView that changes from a login fragment to a register fragment
 */
public class LoginFragment extends Fragment implements IUserServiceProvider.LoginListener {

    EditText email, password;
    TextView signupText;
    Button loginButton;

    UserService userService = new UserService();

    /**
     * This function is called to do initial creation of a fragment.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//         if(userService.hasPreviousLogin()){
//             Intent intent = new Intent(getActivity(), MainActivity.class);
//             startActivity(intent);
//         }
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
                    Toast.makeText(getActivity(), R.string.all_fields_required, Toast.LENGTH_SHORT).show();
                } else {
                    userService.loginUser(txt_email, txt_password, LoginFragment.this);
                }
            }
        });

        // Inflate the layout for this fragment
        return loginFragmentView;
    }
    /**
     * Interface function to switch to main activity upon successful login
     */
    @Override
    public void onLoginSuccess() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

    /**
     * Interface function to show a toast message for unsuccessful login
     */
    @Override
    public void onLoginFailure() {
        Toast.makeText(getActivity(), R.string.authentication_failed, Toast.LENGTH_SHORT).show();
    }
}
