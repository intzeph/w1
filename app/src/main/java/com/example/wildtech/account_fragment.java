package com.example.wildtech;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.sql.Struct;

import account_contents.personal_info_fragment;


public class account_fragment extends Fragment {

    FirebaseAuth auth;

    FirebaseUser user;
    GoogleSignInClient mGoogleSignInClient;
    BottomNavigationView bottomNavigationView;

    DatabaseReference databaseRef;

    TextView user_name;

    ImageView imgProfile;

    LinearLayout sign_out_btn;



    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public account_fragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static account_fragment newInstance(String param1, String param2) {
        account_fragment fragment = new account_fragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_account_fragment, container, false);

        LinearLayout profile_info_btn= view.findViewById(R.id.profile_info_btn);
        LinearLayout change_pass_btn= view.findViewById(R.id.change_pass_btn);
        LinearLayout my_review_btn= view.findViewById(R.id.my_review_btn);
        LinearLayout language_btn= view.findViewById(R.id.language_btn);
        LinearLayout about_btn = view.findViewById(R.id.about_btn);
        LinearLayout terms_condi_btn= view.findViewById(R.id.terms_condi_btn);
        LinearLayout privacy_policy_btn= view.findViewById(R.id.privacy_policy_btn);
        LinearLayout delete_acc_btn=view.findViewById(R.id.delete_acc_btn);

        user_name=view.findViewById(R.id.U_name);
        imgProfile=view.findViewById(R.id.AprofileImageView);

        auth= FirebaseAuth.getInstance();

        sign_out_btn = view.findViewById(R.id.sign_out_btn);

        user = FirebaseAuth.getInstance().getCurrentUser();

        databaseRef = FirebaseDatabase.getInstance(getString(R.string.firebase_url)).getReference("Users");

        profile_info_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Account.class);
                intent.putExtra("show_personal_info", true);
                startActivity(intent);

            }
        });
        change_pass_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Account.class);
                intent.putExtra("show_change_password", true);
                startActivity(intent);
            }
        });
        my_review_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Account.class);
                intent.putExtra("show_my_review", true);
                startActivity(intent);
            }
        });
        language_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Account.class);
                intent.putExtra("show_language", true);
                startActivity(intent);
            }
        });

        about_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Account.class);
                intent.putExtra("show_about_us", true);
                startActivity(intent);
            }
        });

        terms_condi_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getContext(), Account.class);
                intent.putExtra("show_terms", true);
                startActivity(intent);
            }
        });

        privacy_policy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Account.class);
                intent.putExtra("show_privacy", true);
                startActivity(intent);
            }
        });
        delete_acc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getContext(), Account.class);
                intent.putExtra("show_del_account", true);
                startActivity(intent);

            }
        });



        sign_out_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Return to the login activity to allow signing in with another Google account
                            Intent intent = new Intent(getContext(), login.class);
                            startActivity(intent);
                            requireActivity().finish();

                            Toast.makeText(getContext(), "Logout successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            // Handle sign-out failure
                            Toast.makeText(getContext(), "Sign-out failed", Toast.LENGTH_SHORT).show();
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
                    String lastName = snapshot.child("last_name").getValue(String.class);
                    if(snapshot.exists()){
                        user_name.setText(firstName+" "+lastName);
                    }
                    GoogleSignInAccount googleAccount = GoogleSignIn.getLastSignedInAccount(getContext());
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
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else{
            Intent intent = new Intent(getContext(), login.class);
            startActivity(intent);
            requireActivity().finish();
        }

        return  view;
    }
}