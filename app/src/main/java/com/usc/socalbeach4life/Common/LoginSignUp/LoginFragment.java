package com.usc.socalbeach4life.Common.LoginSignUp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.usc.socalbeach4life.R;
import com.usc.socalbeach4life.User.MainActivity;

public class LoginFragment extends Fragment {

    TextInputLayout emailLayout, passLayout;
    EditText emails, pass;
    Button login, forgetPass;
    Animation animation;

    SharedPreferences pref;

    ProgressDialog loader;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener authStateListener;

    public LoginFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater
            , @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emailLayout = view.findViewById(R.id.textInputLayout);
        passLayout = view.findViewById(R.id.textInputLayout2);
        emails = view.findViewById(R.id.email_edit_text);
        pass = view.findViewById(R.id.pass_edit_text);
        login = view.findViewById(R.id.login_bt);
        forgetPass = view.findViewById(R.id.forget_bt);

        animation = AnimationUtils.loadAnimation(getContext(), R.anim.fragment_login_anim);
        emailLayout.setAnimation(animation);
        passLayout.setAnimation(animation);
        login.setAnimation(animation);
        forgetPass.setAnimation(animation);
        loader = new ProgressDialog(getActivity());
        mAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }
            }
        };
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emails.getText().toString().trim();
                final String password = pass.getText().toString().trim();
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("email",email);
                editor.putString("password",password);
                editor.commit();
                if(pref.contains("email") && pref.contains("password")){


                    if (TextUtils.isEmpty(email)) {
                        emails.setError("Email is required");
                    }
                    if (TextUtils.isEmpty(password)) {
                        pass.setError("Password is required");

                    } else {
                        loader.setMessage("Log in in Progress");
                        loader.setCanceledOnTouchOutside(false);
                        loader.show();

                        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Login Successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    startActivity(intent);

                                } else {
                                    Toast.makeText(getActivity(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                                }
                                loader.dismiss();
                            }
                        });

                    }
                }
            }
        });

    }

}
