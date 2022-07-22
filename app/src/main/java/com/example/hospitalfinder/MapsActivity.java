package com.example.hospitalfinder;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class MapsActivity extends FragmentActivity {
    private GoogleMap mMap;
    private FusedLocationProviderClient client;
    private SupportMapFragment supportMapFragment;
    FirebaseAuth mAuth;
    DatabaseReference databaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        client = LocationServices.getFusedLocationProviderClient(this);;
        databaseUser= FirebaseDatabase.getInstance("https://group-project-67a77-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("users");
        mAuth = FirebaseAuth.getInstance();
        if (ActivityCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

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

                databaseUser.child(id).child("userAgent").setValue(System.getProperty("http.agent"));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ERROR: ",error.toString());
            }
        });
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
            address= addressList.get(0).getLocality();
        }
        return address;
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
                            MarkerOptions options=new MarkerOptions().position(latLng).title("User Location")
                                                                     .icon(BitmapFromVector(getApplicationContext(),R.drawable.my_location));
                            drawMarkers(googleMap,options);
                            setLocationInFirebase(latLng);
                            mMap= googleMap;
                        }
                    });
                    Button btn= findViewById(R.id.nearbyHealth);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            makeRequestPlaces(new LatLng(location.getLatitude(),location.getLongitude()));
                        }
                    });
                    ImageView recenter=findViewById(R.id.imageButton);
                    recenter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getCurrentLocation();
                        }
                    });

                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,@NonNull String[] permissions,@NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if(requestCode==44){
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }
        }
    }
    
    private void drawMarkers(GoogleMap googleMap,MarkerOptions options) {

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(options.getPosition(), 15));
        googleMap.addMarker(options);
    }

    private void makeRequestPlaces(LatLng lat){
        StringBuilder stringBuilder= new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        stringBuilder.append("&location="+lat.latitude+","+lat.longitude);
        stringBuilder.append("&radius=2000");
        stringBuilder.append("&keyword=hospital|clinic");
        stringBuilder.append("&key=AIzaSyAAocZbBTTwRXL76uiBfHiaUU5qHPBvCqM");

        String url= stringBuilder.toString();
        Object dataFetch[]=new Object[2];
        dataFetch[0]=mMap;
        dataFetch[1]=url;

        FetchData fetchData= new FetchData();
        fetchData.execute(dataFetch);
    }

    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}