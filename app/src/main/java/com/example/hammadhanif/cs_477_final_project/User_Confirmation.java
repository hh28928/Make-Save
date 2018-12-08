package com.example.hammadhanif.cs_477_final_project;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class User_Confirmation extends AppCompatActivity {

    EditText code;
    String number, type;

    private String verificationid;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__confirmation);

        code = findViewById(R.id.digits_entered);
        progressBar = findViewById(R.id.progressbar);
        mAuth = FirebaseAuth.getInstance();
        type = getIntent().getExtras().getString("TYPE");
        number = getIntent().getExtras().getString("PHONE");
        sendVerificationCode(number);
    }

    private void sendVerificationCode(String number){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken)
        {
            super.onCodeSent(s, forceResendingToken);
            verificationid = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null){
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(User_Confirmation.this, e.getMessage(),Toast.LENGTH_LONG).show();

        }
    };


    public void OnclickContinue(View view) {

        String code_input = code.getText().toString().trim();

        if ((code_input.isEmpty() || code.length() < 6)){

            code.setError("Enter code...");
            code.requestFocus();
            return;
        }
        verifyCode(code_input);
    }


    private void verifyCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationid, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (type.equals("User")) {
                                Intent intent = new Intent(User_Confirmation.this, Request_Service.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {
                                Toast.makeText(User_Confirmation.this, "You are not a Provider, Please try as a user Instead!", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(User_Confirmation.this, task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }

    //in case user wants to sign in through his email
    public void SignInUsingEmail(View view) {
        Intent signInThroughEmail = new Intent(this, SignInUsingEmail.class);
        startActivity(signInThroughEmail);

    }
}
