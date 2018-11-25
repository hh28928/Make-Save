package com.example.hammadhanif.cs_477_final_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class User_Confirmation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__confirmation);
    }

    public void OnclickContinue(View view) {

        Intent UserConfirmation = new Intent(this, Request_Service.class);
        startActivity(UserConfirmation);

    }
}
