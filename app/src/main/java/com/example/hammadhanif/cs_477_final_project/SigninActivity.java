package com.example.hammadhanif.cs_477_final_project;

import android.content.Intent;
import android.hardware.camera2.TotalCaptureResult;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SigninActivity extends AppCompatActivity {

    EditText phone_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        phone_et = findViewById(R.id.phone_number);
    }

    public void onClickProvider(View view) {
        if (phone_et.getText().toString().equals("")) {
            Toast.makeText(this, "Phone Number Not Entered!", Toast.LENGTH_LONG).show();
            return;
        } else {
            String phone = phone_et.getText().toString();
            Intent providerIntent = new Intent(this, ProviderActivity.class);
            providerIntent.putExtra("PHONE", phone);
            startActivity(providerIntent);
        }
    }

    public void OnclickUser(View view) {
        Intent UserConfirmation = new Intent(this, User_Confirmation.class);
        startActivity(UserConfirmation);

    }


    //in case user wants to sign in through his email
    public void SignInUsingEmail(View view) {

        Intent signInThroughEmail = new Intent(this, SignInUsingEmail.class);
        startActivity(signInThroughEmail);

    }
}
