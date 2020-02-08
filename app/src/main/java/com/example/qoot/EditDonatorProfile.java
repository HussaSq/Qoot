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

    private FirebaseFirestore fstore;
    private FirebaseAuth mAuth;
    DocumentReference Refrence;

    // idk wth is this for
    private static final String TAG = "";

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
            editIcon = findViewById(R.id.edit_photo);

            // firebase initialize
            mAuth = FirebaseAuth.getInstance();
            fstore =FirebaseFirestore.getInstance();

        Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //take from user
        s1 = NEW_NAME.getText().toString();
        s2 = NEW_PHONE.getText().toString();

            saveButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

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
                    if (containsLetters(s2)) {
                        NEW_PHONE.setError("Please enter a valid phone number with no letters");
                        return;
                    }
                    if (s2.length() < 10) {
                        NEW_PHONE.setError("Please enter a valid phone length (10 Digits)");
                        return;
                    }
                    if (!s2.startsWith("05")) {
                        NEW_PHONE.setError("Please enter a valid phone length (Start with 05)");
                        return;
                    }

                    // معليش على البدائيه بس اذا عندكم حل احسن قولو
                    int counter=0;
                    if (Uid != null) {
                        if (!(s1.isEmpty() || s1 == null))
                            Updatename(s1);
                        else
                            counter++;
                        if (!(s2.isEmpty() || s2 == null))
                            UpdatePhone(s2);
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

        // same as firebase
        Refrence = fstore.collection("users").document(Uid);

        Refrence

                .update("name", name)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Name successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating Name", e);
                    }
                });
    }
    public void UpdatePhone (String phone){

        Refrence = fstore.collection("users").document(Uid);

        Refrence

                .update("phone", phone)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "phone successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating Phone", e);
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
