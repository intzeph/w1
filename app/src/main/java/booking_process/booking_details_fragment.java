package booking_process;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wildtech.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class booking_details_fragment extends Fragment {


    private ImageView back_btn, room_preview;

    private LinearLayout start_date, end_date;

    private TextView start_date_textView, end_date_textView,price_room_preview;

    private DatabaseReference databaseRef_offers;

    private int value = 1;

    private int C_value = 0;


    private Spinner productSpinner;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public booking_details_fragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static booking_details_fragment newInstance(String param1, String param2) {
        booking_details_fragment fragment = new booking_details_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_booking_details_fragment, container, false);

        MaterialToolbar topAppBar = view.findViewById(R.id.topAppBar_bt);
        int color = Color.parseColor("#F9F9F9");
        topAppBar.setBackgroundColor(color);

        back_btn= view.findViewById(R.id.back_icon);
        start_date_textView= view.findViewById(R.id.start_date_textView);
        end_date=view.findViewById(R.id.end_date);
        end_date_textView=view.findViewById(R.id.end_date_textView);
        start_date=view.findViewById(R.id.start_date);

        room_preview=view.findViewById(R.id.room_preview);
        price_room_preview=view.findViewById(R.id.price_room_preview);

        // Get the current date
        Date currentDate = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = dateFormat.format(currentDate);

        start_date_textView.setText(dateString);

        end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_endDialog();
            }
        });

        start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });



        EditText editText = view.findViewById(R.id.AeditText);
        EditText CeditText = view.findViewById(R.id.CeditText);
        ImageView arrowUp = view.findViewById(R.id.Aarrow_up);
        ImageView arrowDown = view.findViewById(R.id.Aarrow_down);

        ImageView CarrowUp = view.findViewById(R.id.C_arrow_up);
        ImageView CarrowDown = view.findViewById(R.id.C_arrow_down);



        databaseRef_offers = FirebaseDatabase.getInstance(getString(R.string.firebase_url)).getReference("Offers");


        CarrowUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                C_value++;
                CeditText.setText(String.valueOf(C_value));
            }
        });

        CarrowDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (C_value >0 ) {
                    // Decrement the value (if it's greater than 1)
                    C_value--;
                    CeditText.setText(String.valueOf(C_value));


                }

            }
        });

        CeditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    // Update the value when the text changes
                    C_value = Integer.parseInt(s.toString());
                } catch (NumberFormatException e) {
                    // Handle the case where the input is not a valid number
                    C_value = 0; // You can set a default value or handle the error as needed
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        arrowUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Increment the value
                value++;
                editText.setText(String.valueOf(value));





            }
        });

        arrowDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (value > 1) {
                    // Decrement the value (if it's greater than 1)
                    value--;
                    editText.setText(String.valueOf(value));


                }
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    // Update the value when the text changes
                    value = Integer.parseInt(charSequence.toString());
                } catch (NumberFormatException e) {
                    // Handle the case where the input is not a valid number
                    value = 0; // You can set a default value or handle the error as needed
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not needed
            }
        });


        productSpinner = view.findViewById(R.id.productSpinner);

        Query query = databaseRef_offers.orderByChild("category").equalTo("room");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> roomsNames = new ArrayList<>();
                HashMap<String, String> productImageMap = new HashMap<>();
                HashMap<String, String> productPriceMap = new HashMap();


                roomsNames.add("None");

                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    String roomsName = productSnapshot.child("name").getValue(String.class);
                    String productImageURL = productSnapshot.child("image").getValue(String.class);
                    String roomPrice = productSnapshot.child("price").getValue(String.class);

                    roomsNames.add(roomsName);


                    productPriceMap.put(roomsName, roomPrice);
                    productImageMap.put(roomsName, productImageURL);
                }
                // Now that you have the product names, you can populate the Spinner
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, roomsNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                productSpinner.setAdapter(adapter);

                productSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        String selectedProduct = productSpinner.getSelectedItem().toString();
                        String imageURL = productImageMap.get(selectedProduct);

                        DataSnapshot productSnapshot = snapshot.child(selectedProduct);

                        String Price = productSnapshot.child("price").getValue(String.class);

                        if (imageURL != null) {
                            // Load the image from Firebase Storage and display it in the room_preview ImageView using Picasso
                            Picasso.get()
                                    .load(imageURL)
                                    .into(room_preview);
                        } else {
                            // Handle the case where there's no image for the selected product
                            // You can set a default image or clear the room_preview ImageView, as needed
                            room_preview.setImageDrawable(null); // Clears the ImageView
                        }

                        String price = productPriceMap.get(selectedProduct);

                        if (price != null) {
                            // Display the price in a TextView (replace with your actual TextView ID)
                            price_room_preview.setText("Price: " + price);
                        } else {
                            // Handle the case where there's no price for the selected product
                            price_room_preview.setText("Price: N/A");
                        }


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // Handle the case where nothing is selected if needed
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
















        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getActivity() !=null){
                    getActivity().onBackPressed();
                }

            }
        });

        return view;
    }

    private void open_endDialog(){
        // Get the current date
        Calendar currentDate = Calendar.getInstance();

        currentDate.add(Calendar.DAY_OF_MONTH, 1);

        DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Check if the selected date is not earlier than tomorrow
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);
                if (selectedDate.before(currentDate)) {
                    // The selected date is earlier than tomorrow, show a message
                    Toast.makeText(getContext(), "Please select a date not earlier than tomorrow.", Toast.LENGTH_SHORT).show();
                } else {
                    // Set the selected date as the text in the "End" TextView
                    end_date_textView.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(year));
                }
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));

        dialog.show();
    }

    private void openDialog(){

        Calendar currentDate = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);
                if (selectedDate.before(currentDate)) {

                    Toast.makeText(getContext(), "Please select a date not earlier than today.", Toast.LENGTH_SHORT).show();
                } else {

                    start_date_textView.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(year));
                }
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));

        dialog.show();
    }






}