package com.example.hospitalfinder;

import android.Manifest;
import android.content.Intent;
import android.location.LocationRequest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileActivity extends AppCompatActivity {
    FirebaseAuth auth;
    DatabaseReference databaseUser;
    TextView profileText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileText= (TextView) findViewById(R.id.textView);
        auth= FirebaseAuth.getInstance();
        databaseUser= FirebaseDatabase.getInstance("https://group-project-67a77-default-rtdb.asia-southeast1.firebasedatabase.app/")
                                      .getReference("users");
        databaseUser.child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User currentUser =  snapshot.getValue(User.class);
                profileText.setText("Hello "+currentUser.userName);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ERROR: ",error.toString());
            }
        });
    }

    public void openMap(View v){
        Intent i = new Intent(this,MapsActivity.class);
        startActivity(i);
    }

    public void openAboutUs(View v){
        Intent i = new Intent(this,AboutUs.class);
        startActivity(i);
    }

    public void signout(View v){
        auth.signOut();
        finish();
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }
}
