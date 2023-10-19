package product_details;

import static android.content.Intent.getIntent;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wildtech.Account;
import com.example.wildtech.Item_details;
import com.example.wildtech.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link product_details_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class product_details_fragment extends Fragment {


    DatabaseReference databaseReference,databaseRef;
    Button bookBtn;


    ImageView shareBtn;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public product_details_fragment() {
        // Required empty public constructor
    }

    public static product_details_fragment newInstance(String param1, String param2) {
        product_details_fragment fragment = new product_details_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_product_details_fragment, container, false);





        shareBtn=view.findViewById(R.id.shareBtn);
        Bundle args = getArguments();

        databaseReference = FirebaseDatabase.getInstance(getString(R.string.firebase_url)).getReference("Offers");
        databaseRef = FirebaseDatabase.getInstance(getString(R.string.firebase_url)).getReference("Offers");


        if (args != null) {
            String incomingProductID = args.getString("product_id");


            if (incomingProductID != null) {
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

                                ImageView imageView = view.findViewById(R.id.item_image);
                                TextView itemNameView = view.findViewById(R.id.item_name);
                                TextView itemLocationView = view.findViewById(R.id.item_location);
                                TextView itemPriceView = view.findViewById(R.id.item_price);
                                TextView itemDetails = view.findViewById(R.id.item_details);
                                TextView itemRatings = view.findViewById(R.id.item_rating);
                                TextView itemRatingAvg = view.findViewById(R.id.rate_by);

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

            } else {
                Log.e("Item_details", "Invalid or missing productID");
            }
        }


        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getContext(), Account.class);
                intent.putExtra("show_item_details", true);
                startActivity(intent);
            }
        });



        //share btn
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "Content to share");

                    // Create and start the chooser
                    Intent chooser = Intent.createChooser(shareIntent, "Share via");
                    if (shareIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(chooser);
                    }

            }
        });




        return view;
    }



}