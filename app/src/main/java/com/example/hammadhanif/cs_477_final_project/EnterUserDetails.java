package com.example.hammadhanif.cs_477_final_project;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

//import com.basgeekball.awesomevalidation.AwesomeValidation;
//import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EnterUserDetails extends AppCompatActivity {

    Button register;
    EditText firstName;
    EditText lastName;
    EditText email;
    EditText location;
    EditText phoneNumber;

    EditText dateOfBirth;

    EditText password;

    private ProgressDialog progressDialog;

    //Firebase authentication object.

    FirebaseAuth firebaseAuth;


    String fName;
    String lName;
    String Pass;
    String DOB;
    String userEmail;
    String userLocation;
    String userPhone;
    Calendar myCalendar;

    final String userType = "Costumer";

    private FirebaseAuth.AuthStateListener firebaseAuthListner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_user_details);
        //awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        //calander


        //find the ids that will be saved in the database for now

        //TODO: the address should be saved as well.

        register = findViewById(R.id.next_btn);
        email = findViewById(R.id.et_email);
        password = findViewById(R.id.pass);

        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        dateOfBirth = findViewById(R.id.date_of_birth);
        location = findViewById(R.id.address);

        phoneNumber = findViewById(R.id.phone_number);

        /*
        since this is an internet operation it will take time
        so we will make a progress bar...
         */

        progressDialog = new ProgressDialog(this);


        firebaseAuth = FirebaseAuth.getInstance();


        //going to set an onclick listen for the register button.
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //saving the email and password
                Pass = password.getText().toString();
                userEmail = email.getText().toString();

                if (Pass.equals("") || userEmail.equals("")) {
                    Toast.makeText(getApplicationContext(),
                            "Please add the missing information!",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                //once we start this function, a progress dialog will appear.
                progressDialog.setMessage("Registering User...");
                progressDialog.show();

                /*
                 * use firebase to register the user:
                 * we will also attach a listener to check if task is successful.
                 * if the task is successful then start the next activity
                 */

                firebaseAuth.createUserWithEmailAndPassword(userEmail, Pass)
                        .addOnCompleteListener(EnterUserDetails.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    progressDialog.hide();

                                    //if task is successful then we will send the user
                                    //an email to be verified.
                                    sendEmailVerification();

                                    Toast.makeText(getApplicationContext(),
                                            "Registered Successfully.",
                                            Toast.LENGTH_LONG).show();

                                    String userEmail = email.getText().toString();

                                    //variable for the format of the email

                                    boolean rightFormat = isEmailValid(userEmail);

                                    boolean fieldsFilled = true;

                                    boolean passwordLengthFine = true;

                                    //checking if some fields NOT ALL OF THEM, are empty..
                                    //TODO
                                    //FIX THE APP CRASHING
                                    if (email.getText().toString().equals("") ||
                                            firstName.getText().toString().equals("") ||
                                            lastName.getText().toString().equals("") ||
                                            dateOfBirth.getText().toString().equals("") ||
                                            password.getText().toString().equals("") ||
                                            location.getText().toString().equals("") ||
                                            phoneNumber.getText().toString().equals("")


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

                                        /*
                                        when the account is created, we want to save his info
                                        such as name, dob, address
                                        */

                                        //saving the fname, lastname and DOB...
                                        fName = firstName.getText().toString();
                                        lName = lastName.getText().toString();
                                        DOB = dateOfBirth.getText().toString();
                                        userLocation = location.getText().toString();
                                        userPhone = phoneNumber.getText().toString();


                                        FirebaseUser user = firebaseAuth.getCurrentUser();
                                        //check if there is a user:

                                        String user_id = user.getUid();

                                        //create a database reference:
                                        DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference()
                                                .child("Users").child(user_id);

                                        Map newPost = new HashMap();

                                        newPost.put("First Name", fName);
                                        newPost.put("Last Name", lName);
                                        newPost.put("Date of Birth", DOB);
                                        newPost.put("Location", userLocation);
                                        newPost.put("user's phone number", userPhone);
                                        newPost.put("User type is: ", userType);

                                        //now set the value to current user databade
                                        current_user_db.setValue(newPost);

                                        //start the next activity
                                        Intent bankInfoInent = new Intent(getApplicationContext(), Request_Service.class);
                                        startActivity(bankInfoInent);
                                    }

                                } else {

                                    Toast.makeText(getApplicationContext(),
                                            "Couldn't Register, please try again!",
                                            Toast.LENGTH_LONG).show();

                                }

                            }
                        });
            }
        });

        firebaseAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Intent intent = new Intent(EnterUserDetails.this, Request_Service.class);
                startActivity(intent);
                finish();
                return;
            }
        };


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
                new DatePickerDialog(EnterUserDetails.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void sendEmailVerification() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful()){

                        Toast.makeText(getApplicationContext(),
                                "Please verify your email!",
                                Toast.LENGTH_LONG).show();

                        //FirebaseAuth.s

                    }

                }
            });

        }

    }

    //this function checks if the email is in the right format
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateOfBirth.setText(sdf.format(myCalendar.getTime()));
    }


    public void onclickServicee(View view) {
        Intent providerIntent = new Intent(this, Request_Service.class);
        startActivity(providerIntent);
    }

    public void onclicksignin(View view) {
        Intent providerIntent = new Intent(this, SignInUsingEmail.class);
        startActivity(providerIntent);
    }
}
