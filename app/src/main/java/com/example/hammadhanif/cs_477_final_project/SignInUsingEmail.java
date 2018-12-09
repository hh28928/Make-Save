package com.example.hammadhanif.cs_477_final_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInUsingEmail extends AppCompatActivity {

    EditText editEmail;
    EditText editPassword;
    Button signIn;

    DatabaseReference mDatabase;

    boolean userVerifiedEmail;

    private ProgressDialog progressDialog;

    //firebase object

    String userType;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_using_email);

        editEmail = findViewById(R.id.usersEmail);
        editPassword = findViewById(R.id.usersPassword);
        signIn = findViewById(R.id.UserSignIn);

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }



    //TODO: intent, check if same, allow user if in database to sign in
    public void UserSignIn(View view){

        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();

        if (email.equals("")) {


            Toast.makeText(getApplicationContext(),
                    "Please enter your email!",
                    Toast.LENGTH_LONG).show();

        }

        else if (password.equals("")) {


            Toast.makeText(getApplicationContext(),
                    "Please enter your password!",
                    Toast.LENGTH_LONG).show();

        }

        progressDialog.setMessage("Signing In...");
        progressDialog.show();
        WasEmailVerified();

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        //checks if the task is succesful:
                        //TODO: must check if the email belongs to user or provider
                        if(task.isSuccessful()){

                            //if user is trying to sign in with a virified email then start
                            //next activity
                            if(userVerifiedEmail == true){

                                //I want to check the database and see if he is user or provider
                                FirebaseUser user = firebaseAuth.getCurrentUser();

                                String user_id = user.getUid();

                                mDatabase.child("Users").child(user_id).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        userType = (String) dataSnapshot.child("User type is: ").getValue();
                                        if(userType.equals("Provider")){

                                            Intent intent = new Intent(getApplicationContext(), CurrentLocationMap.class);
                                            startActivity(intent);

                                        }
                                        else{
                                            //start the next activity
                                            Intent UserProfile = new Intent(getApplicationContext(), Request_Service.class);
                                            startActivity(UserProfile);
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                            }else {
                                Toast.makeText(getApplicationContext(),
                                        "Please verify your email!", Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                });
    }

    private void WasEmailVerified(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //this checks if the
        userVerifiedEmail = user.isEmailVerified();

    }

}