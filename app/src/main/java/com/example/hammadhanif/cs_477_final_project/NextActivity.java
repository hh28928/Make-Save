package com.example.hammadhanif.cs_477_final_project;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NextActivity extends AppCompatActivity{

    Button register;

    EditText firstName;

    EditText lastName;

    EditText email;

    static EditText dateOfBirth;

    EditText password;

    private ProgressDialog progressDialog;

    //Firebase authentication object.

    private FirebaseAuth firebaseAuth;



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



        /*
        since this is an internet operation it will take time
        so we will make a progress bar...
         */

        progressDialog = new ProgressDialog(this);


        //firebase initializing:

        firebaseAuth = FirebaseAuth.getInstance();


    }



    //this function checks if the email is in the right format
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    //this function saves the info to the database:
    private void registerUser(){

        String fName = firstName.getText().toString();
        String lName = lastName.getText().toString();
        final String Pass = password.getText().toString();
        String DOB = dateOfBirth.getText().toString();
        String userEmail = email.getText().toString();




        //once we start this function, a progress dialog will appear.

        progressDialog.setMessage("Registering User...");
        progressDialog.show();




        /*
         * use firebase to register the user:
         * we will also attach a listener to check if task is successful.
         * if the task is successful then start the next activity
         */



        firebaseAuth.createUserWithEmailAndPassword(userEmail, Pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if (task.isSuccessful()){

                            progressDialog.hide();

                            Toast.makeText(getApplicationContext(),
                                    "Registered Successfully.",
                                    Toast.LENGTH_LONG).show();

                            //start the next activity
                            Intent bankInfoInent = new Intent(getApplicationContext(), BankInfoActivity.class);
                            startActivity(bankInfoInent);

                        } else {

                            Toast.makeText(getApplicationContext(),
                                    "Couldn't Register, please try again!",
                                    Toast.LENGTH_LONG).show();

                        }

                    }
                });


    }

    public void onClickNext(View view) {


        String userEmail = email.getText().toString();

        //variable for the format of the email

        boolean rightFormat = isEmailValid(userEmail);

        boolean fieldsFilled = true;

        boolean passwordLengthFine = true;

        //checking if some fields NOT ALL OF THEM, are empty..

        if (email.getText().toString().equals("") ||
                firstName.getText().toString().equals("") ||
                lastName.getText().toString().equals("") ||
                dateOfBirth.getText().toString().equals("") ||
                password.getText().toString().equals("")
                ) {

            Toast.makeText(getApplicationContext(),
                    "Please add the missing information!",
                    Toast.LENGTH_LONG).show();

            fieldsFilled = false;
        }


        //checking the length of the password!
        if (password.getText().toString().length() < 6) {

            Toast.makeText(getApplicationContext(),
                    "Your Password is too short",
                    Toast.LENGTH_LONG).show();

            passwordLengthFine = false;


        }


        if (rightFormat == false) {

            Toast.makeText(getApplicationContext(),
                    "please write a correct email",
                    Toast.LENGTH_LONG).show();

        }

        rightFormat = isEmailValid(userEmail);

        if (passwordLengthFine == true && fieldsFilled == true && rightFormat == true) {

            registerUser();

        }
    }


    }