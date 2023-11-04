package com.example.wildtech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    MaterialToolbar topAppBar;
    FirebaseAuth auth;

    TextView textView, user_name, textView3, textView4, textView5, textView6,textView7,textView8;
    FirebaseUser user;

    ImageView popular_img;

    DatabaseReference databaseRef, databaseRef_offers;

    GoogleSignInClient mGoogleSignInClient;

    ImageView img;

    BottomNavigationView bottomNavigationView;

    RecyclerView recyclerView;
    ProductAdapter productAdapter;

   List<item> itemList;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        auth=FirebaseAuth.getInstance();
//        user_name = findViewById(R.id.user_name);
//        topAppBar= findViewById(R.id.topAppBar);
        bottomNavigationView = findViewById(R.id.h_bottom_navigation);
//        recyclerView = findViewById(R.id.cottages_sv);
//        productAdapter = new ProductAdapter(this, itemList);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        recyclerView.setAdapter(productAdapter);
//        img = findViewById(R.id.profileImageView);




//        int color = Color.parseColor("#F9F9F9");
//
//        topAppBar.setBackgroundColor(color);


//
//        databaseRef = FirebaseDatabase.getInstance(getString(R.string.firebase_url)).getReference("Users");
//
//        databaseRef_offers = FirebaseDatabase.getInstance(getString(R.string.firebase_url)).getReference("Offers");
//
//        user = FirebaseAuth.getInstance().getCurrentUser();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){

                case R.id.home_nav:
                    loadFragment(new home_fragment());
                    return true;
                case R.id.account_nav:
                    loadFragment(new account_fragment());
                    return true;
                case R.id.booking_nav:
                    loadFragment(new booking_fragment());
                    return  true;

                case R.id.direction_nav:
                    loadFragment(new direction_fragment());
                    return true;
            }
            return false;
        });

        //default fragment when app starts
        loadFragment(new home_fragment());

    }
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
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