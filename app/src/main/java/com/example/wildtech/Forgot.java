package com.example.wildtech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Forgot extends AppCompatActivity {

    TextInputEditText emailEditText;
    TextInputLayout emailLayout;

    ProgressBar progressBar;

    Button sendCodeBtn;

    DatabaseReference databaseRef;

    FirebaseAuth mAuth;

    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        emailEditText =findViewById(R.id.emailFieldF);
        emailLayout = findViewById(R.id. emailLayout);
        progressBar = findViewById(R.id.progressBar);
        sendCodeBtn = findViewById(R.id.sendCodeBtn);
        mAuth = FirebaseAuth.getInstance();

        databaseRef = FirebaseDatabase.getInstance(getString(R.string.firebase_link)).getReference("Users");





        sendCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailEditText.getText().toString();

                String enteredEmail = emailEditText.getText().toString().trim();
                if(!TextUtils.isEmpty(enteredEmail)){
                    resetPassword();
                }
                else{
                    emailLayout.setError("This field cannot be empty!");
                }
            }
        });


    }



    private void resetPassword(){
        progressBar.setVisibility(View.VISIBLE);
        sendCodeBtn.setVisibility(View.VISIBLE);

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(Forgot.this, "Reset password link has be successfully sent to you email", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Forgot.this, login.class );
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(Forgot.this, "Enter correct email.", Toast.LENGTH_SHORT).show();
                }

            }
        }) .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Forgot.this, "Error email not found", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void goToLogin(View view) {
        Intent intent = new Intent(this, login.class);
        startActivity(intent);
        finish();
    }
}