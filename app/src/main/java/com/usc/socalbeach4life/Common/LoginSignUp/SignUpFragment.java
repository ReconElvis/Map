package com.usc.socalbeach4life.Common.LoginSignUp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.usc.socalbeach4life.R;
import com.usc.socalbeach4life.User.MainActivity;
import com.usc.socalbeach4life.R;

import java.util.HashMap;

public class SignUpFragment extends Fragment {

    EditText username, emails, pass;
    Button signup;
    ProgressDialog loader;
    FirebaseAuth mAuth;
    DatabaseReference userDatabaseRef;

    public SignUpFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater
            , @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        username = view.findViewById(R.id.username_edit_text);
        emails = view.findViewById(R.id.email_edit_text);
        pass = view.findViewById(R.id.pass_edit_text);

        signup = view.findViewById(R.id.signup_bt);
        loader=new ProgressDialog(getActivity());

        mAuth=FirebaseAuth.getInstance();
        signup.setOnClickListener(View->{
            final String email=emails.getText().toString().trim();
            final String password=pass.getText().toString().trim();
            final String fullName=username.getText().toString().trim();


            if(TextUtils.isEmpty(email)){
                emails.setError("Email is Required!");
                return;
            }
            if(TextUtils.isEmpty(password)){
                pass.setError("Password is Required!");
                return;
            }
            if(TextUtils.isEmpty(fullName)) {
                username.setError("Name is Required!");
                return;
            }


            else {
                loader.setMessage("Registering you....");
                loader.setCanceledOnTouchOutside(false);
                loader.show();
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            String error = task.getException().toString();
                            Toast.makeText(getActivity(), "Error" + error, Toast.LENGTH_SHORT).show();


                        } else {
                            String currentUserId = mAuth.getCurrentUser().getUid();
                            userDatabaseRef = FirebaseDatabase.getInstance().getReference().child("sign").child(currentUserId);
                            HashMap userInfo = new HashMap();
                            userInfo.put("id", currentUserId);
                            userInfo.put("name", fullName);
                            userInfo.put("email", email);
                            userInfo.put("type", "sign");


                            userDatabaseRef.updateChildren(userInfo).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getActivity(), "Data Set Successfully", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(getActivity(), MainActivity.class);
                                        startActivity(intent);
                                        loader.dismiss();

                                    } else {
                                        Toast.makeText(getActivity(), task.getException().toString(), Toast.LENGTH_SHORT).show();

                                        loader.dismiss();
                                    }
                                }
                            });

                        }
                    }
                });
            }

    });
}
}
