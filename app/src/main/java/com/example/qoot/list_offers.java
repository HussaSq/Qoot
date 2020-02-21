package com.example.qoot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.annotation.Documented;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.zip.Inflater;

import java.util.ArrayList;

public class list_offers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_offers);
    }

    /*class MyCustomAdapter extends BaseAdapter
    {
        ArrayList<single_offer> Items=new ArrayList<single_offer>();
        MyCustomAdapter(ArrayList<single_offer> Items ) {
            this.Items=Items; }

        @Override
        public int getCount() {
            return Items.size();
        }

        @Override
        public String getItem(int position) {
            return Items.get(position).username;
        }

        @Override
        public long getItemId(int position) {
            return  position; }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater linflater =getLayoutInflater();
            View view1=linflater.inflate(R.layout.activity_single_offer, null);

            TextView txtname =(TextView) view1.findViewById(R.id.username);
            TextView txtaway =(TextView) view1.findViewById(R.id.away_text);
            TextView textvehicle  =(TextView)  view1.findViewById(R.id.vehicle_text);
            TextView textvehicle_type=(TextView)  view1.findViewById(R.id.vehicle_type);
            ImageView imageuser=(ImageView) view1.findViewById(R.id.userimage);
            ImageView imagerate=(ImageView) view1.findViewById(R.id.Rateimsge);



            textvehicle.setText(Items.get(i).vehicle_text);
            textvehicle_type.setText(Items.get(i).vehicle_type);
            txtname.setText(Items.get(i).username);
            txtaway.setText(Items.get(i).away_text);
            imagerate.setImageIcon(@);Items.get(i).userimage);

            return view1;

        }


    }
    }*/


    public void OoenDonatorRequestInfo(View view) {
        startActivity(new Intent(list_offers.this,DonatorRequestInfo.class));
    }

}