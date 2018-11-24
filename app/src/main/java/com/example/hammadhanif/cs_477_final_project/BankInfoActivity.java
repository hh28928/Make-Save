package com.example.hammadhanif.cs_477_final_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class BankInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_info);
    }

    public void onClickNext(View view) {
        Intent qualificationIntent = new Intent(this, QualificationInfoActivity.class);
        startActivity(qualificationIntent);
    }
}
