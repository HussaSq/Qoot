package com.example.qoot;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class LogIn extends AppCompatActivity {
    EditText userEmail,userPassword;
    Button loginbtn;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    String typeUSER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        userEmail = findViewById(R.id.emailText);
        userPassword = findViewById(R.id.passwordText);
        fAuth = FirebaseAuth.getInstance();
        loginbtn = findViewById(R.id.LogInBTN);


        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = userEmail.getText().toString().trim();
                String password = userPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    userEmail.setError("Please Enter Your Email, It Is Required");
                    return;
                }

                else if(TextUtils.isEmpty(password)){
                    userPassword.setError("Please Enter Password, It Is Required");
                    return;
                }

                else if(password.length() < 8){
                    userPassword.setError("The Characters Must Be At Least 8 Characters");
                    return;
                }
                else if(TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
                    userEmail.setError("Please Enter Your Email and Password, It Is Required");
                    return;
                }
                else if(!(TextUtils.isEmpty(email) && TextUtils.isEmpty(password))) {

                    // authenticate the user

                    fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                String Uid = fAuth.getCurrentUser().getUid();
                                DocumentReference docRef = fstore.collection("users").document(Uid);
                                String type = checkType(docRef);
                                if (type != null && type.equals("Volunteer")){
                                    Toast.makeText(LogIn.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LogIn.this,VolunteerProfile.class));
                                }
                                else if (type != null && type.equals("Donator")){
                                    Toast.makeText(LogIn.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LogIn.this,DonatorProfile.class));
                                }
                            }else {

                                Toast.makeText(LogIn.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }
        });
    }
    public String checkType(DocumentReference doc){

        doc.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
               typeUSER =  documentSnapshot.getString("type");
            }
        });
    return typeUSER;
    }
    public void OpenSignupAsPage(View view) {
        startActivity(new Intent(LogIn.this,SignUpAs.class));
    }

    public void openForPassPage(View view) {
        startActivity(new Intent(LogIn.this,ForgotPassword.class));
    }

    public void OpenSignupPage(View view) {
        //for Test
        //  startActivity(new Intent(LogIn.this,profile.class));
    }

}

