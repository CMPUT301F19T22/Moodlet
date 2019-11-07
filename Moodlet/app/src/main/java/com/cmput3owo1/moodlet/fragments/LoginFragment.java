package com.cmput3owo1.moodlet.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.cmput3owo1.moodlet.activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {

    EditText email, password;
    TextView signupText;
    Button loginButton;

    FirebaseAuth auth;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View loginFragmentView = inflater.inflate(R.layout.fragment_login, container, false);

        signupText = loginFragmentView.findViewById(R.id.sign_up_text);
        email = loginFragmentView.findViewById(R.id.edit_text_email);
        password = loginFragmentView.findViewById(R.id.edit_text_password);
        loginButton = loginFragmentView.findViewById(R.id.btn_login);

        auth = FirebaseAuth.getInstance();

        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new RegisterFragment());
                fragmentTransaction.addToBackStack(null);
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
                    login(txt_email, txt_password);
                }
            }
        });

        // Inflate the layout for this fragment
        return loginFragmentView;
    }

    public void login(String txt_email, String txt_password){
        auth.signInWithEmailAndPassword(txt_email, txt_password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful()) {

                           Intent intent = new Intent(getActivity(), MainActivity.class);
                           startActivity(intent);

                       } else {
                           Toast.makeText(getActivity(), "Authentication failed", Toast.LENGTH_SHORT).show();
                       }
                   }
               }
        );
    }
}
