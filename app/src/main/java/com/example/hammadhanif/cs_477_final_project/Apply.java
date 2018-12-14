package com.example.hammadhanif.cs_477_final_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Apply extends AppCompatActivity {
    TextView text;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);
        title = (TextView)findViewById(R.id.title_job);
        text = (TextView) findViewById(R.id.problem);
       // Intent i=getIntent();
        //String prob= i.getStringExtra("Problem");
        text.setText("My Dishwasher is leaking");
        title.setText("Dish Washer");
    }

    public void onClickBack(View view) {
        Intent intent = new Intent(this,CurrentLocationMap.class);
        startActivity(intent);

    }

    public void onClickApply(View view) {
        Toast.makeText(this,"Job has been Applied, waiting for confirmation",Toast.LENGTH_SHORT).show();
    }
}
