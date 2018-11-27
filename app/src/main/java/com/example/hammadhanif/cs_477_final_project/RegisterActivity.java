package com.example.hammadhanif.cs_477_final_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void onClickProvider(View view) {
        Intent nextIntent = new Intent(this, NextActivity.class);
        startActivity(nextIntent);
    }

    public void onClickuser(View view) {
        Intent providerIntent = new Intent(this, User_Registration.class);
        startActivity(providerIntent);

    }
}
