package com.example.hammadhanif.cs_477_final_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInUsingEmail extends AppCompatActivity {

    EditText editEmail;
    EditText editPassword;
    Button signIn;

    private ProgressDialog progressDialog;

    //firebase object

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_using_email);

        editEmail = findViewById(R.id.usersEmail);
        editPassword = findViewById(R.id.usersPassword);
        signIn = findViewById(R.id.UserSignIn);

        progressDialog = new ProgressDialog(this);
        //initialize the firebase object:
        firebaseAuth = FirebaseAuth.getInstance();

    }



    //TODO: intent, check if same, allow user if in database to sign in
    public void UserSignIn(View view) {
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        if (email.equals("")) {


            Toast.makeText(getApplicationContext(),
                    "Please enter your email!",
                    Toast.LENGTH_LONG).show();

        } else if (password.equals("")) {


            Toast.makeText(getApplicationContext(),
                    "Please enter your password!",
                    Toast.LENGTH_LONG).show();

        }

        //if email and password are not empty then
        //display the progress dialog

        progressDialog.setMessage("Signing In...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        //checks if the task is succesful:
                        //TODO: must check if the email belongs to user or provider
                        if (task.isSuccessful()) {

                            //start the next activity
                            Intent UserProfile = new Intent(getApplicationContext(), Request_Service.class);
                            startActivity(UserProfile);

                        }
                    }
                });

    }
    }

