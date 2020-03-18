package com.example.qoot;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.internal.OnConnectionFailedListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback, OnConnectionFailedListener, GoogleApiClient.OnConnectionFailedListener {


    private GoogleMap mMap;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    String userId;
    private PlaceAutocompleteAdapter mAdapter;
    private GoogleApi Client;
    Marker m;
    FusedLocationProviderClient mFusedLocationClient;

    //private
    Button Choose;
    String location, req;
    String loc;
    double lat, lang;
    Location ADDRESS;
    private static final LatLngBounds BOUNDRY = new LatLngBounds(new LatLng(-40, -168), new LatLng(71, 136));

    //widget section here
    //private AutoCompleteTextView mSearch;
    private AutoCompleteTextView mSearch;
    private ImageView GoBack;

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Choose = findViewById(R.id.pickLocation);
        mAuth = FirebaseAuth.getInstance();
        mSearch = findViewById(R.id.SearchBox);
        db = FirebaseFirestore.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        GoBack = findViewById(R.id.imageView);

        mSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH
                        || i == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {
                    // now we're ok to search
                    GeoLocate();
                }
                return false;
            }
        });

        GoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MapsActivity2.this, requestForm.class);
                startActivity(i);
            }
        });
    }

    private void GeoLocate() {
        String search = mSearch.getText().toString();
        Geocoder geocoder = new Geocoder(MapsActivity2.this);
        List<Address> list = new ArrayList<>();
        if (m != null)
            mMap.clear();

        if (search != null) {
            try {
                list = geocoder.getFromLocationName(search, 1);
            } catch (Exception e) {
                Log.e("TAG", e.getMessage().toString());
            }

            if (list.size() > 0) {
                Address address = list.get(0);
                //----------------------------------------------هنا ناخذ من البحث اللوكيشن-----------------
                // Add a marker in Sydney and move the camera
                lang = address.getLongitude();
                lat = address.getLatitude();
                LatLng Point = new LatLng(lat, lang);
                // address.get
                m = mMap.addMarker(new MarkerOptions().position(Point).title("" + address.getAddressLine(0)));
                //  mMap.moveCamera(CameraUpdateFactory.newLatLng(Point));
                loc = lat + " ," + lang;

                CameraPosition myPosition = new CameraPosition.Builder()
                        .target(Point).zoom(17).bearing(90).tilt(30).build();
                mMap.animateCamera(
                        CameraUpdateFactory.newCameraPosition(myPosition));

            }
        } else {
            mSearch.clearComposingText();
            mMap.clear();
        }

        Choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mMap.getMyLocation();
                Bundle extras = getIntent().getExtras();
                req = extras.getString("RequestID");
                DocumentReference documentReference = db.collection("Requests").document(req);
                documentReference.update("Location", loc).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText( MapsActivity2.this,"Request is Submitted Successfully",Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(MapsActivity2.this, DonatorRequests.class);
                        startActivity(i);
                    }
                });
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mMap.setMyLocationEnabled(true);

        if (mFusedLocationClient != null){
            mFusedLocationClient.getLastLocation().addOnCompleteListener(
                new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        ADDRESS = task.getResult();
                        lat =  ADDRESS.getLatitude();
                        lang = ADDRESS.getLongitude();
                        LatLng ur = new LatLng(lat,lang);
                        Bundle extras = getIntent().getExtras();
                        req= extras.getString("RequestID");
                        location=lat+","+lang;
                     m = mMap.addMarker(new MarkerOptions().position(ur).title(ur.toString()));
                      CameraPosition myPosition = new CameraPosition.Builder().target(ur).zoom(17).bearing(90).tilt(30).build();
                           mMap.animateCamera(
                            CameraUpdateFactory.newCameraPosition(myPosition));

                        mSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                            @Override
                            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                                if (i == EditorInfo.IME_ACTION_SEARCH
                                        || i == EditorInfo.IME_ACTION_DONE
                                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {
                                    // now we're ok to search
                                    GeoLocate();
                                }
                                return false;
                            }
                        });
                        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                            @Override
                            public void onMapClick(LatLng latLng) {
                                if (m != null)
                                    mMap.clear();
                                m = mMap.addMarker(new MarkerOptions().position(latLng).title("" + latLng.toString()));
                                lat = latLng.latitude;
                                lang = latLng.longitude;
                                location=lat+","+lang;
                                CameraPosition myPosition = new CameraPosition.Builder()
                                        .target(latLng).zoom(17).bearing(90).tilt(10).build();
                                mMap.animateCamera(
                                        CameraUpdateFactory.newCameraPosition(myPosition));
                            }
                        });
                        Choose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DocumentReference documentReference =db.collection("Requests").document(req);
                                documentReference.update("Location",location).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText( MapsActivity2.this,"Request is Submitted Successfully",Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(MapsActivity2.this, DonatorRequests.class);
                                        startActivity(i);
                                    }
                                });
                            }
                        });
                    }
                }
            );
        }


        // --------------------------HERE WHERE WE GOT THE ERRORS ----------------------------------
        //noinspection deprecation
//         Client = new GoogleApi
//                 .Builder(this)
//                .addApi(Places.GEO_DATA_API)
//                .addApi(Places.PLACE_DETECTION_API)
//                .enableAutoManage(this,this).build();
//        //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
//      mAdapter = new PlaceAutocompleteAdapter(this, Client,BOUNDRY, null);

        // --------------------------HERE WHERE WE GOT THE ERRORS ----------------------------------

//        mSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//                if (i == EditorInfo.IME_ACTION_SEARCH
//                        || i == EditorInfo.IME_ACTION_DONE
//                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
//                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {
//                    // now we're ok to search
//                    GeoLocate();
//
//                }
//                return false;
//            }
//        });




    }
}

