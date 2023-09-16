package com.example.wildtech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity {


    MaterialToolbar topAppBar;
    FirebaseAuth auth;

    TextView textView, textView2, textView3, textView4, textView5, textView6,textView7,textView8;
    FirebaseUser user;

    ImageView popular_img;

    DatabaseReference databaseRef, databaseRef_offers;

    GoogleSignInClient mGoogleSignInClient;

    ImageView img;

    BottomNavigationView bottomNavigationView;



    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth=FirebaseAuth.getInstance();

        textView= findViewById(R.id.user_details);
        textView2 = findViewById(R.id.user_details2);
        textView3 = findViewById(R.id.user_details3);
        textView4 = findViewById(R.id.user_details4);
        topAppBar= findViewById(R.id.topAppBar);

        bottomNavigationView = findViewById(R.id.h_bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home_nav);




        int color = Color.parseColor("#F9F9F9");

        topAppBar.setBackgroundColor(color);

        img = findViewById(R.id.profileImageView);

        databaseRef = FirebaseDatabase.getInstance(getString(R.string.firebase_url)).getReference("Users");

        databaseRef_offers = FirebaseDatabase.getInstance(getString(R.string.firebase_url)).getReference("Offers");

        user = FirebaseAuth.getInstance().getCurrentUser();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){

                case R.id.home_nav:
                    return true;
                case R.id.account_nav:
                    startActivity(new Intent(getApplicationContext()
                            ,Account.class));
                    overridePendingTransition(0,0);
                    return true;


            }
            return false;
        });


//        textView8=findViewById(R.id.rating_text);
//        textView7=findViewById(R.id.price_text);
//        textView6=findViewById(R.id.location_text);
//        textView5= findViewById(R.id.popular_text);
//        popular_img= findViewById(R.id.popular_img);



//        USE TO UPLOAD DATA
//
        String productId = "room5";
        DatabaseReference newProductRef = databaseRef_offers.child(productId);

        newProductRef.child("category").setValue("room");
        newProductRef.child("image").setValue("https://firebasestorage.googleapis.com/v0/b/wildtech-b4fc6.appspot.com/o/rooms%2Froom8.jpg?alt=media&token=accb26c8-34ab-490e-9776-e9f0cb91cdbc");
        newProductRef.child("location").setValue("Somewhere, Philippines");
        newProductRef.child("name").setValue("room8");
        newProductRef.child("price").setValue("2000");
        newProductRef.child("ratingAvg").setValue(0);
        newProductRef.child("ratings").setValue("0");



        //highlights_layout
        databaseRef_offers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = 0;
                LinearLayout currentLinearLayout = null;

                LinearLayout linearLayout = findViewById(R.id.highlight_layout);

                for (DataSnapshot offerSnapshot : snapshot.getChildren()) {
                    String category = offerSnapshot.child("category").getValue(String.class);

                    if ("highlight".equals(category)) {
                        if (count % 2 == 0) {
                            // Create a new vertical LinearLayout for every second item
                            currentLinearLayout = new LinearLayout(MainActivity.this);
                            currentLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
                            linearLayout.addView(currentLinearLayout);
                        }

                        // inflate the item layout
                        View itemLayout = getLayoutInflater().inflate(R.layout.highlights_layout, null);


                        ImageView imageView = itemLayout.findViewById(R.id.highlight_img);


                        String imageURL = offerSnapshot.child("image").getValue(String.class);


                        Picasso.get().load(imageURL).fit().into(imageView);


                        // Add the item layout to the current row's LinearLayout
                        currentLinearLayout.addView(itemLayout);

                        count++;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled if needed
            }
        });


        //rooms_layout
        databaseRef_offers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = 0;
                LinearLayout currentLinearLayout = null;

                LinearLayout linearLayout = findViewById(R.id.rooms_linear_layout);

                for (DataSnapshot offerSnapshot : snapshot.getChildren()) {
                    String category = offerSnapshot.child("category").getValue(String.class);
                    String subCategory = offerSnapshot.child("subCategory").getValue(String.class);

                    if ("room".equals(subCategory) || "room".equals(category) ) {
                        if (count % 2 == 0) {
                            // Create a new vertical LinearLayout for every second item
                            currentLinearLayout = new LinearLayout(MainActivity.this);
                            currentLinearLayout.setOrientation(LinearLayout.VERTICAL);
                            linearLayout.addView(currentLinearLayout);
                        }

                        // inflate the item layout
                        View itemLayout = getLayoutInflater().inflate(R.layout.popular_layout, null);


                        ImageView imageView = itemLayout.findViewById(R.id.popular_img);
                        TextView titleTextView = itemLayout.findViewById(R.id.popular_text);
                        TextView locationTextView = itemLayout.findViewById(R.id.location_text);
                        TextView priceTextView = itemLayout.findViewById(R.id.price_text);
                        TextView ratingTextView = itemLayout.findViewById(R.id.rating_text);


                        String imageURL = offerSnapshot.child("image").getValue(String.class);
                        String name = offerSnapshot.child("name").getValue(String.class);
                        String location = offerSnapshot.child("location").getValue(String.class);
                        String price = offerSnapshot.child("price").getValue(String.class);
                        int rating = offerSnapshot.child("ratingAvg").getValue(Integer.class);

                        String ratingString = String.valueOf(rating);


                        Picasso.get().load(imageURL).fit().into(imageView);
                        titleTextView.setText(name);
                        locationTextView.setText(location);
                        priceTextView.setText(price);
                        ratingTextView.setText(ratingString);

                        // Add the item layout to the current row's LinearLayout
                        currentLinearLayout.addView(itemLayout);

                        count++;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled if needed
            }
        });


        //cottages_layout
        databaseRef_offers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = 0;
                LinearLayout currentLinearLayout = null;

                LinearLayout linearLayout = findViewById(R.id.cottages_linear_layout);

                for (DataSnapshot offerSnapshot : snapshot.getChildren()) {
                    String category = offerSnapshot.child("category").getValue(String.class);

                    if ("popular".equals(category)) {
                        if (count % 2 == 0) {
                            // Create a new vertical LinearLayout for every second item
                            currentLinearLayout = new LinearLayout(MainActivity.this);
                            currentLinearLayout.setOrientation(LinearLayout.VERTICAL);
                            linearLayout.addView(currentLinearLayout);
                        }

                        // inflate the item layout
                        View itemLayout = getLayoutInflater().inflate(R.layout.popular_layout, null);


                        ImageView imageView = itemLayout.findViewById(R.id.popular_img);
                        TextView titleTextView = itemLayout.findViewById(R.id.popular_text);
                        TextView locationTextView = itemLayout.findViewById(R.id.location_text);
                        TextView priceTextView = itemLayout.findViewById(R.id.price_text);
                        TextView ratingTextView = itemLayout.findViewById(R.id.rating_text);


                        String imageURL = offerSnapshot.child("image").getValue(String.class);
                        String name = offerSnapshot.child("name").getValue(String.class);
                        String location = offerSnapshot.child("location").getValue(String.class);
                        String price = offerSnapshot.child("price").getValue(String.class);
                        int rating = offerSnapshot.child("ratingAvg").getValue(Integer.class);

                        String ratingString = String.valueOf(rating);


                        Picasso.get().load(imageURL).fit().into(imageView);
                        titleTextView.setText(name);
                        locationTextView.setText(location);
                        priceTextView.setText(price);
                        ratingTextView.setText(ratingString);

                        // Add the item layout to the current row's LinearLayout
                        currentLinearLayout.addView(itemLayout);

                        count++;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled if needed
            }
        });


        //popular_layout
        databaseRef_offers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (DataSnapshot offerSnapshot : snapshot.getChildren()) {

                    String category = offerSnapshot.child("category").getValue(String.class);

                    if ("popular".equals(category)) {


                        String imageURL = offerSnapshot.child("image").getValue(String.class);
                        String name = offerSnapshot.child("name").getValue(String.class);
                        String location = offerSnapshot.child("location").getValue(String.class);
                        String price = offerSnapshot.child("price").getValue(String.class);
                        int rating = offerSnapshot.child("ratingAvg").getValue(Integer.class);

                        String ratingString = String.valueOf(rating);


                        View itemLayout = getLayoutInflater().inflate(R.layout.popular_layout, null);

                        // Populate the item layout with data
                        ImageView imageView = itemLayout.findViewById(R.id.popular_img);
                        TextView titleTextView = itemLayout.findViewById(R.id.popular_text);
                        TextView locationTextView = itemLayout.findViewById(R.id.location_text);
                        TextView priceTextView = itemLayout.findViewById(R.id.price_text);
                        TextView ratingTextView = itemLayout.findViewById(R.id.rating_text);


                        Picasso.get().load(imageURL).fit().into(imageView);
                        titleTextView.setText(name);
                        locationTextView.setText(location);
                        priceTextView.setText(price);
                        ratingTextView.setText(ratingString);

                        // Add the item layout to the LinearLayout
                        LinearLayout linearLayout = findViewById(R.id.popular_linear_layout);
                        linearLayout.addView(itemLayout);
                    }
                }

                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //pools_layout
        databaseRef_offers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = 0;
                LinearLayout currentLinearLayout = null;

                LinearLayout linearLayout = findViewById(R.id.pools_linear_layout);

                for (DataSnapshot offerSnapshot : snapshot.getChildren()) {
                    String category = offerSnapshot.child("category").getValue(String.class);

                    if ("pools".equals(category)) {
                        if (count % 2 == 0) {
                            // Create a new vertical LinearLayout for every second item
                            currentLinearLayout = new LinearLayout(MainActivity.this);
                            currentLinearLayout.setOrientation(LinearLayout.VERTICAL);
                            linearLayout.addView(currentLinearLayout);
                        }

                        // inflate the item layout
                        View itemLayout = getLayoutInflater().inflate(R.layout.popular_layout, null);


                        ImageView imageView = itemLayout.findViewById(R.id.popular_img);
                        TextView titleTextView = itemLayout.findViewById(R.id.popular_text);
                        TextView locationTextView = itemLayout.findViewById(R.id.location_text);
                        TextView priceTextView = itemLayout.findViewById(R.id.price_text);
                        TextView ratingTextView = itemLayout.findViewById(R.id.rating_text);


                        String imageURL = offerSnapshot.child("image").getValue(String.class);
                        String name = offerSnapshot.child("name").getValue(String.class);
                        String location = offerSnapshot.child("location").getValue(String.class);
                        String price = offerSnapshot.child("price").getValue(String.class);
                        int rating = offerSnapshot.child("ratingAvg").getValue(Integer.class);

                        String ratingString = String.valueOf(rating);


                        Picasso.get().load(imageURL).fit().into(imageView);
                        titleTextView.setText(name);
                        locationTextView.setText(location);
                        priceTextView.setText(price);
                        ratingTextView.setText(ratingString);

                        // Add the item layout to the current row's LinearLayout
                        currentLinearLayout.addView(itemLayout);

                        count++;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled if needed
            }
        });



        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



        user= auth.getCurrentUser();
        String firstName = "";


            if(user != null){
                String userId = user.getUid();
                databaseRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {
                            // Retrieve the user data from the dataSnapshot
                            String firstName = snapshot.child("first_name").getValue(String.class);
                            String lastName = snapshot.child("last_name").getValue(String.class);

                            textView.setText(user.getEmail());
                            textView2.setText(firstName);
                            textView3.setText(lastName);
                            textView4.setText( user.getUid());

                        }

                        GoogleSignInAccount googleAccount = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                        if (googleAccount != null) {
                            String googleDisplayName = googleAccount.getDisplayName();
                            String googleEmail = googleAccount.getEmail();
                            String googleId = googleAccount.getId();

                            // Split the Google Display Name to get the first name
                            String[] nameParts = googleDisplayName.split(" ");
                            String googleFirstName = nameParts.length > 0 ? nameParts[0] : "";

                            String profileImageUrl = user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : "";


                            if (user.getProviderData().get(1).getProviderId().equals("google.com")) {
                                textView2.setText(googleFirstName);
                                textView3.setText(googleEmail);
                                textView4.setText(user.getUid());
                                if (!profileImageUrl.isEmpty()) {
                                    Picasso.get().load(profileImageUrl).into(img);
                                }
                            } else {
                                // The user signed in with another method (not Google)
                                textView2.setText(firstName);
                                textView3.setText(user.getEmail());
                                textView4.setText( user.getUid());
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

    public void notification(View view) {

        Intent intent = new Intent(this, Details.class);
        startActivity(intent);

    }
}