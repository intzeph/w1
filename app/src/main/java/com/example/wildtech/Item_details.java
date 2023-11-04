package com.example.wildtech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Button;

import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class Item_details extends AppCompatActivity {

    DatabaseReference databaseReference,databaseRef;

    Button bookBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        Intent intent = getIntent();

        bookBtn=findViewById(R.id.bookBtn);


        String incomingProductID = intent.getStringExtra("product_id");


        if(incomingProductID != null){
            Log.d("Item_details", "Received productID: " + incomingProductID);


            databaseReference = FirebaseDatabase.getInstance(getString(R.string.firebase_url)).getReference("Offers");
            databaseRef = FirebaseDatabase.getInstance(getString(R.string.firebase_url)).getReference("Offers");


            databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.d("Item_details", "Snapshot: " + snapshot.toString());
                    boolean foundProduct = false;

                    for(DataSnapshot productSnapshot : snapshot.getChildren()){
                        String productID = productSnapshot.child("productID").getValue(String.class);
                        Log.d("Item_details", "Product ID from snapshot: " + productID);

                        if(productID !=null && productID.equals(incomingProductID)){
                            String item_name = productSnapshot.child("name").getValue(String.class);
                            String item_price = productSnapshot.child("price").getValue(String.class);
                            String item_location = productSnapshot.child("location").getValue(String.class);
                            String item_image = productSnapshot.child("image").getValue(String.class);
                            String item_details= productSnapshot.child("details").getValue(String.class);
                            int item_rating = productSnapshot.child("ratingAvg").getValue(Integer.class);
                            String ratingString = String.valueOf(item_rating);
                            String item_avRating = productSnapshot.child("ratings").getValue(String.class);

                            ImageView imageView = findViewById(R.id.item_image);
                            TextView itemNameView = findViewById(R.id.item_name);
                            TextView itemLocationView = findViewById(R.id.item_location);
                            TextView itemPriceView = findViewById(R.id.item_price);
                            TextView itemDetails = findViewById(R.id.item_details);
                            TextView itemRatings = findViewById(R.id.item_rating);
                            TextView itemRatingAvg = findViewById(R.id.rate_by);

                            Picasso.get().load(item_image).fit().into(imageView);
                            itemNameView.setText(item_name);
                            itemPriceView.setText(item_price);
                            itemLocationView.setText(item_location);
                            itemDetails.setText(item_details);
                            itemRatings.setText(ratingString);
                            itemRatingAvg.setText("("+item_avRating+")");

                            foundProduct = true;
                            break;
                        }

                    }
                    if (!foundProduct) {
                        Log.e("Item_details", "Product not found for productID: " + incomingProductID);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else {
            Log.e("Item_details", "Invalid or missing productID");
        }


        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Item_details.this, Account.class);
                intent.putExtra("show_booking1_process", true);
                startActivity(intent);
            }
        });





    }

    public void backBtn(View view) {
        onBackPressed();
    }

    public void shareBtn(View view) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Content to share");

        // Create and start the chooser
        Intent chooser = Intent.createChooser(shareIntent, "Share via");
        if (shareIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        }
    }
}