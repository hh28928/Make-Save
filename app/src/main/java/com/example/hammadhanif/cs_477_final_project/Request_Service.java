package com.example.hammadhanif.cs_477_final_project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Request_Service extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int CHOOSE_IMAGE = 102;
    String profileImageUrl;
    ListView listView;
    EditText description;
    Uri uriProfileImage;
    ArrayAdapter arrayAdapter;
    ArrayList<String> array_list = new ArrayList<String>();
    private DrawerLayout drawer;
    TextView navUsername;
    ImageView usersImage;
    ProgressBar progressBar;
    String userNameFirst;
    String userNameLast;
    FirebaseAuth firebaseAuth;
    DatabaseReference mDatabase;
    TextView name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request__service);

        description = findViewById(R.id.moreInfoEditText);
        listView = findViewById(R.id.listview_services);

        array_list.add("Washing Machine");
        array_list.add("Air Condition");
        array_list.add("Electrical");
        array_list.add("Child care provider");
        array_list.add("Machine Fix");
        array_list.add("Need a Painter");
        array_list.add("Moving Out/Moving In");
        array_list.add("Lawn Moving");
        array_list.add("Carpenter");
        array_list.add("Sink Blockage");
        array_list.add("Handyman");

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array_list);
        listView.setAdapter(arrayAdapter);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.setTitle("New Services");
        firebaseAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        //setting an on click listener for the menu.
        NavigationView navigationView = findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);

        navUsername = (TextView) headerView.findViewById(R.id.nameOfUser);

        usersImage = headerView.findViewById(R.id.userImage);
        progressBar = headerView.findViewById(R.id.progressbar);


        navUsername.setText(userNameFirst + " " + userNameLast);


        FirebaseUser user = firebaseAuth.getCurrentUser();


        String user_id = user.getUid();

        //retrieving first name and last name
        mDatabase.child("Users").child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userNameFirst = (String) dataSnapshot.child("First Name").getValue();
                userNameLast = (String)  dataSnapshot.child("Last Name").getValue();
                navUsername.setText(userNameFirst + " " + userNameLast);
//                Toast.makeText(Request_Service.this,
//                        "this user name is: " + userNameFirst, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        drawer = findViewById(R.id.drawer_layout);

        navUsername.setText(userNameFirst + " " + userNameLast);

        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        name.setText(userNameFirst + " " + userNameLast);


        switch(item.getItemId()){
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();
                break;



            case R.id.notification:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new NotificationFragment()).commit();
                break;


            case R.id.payment:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new PaymentFragment()).commit();
                break;


        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)){

            drawer.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();
        }

    }

    public void onclickPayment(View view) {
        Intent continueIntent = new Intent(this,Paymentactivity.class);
        startActivity(continueIntent);
    }

    public void CostumerImage(View v){

        //create a file
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), CHOOSE_IMAGE);

        //get the image



    }
    //for the image as well
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if true, then this mean we have the image
        if(requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null ){

            uriProfileImage = data.getData();

            //this method generates an exception so we have to put it in try...
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
                usersImage.setImageBitmap(bitmap);

                navUsername.setText(userNameFirst + " " + userNameLast);
                //uploading the image to the cloud.
                uploadImageToFirebaseStorage();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //uploading the image to the cloud:
    private void uploadImageToFirebaseStorage(){

        //path to the file
        final StorageReference profileImageRef = FirebaseStorage.getInstance().getReference
                ("profilepics/"+ System.currentTimeMillis() + ".jpg");

        if (uriProfileImage != null) {

            progressBar.setVisibility(View.VISIBLE);


            //if the image is not null ad the image, and on completion of task do something
            profileImageRef.putFile(uriProfileImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            progressBar.setVisibility(View.GONE);

                            profileImageUrl = profileImageRef.getDownloadUrl().toString();

                        }
                    })


                    //handles errors.
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            progressBar.setVisibility(View.GONE);

                            Toast.makeText(Request_Service.this, e.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    });


        }


    }

}
