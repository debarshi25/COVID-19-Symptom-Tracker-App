package com.example.covid_19symptomtrackerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class LocationActivity extends AppCompatActivity {

    private TextView textViewLatitude, textViewLongitude;
    String location1 = "";
    Intent returnIntent;
    LocationActivity locationActivity;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        returnIntent = new Intent();
        locationActivity = this;

        textViewLatitude = findViewById(R.id.latitude);
        textViewLongitude = findViewById(R.id.longitude);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LocationActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 1, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                String latitude = String.valueOf(location.getLatitude());
                String longitude = String.valueOf(location.getLongitude());
                String coordinates = latitude + ", " + longitude;
                if (!(coordinates.equals(", ")|| latitude.equals("")|| longitude.equals(""))) {
                    if (count == 0) {
                        count = count + 1;
                        returnIntent.putExtra("LOCATION", coordinates);
                        locationActivity.setResult(2, returnIntent);
                        Toast.makeText(LocationActivity.this, "Location has been recorded, navigate back to upload the value!", Toast.LENGTH_LONG).show();
                    }
                }
                location1 = coordinates;

                textViewLatitude.setText(latitude);
                textViewLongitude.setText(longitude);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(@NonNull String s) {

            }

            @Override
            public void onProviderDisabled(@NonNull String s) {

            }
        });
    }
}