package com.example.wildtech;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class login extends AppCompatActivity {


    TextInputEditText emailEditText,passwordEditText;

    TextInputLayout emailLayout, passwordLayout;
    Button loginBtn, googleBtn, LfbBtn;

    ProgressBar progressBar;

    FirebaseAuth mAuth;

    FirebaseUser mUser;

    FirebaseDatabase database;

    GoogleSignInClient mGoogleSignInClient;

    String emailPattern="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    String passwordPattern = "^(?=.*[0-9])(?=.*[A-Z])(?!.*[/\\?]).{8,}$";

    int RC_SIGN_IN = 20;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_login);


        emailEditText = findViewById(R.id.L_emailField);
        passwordEditText = findViewById(R.id.L_passwordField);
        loginBtn = findViewById(R.id.loginBtn);
        progressBar= findViewById(R.id.progressBarLogin);
        googleBtn = findViewById(R.id.LgoogleBtn);
        LfbBtn=findViewById(R.id.LfbBtn);
        emailLayout=findViewById(R.id.L_emailLayout);
        passwordLayout=findViewById(R.id.R_passwordLayout);

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailPassAuth();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailPassAuth();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean termsAccepted = sharedPreferences.getBoolean("termsAccepted", false);

        if (!termsAccepted) {
            displayTermsNotAcceptedMessage();
        }

        //remove BGTint
        googleBtn.setBackgroundTintList(null);
        LfbBtn.setBackgroundTintList(null);


        mAuth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail().build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                PerformAuth();



            }
        });

        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignIn();
            }
        });


    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Are you sure you want to exit the app?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Close the app or all act
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }


    private void displayTermsNotAcceptedMessage() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Terms and Conditions");
        builder.setMessage("You must accept the terms and conditions to use this app.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(login.this, TermsAndCondition.class);
                startActivity(intent);
                finish(); // Finish the LoginActivity.
            }
        });
        builder.setCancelable(false);
        builder.show();
    }


    private void emailPassAuth(){

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();


        emailLayout.setError(null);
        passwordLayout.setError(null);


        if (email.isEmpty() || password.isEmpty() ) {

            if (TextUtils.isEmpty(email)) {
                emailLayout.setError("This field is required.");
            }
            if (TextUtils.isEmpty(password)) {
                passwordLayout.setError("This field is required.");
            }
        }
        else if (!email.contains("@") || !email.matches(emailPattern) || email.contains(" ")) {
            emailLayout.setError("Enter a valid email address!");

        }


    }


    private void PerformAuth() {

        int color= Color.parseColor("#EBECF0");


        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();


        if (email.isEmpty() || password.isEmpty() ) {

            if (TextUtils.isEmpty(email)) {
                emailLayout.setError("This field is required.");
            }
            if (TextUtils.isEmpty(password)) {
                passwordLayout.setError("This field is required.");
            }
        } else if (!email.matches(emailPattern)) {
            emailLayout.setError("Enter a valid email");
        }
        else {

            emailLayout.setError(null);
            passwordLayout.setError(null);
            progressBar.setVisibility(View.VISIBLE);
            loginMethod(email, password);
        }


    }

    private void loginMethod(String email, String password) {


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.VISIBLE);
                            FirebaseUser user = mAuth.getCurrentUser();

                            Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();


                        } else {


                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(login.this, "Invalid credentials or account not registered yet.",
                                    Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }


    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    handleGoogleSignInResult(data);
                }
            }
    );

    private void googleSignIn() {
        Intent intent = mGoogleSignInClient.getSignInIntent();
        signInLauncher.launch(intent);

    }

    private void handleGoogleSignInResult(Intent data) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            if (account != null) {
                firebaseAuth(account.getIdToken());
                Toast.makeText(this, "Sign-in sucessful", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Sign-in failed", Toast.LENGTH_SHORT).show();
            }
        } catch (ApiException e) {
            Toast.makeText(this, "Sign-in failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try{
                GoogleSignInAccount account= task.getResult(ApiException.class);
                firebaseAuth(account.getIdToken());

            }
            catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuth(String idToken) {

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){


                            FirebaseUser user = mAuth.getCurrentUser();
                            String uid = user.getUid();
                            String displayName = user.getDisplayName();


                            HashMap<String, Object> map = new HashMap<>();
                            map.put("id", user.getUid());
                            map.put("name", user.getDisplayName());
                            map.put("profile", user.getPhotoUrl().toString());

                            database.getReference().child("users").child(user.getUid()).setValue(map);

                            Intent intent = new Intent(login.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        }else{
                            Toast.makeText(login.this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


    }


    public  void goToRegister(View view){
        Intent intent =  new Intent(this, Register.class);
        startActivity(intent);
    }

    public void goToForgot(View view) {
        Intent intent = new Intent(this, Forgot.class);
        startActivity(intent);

    }
}