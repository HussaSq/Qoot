package com.example.qoot;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.content.Intent.getIntent;


/**
 * A simple {@link Fragment} subclass.
 */
public class tab1 extends Fragment {

    TextView textView;
    TextView dateTimeDisplay;
    Calendar calendar;
    int day,month,year;
    EditText mType,mNumOfGuest,mTime,mNotes,mLocation,mDate;
    FloatingActionButton submit;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    String type,numOfGuest,userId,time,date,location,name;
    private static final String TAG = "tab1";



    public tab1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab1, container, false);
        dateTimeDisplay = (TextView)view.findViewById(R.id.pickUpDate);
        calendar = Calendar.getInstance();
        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH)+1;
        day=calendar.get(Calendar.DAY_OF_MONTH);
        dateTimeDisplay.setText(year+"/"+month+"/"+day);

        textView = (TextView) view.findViewById(R.id.pickUpTime);
        textView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(), "TimePicker");
            }

        });
        mType = view.findViewById(R.id.FoodType);
        mNumOfGuest = view.findViewById(R.id.numberOfGuest);
        // mTime = view.findViewById(R.id.pickUpTime);
        mNotes = view.findViewById(R.id.note);
        submit = view.findViewById(R.id.submitReq);
        mLocation= view.findViewById(R.id.location);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = mType.getText().toString();
                numOfGuest = mNumOfGuest.getText().toString();
                date = dateTimeDisplay.getText().toString();
                time=textView.getText().toString();
                location=mLocation.getText().toString();

                db= FirebaseFirestore.getInstance();

                //check fields

                if (TextUtils.isEmpty(type)) {
                    mType.setError("Please Enter Your Event Type, It is Required");
                    return;
                }   if (TextUtils.isEmpty(numOfGuest)) {
                    mNumOfGuest.setError("Please Enter Amount Of Guests , It is Required");
                    return;
                }   if (TextUtils.isEmpty(time)) {
                    mTime.setError("Please Enter Pick Up Time, It is Required");
                    return;
                }
                if (TextUtils.isEmpty(location)) {
                    mLocation.setError("Please Enter Your Event Location, It is Required");
                    return;
                }
                //check if there is no characters in this fields
                numOfGuest = mNumOfGuest.getText().toString();
                if (containsLetters(numOfGuest)) {
                    mNumOfGuest.setError("Enter  numbers with no letters");
                    return;
                }

//*********************************************** DO NOT FORGET TIME YOU NEED TO CHECK THAT.************************
                // here i will send the request to database ,
                //Intent intent = getIntent();
                Intent intent=getActivity().getIntent();


                userId = intent.getStringExtra("user");

                name = intent.getStringExtra("Name");
                // userId=mAuth.getCurrentUser().getUid();
                db= FirebaseFirestore.getInstance();
                //String reqId = UUID.randomUUID().toString();

                // DocumentReference documentReference=db.collection("Requests").document(reqId);
                Map<String,Object> request = new HashMap<>();
                request.put("TypeOfEvent",type);
                request.put("NumberOfGuests",numOfGuest);
                request.put("Time",time);
                request.put("Date",date);
                request.put("Location",location);
                request.put("Note",""+mNotes.getText().toString());
                request.put("State","Pending");
                request.put("DonatorID",userId);
                request.put("DonatorName",name);
                request.put("VolnteerID","--");
                request.put("VolnteerName","--");

                db.collection("Requests")
                        .add(request)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });

               /* documentReference.set(request).addOnSuccessListener(new OnSuccessListener<Void>() {

                    @Override
                    public void onSuccess(Void aVoid) {

                        Log.d(TAG," Your Request Submitted Successfully");

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG,"Something Went Wrong , Try Again ");
                    }
                });*/
            }
        });







        return view;

    }
    public void submitReq(View view) {

    }


    public final boolean containsLetters(String s) {
        boolean containsLetters = false;

        if (s != null && !s.isEmpty()) {
            for (char c : s.toCharArray()) {
                if (containsLetters = Character.isLetter(c)) {
                    break;
                }
            }
        }

        return containsLetters;
    }

}

