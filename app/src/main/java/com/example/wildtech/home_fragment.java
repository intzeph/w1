package com.example.wildtech;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link home_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class home_fragment extends Fragment {

    private LinearLayout currentLinearLayout;
    private FirebaseAuth auth;
    private TextView user_name;
    private AppBarLayout topAppBar;
    private BottomNavigationView bottomNavigationView;
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private ImageView img;
    private DatabaseReference databaseRef;
    private DatabaseReference databaseRef_offers;
    private FirebaseUser user;

    private GoogleSignInClient mGoogleSignInClient;

    List<item> itemList;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public home_fragment() {
        // Required empty public constructor
    }

    public static home_fragment newInstance(String param1, String param2) {
        home_fragment fragment = new home_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        currentLinearLayout = new LinearLayout(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_fragment, container, false);

        MaterialToolbar topAppBar = view.findViewById(R.id.topAppBar);



        auth = FirebaseAuth.getInstance();
        user_name = view.findViewById(R.id.user_name);


        TextView viewMore_tv = view.findViewById(R.id.viewMore_tv);
        Button bookBtn = view.findViewById(R.id.bookBtn);
        recyclerView = view.findViewById(R.id.cottages_sv);
        productAdapter = new ProductAdapter(getContext(), itemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(productAdapter);

        img = view.findViewById(R.id.profileImageView);

        int color = Color.parseColor("#F9F9F9");
        topAppBar.setBackgroundColor(color);

        List<item> roomItems = new ArrayList<>();
        List<item> itemList  = new ArrayList<>();
        List<item> poolItems = new ArrayList<>();
        List<item> popular = new ArrayList<>();

        databaseRef = FirebaseDatabase.getInstance(getString(R.string.firebase_url)).getReference("Users");
        databaseRef_offers = FirebaseDatabase.getInstance(getString(R.string.firebase_url)).getReference("Offers");
        user = FirebaseAuth.getInstance().getCurrentUser();

        //goHighlights
        viewMore_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getContext(), Account.class);
                intent.putExtra("show_highlights", true);
                startActivity(intent);
            }
        });

        //bookingProcess
        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getContext(), Account.class);
                intent.putExtra("show_booking1_process", true);
                startActivity(intent);
            }
        });


        //popular_layout done
        databaseRef_offers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot offerSnapshot : snapshot.getChildren()) {

                    String category = offerSnapshot.child("category").getValue(String.class);

                    if ("popular".equals(category)) {
                        String productID = offerSnapshot.child("productID").getValue(String.class);
                        String imageURL = offerSnapshot.child("image").getValue(String.class);
                        String name = offerSnapshot.child("name").getValue(String.class);
                        String location = offerSnapshot.child("location").getValue(String.class);
                        String price = offerSnapshot.child("price").getValue(String.class);
                        int rating = offerSnapshot.child("ratingAvg").getValue(Integer.class);

                        String ratingString = String.valueOf(rating);

                        item product = new item(productID, name, location, price, imageURL, rating);
                        popular.add(product);
                    }
                }
                RecyclerView recyclerView = view.findViewById(R.id.popular_sv);
                ProductAdapter productAdapter = new ProductAdapter(getContext(), popular);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                recyclerView.setAdapter(productAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //pools_layout done
        databaseRef_offers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                boolean addToLeft = true;

                for (DataSnapshot offerSnapshot : snapshot.getChildren()) {
                    String category = offerSnapshot.child("category").getValue(String.class);

                    if ("pools".equals(category)) {
                        String productID = offerSnapshot.child("productID").getValue(String.class);
                        String imageURL = offerSnapshot.child("image").getValue(String.class);
                        String name = offerSnapshot.child("name").getValue(String.class);
                        String location = offerSnapshot.child("location").getValue(String.class);
                        String price = offerSnapshot.child("price").getValue(String.class);
                        int rating = offerSnapshot.child("ratingAvg").getValue(Integer.class);

                        String ratingString = String.valueOf(rating);

                        item product = new item(productID, name, location, price, imageURL, rating);
                        poolItems.add(product);
                    }
                }
                RecyclerView recyclerView = view.findViewById(R.id.pools_sv);
                ProductAdapter productAdapter = new ProductAdapter(getContext(), poolItems);


                GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2, RecyclerView.HORIZONTAL, false);

                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(productAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //cottages_layout done
        databaseRef_offers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (DataSnapshot offerSnapshot : snapshot.getChildren()) {


                    String category = offerSnapshot.child("category").getValue(String.class);

                    if ("cottage".equals(category)) {
                        String productID = offerSnapshot.child("productID").getValue(String.class);
                        String imageURL = offerSnapshot.child("image").getValue(String.class);
                        String name = offerSnapshot.child("name").getValue(String.class);
                        String location = offerSnapshot.child("location").getValue(String.class);
                        String price = offerSnapshot.child("price").getValue(String.class);
                        int rating = offerSnapshot.child("ratingAvg").getValue(Integer.class);

                        String ratingString = String.valueOf(rating);

                        item product = new item(productID, name, location, price, imageURL, rating);
                        itemList.add(product);

                    }
                }

                RecyclerView recyclerView = view.findViewById(R.id.cottages_sv);
                ProductAdapter productAdapter = new ProductAdapter(getContext(), itemList);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                recyclerView.setAdapter(productAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //rooms_layout
        databaseRef_offers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



                boolean addToLeft = true;

                for (DataSnapshot offerSnapshot : snapshot.getChildren()) {
                    String category = offerSnapshot.child("category").getValue(String.class);
                    String subCategory = offerSnapshot.child("subCategory").getValue(String.class);

                    if ("room".equals(subCategory) || "room".equals(category) ) {
                        String productID = offerSnapshot.child("productID").getValue(String.class);
                        String imageURL = offerSnapshot.child("image").getValue(String.class);
                        String name = offerSnapshot.child("name").getValue(String.class);
                        String location = offerSnapshot.child("location").getValue(String.class);
                        String price = offerSnapshot.child("price").getValue(String.class);
                        int rating = offerSnapshot.child("ratingAvg").getValue(Integer.class);
                        String ratingString = String.valueOf(rating);

                        item product = new item(productID, name, location, price, imageURL, rating);
                        roomItems.add(product);


                    }
                }

                RecyclerView recyclerView = view.findViewById(R.id.rooms_sv);
                ProductAdapter productAdapter = new ProductAdapter(getContext(), roomItems);

                // Set up a GridLayoutManager with horizontal orientation and 2 columns
                GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2, RecyclerView.HORIZONTAL, false);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(productAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled if needed
            }
        });

        //highlights_layout
        databaseRef_offers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = 0;
                LinearLayout currentLinearLayout = null;

                LinearLayout linearLayout = view.findViewById(R.id.highlight_layout);

                for (DataSnapshot offerSnapshot : snapshot.getChildren()) {
                    String category = offerSnapshot.child("category").getValue(String.class);

                    if ("highlight".equals(category)) {
                        if (count % 2 == 0) {
                            if (getContext() != null) {
                                currentLinearLayout = new LinearLayout(getContext());
                                currentLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
                                linearLayout.addView(currentLinearLayout);
                            }
                        }

                        // inflate the item layout
                        if (getContext() != null){
                            View itemLayout = getLayoutInflater().inflate(R.layout.highlights_layout, null);
                            ImageView imageView = itemLayout.findViewById(R.id.highlight_img);
                            String imageURL = offerSnapshot.child("image").getValue(String.class);
                            Picasso.get().load(imageURL).fit().into(imageView);

                            // Add the item layout to the current row's LinearLayout
                            if(currentLinearLayout !=null){
                                currentLinearLayout.addView(itemLayout);
                            }
                        }
                        count++;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled if needed
            }
        });


        user= auth.getCurrentUser();

        if(user != null){
            String userId = user.getUid();
            databaseRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {


                    if (snapshot.exists()) {
                        String firstName = snapshot.child("first_name").getValue(String.class);
                        user_name.setText(firstName);
                    }

                    if (isAdded()){
                        GoogleSignInAccount googleAccount = GoogleSignIn.getLastSignedInAccount(requireContext());
                        if (googleAccount != null) {
                            String googleDisplayName = googleAccount.getDisplayName();
                            String googleEmail = googleAccount.getEmail();
                            String googleId = googleAccount.getId();

                            // gonna to split the google name into first name only
                            String[] nameParts = googleDisplayName.split(" ");
                            String googleFirstName = nameParts.length > 0 ? nameParts[0] : "";

                            String profileImageUrl = user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : "";


                            if (user.getProviderData().get(1).getProviderId().equals("google.com")) {
                                user_name.setText(googleFirstName);
//                                textView3.setText(googleEmail);
//                                textView4.setText(user.getUid());
                                if (!profileImageUrl.isEmpty()) {
                                    Picasso.get().load(profileImageUrl).into(img);
                                }
                            }
                        }
                    }




                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        else{

            Intent intent = new Intent(requireContext(),login.class);
            startActivity(intent);
            requireActivity().finish();

        }




        return view;
    }
}