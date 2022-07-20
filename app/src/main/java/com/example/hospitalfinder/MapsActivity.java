package com.example.hospitalfinder;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
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

    private String getLocationName(Double latitude,Double longitude){
        List<Address> addressList = null;
        try {
            addressList = new Geocoder(this).getFromLocation(latitude,longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String address="Unidentified";
        if (addressList != null & addressList.size() > 0) {
            address= addressList.get(0).getLocality().toString();
        }
            return address;
    }
    private void setLocationInFirebase(LatLng loc){
        String id = mAuth.getCurrentUser().getUid();
        databaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String dateInserted = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                String timeInserted = new SimpleDateFormat("HH:mm:sss").format(new Date());

                databaseUser.child(id).child("userCoordinate")
                            .setValue(loc.latitude+","+loc.longitude);

                databaseUser.child(id).child("insertedAt")
                            .child("date")
                            .setValue(dateInserted);

                databaseUser.child(id).child("insertedAt")
                            .child("time")
                            .setValue(timeInserted);

                databaseUser.child(id).child("userLocation")
                            .setValue(getLocationName(loc.latitude,loc.longitude));
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