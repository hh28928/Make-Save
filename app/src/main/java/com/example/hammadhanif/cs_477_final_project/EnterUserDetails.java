package com.example.hammadhanif.cs_477_final_project;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import com.basgeekball.awesomevalidation.AwesomeValidation;
//import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EnterUserDetails extends AppCompatActivity {

    EditText edit_first_name, edit_email, edit_last_name, edit_dob, edit_pass, edit_street, edit_city_state;
    EditText edit_phone_number;

    Button btn_next;
    String email, name, password;
    private FirebaseAuth firebaseAuth;
    //AwesomeValidation awesomeValidation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_user_details);
        //awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        setupUIViews();

        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void setupUIViews() {
        edit_pass = (EditText) findViewById(R.id.pass);
        edit_email = (EditText) findViewById(R.id.et_email);
        edit_phone_number = (EditText) findViewById(R.id.phone_number);
        edit_first_name = (EditText) findViewById(R.id.first_name);
        edit_last_name = (EditText) findViewById(R.id.last_name);
        edit_street = (EditText) findViewById(R.id.address);
        edit_city_state = (EditText) findViewById(R.id.city_state);
        edit_dob = (EditText) findViewById(R.id.date_of_birth);
        btn_next = (Button) findViewById(R.id.next_btn);

//        String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";
//        awesomeValidation.addValidation(EnterUserDetails.this, R.id.first_name, "[a-zA-Z\\s]+", R.string.first_name_Err);
//        awesomeValidation.addValidation(EnterUserDetails.this, R.id.last_name, "[a-zA-Z\\s]+", R.string.last_name_Err);
//        awesomeValidation.addValidation(EnterUserDetails.this, R.id.et_email, android.util.Patterns.EMAIL_ADDRESS, R.string.email_Err);
//        // awesomeValidation.addValidation(EnterUserDetails.this,R.id.phno, RegexTemplate.TELEPHONE,R.string.phoneerr);
//        awesomeValidation.addValidation(EnterUserDetails.this, R.id.pass, regexPassword, R.string.password_Err);
//        //Address
//        awesomeValidation.addValidation(EnterUserDetails.this, R.id.address, "[0-9a-zA-Z\\s]+", R.string.address_Err);
    }


//    public void onclicknext(View view) {
//        //if (awesomeValidation.validate()) {
//            //Upload data to the database
//            String user_email = edit_email.getText().toString().trim();
//            String user_password = edit_pass.getText().toString().trim();
//
//            firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//
//                    if (task.isSuccessful()) {
//                        sendEmailVerification();
//                    } else {
//                        Toast.makeText(EnterUserDetails.this, "Registration Failed", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        } else {
//            Toast.makeText(EnterUserDetails.this, "Error", Toast.LENGTH_SHORT).show();
//        }
//    }

    private void sendEmailVerification() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser!=null) {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(EnterUserDetails.this, "Successfully Registered, Verification mail sent!", Toast.LENGTH_LONG).show();
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(EnterUserDetails.this, MainActivity.class));
                    }else{
                        Toast.makeText(EnterUserDetails.this, "Verification mail has'nt been sent!", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }

    public void onclickServicee(View view) {
        Intent providerIntent = new Intent(this, Request_Service.class);
        startActivity(providerIntent);
    }

    public void onclicksignin(View view) {
        Intent providerIntent = new Intent(this, SigninActivity.class);
        startActivity(providerIntent);
    }
}
