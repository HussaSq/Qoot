package com.example.qoot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditDonatorProfile extends AppCompatActivity {

    public EditText NEW_NAME;
    public EditText NEW_PHONE;

    public ImageView NEW_IMAGE; // not important now
    private ImageView editIcon; // not important now
    FirebaseFirestore db;
    private FirebaseFirestore fstore;
    private FirebaseAuth mAuth;
    DocumentReference Refrence;
    String userId;

    // idk wth is this for
    private static final String TAG = "EditDonatorProfile";

    //  save button
    Button saveButton;

    // what user type in fields
    String s1, s2, Uid;

    //if save is pressed
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_donator_profile);
        
            NEW_NAME = findViewById(R.id.Name);
            NEW_PHONE = findViewById(R.id.Phone_v);
            NEW_IMAGE = findViewById(R.id.UserImage);

            saveButton = findViewById(R.id.button);


            // firebase initialize
            mAuth = FirebaseAuth.getInstance();
            fstore =FirebaseFirestore.getInstance();
            db=FirebaseFirestore.getInstance();
        Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userId=mAuth.getCurrentUser().getUid();

        //take from user


            saveButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    s1 = NEW_NAME.getText().toString();
                    s2 = NEW_PHONE.getText().toString();
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
                    s1 = NEW_NAME.getText().toString();
                    s2 = NEW_PHONE.getText().toString();
                    // معليش على البدائيه بس اذا عندكم حل احسن قولو
                    //حلك رائع بلا دراما:)
                    int counter=0;
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
                        if (counter == 2)
                        {
                            Toast.makeText(EditDonatorProfile.this, "No Changes on profile", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(EditDonatorProfile.this, DonatorProfile.class));
                        }
                        Toast.makeText(EditDonatorProfile.this, "Changes Saved successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EditDonatorProfile.this, DonatorProfile.class));
                    }
                }
            });
        }
    // -------------------------------------------------METHODS------------------------------------------------
    public void editPhoto(View v){

        // .... here we will have the code for editing the photo that I ( ABEER ) don't know yet :(
    }

    public void Updatename(String name){

        //MINE -Hussa
        DocumentReference documentReference =db.collection("Donators").document(userId);
        documentReference.update("UserName",NEW_NAME.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText( EditDonatorProfile.this,"user updated",Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void UpdatePhone (String phone){

        //Written by Hussa
        DocumentReference documentReference =db.collection("Donators").document(userId);
        documentReference.update("PhoneNumber",NEW_PHONE.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText( EditDonatorProfile.this,"user updated",Toast.LENGTH_SHORT).show();
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

    public void OpenProfileDonator(View view) {
        startActivity(new Intent(EditDonatorProfile.this,DonatorProfile.class));
    }

        /*      اذا ما ضبط اللي فوق هذي بلان بي -عبير
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
        .setDisplayName("Jane Q. User")
        .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
        .build();

user.updateProfile(profileUpdates)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "User profile updated.");
                }
            }
        });
     */

}
