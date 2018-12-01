package com.example.hammadhanif.cs_477_final_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class QualificationInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qualification_info);
    }





    public void onClickContinue(View view) {
        Intent continueIntent = new Intent(this, PhotoActivity.class);
        startActivity(continueIntent);
    }
}
