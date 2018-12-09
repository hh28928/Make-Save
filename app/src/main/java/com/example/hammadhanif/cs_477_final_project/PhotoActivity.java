package com.example.hammadhanif.cs_477_final_project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.storage.StorageManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hammadhanif.cs_477_final_project.config.StringUtility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class PhotoActivity extends AppCompatActivity {

    //for the image function
    private static final int CHOOSE_IMAGE = 101;
    Uri uriProfileImage;

    FirebaseAuth firebaseAuth;

    ProgressBar progressBar;

    ImageView usersImage;

    String profileImageUrl;

    String userNameFirst;
    String userNameLast;

    DatabaseReference mDatabase;

    TextView name;
    String imageFileName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        name = findViewById(R.id.textView6);

        usersImage = findViewById(R.id.imageView12);

        progressBar = findViewById(R.id.progressbar);

        //initialzing firebase
        firebaseAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        GetUserName();


    }

    //used to retrieve the name of the user.
    private void GetUserName() {

        FirebaseUser user = firebaseAuth.getCurrentUser();


        String user_id = user.getUid();

        //retrieving first name and last name
        mDatabase.child("Users").child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userNameFirst = (String) dataSnapshot.child("First Name").getValue();
                userNameLast = (String)  dataSnapshot.child("Last Name").getValue();
                name.setText(userNameFirst + " " + userNameLast);
                Toast.makeText(PhotoActivity.this,
                        "this user name is: " + userNameFirst, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        name.setText(userNameFirst + " " + userNameLast);




    }


    //this method is for when the user tries to insert an image
    public void UserInsertImage(View v){

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

                //uploading the image to the cloud.
                uploadImageToFirebaseStorage();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //uploading the image to the cloud:
    private void uploadImageToFirebaseStorage(){

        this.imageFileName = StringUtility.getRandomString(20) + ".jpg";
        Log.d("TAG", "Image Name is: " + imageFileName);
        //path to the file
        final StorageReference profileImageRef = FirebaseStorage.getInstance().getReference
                ("profilepics/ "+ imageFileName);

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

                            Toast.makeText(PhotoActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    });


        }


    }


    public void onClickQualification(View view) {
        Intent backIntent = new Intent(this, QualificationInfoActivity.class);
        startActivity(backIntent);
    }

        public void onClickContinue(View view) {

        saveUserInfo();
        }


        //once user hits continue, we will save the image:
    public void saveUserInfo(){

        if (usersImage.getDrawable() == null) {
            Toast.makeText(this, "You must select a profile Image", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();

        String user_id = user.getUid();

        DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(user_id).child("Image_Link");

        Map newPost = new HashMap();
        newPost.put("Image", imageFileName);

        current_user_db.setValue(newPost);


        if(user != null && profileImageUrl != null){

            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(Uri.parse(profileImageUrl)).build();

            user.updateProfile(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){

                                Toast.makeText(PhotoActivity.this,
                                        "profile picture is updated", Toast.LENGTH_LONG).show();

                            }
                        }
                    });
        }

        Intent toMapActivity = new Intent(this, CurrentLocationMap.class);
        startActivity(toMapActivity);


    }
}
