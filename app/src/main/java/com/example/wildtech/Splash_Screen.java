package com.example.wildtech;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.text.TextRunShaper;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Splash_Screen extends AppCompatActivity {

    ViewPager viewPager;
    LinearLayout dotsLayout;

    sliderAdapter SliderAdapter;

    TextView[] dots;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        viewPager = findViewById(R.id.slider);
        dotsLayout = findViewById(R.id.dots);

        SliderAdapter = new sliderAdapter(this);

        viewPager.setAdapter(SliderAdapter);
        addDots(0);
        viewPager.addOnPageChangeListener(changeListener);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean termsAccepted = sharedPreferences.getBoolean("termsAccepted", false);

        Log.d("Splash_Screen", "termsAccepted: " + termsAccepted);
        Log.d("Splash_Screen", "user: " + user);

        if (termsAccepted) {

            Log.d("Splash_Screen", "Redirecting to login");
            startActivity(new Intent(Splash_Screen.this, login.class));
            finish();
        }
        else{
            Log.d("Splash_Screen", "Redirecting to login (terms not accepted)");
        }



        if (user != null) {
            Log.d("Splash_Screen", "Redirecting to MainActivity");

            startActivity(new Intent(Splash_Screen.this, MainActivity.class));
            finish(); // Finish the Splash_Screen activity.
        } else {
            Log.d("Splash_Screen", "User is not logged in");
        }

    }




    private void addDots(int position){

        dots=new TextView[3];
        dotsLayout.removeAllViews();

        for(int i=0; i<dots.length; i++){
            dots[i] = new TextView(this);
            dots[i].setText("•");
            dots[i].setTextSize(35);

            dotsLayout.addView(dots[i]);


        }

        if(dots.length>0){
            dots[position].setTextColor(ContextCompat.getColor(this, R.color.app_green));

        }


    }

    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public void getStartedBtn(View view) {

        Intent intent = new Intent(this, TermsAndCondition.class);
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //user click yes
                finish();
                System.exit(0);


            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //user click no

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }
}