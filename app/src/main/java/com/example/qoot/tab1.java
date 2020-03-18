package com.example.qoot;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import static android.content.Intent.getIntent;


/**
 * A simple {@link Fragment} subclass.
 */
public class tab1 extends Fragment {

    TextView textView;
    TextView dateTimeDisplay;
    Calendar calendar;
    int day,month,year;
    EditText mNumOfGuest,mTime,mNotes;
    ImageView submit;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    String type,numOfGuest,userId,time,date,name;
    private static final String TAG = "tab1";
    Spinner events;
    String DonatorName;



    public tab1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab1, container, false);
        dateTimeDisplay = (TextView)view.findViewById(R.id.pickUpDate);

        events =(Spinner) view.findViewById(R.id.FoodType);
        final String[] eventTypes = new String[]{"Select Event Type","Wedding", "BBQ", "Small Party","Funeral","Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, eventTypes);
        events.setAdapter(adapter);




        calendar = Calendar.getInstance();
        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH)+1;
        day=calendar.get(Calendar.DAY_OF_MONTH);
        dateTimeDisplay.setText(month+"/"+day+"/"+year);



        textView = (TextView) view.findViewById(R.id.pickUpTime);
        textView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(), "TimePicker");
            }

        });




        //mType = view.findViewById(R.id.FoodType);
        mNumOfGuest = view.findViewById(R.id.numberOfGuest);
        // mTime = view.findViewById(R.id.pickUpTime);
        mNotes = view.findViewById(R.id.note);
        submit = view.findViewById(R.id.submitReq);
      //  mLocation= view.findViewById(R.id.location);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = events.getSelectedItem().toString();
                numOfGuest = mNumOfGuest.getText().toString();
                date = dateTimeDisplay.getText().toString();
                time=textView.getText().toString();
               // location=mLocation.getText().toString();
                db= FirebaseFirestore.getInstance();
                //check fields
                if (TextUtils.isEmpty(type)) {
                    //mType.setError("Please Enter Your Event Type, It is Required");
                    ((TextView)events.getSelectedView()).setError("Please Select Your Event Type, It is Required");
                    return;
                }if(type.equals("Select Event Type")){
                    //type=((TextView)events.getSelectedView());
                    //((TextView)events.getSelectedView()).setError("Please Select Your Event Type, It is Required");
                    TextView errorTextView=(TextView)events.getSelectedView();
                    errorTextView.setError("");
                    errorTextView.setTextColor(Color.RED);
                    errorTextView.setText("Select Event Type");
                    return;
                }
                /*type = events.getSelectedItem().toString();
                if (type.length()>20) {
                    mType.setError("The Characters Must Be At Most 20");
                    return;
                }*/
               if (TextUtils.isEmpty(numOfGuest)) {
                    mNumOfGuest.setError("Please Enter Amount Of Guests , It is Required");
                    return;
                }   if (TextUtils.isEmpty(time)) {
                    mTime.setError("Please Enter Pick Up Time, It is Required");
                    return;
                }
               // if (TextUtils.isEmpty(location)) {
                //    mLocation.setError("Please Enter Your Event Location, It is Required");
                 //   return;
              //  }
                //check if there is no characters in this fields
                numOfGuest = mNumOfGuest.getText().toString();
                if (containsLetters(numOfGuest)) {
                    mNumOfGuest.setError("Enter  numbers with no letters");
                    return;
                }
//*********************************************** DO NOT FORGET TIME YOU NEED TO CHECK THAT.************************
                // here i will send the request to database ,
                //Intent intent = getIntent();
                //Intent intent=getActivity().getIntent();
               // userId = intent.getStringExtra("user");
               // Toast.makeText( getActivity(),"user in tab"+userId,Toast.LENGTH_SHORT).show();

                 //userId=mAuth.getCurrentUser().getUid();
                mAuth = FirebaseAuth.getInstance();
                db = FirebaseFirestore.getInstance();
                userId = mAuth.getCurrentUser().getUid();



                //String reqId = UUID.randomUUID().toString();
               /* DocumentReference documentReference = db.collection("Donators").document(userId);
                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        name =(String)documentSnapshot.getString("UserName");
                    }
                });*/

                // DocumentReference documentReference=db.collection("Requests").document(reqId);

                DocumentReference VolRef=db.collection("Donators").document(userId);
                VolRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        DonatorName=documentSnapshot.getString("UserName") ;
                        //Toast.makeText( getContext(),"the donator name"+DonatorName,Toast.LENGTH_SHORT).show();
                       /* documentReference.update("VolnteerName", DonatorName).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }

                        });*/

                Map<String,Object> request = new HashMap<>();
                request.put("TypeOfEvent",type);
                request.put("NumberOfGuests",numOfGuest);
                request.put("Time",time);
                request.put("Date",date);
                request.put("Location","");
                request.put("Note",""+mNotes.getText().toString());
                request.put("State","Pending");
                request.put("DonatorID",userId);
                request.put("DonatorName",DonatorName);
                request.put("VolnteerID","--");
                request.put("VolnteerName","--");
                request.put("RequestID","--");
                request.put("RequestType","Urgent");

                db.collection("Requests")
                        .add(request)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                documentReference.update("RequestID",documentReference.getId()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                //    reqID= documentReference.getId();
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                      //  Toast.makeText( EditDonatorProfile.this,"user updated",Toast.LENGTH_SHORT).show();

                                    }
                                });
                               // Toast
                                //Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                               // Toast.makeText(getActivity(), "Your Request Submitted Successfully " , Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getActivity(), location.class);
                               // Toast.makeText(getActivity(),"ID "+documentReference.getId(),Toast.LENGTH_SHORT ).show();
                                i.putExtra("RequestID",documentReference.getId());
                                startActivity(i);
                                ((Activity) getActivity()).overridePendingTransition(0, 0);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Something Went Wrong,Try Again ! " , Toast.LENGTH_SHORT).show();
                               // Log.w(TAG, "Error adding document", e);
                            }
                        });

                    }
                });

               /*documentReference.set(request).addOnSuccessListener(new OnSuccessListener<Void>() {
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

