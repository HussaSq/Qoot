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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    EditText mUsername, mEmail, mPassword;
    Button register;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    RadioGroup GenderGroup;
    RadioButton gender;
    public static final String TAG = "RegisterAsDonator";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_as_donator);
        mUsername = findViewById(R.id.userText);
        mEmail = findViewById(R.id.emailText);
        mPassword = findViewById(R.id.passwordText);
        register = findViewById(R.id.button);
        mAuth = FirebaseAuth.getInstance();
        GenderGroup = (RadioGroup) findViewById(R.id.radioGender);

        // RADIO CODE..
        int selectedId = GenderGroup.getCheckedRadioButtonId();
        gender = (RadioButton) findViewById(selectedId);
        Toast.makeText(RegisterAsDonator.this, gender.getText(), Toast.LENGTH_SHORT).show();
        /*if(mAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), checking.class));
            finish();
        }*/
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username= mUsername.getText().toString();
                final String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Please Enter Your Email, It Is Required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Please Enter Password, It Is Required");
                    return;
                }

                if (password.length() < 8) {
                    mPassword.setError("The Characters Must Be At Least 8 Characters ");
                    return;
                }
                //register user
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterAsDonator.this, "Registration Was Successful!!", Toast.LENGTH_SHORT).show();
                            //we MUST CHANGE THIS to THE HOME PAGE
                            // here call save user
                           // saveUSer();
                            db = FirebaseFirestore.getInstance();
                            String  USER = mAuth.getCurrentUser().getUid();
                            Donator donator = new Donator(username,email,(String)gender.getText());
                            db.collection("users").document(USER).set(donator);

                            startActivity(new Intent(getApplicationContext(), DonatorProfile.class));
                        } else {
                            Toast.makeText(RegisterAsDonator.this, "Something Went Wrong " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    /*
    public void  saveUSer(){
        db = FirebaseFirestore.getInstance();
        String  USER = mAuth.getCurrentUser().getUid();
       // DocumentReference dRef = db.collection("Donators").document(USER);
        Donator donator = new Donator()

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
    }*/

    public void OpenSignInPage(View view) {
        startActivity(new Intent(RegisterAsDonator.this,LogIn.class));
    }

    public void OpenSignupAsPage(View view) {
        startActivity(new Intent(RegisterAsDonator.this,SignUpAs.class));
    }
}


