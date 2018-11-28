package com.example.hammadhanif.cs_477_final_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentDetails extends AppCompatActivity {

    TextView text1;
    TextView text22;
    TextView text3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        text1 = (TextView) findViewById(R.id.text_idd);
        text22 = (TextView) findViewById(R.id.textamout);
        text3=(TextView)findViewById(R.id.textstatus);
        Intent intent = getIntent();
        try{
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("PaymentDetails"));
            showDetails(jsonObject.getJSONObject("response"),intent.getStringExtra("PaymentAmount"));
        }catch(JSONException e)
        {
            e.printStackTrace();
        }
    }

    private void showDetails(JSONObject response, String paymentAmount) {

        try {
            text1.setText(response.getString("id"));
            text22.setText(response.getString("state"));
            text3.setText("$" + paymentAmount);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }


}
