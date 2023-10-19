package com.example.wildtech;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    TextInputEditText firstnameEditText, lastnameEditText, emailEditText,passwordEditText, confirmPasswordEditText;

    TextInputLayout nameLayout, lastNameLayout, emailLayout, passwordLayout, ConfirmPasswordLayout;
    Button registerBtn, R_googleBtn,R_fbBtn;

    ProgressBar progressBar;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    DatabaseReference databaseRef;

    FirebaseDatabase database;

    GoogleSignInClient mGoogleSignInClient;

    String passwordPattern = "^(?=.*[0-9])(?=.*[A-Z])(?!.*[/\\?]).{8,}$";


    String emailPattern="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    String namePattern="^[^0-9]+$";


    int RC_SIGN_IN = 20;

    private boolean hasErrors = false;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        databaseRef = FirebaseDatabase.getInstance(getString(R.string.firebase_link)).getReference("Users");
        firstnameEditText =findViewById(R.id.firstNameField);
        lastnameEditText = findViewById(R.id.lastNameField);
        emailEditText = findViewById(R.id.emailField);
        passwordEditText = findViewById(R.id.passwordField);
        registerBtn = findViewById(R.id.registerBtn);
        R_googleBtn=findViewById(R.id.R_googleBtn);
        progressBar= findViewById(R.id.progressBar);
        nameLayout = findViewById(R.id.nameLayout);
        lastNameLayout = findViewById(R.id.lastNameLayout);
        emailLayout = findViewById(R.id.emailLayout);
        passwordLayout =findViewById(R.id.passwordLayout);
        ConfirmPasswordLayout = findViewById(R.id.ConfirmPasswordLayout);
        confirmPasswordEditText= findViewById(R.id.ConfirmPasswordField);
        R_fbBtn= findViewById(R.id.R_fbBtn);


        //remove backgroundTint
        R_googleBtn.setBackgroundTintList(null);
        R_fbBtn.setBackgroundTintList(null);




        firstnameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                allAuth();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        lastnameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                allAuth();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                allAuth();
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
                allAuth();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        confirmPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                allAuth();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });






        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        registerBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                PerformAuth();

                //lets see if we can use this instead
//                if(hasErrors){
//                    registerBtn.setEnabled(false);
//                }
//                else{
//                    registerBtn.setEnabled(true);
//                }



            }
        });


        R_googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignIn();
            }
        });


    }

    private void allAuth() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        String firstName = firstnameEditText.getText().toString();
        String lastName = lastnameEditText.getText().toString();

        emailLayout.setError(null);
        passwordLayout.setError(null);
        ConfirmPasswordLayout.setError(null);
        nameLayout.setError(null);
        lastNameLayout.setError(null);

        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {


            if (TextUtils.isEmpty(firstName)) {
                nameLayout.setError("This field is required.");

            }
            if (TextUtils.isEmpty(lastName)) {
                lastNameLayout.setError("This field is required.");

            }
            if (TextUtils.isEmpty(email)) {
                emailLayout.setError("This field is required.");

            }
            if (TextUtils.isEmpty(password)) {
                passwordLayout.setError("This field is required.");

            }
            if (TextUtils.isEmpty(confirmPassword)) {
                ConfirmPasswordLayout.setError("This field is required.");

            }
            hasErrors =true;
        }
        if (firstName.contains("@") || firstName.contains(".") || firstName.contains("/") || firstName.contains("*") || !firstName.matches(namePattern)) {
            nameLayout.setError("Invalid first name");
            hasErrors =true;

        }
        if (lastName.contains("@") || lastName.contains(".") || lastName.contains("/") || lastName.contains("*") || !lastName.matches(namePattern)) {
            lastNameLayout.setError("Invalid last name");

            hasErrors =true;
        }
        else if (!email.contains("@") || !email.matches(emailPattern)) {
            emailLayout.setError("Enter a valid email address!");
            hasErrors =true;

        }

        else if (!password.matches(passwordPattern)) {
            passwordLayout.setError("Password must contain at least 8 characters, including an uppercase letter and a number only.");
            hasErrors =true;

        }

        else if (password.contains(" ")) {
            passwordLayout.setError("Password cannot contain spaces.");
            hasErrors =true;

        }
        else if (password.contains(".")) {
            passwordLayout.setError("Password cannot contain dot.");
            hasErrors =true;

        }
        else if(!confirmPassword.matches(password)){
            ConfirmPasswordLayout.setError("Password didn't match");
            hasErrors =true;

        }
    }

    public void onBackPressed() {
        super.onBackPressed();
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
                // Handle the signed-in Google account
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

                            Intent intent = new Intent(Register.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        }else{
                            Toast.makeText(Register.this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }


    //validation
    private void PerformAuth() {

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        String firstName = firstnameEditText.getText().toString();
        String lastName = lastnameEditText.getText().toString();

        emailLayout.setError(null);
        passwordLayout.setError(null);
        nameLayout.setError(null);
        lastNameLayout.setError(null);

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(confirmPassword)) {

            if (TextUtils.isEmpty(firstName)) {
                nameLayout.setError("This field is required.");

            }
            if (TextUtils.isEmpty(lastName)) {
                lastNameLayout.setError("This field is required.");

            }
            if (TextUtils.isEmpty(email)) {
                emailLayout.setError("This field is required.");

            }
            if (TextUtils.isEmpty(password)) {
                passwordLayout.setError("This field is required.");

            }
            if (TextUtils.isEmpty(confirmPassword)) {
                ConfirmPasswordLayout.setError("This field is required.");

            }
            hasErrors =true;
        }
        else if (firstName.contains("@") || firstName.contains(" . ") || firstName.contains("/") || firstName.contains("*")) {
            nameLayout.setError("Invalid first name");
            hasErrors =true;

        }
        else if (lastName.contains("@") || lastName.contains(" . ") || lastName.contains("/") || lastName.contains("*")) {
            lastNameLayout.setError("Invalid last name");
            hasErrors =true;

        }
        else if (!email.contains("@")) {
            emailLayout.setError("Enter a valid email address!");
            hasErrors =true;
        }
        else if (!email.matches(emailPattern)) {
            emailLayout.setError("Enter a valid email address!");
            hasErrors =true;
        }
        else if (!password.matches(passwordPattern)) {
            passwordLayout.setError("Password must contain at least 8 characters, including an uppercase letter and a number only.");
            hasErrors =true;
        }

        else if (password.contains(" ")) {
            passwordLayout.setError("Password cannot contain spaces.");
            hasErrors =true;
        }
        else if (password.contains(".")) {
            passwordLayout.setError("Password cannot contain dot.");
            hasErrors =true;
        }
        else if(!confirmPassword.matches(password)){
            ConfirmPasswordLayout.setError("Password didn't match");
            hasErrors =true;

        }
        else {
            progressBar.setVisibility(View.VISIBLE);
            checkEmailExists(email);

        }

    }

    //checkEmailIfExist
    private void checkEmailExists(String email) {
        mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                if (task.isSuccessful()) {
                    SignInMethodQueryResult result = task.getResult();
                    if (result != null && result.getSignInMethods() != null && !result.getSignInMethods().isEmpty()) {
                        emailLayout.setError("Email already exists");
                        progressBar.setVisibility(View.GONE);

                    } else {
                        createUser(email);
                    }
                } else {
                    Toast.makeText(Register.this, "Failed to check email existence", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);

                }
            }
        });
    }


    //createAccount
    private void createUser(String email) {
        String password = passwordEditText.getText().toString();
        String firstName = firstnameEditText.getText().toString();
        String lastName = lastnameEditText.getText().toString();
        String emailR =emailEditText.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {

                            String userId = mAuth.getCurrentUser().getUid();
                            UserHelperClass newUser = new UserHelperClass(firstName, lastName, emailR);
                            DatabaseReference userRef = databaseRef.child(userId);
                            userRef.setValue(newUser);

                            Intent intent = new Intent(getApplicationContext(), login.class);
                            startActivity(intent);
                            finish();

                            Toast.makeText(Register.this, "Registration successful.", Toast.LENGTH_SHORT).show();
                        } else {

                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                // Handle the case where the email is already in use
                                emailLayout.setError("Email already exists");
                            } else {

                                Toast.makeText(Register.this, "Registration failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }


    public void goToLogin(View view){

        Intent intent = new Intent(getApplicationContext(), login.class);
        startActivity(intent);
        finish();
    }

}


