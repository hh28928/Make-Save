package com.example.hammadhanif.cs_477_final_project;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;

import java.text.SimpleDateFormat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.Locale;

public class NextActivity extends AppCompatActivity {

    Button register;
    EditText firstName;
    EditText lastName;
    EditText email, address_et, city_state;
    static EditText dateOfBirth;
    EditText password;
    private ProgressDialog progressDialog;

    //Firebase authentication object.
    private FirebaseAuth firebaseAuth;
    AwesomeValidation awesomeValidation;
    Calendar myCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        //find the ids that will be saved in the database for now
        //TODO: the address should be saved as well.

        register = findViewById(R.id.next_btn);
        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        email = findViewById(R.id.editText);
        dateOfBirth = findViewById(R.id.date_of_birth);
        password = findViewById(R.id.editText8);
        address_et = findViewById(R.id.address);
        /*
        since this is an internet operation it will take time
        so we will make a progress bar...
         */

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        progressDialog = new ProgressDialog(this);
        String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";
        awesomeValidation.addValidation(NextActivity.this,R.id.first_name, "[a-zA-Z\\s]+",R.string.first_name_Err);
        awesomeValidation.addValidation(NextActivity.this,R.id.last_name, "[a-zA-Z\\s]+",R.string.last_name_Err);
        awesomeValidation.addValidation(NextActivity.this,R.id.editText,android.util.Patterns.EMAIL_ADDRESS,R.string.email_Err);
        awesomeValidation.addValidation(NextActivity.this,R.id.editText8,regexPassword,R.string.password_Err);

        //firebase initializing:
        firebaseAuth = FirebaseAuth.getInstance();
        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        dateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(NextActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateOfBirth.setText(sdf.format(myCalendar.getTime()));
    }

    //this function saves the info to the database:
    private void registerUser() {

        String fName = firstName.getText().toString();
        String lName = lastName.getText().toString();
        final String Pass = password.getText().toString();
        String DOB = dateOfBirth.getText().toString();
        String userEmail = email.getText().toString();


        //once we start this function, a progress dialog will appear.

        progressDialog.setMessage("Registering User...");
        progressDialog.show();

    }

    public void onClickNext(View view) {
        if (awesomeValidation.validate()) {
            //Upload data to the database
            String user_email = email.getText().toString().trim();
            String user_password = password.getText().toString().trim();

            Log.d("TAG", "" + user_email + " " + user_password);

            firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        sendEmailVerification();
                    } else {
                        Toast.makeText(NextActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(NextActivity.this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(NextActivity.this, "Successfully Registered, Verification mail sent!", Toast.LENGTH_LONG).show();
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(NextActivity.this, MainActivity.class));
                    }else{
                        Toast.makeText(NextActivity.this, "Verification mail has'nt been sent!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}