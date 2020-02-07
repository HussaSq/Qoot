package com.example.qoot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


// i study 444
public class RegisterAsDonator extends AppCompatActivity {
    EditText username, email, password;
    Button register;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    public static final String TAG = "RegisterAsDonator";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_as_donator);
        username = findViewById(R.id.userText);
        email = findViewById(R.id.emailText);
        password = findViewById(R.id.passwordText);
        register = findViewById(R.id.button);
        mAuth = FirebaseAuth.getInstance();

        /*if(mAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), checking.class));
            finish();
        }*/
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Em = email.getText().toString().trim();
                String passW = password.getText().toString().trim();

                if (TextUtils.isEmpty(Em)) {
                    email.setError("Please Enter Your Email, It Is Required");
                    return;
                }
                if (TextUtils.isEmpty(passW)) {
                    password.setError("Please Enter Password, It Is Required");
                    return;
                }

                if (passW.length() < 8) {
                    password.setError("The Characters Must Be At Least 8 Characters ");
                    return;
                }
                //register user
                mAuth.createUserWithEmailAndPassword(Em, passW).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterAsDonator.this, "Registration Was Successful!!", Toast.LENGTH_SHORT).show();
                            //we MUST CHANGE THIS to THE HOME PAGE
                            // here call save user
                            saveUSer();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            Toast.makeText(RegisterAsDonator.this, "Something Went Wrong " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    public void  saveUSer(){
        db = FirebaseFirestore.getInstance();
        String  USER = mAuth.getCurrentUser().getUid();
        DocumentReference dRef = db.collection("Donators").document(USER);
        Map<String, Object> Donator = new HashMap<>();
        Donator.put("Name", username);
        Donator.put("Email",email);
        Donator.put("Phone","0500000000");
        Donator.put("Password",password);
        dRef.set(Donator).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG,"onSuccess: Added user successfully!");
            }
        });
    }

    public void OpenSignInPage(View view) {
        startActivity(new Intent(RegisterAsDonator.this,LogIn.class));
    }

    public void OpenSignupAsPage(View view) {
        startActivity(new Intent(RegisterAsDonator.this,SignUpAs.class));
    }
}


