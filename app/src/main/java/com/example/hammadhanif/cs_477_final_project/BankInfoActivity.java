package com.example.hammadhanif.cs_477_final_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class BankInfoActivity extends AppCompatActivity {

    EditText nameBank;
    EditText routingNumber;
    EditText accountNumber;
    EditText confirmAccountNumber;

    String userName;
    String userRoutingNumber;
    String userAccountNumber;
    String userConfirmAccountNumber;

    FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_info);


        nameBank = findViewById(R.id.full_name);
        routingNumber = findViewById(R.id.routing_et);
        accountNumber = findViewById(R.id.acc_number1);
        confirmAccountNumber = findViewById(R.id.acc_number2);


        firebaseAuth = FirebaseAuth.getInstance();



    }

    public void SaveAccountInformation(View view) {


        //saving the fname, lastname and DOB...
        userName = nameBank.getText().toString();
        userRoutingNumber = routingNumber.getText().toString();
        userAccountNumber = accountNumber.getText().toString();
        userConfirmAccountNumber = confirmAccountNumber.getText().toString();


        FirebaseUser user = firebaseAuth.getCurrentUser();
        //check if there is a user:

        String user_id = user.getUid();

        //create a database reference:
        DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(user_id).child("Bank Information");

        Map newPost = new HashMap();

        newPost.put("Full Name", userName);
        newPost.put("Routing Number", userRoutingNumber);
        newPost.put("Account Number", userAccountNumber);
        newPost.put("conf Account Number", userConfirmAccountNumber);

        //now set the value to current user databade
        current_user_db.setValue(newPost);


        Intent qualificationIntent = new Intent(this, QualificationInfoActivity.class);
        startActivity(qualificationIntent);
    }

}
