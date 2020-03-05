package com.example.qoot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.regex.Pattern;

import javax.annotation.Nullable;

import io.grpc.Context;

public class EditDonatorProfile extends AppCompatActivity {


    private static final int PICK_IMAGE_REQUEST=1;
    private  ImageView Photo;
    private Uri imageuri;
    private StorageReference mStorageRef;//!!!!!!!!!!!!!!!!!!!



    public EditText NEW_NAME;
    public EditText NEW_PHONE;

    public EditText NEW_PASSWORD;
    public EditText NEW_EMAIL;

    public ImageView NEW_IMAGE;
    private ImageView editIcon;


    FirebaseFirestore db;
    private FirebaseFirestore fstore;//!!!!!!!!!!!!!!!!!!!!!!!!
    private FirebaseAuth mAuth;
    String userId;

    // idk wth is this for
    private static final String TAG = "EditDonatorProfile";

    //  save button
    Button saveButton;

    // what user type in fields
    String s1, s2, s3 ,s4, Uid;

    //if save is pressed


    // Intent intent1 = getIntent();
    // String userId1 = intent1.getStringExtra("user");
    //String name = intent1.getStringExtra("Name");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_donator_profile);

        NEW_IMAGE = findViewById(R.id.UserImage);
        NEW_NAME = findViewById(R.id.Name);
        NEW_PHONE = findViewById(R.id.Phone_v);

        NEW_EMAIL =findViewById(R.id.emailDonator);
        NEW_PASSWORD =findViewById(R.id.Password);

        saveButton = findViewById(R.id.button);
        editIcon = findViewById(R.id.edit_photo);




        //*************************************
        editIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });


        //*******************

        // firebase initialize
        mAuth = FirebaseAuth.getInstance();
        fstore =FirebaseFirestore.getInstance();//***************
        db=FirebaseFirestore.getInstance();
        Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userId=mAuth.getCurrentUser().getUid();
        mStorageRef = FirebaseStorage.getInstance().getReference("Images");//****************



//--------------------------------------------------------------------------------------------------------------------
        DocumentReference documentReference =db.collection("Donators").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                NEW_NAME.setHint(documentSnapshot.getString("UserName"));
            }
        });
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                NEW_PHONE.setHint(documentSnapshot.getString("PhoneNumber"));
            }
        });
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                NEW_EMAIL.setHint(documentSnapshot.getString("Email"));
            }
        });

        // retrieve the image
//---------------------------------------------------------------------------------------------------------------------
        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                s1 = NEW_NAME.getText().toString();
                s2 = NEW_PHONE.getText().toString();
                s3 =NEW_PASSWORD.getText().toString();
                s4 = NEW_EMAIL.getText().toString();

                //------ check name -------------
                if (!s1.isEmpty()) {
                    s1 = NEW_NAME.getText().toString();
                    if (s1.length() == 1) {
                        NEW_NAME.setError("Please enter a valid name length");
                        return;
                    }
                    s1 = NEW_NAME.getText().toString();
                }

                if (!s4.isEmpty()){
                    if(!isValid(s4)){
                        NEW_EMAIL.setError("Enter a valid Email");
                        return;}
                    s4 =NEW_EMAIL.getText().toString();
                }
                // ---------------- check number -------------
                if(!s2.isEmpty()) {
                    s2 = NEW_PHONE.getText().toString();
                    if (s2.length() != 10) {
                        NEW_PHONE.setError("Enter a valid phone length (10 Digits)");
                        return;
                    }
                    s2 = NEW_PHONE.getText().toString();
                    if (!s2.startsWith("05")) {
                        NEW_PHONE.setError("Enter a valid phone number (Start with 05)");
                        return;
                    }
                    s2 = NEW_PHONE.getText().toString();
                    if (containsLetters(s2)) {
                        NEW_PHONE.setError("Enter a phone number with no letters");
                        return;
                    }
                    s2 = NEW_PHONE.getText().toString();
                }//end if empty

                if (!s3.isEmpty()){
                    if (s3.length() < 8) {
                        NEW_PASSWORD.setError("Must Be At Least 8 Characters");
                        return;
                    }
                    s3 =NEW_PASSWORD.getText().toString();
                }
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
                    if (!s3.isEmpty() && s3 != null){
                        UpdatePassword(s3);
                    }
                    else
                        counter++;
                    if (!s4.isEmpty() && s4 != null){
                        UpdateEmail(s4);
                    }else
                        counter++;

                    if (counter == 4)
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


    public void OpenProfileDonator(View view) {
        startActivity(new Intent(EditDonatorProfile.this,DonatorProfile.class));
    }

    // -------------------------------------------------METHODS------------------------------------------------
    private void openFileChooser(){
        Intent intent =new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
        //open file chooser
        //  OpenFileChooser();

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK
                && data != null && data.getData()!= null){

            imageuri=data.getData();

            NEW_IMAGE.setImageURI(imageuri);

        }

    }

    public void Updatename(String name){

        //MINE -Hussa
        DocumentReference documentReference =db.collection("Donators").document(userId);
        documentReference.update("UserName",NEW_NAME.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //  Toast.makeText( EditDonatorProfile.this,"user updated",Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void UpdatePassword(String pass){
        // abeer
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            user.updatePassword(pass);
        }
    }
    public void UpdateEmail(String em){
        // abeer
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            user.updateEmail(em);
        }
    }
    public void UpdatePhone (String phone){
        //Written by Hussa
        DocumentReference documentReference =db.collection("Donators").document(userId);
        documentReference.update("PhoneNumber",NEW_PHONE.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Toast.makeText( EditDonatorProfile.this,"user updated",Toast.LENGTH_SHORT).show();
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
    public static boolean isValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
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
        /*
                *******    CODE FOR UPLOADING A FILE TO THE STORAGE *******

                          Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
        StorageReference riversRef = storageRef.child("images/rivers.jpg");

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });



         */

    // --------------------- هذي خرابيطي -----------------------------------
    /*  private void OpenFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==PICK_IMAGE_REQUEST && resultCode == RESULT_OK &&
        data !=null && data.getData() != null){
            ImageUri = data.getData(); // has the uri of our pic
            Picasso.with(this).load(ImageUri).into(NEW_IMAGE);
            UploadImage();
        }
    }
    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
    return mime.getExtensionFromMimeType(cr.getType(uri));
    }
    public void UploadImage(){
        if (ImageUri != null){
                                // 786876.JPG for example
            StorageReference fireRef = mStorageRef.child(System.currentTimeMillis()+"."+getFileExtension(ImageUri));

            fireRef.putFile(ImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(EditDonatorProfile.this,"Image Uploaded", Toast.LENGTH_SHORT).show();
                           // Image img = new Image(taskSnapshot.get.toString());
                        }
                    });
        }else{
            Toast.makeText(this,"No Image Selected", Toast.LENGTH_SHORT).show();
       */

}

