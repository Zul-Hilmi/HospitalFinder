package com.example.hospitalfinder;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    MarkerOptions marker;
    FirebaseAuth mAuth;
    DatabaseReference databaseUser;
    LatLng userLocation;
    Vector<MarkerOptions> markerOptions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        databaseUser= FirebaseDatabase.getInstance("https://group-project-67a77-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("users");
        mAuth = FirebaseAuth.getInstance();
        userLocation = new LatLng(37.422031,-122.083985);
        setLocationInFirebase(userLocation);
        markerOptions = new Vector<>();

        markerOptions.add(new MarkerOptions().title("Hospital Byoouin")
                .position(new LatLng(37.422631,-122.082985))
                .snippet("Open during MCO: 8am - 10pm")
        );

        markerOptions.add(new MarkerOptions().title("Hospital Nevada")
                .position(new LatLng(37.425631,-122.092985))
                .snippet("Covid Test Center")
        );

        markerOptions.add(new MarkerOptions().title("Hospital Gosling The Great")
                .position(new LatLng(37.421155,-122.083210))
                .snippet("Open:8am - 10pm everyday")
        );

    }

    private void setLocationInFirebase(LatLng loc){
        String id = mAuth.getCurrentUser().getUid();
        databaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SimpleDateFormat ISO_8601_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss");

                String insertedAt = ISO_8601_FORMAT.format(new Date());

                databaseUser.child(id).child("userLocation")
                            .setValue(loc.latitude+","+loc.longitude);
                databaseUser.child(id).child("timeAt")
                            .setValue(insertedAt);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ERROR: ",error.toString());
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        for (MarkerOptions mark: markerOptions) {
            mMap.addMarker(mark);
        }

        enableMyLocation();

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,8));
    }
    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
        } else {
            String perms[] = {"android.permission.ACCESS_FINE_LOCATION"};
            ActivityCompat.requestPermissions(this, perms,200);
        }
    }

}