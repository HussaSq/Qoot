package com.example.qoot;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Calendar;

import javax.annotation.Nullable;

public class VolunteerMap extends FragmentActivity implements OnMapReadyCallback {
    Bundle intent1;
    private GoogleMap mMap;
    String ReqIDDD;
    FirebaseFirestore db;
    String location;
    double lat,lang;
    ImageView back;
    String Abeer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        db = FirebaseFirestore.getInstance();
        intent1 = getIntent().getExtras();
        back= findViewById(R.id.back);

        if(intent1 != null){
            ReqIDDD = (String) intent1.getSerializable("RequestID");
            Abeer = (String)  intent1.getSerializable("Where");

            DocumentReference documentReference=db.collection("Requests").document(ReqIDDD);
            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    location=documentSnapshot.getString("Location") ;
                    lat=Double.parseDouble(location.substring(0,location.indexOf(',')));
                    lang=Double.parseDouble(location.substring(location.indexOf(',')+1));
                    //System.out.println("lat"+lat);
                    //System.out.println("lang"+lang);
                    //System.out.println("loxaa******************"+location);
                    LatLng loc = new LatLng(lat, lang);
                    mMap.addMarker(new MarkerOptions().position(loc).title(loc.toString()));
                    CameraPosition myPosition = new CameraPosition.Builder()
                            .target(loc).zoom(10).bearing(90).tilt(30).build();
                    mMap.animateCamera(
                            CameraUpdateFactory.newCameraPosition(myPosition));
                    //mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                    back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(VolunteerMap.this,VolunteerRequestInfo.class);
                            i.putExtra("RequestID",ReqIDDD);
                            i.putExtra("Where",Abeer);
                            startActivity(i);
                        }
                    });
                }
            });
            }//end of my if


        // Add a marker in Sydney and move the camera


    }
}
