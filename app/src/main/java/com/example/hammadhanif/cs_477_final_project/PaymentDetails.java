package com.example.hammadhanif.cs_477_final_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PaymentDetails extends AppCompatActivity {

    TextView text1;
    TextView text22;
    TextView text3;
    Button PostJob;
    double lat;
    double longitude;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public static ArrayList data_add;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        text1 = (TextView) findViewById(R.id.text_idd);
        text22 = (TextView) findViewById(R.id.textamout);
        text3=(TextView)findViewById(R.id.textstatus);
        PostJob = (Button) findViewById(R.id.PostJob);
        Intent intent = getIntent();
        data_add = new ArrayList();
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


    public void onClickPostJob(View view) {
        String address = "4450 Rivanna River Way";
        String address2 = "Fair Oaks Mall";
        //Toast.makeText(this, "4450 Rivanna River Way", Toast.LENGTH_SHORT).show();
        Geocoder geo = new Geocoder(PaymentDetails.this);
        List<Address> list_address = new ArrayList<>();
        List<Address> list_address2 = new ArrayList<>();

        try {
            list_address = geo.getFromLocationName(address, 1);
            list_address = geo.getFromLocationName(address2, 1);


        } catch (IOException e) {
            e.printStackTrace();
        }
        if (list_address.size() > 0) {
            Address add = list_address.get(0);
            Address add2 = list_address2.get(0);
            double lat2 = add2.getLatitude();
            double longitude2 = add2.getLongitude();
            lat = add.getLatitude();
            longitude = add.getLongitude();
            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.clear();
            editor.putString("lat", Double.toString(lat));
            editor.putString("longitude", Double.toString(longitude));
            editor.putString("lat2", Double.toString(lat2));
            editor.putString("longitude2", Double.toString(longitude2));

            editor.apply();
//            Intent intent = new Intent(CurrentLocationMap.class).putExtra("lat",lat);
//
//            LocalBroadcastManager.getInstance(Activity1.this).sendBroadcast(intent);
//            Intent intent = new Intent("INTENT_NAME").putExtra(BG_SELECT, hexColor);
//            LocalBroadcastManager.getInstance(Activity1.this).sendBroadcast(intent);
//            Intent i = new Intent(this, CurrentLocationMap.class);
//            i.putExtra("Value1", lat);
//            i.putExtra("Value2", longitude);
            //startActivity(i);
        }
    }
}
