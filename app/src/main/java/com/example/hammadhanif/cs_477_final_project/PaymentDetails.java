package com.example.hammadhanif.cs_477_final_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PaymentDetails extends AppCompatActivity {

    TextView text1;
    TextView text22;
    TextView text3;
    Button PostJob;
    double lat;
    double longitude;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public static ArrayList data_add;

    String user_id;

    ArrayList<String> listOfLocations = new ArrayList<String>();


    DatabaseReference mDatabase;

    String location;

    FirebaseAuth firebaseAuth;

    String userType;

    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        //initialzing firebase


        text1 = (TextView) findViewById(R.id.text_idd);
        text22 = (TextView) findViewById(R.id.textamout);
        text3 = (TextView) findViewById(R.id.textstatus);
        PostJob = (Button) findViewById(R.id.PostJob);
        Intent intent = getIntent();
        data_add = new ArrayList();
        try {
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("PaymentDetails"));
            showDetails(jsonObject.getJSONObject("response"), intent.getStringExtra("PaymentAmount"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        firebaseAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();


        user = firebaseAuth.getCurrentUser();

        user_id = user.getUid();

        mDatabase.child("Users").child(user_id).addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                location = (String) dataSnapshot.child("Location").getValue();
//
//                Toast.makeText(PaymentDetails.this,
//                        "Location" + location, Toast.LENGTH_LONG).show();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        }

    private void showDetails(JSONObject response, String paymentAmount) {

        try {
            text1.setText(response.getString("id"));
            text22.setText(response.getString("state"));
            text3.setText("$" + paymentAmount);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    public void onClickPostJob(View v) {


        //flagging that this user started a job.
        user = firebaseAuth.getCurrentUser();

        user_id = user.getUid();

        //create a database reference:
        DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(user_id).child("services");

        Map newPost = new HashMap();

        //now set the value to current user database
        current_user_db.setValue(newPost);


        newPost.put("User Posted a Job", "posted");


        //now set the value to current user databade
        current_user_db.setValue(newPost);


        //looping through users and saving flaged users to an array



        mDatabase.child("Users").addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    listOfLocations.clear();
                    for (com.google.firebase.database.DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        Map userType = (HashMap) postSnapshot.child("services").getValue();
                        if (userType != null) {
                            if (userType.size() > 0) {
                                Iterator it = userType.entrySet().iterator();
                                while (it.hasNext()) {
                                    Map.Entry pair = (Map.Entry) it.next();
                                    System.out.println(pair.getKey() + " = " + pair.getValue());
                                    if (pair.getKey().equals("User Posted a Job")) {
                                        if (pair.getValue().equals("posted")) {
                                            listOfLocations.add((String) postSnapshot.child("Location").getValue());
                                        }
                                    }
                                    //it.remove(); // avoids a ConcurrentModificationException
                                }
                            }
                        }
                        Toast.makeText(PaymentDetails.this, "number of people:" + listOfLocations.size(), Toast.LENGTH_LONG).show();


                    }


                }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



         //Toast.makeText(this, "job for 4450 Rivanna River Way has been posted", Toast.LENGTH_SHORT).show();
        Geocoder geo = new Geocoder(PaymentDetails.this);
        List<Address> list_address = new ArrayList<>();

        try {
            list_address = geo.getFromLocationName(location, 1);


        } catch (IOException e) {
            e.printStackTrace();
        }
        if (list_address.size() > 0) {
            Address add = list_address.get(0);
            lat = add.getLatitude();
            longitude = add.getLongitude();
            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.clear();
            editor.putString("lat", Double.toString(lat));
            editor.putString("longitude", Double.toString(longitude));
            editor.apply();




        }


    }
}
