package com.example.qoot;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class tab1 extends Fragment {

    TextView textView;
    TextView dateTimeDisplay;
    Calendar calendar;
    int day,month,year;
    FloatingActionButton floatingActionButton;

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

        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.sendrequest);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(),DonatorRequests.class));
            }

        });
        return view;

    }
}