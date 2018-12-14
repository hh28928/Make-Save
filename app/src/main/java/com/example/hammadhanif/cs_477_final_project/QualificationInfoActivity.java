package com.example.hammadhanif.cs_477_final_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;



public class QualificationInfoActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    EditText education, currentlyWorking, pastExperience, describeYourself;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qualification_info);

        education = findViewById(R.id.education_et);
        currentlyWorking = findViewById(R.id.enter_working);
        pastExperience = findViewById(R.id.enter_experiences);
        describeYourself = findViewById(R.id.moreInfo);

        firebaseAuth = FirebaseAuth.getInstance();



    }





    public void onClickContinue(View view) {

        String userEducation = education.getText().toString();
        String userWorkingAt = currentlyWorking.getText().toString();
        String Experience = pastExperience.getText().toString();
        String moreInfo = describeYourself.getText().toString();

        if (userEducation.equals("") || userWorkingAt.equals("") || Experience.equals("") || moreInfo.equals("")) {
            Toast.makeText(this, "Please add the missing information first!", Toast.LENGTH_LONG).show();
            return;
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();
        //check if there is a user:

        String user_id = user.getUid();

        //create a database reference:
        DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(user_id).child("Qualifications");

        Map newPost = new HashMap();

        newPost.put("User Education", userEducation);
        newPost.put("User Working at", userWorkingAt);
        newPost.put("User Experience", Experience);
        newPost.put("User more Info", moreInfo);

        //now set the value to current user database
        current_user_db.setValue(newPost);


        Intent continueIntent = new Intent(this, PhotoActivity.class);
        startActivity(continueIntent);
    }
}
