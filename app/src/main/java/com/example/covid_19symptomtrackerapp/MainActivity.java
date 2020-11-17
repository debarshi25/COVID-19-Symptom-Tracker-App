package com.example.covid_19symptomtrackerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Database myDatabase;
    Accelerometer myAccelerometer;
    TextView heartRateTextView;
    TextView respiratoryRateTextView;
    TextView latitudeTextView;
    TextView longitudeTextView;

    public String heartRate = "0";
    public String respiratoryRate = "0";
    public String latitude = "0";
    public String longitude = "0";
    public String location = "0";

    float timestamp;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showCameraPermission();
        showPhoneStatePermission();

        intent = getIntent();

        // Symptoms Button
        Button symptomsButton = findViewById(R.id.symptoms);
        symptomsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent symptomsIntent = new Intent(getApplicationContext(), MainActivity2.class);
                    symptomsIntent.putExtra("HEART_RATE", heartRate);
                    symptomsIntent.putExtra("RESPIRATORY_RATE", respiratoryRateTextView.getText().toString());
                    symptomsIntent.putExtra("LOCATION", location);
                    startActivity(symptomsIntent);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Upload Signs Button
        Button uploadSignsButton = findViewById(R.id.uploadSigns);
        uploadSignsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    myDatabase = new Database(getApplicationContext());
                    myDatabase.onUpgrade(myDatabase.getWritableDatabase(), 0, 1);
                    myDatabase.setHeartRate(heartRate);
                    myDatabase.setRespiratoryRate(respiratoryRate);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Heart Rate Text View
        heartRateTextView = findViewById(R.id.heartRate);

        // Measure Heart Rate Button
        Button measureHeartRateButton = findViewById(R.id.measureHeartRate);
        measureHeartRateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent captureVideoIntent = new Intent(getApplicationContext(), CameraActivity.class);
                    startActivityForResult(captureVideoIntent, 1);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Respiratory Rate Text View
        respiratoryRateTextView = findViewById(R.id.respiratoryRate);

        // Measure Respiratory Rate Button
        Button measureRespiratoryRateButton = findViewById(R.id.measureRespiratoryRate);
        measureRespiratoryRateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Toast.makeText(MainActivity.this, "Sensor started, respiratory rate is being measured!", Toast.LENGTH_LONG).show();
                    timestamp = (float) (System.currentTimeMillis() / 1000.0);
                    myAccelerometer = new Accelerometer(getApplicationContext(), timestamp, respiratoryRateTextView);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Latitude and Longitude Text View
        latitudeTextView = findViewById(R.id.latitude);
        longitudeTextView = findViewById(R.id.longitude);

        // Get Location Button
        Button getLocationButton = findViewById(R.id.getLocation);
        getLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent getLocationIntent = new Intent(getApplicationContext(), LocationActivity.class);
                    startActivityForResult(getLocationIntent, 2);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            if (data.hasExtra("HEART_RATE")) {
                heartRate = data.getStringExtra("HEART_RATE");
                Toast.makeText(this, "Heart Rate= " + heartRate, Toast.LENGTH_LONG).show();
                heartRateTextView.setText(heartRate);
            }
        }
        if (requestCode == 2 && resultCode == 2) {
            if (data.hasExtra("LOCATION")) {
                location = data.getStringExtra("LOCATION");
                Toast.makeText(this, "Location= " + location, Toast.LENGTH_LONG).show();
                String [] i = location.split(", ");
                latitude = i[0];
                longitude = i[1];
                latitudeTextView.setText(latitude);
                longitudeTextView.setText(longitude);
            }
        }
    }

    private void showCameraPermission() {
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            int REQUEST_PERMISSION_CAMERA = 1;
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CAMERA)) {
                showExplanation(Manifest.permission.CAMERA, REQUEST_PERMISSION_CAMERA);
            }
            else {
                requestPermission(Manifest.permission.CAMERA, REQUEST_PERMISSION_CAMERA);
            }
        }
    }

    private void showPhoneStatePermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            int REQUEST_PERMISSION_PHONE_STATE = 1;
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                showExplanation(Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_PERMISSION_PHONE_STATE);
            }
            else {
                requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_PERMISSION_PHONE_STATE);
            }
        }
    }

    private void showExplanation(final String permission, final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permission needed")
                .setMessage("Rationale")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission(permission, permissionRequestCode);
                    }
                });
        builder.create()
                .show();
    }

    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(this, new String[] {permissionName}, permissionRequestCode);
    }
}