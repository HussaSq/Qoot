package com.example.qoot;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class tab3 extends Fragment {
    LinearLayout linearLayout;

    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }


    public tab3() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_tab3, container, false);
        linearLayout = (LinearLayout) view.findViewById(R.id.req1);
        linearLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(),VolunteerRequestInfo.class));
            }

        });
        return view;
    }

    public void CreateXML (){

        // this is the bigger request layout ((Root))
        LinearLayout parent = new LinearLayout(mContext);
        parent.setLayoutParams(new LinearLayout.LayoutParams(160,125));
        //parent.setLeftTopRightBottom(15,6,5,10);
        parent.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new
                LinearLayout.LayoutParams(new LinearLayout.LayoutParams(160,125));
        layoutParams.setMargins(30, 20, 30, 0);
        parent.setClickable(true);
        //parent.addView(layoutParams);
       // parent.setBackground("#80E4E2E2");

        TextView event = new TextView(mContext);
        event.setLayoutParams(new
                LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        event.setText("EventType");
        event.setTextSize(22);

        parent.addView(event);

        LinearLayout child = new LinearLayout(mContext);
        child.setLayoutParams(new LinearLayout.LayoutParams(140,49));
        child.setOrientation(LinearLayout.HORIZONTAL);

        parent.addView(child);

        ImageView img = new ImageView(mContext);
        img.setLayoutParams(new LinearLayout.LayoutParams(30,50));
        img.setImageResource(R.drawable.pickuptime);
        parent.addView(img);

        TextView time = new TextView(mContext);
        time.setLayoutParams(new LinearLayout.LayoutParams(68,40));
        time.setText("Time");
        time.setTextSize(20);

        parent.addView(time);

        /*
         android:background="#80E4E2E2"
         android:src="@drawable/pickuptime" />

                <TextView
                    android:id="@+id/time1"
                    android:layout_width="68dp"
                    android:layout_height="40dp"
                    android:layout_marginLeft="10dp"
                    android:layout_marginTop="8dp"
                    android:text="time"
                    android:textSize="20dp" />
            </LinearLayout>
        */
    }

}
