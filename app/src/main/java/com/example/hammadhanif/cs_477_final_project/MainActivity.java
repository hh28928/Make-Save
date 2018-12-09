package com.example.hammadhanif.cs_477_final_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickSignIn(View view) {
        Intent signIn_Intent = new Intent(MainActivity.this, SignInUsingEmail.class);
        startActivity(signIn_Intent);
    }

    public void onClickRegister(View view) {
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        startActivity(registerIntent);
    }
}
