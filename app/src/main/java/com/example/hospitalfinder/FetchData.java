package com.example.hospitalfinder;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class FetchData extends AsyncTask<Object,String,String> {
    String getNearbyHospital;
    GoogleMap googleMap;
    String url;

    @Override
    protected void onPostExecute(String s) {
        try{
            JSONObject jsonObject=new JSONObject(s);
            JSONArray result=jsonObject.getJSONArray("results");
            parseResult(result);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void parseResult(JSONArray result) throws JSONException {
        JSONObject currentObject;
        JSONObject location;
        Log.w("current",result+"");

        for (int i = 0; i < result.length(); i++) {
            currentObject = result.getJSONObject(i);

            String name;
            if (currentObject.has("name") && currentObject.getString("name") != "") {
                name = currentObject.getString("name");
            } else {
                name = "Unknown";
            }

            String bs_status;
            if (currentObject.has("business_status") && currentObject.getString("business_status") != "") {
                bs_status = currentObject.getString("business_status");
            } else {
                bs_status = "Unknown";
            }

            location = currentObject.getJSONObject("geometry").getJSONObject("location");
            Double lat = Double.parseDouble(location.getString("lat"));
            Double lng = Double.parseDouble(location.getString("lng"));
            MarkerOptions markerOptions= new MarkerOptions().position(new LatLng(lat, lng)).title(name).snippet(bs_status);
            googleMap.addMarker(markerOptions);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng),15));

            Log.w("inside",i+"");
        }
    }

    @Override
    protected String doInBackground(Object... objects) {
        try {
            googleMap=(GoogleMap) objects[0];
            url=(String) objects[1];
            DownloadURL downloadURL=new DownloadURL();
            getNearbyHospital=downloadURL.retrieveUrl(url);
        }catch (IOException e){
            Log.e("Error:",e.toString());
        }
        return getNearbyHospital;
    }
}
