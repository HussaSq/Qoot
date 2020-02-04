package com.example.qootapplication;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.ImageView;
import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.Map;

//---------------------------------------------------------------------------------------------------------
// ---------------------THIS CLASS IS FOR RETRIEVING THE NAME, USER NAME , COMMENTS AND USER IMAGE-------
// ---------------------DONT TOUCH THIS UNTIL YOU ARE ALL SURE THAT FIREBASE WORKS CORRECTLY - ABEER-----
//---------------------------------------------------------------------------------------------------------
@SuppressLint("Registered")
public class VolunteerProfileActivity extends AppCompatActivity {


    private TextView NameTextView , UserNameTextView ;
    private ImageView UserImageView;


    private final String TAG = this.getClass().getName().toUpperCase();
    private String UserName;
    private static final String USERS = "users"; // here must be the same as firebase
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_volunteer_profile);

        // get from text
        UserNameTextView = findViewById(R.id.UserName);
        NameTextView = findViewById(R.id.Name);
        UserImageView = findViewById(R.id.UserImage);

        // data base stuff
        Intent intent = getIntent();
        UserName = intent.getStringExtra("UserName"); // the logged in or registered user



        /*
        DatabaseReference rootRef =FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef =rootRef.child(USERS);
        Log.v("UserName",userRef.getKey());
        userRef.addValueEventListener(new ValueEventListener() {
            String name,username;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren())
                    if (ds.child("UserName").getValue().equals(UserName)) {
                        username = UserName;
                        //  UserImageView.setText(ds.child("UserImage").getValue(String.class)); // here too
                        break;
                    }
                NameTextView.setText(name);
                UserNameTextView.setText(username);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // if failure happend this will show
                Log.w(TAG,"failed to read value from Firebase.");
            }
        });
    */}
}