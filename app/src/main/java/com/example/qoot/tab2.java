package com.example.qoot;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class tab2 extends Fragment {
    TextView textView;
    TextView dateOfPickUp;
    String selectedDate;
    public static final int REQUEST_CODE = 11;
     //private OnFragmentInteractionListener mListener;
    public static tab2 newInstance() {
        tab2 fragment = new tab2();
        return fragment;
    }

    public tab2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab2, container, false);
        dateOfPickUp = (TextView) view.findViewById(R.id.pickUpDate1);
        final FragmentManager fm = ((AppCompatActivity)getActivity()).getSupportFragmentManager();
        //calendar = Calendar.getInstance();
        //year=calendar.get(Calendar.YEAR);
        //month=calendar.get(Calendar.MONTH)+1;
        //day=calendar.get(Calendar.DAY_OF_MONTH);
        //dateTimeDisplay.setText(year+"/"+month+"/"+day);

        dateOfPickUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create the datePickerFragment
                AppCompatDialogFragment newFragment = new DatePickerFragment();
                // set the targetFragment to receive the results, specifying the request code
                newFragment.setTargetFragment(tab2.this, REQUEST_CODE);
                // show the datePicker
                newFragment.show(fm, "datePicker");
            }
        });

        textView = (TextView) view.findViewById(R.id.pickUpTime);
        textView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(), "TimePicker");
            }

        });



        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check for the results
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // get date from string
            selectedDate = data.getStringExtra("selectedDate");
            // set the value of the editText
            dateOfPickUp.setText(selectedDate);
        }
    }


}

