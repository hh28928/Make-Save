package com.example.hammadhanif.cs_477_final_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class EnterUserDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_user_details);
    }

    public void onclicknext(View view) {
        Intent UserConfirmation = new Intent(this, Request_Service.class);
        startActivity(UserConfirmation);

    }
}
