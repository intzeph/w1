package com.example.wildtech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hbb20.CountryCodePicker;

public class VerifyPhoneNumber extends AppCompatActivity {




    EditText phoneInput;

    Button sendOtpBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_number);


        phoneInput = findViewById(R.id.phone_number);
        sendOtpBtn = findViewById(R.id.send_otp_btn);

        sendOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = phoneInput.getText().toString().trim();

                if (phoneNumber.isEmpty()) {
                    phoneInput.setError("Phone number cannot be empty");
                } else {
                    Intent intent = new Intent(VerifyPhoneNumber.this, VerifyOTP.class);
                    intent.putExtra("phone", phoneNumber);
                    startActivity(intent);
                }
            }
        });


    }


//    public void callVerificationScreen(View view) {
//
//
//        String _getUserEnteredPhoneNumber = phoneNumber.getText().toString().trim(); // Get Phone Number
//
////        if (_getUserEnteredPhoneNumber.charAt(0) == '0') {
////            _getUserEnteredPhoneNumber = _getUserEnteredPhoneNumber.substring(1);
////        }
//       String _phoneNo = _getUserEnteredPhoneNumber;
////        final String _phoneNo = "+" + countryCodePicker.getFullNumber() + _getUserEnteredPhoneNumber;
//
//
//        Intent intent = new Intent(getApplicationContext(), VerifyOTP.class);
//        intent.putExtra("phoneNo", _phoneNo);
//        startActivity(intent);
//
//
////        if (countryCodePicker != null) {
////
////
////        } else {
////            // Handle the case where countryCodePicker is null (optional)
////        }
//
//
//    }
}