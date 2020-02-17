package com.example.qoot;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class EditVolunteerProfile extends AppCompatActivity {

    Spinner cars;
    public EditText NEW_NAME;
    public EditText NEW_PHONE;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    String userId;
    String actualCar;



    //  save button
    Button saveButton;

    // what user type in fields
    String s1, s2, Uid;
    //String[]  types = new String[]{",","Small", "Medium", "Truck","None"};
    String[]  types = new String[]{"Sedan", "SUV", "Truck"};
    String[]  types1 = new String[]{".", "Medium", "Truck","None"};
    String urCar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_volunteer_profile);

        // DROP DOWN CODE
        cars = findViewById(R.id.carDD);

       /* final String[] types = new String[]{"Default","Small", "Medium", "Truck","None"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, types);
        cars.setAdapter(adapter);*/
        // I think here we need to fetch the type from DB.. not like the above




        NEW_NAME = findViewById(R.id.Name);
        NEW_PHONE = findViewById(R.id.Phone_v);


        saveButton = findViewById(R.id.button);


        // firebase initialize
        mAuth = FirebaseAuth.getInstance();

        db=FirebaseFirestore.getInstance();
        Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userId=mAuth.getCurrentUser().getUid();
        /* the last one
        DocumentReference documentReference =db.collection("Volunteers").document(Uid);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                types[0] = (String)documentSnapshot.getString("Vehicle")+",";
                magic.setText(documentSnapshot.getString("Vehicle"));
                urCar=magic.getText().toString() ;


            }
        });*/
        DocumentReference documentReference =db.collection("Volunteers").document(Uid);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                types1[0] = (String)documentSnapshot.getString("Vehicle")+"";
               // urCar = (String)documentSnapshot.getString("Vehicle")+"";
             //   magic.setText(documentSnapshot.getString("Vehicle"));
                int index =-1  ;
                for(int i=0;i<types.length;i++){
                   // Toast.makeText( EditVolunteerProfile.this,""+i,Toast.LENGTH_SHORT).show();
                    if(types1[0].equals(types[i]) ){
                        index = i;
                       // Toast.makeText( EditVolunteerProfile.this,"inside 1 if",Toast.LENGTH_LONG).show();
                        if(index != -1) {
                            String temp = types[0];
                            types[0] = types[index];
                            types[index] = temp;
                          //  Toast.makeText( EditVolunteerProfile.this,"inside 2 if",Toast.LENGTH_LONG).show();
                            return;
                        }// end if 2
                    }//end if

                }




            }
        });


        //  actualCar = ReadCar();







        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, types);
        cars.setAdapter(adapter);



        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //take from user
                s1 = NEW_NAME.getText().toString();
                s2 = NEW_PHONE.getText().toString();
                // read the actual car
                actualCar = ReadCar();
                int counter=0;
                //------ check name -------------
                if (containsDigit(s1)) {
                    NEW_NAME.setError("Please enter a valid name with no numbers");
                    return;
                }
                if (s1.length() == 1) {
                    NEW_NAME.setError("Please enter a valid name length");
                    return;
                }
                // ---------------- check number -------------
                if(!s2.isEmpty()) {
                    s2 = NEW_PHONE.getText().toString();
                    if (s2.length() > 10) {
                        NEW_PHONE.setError("Please enter a valid phone length (10 Digits)");
                        return;
                    }
                    s2 = NEW_PHONE.getText().toString();

                    if (s2.length() < 10) {
                        NEW_PHONE.setError("Please enter a valid phone length (10 Digits)");
                        return;
                    }

                    s2 = NEW_PHONE.getText().toString();
                    if (!s2.startsWith("05")) {
                        NEW_PHONE.setError("Please enter a valid phone length (Start with 05)");
                        return;
                    }
                    if (containsLetters(s2)) {
                        NEW_PHONE.setError("Please enter a valid phone number with no letters");
                        return;
                    }
                }//end if empty
                //check on car
               /* if (cars.getSelectedItem().toString().equals(actualCar)) { // if same
                    Toast.makeText(EditVolunteerProfile.this, "No Changes on Vehicle", Toast.LENGTH_SHORT).show();
                    return;
                }*/
                /*if (cars.getSelectedItem().toString().equals(null)) { // not sure about this
                    return;
                }*/
                if (!(cars.getSelectedItem().toString().equals(types[0]))) { // if NOT same then UPDATE
                    UpdateVehicle(cars.getSelectedItem().toString());

                }else{
                        counter++;
                }

                s1 = NEW_NAME.getText().toString();
                s2 = NEW_PHONE.getText().toString();
                // معليش على البدائيه بس اذا عندكم حل احسن قولو
                //حلك رائع بلا دراما:)

                if (Uid != null) {
                    if (!(s1.isEmpty()) ){
                        if (s1 != null)
                            Updatename(s1);
                    }

                    else
                        counter++;
                    if (!(s2.isEmpty())) {
                        if (s2 != null)
                            UpdatePhone(s2);
                    }
                    else
                        counter++;

                    if (counter == 3)
                    {
                        Toast.makeText(EditVolunteerProfile.this, "No Changes on profile", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EditVolunteerProfile.this, VolunteerProfile.class));
                    }
                    Toast.makeText(EditVolunteerProfile.this, "Changes Saved successfully", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(EditVolunteerProfile.this, VolunteerProfile.class));
                }
            }
        });
    }

    private String ReadCar() {

        DocumentReference documentReference =db.collection("Volunteers").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                actualCar= documentSnapshot.getString("Vehicle");
               // magic.setText(documentSnapshot.getString("Vehicle"));
               // urCar =(String)magic.getText().toString();
            }
        });

 return actualCar;
    }

    public void OpenProfile(View view) {
        startActivity(new Intent(EditVolunteerProfile.this,VolunteerProfile.class));
    }
    public void Updatename(String name){

        //MINE -Hussa
        DocumentReference documentReference =db.collection("Volunteers").document(userId);
        documentReference.update("UserName",NEW_NAME.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText( EditVolunteerProfile.this,"user updated",Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void UpdatePhone (String phone){

        //Written by Hussa
        DocumentReference documentReference =db.collection("Volunteers").document(userId);
        documentReference.update("PhoneNumber",NEW_PHONE.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText( EditVolunteerProfile.this,"user updated",Toast.LENGTH_SHORT).show();
            }
        });
    }

    // extra methods for checking
    public final boolean containsDigit(String s) {
        boolean containsDigit = false;

        if (s != null && !s.isEmpty()) {
            for (char c : s.toCharArray()) {
                if (containsDigit = Character.isDigit(c)) {
                    break;
                }
            }
        }

        return containsDigit;
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

    public void UpdateVehicle(String newVehicle){


        DocumentReference documentReference =db.collection("Volunteers").document(userId);
        documentReference.update("Vehicle",newVehicle).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText( EditVolunteerProfile.this,"Vehicle updated",Toast.LENGTH_SHORT).show();
            }
        });
    }


}




