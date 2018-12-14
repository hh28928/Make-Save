package com.example.hammadhanif.cs_477_final_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
    private String authUserId, userType;
    private ProgressDialog progressDialog;

    //firebase object

    private FirebaseAuth firebaseAuth;
    DatabaseReference mDatabase;

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
    public void UserSignIn(View view) {
        String user = editEmail.getText().toString();
        String pass = editPassword.getText().toString();
        Log.d("TAG", "Username is: " + user + " Password is: " + pass);
        validate(user, pass);
    }

    private void validate(final String userName, String userPassword) {
        if (userName.equals("") || userPassword.equals("")) {
            Toast.makeText(this, "Either Username or Password not entered!", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            firebaseAuth.signInWithEmailAndPassword(userName, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        checkEmailVerification();
                    } else {
                        Toast.makeText(SignInUsingEmail.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    private void checkEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        authUserId = firebaseUser.getUid();
        Boolean emailFlag = firebaseUser.isEmailVerified();

        if(emailFlag){
            Toast.makeText(SignInUsingEmail.this, "Login Successful", Toast.LENGTH_SHORT).show();
            finish();
            mDatabase.child("Users").child(authUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userType = (String) dataSnapshot.child("User type is: ").getValue();
                    //if user is equal to provider then go to map activity
                    if(userType.equals("Provider")){

                        Intent intent = new Intent(getApplicationContext(), CurrentLocationMap.class);
                        startActivity(intent);

                    }
                    //if user is not provider, go to request...
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
        }else{
            Toast.makeText(this, "Verify your email", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }
}
