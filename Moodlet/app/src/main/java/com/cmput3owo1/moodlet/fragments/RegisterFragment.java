package com.cmput3owo1.moodlet.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput3owo1.moodlet.R;
import com.cmput3owo1.moodlet.activities.LoginActvity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RegisterFragment extends Fragment {

    EditText fullname, username, email, password, confirmPassword;
    TextView loginText;
    Button registerButton;

    FirebaseAuth auth;
    FirebaseFirestore db;

    public RegisterFragment() {
        // Required empty public constructor
    }


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

                if(TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password) ) {
                    Toast.makeText(getActivity(), "All fields are required", Toast.LENGTH_SHORT).show();
                } else if (txt_password.length() < 6) {
                    Toast.makeText(getActivity(), "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
                } else {
                    register(collectionReference, txt_username, txt_email, txt_password, txt_fullname);
                }
            }
        });

        return registerFragmentView;
    }

    private void register(final CollectionReference collectionReference, final String username, final String email, final String password, final String fullname) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                HashMap<String, String> data = new HashMap<>();
                data.put("email", email);
                data.put("fullname", fullname);
                data.put("username", username);

                collectionReference.document(username).set(data)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(getActivity(), LoginActvity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getActivity(), "You can't register with this email or password", Toast.LENGTH_SHORT).show();
                            }
                        }
                });
            }
        });

    }
}
