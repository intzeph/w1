package com.example.wildtech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class Account extends AppCompatActivity {

    Button button;
    FirebaseAuth auth;

    FirebaseUser user;
    GoogleSignInClient mGoogleSignInClient;
    BottomNavigationView bottomNavigationView;

    DatabaseReference databaseRef;

    TextView user_name;

    ImageView imgProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);



        user_name=findViewById(R.id.U_name);
        imgProfile=findViewById(R.id.AprofileImageView);

        auth=FirebaseAuth.getInstance();

        bottomNavigationView= findViewById(R.id.a_bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.account_nav);

        button = findViewById(R.id.btnLogout);

        user = FirebaseAuth.getInstance().getCurrentUser();

        databaseRef = FirebaseDatabase.getInstance(getString(R.string.firebase_url)).getReference("Users");


        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home_nav:
                    startActivity(new Intent(getApplicationContext()
                            ,MainActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.account_nav:
                    return true;
            }
            return  false;
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Return to the login activity to allow signing in with another Google account
                            Intent intent = new Intent(getApplicationContext(), login.class);
                            startActivity(intent);
                            finish();

                            Toast.makeText(Account.this, "Logout successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            // Handle sign-out failure
                            Toast.makeText(Account.this, "Sign-out failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });


        if(user !=null){
            String userId= user.getUid();
            databaseRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    String firstName = snapshot.child("first_name").getValue(String.class);
                    if(snapshot.exists()){
                        user_name.setText(firstName);
                    }
                    GoogleSignInAccount googleAccount = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                    if(googleAccount !=null){
                        String googleDisplayName = googleAccount.getDisplayName();

                        // gonna to split the google name into first name only
                        String[] nameParts = googleDisplayName.split(" ");
                        String googleFirstName = nameParts.length > 0 ? nameParts[0] : "";

                        String profileImageUrl = user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : "";

                        if(user.getProviderData().get(1).getProviderId().equals("google.com")){
                            user_name.setText(googleDisplayName);
                            if (!profileImageUrl.isEmpty()) {
                                Picasso.get().load(profileImageUrl).into(imgProfile);
                            }
                        }
                        else{
                            user_name.setText(firstName);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else{
            Intent intent = new Intent(getApplicationContext(), login.class);
            startActivity(intent);
            finish();
        }




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


}

