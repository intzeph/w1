package com.example.wildtech;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class TermsAndCondition extends AppCompatActivity {


    CheckBox checkbox;
    ToggleButton button;

    Button exitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_condition);

        checkbox=findViewById(R.id.checkBox);
        button=findViewById(R.id.button1);
        exitBtn=findViewById(R.id.exitBtn);

        exitBtn.setBackgroundTintList(null);


        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkbox.isChecked()){

                    Drawable drawable=getResources().getDrawable(R.drawable .custom_blue_btn);
                    button.setBackground(drawable);
                    button.setEnabled(true);
                }else{
                    Drawable drawable=getResources().getDrawable(R.drawable .custom_grey);
                    button.setBackground(drawable);
                    button.setEnabled(false);
                }
            }
        });


    }



    public void privacy_continue(View view) {

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("termsAccepted", true);
        editor.apply();

        Intent intent= new Intent(this, login.class);
        startActivity(intent);
        finish();

    }

    public void exitBtn(View view) {


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
