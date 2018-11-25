package com.example.hammadhanif.cs_477_final_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SigninActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);


    }

    public void onClickProvider(View view) {
        Intent providerIntent = new Intent(this, ProviderActivity.class);
        startActivity(providerIntent);
    }

    public void OnclickUser(View view) {
        Intent UserConfirmation = new Intent(this, User_Confirmation.class);
        startActivity(UserConfirmation);

    }
}
