package com.example.covid_19symptomtrackerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;

public class MainActivity2 extends AppCompatActivity {

    MainActivity2 mainActivity2;
    Database myDatabase;

    String heartRate;
    String respiratoryRate;
    String location;
    Intent int1;
    int symptomIndex = 0;
    int[] ratingList = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        myDatabase = new Database(getApplicationContext());

        int1 = getIntent();
        if (int1.hasExtra("HEART_RATE")) {
            heartRate = int1.getStringExtra("HEART_RATE");
        }
        if (int1.hasExtra("RESPIRATORY_RATE")) {
            respiratoryRate = int1.getStringExtra("RESPIRATORY_RATE");
        }
        if (int1.hasExtra("LOCATION")) {
            location = int1.getStringExtra("LOCATION");
        }

        //Symptom Spinner
        final Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.symptoms_array));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Upload Symptoms Button
        Button uploadSymptomsButton = findViewById(R.id.uploadSymptoms);
        uploadSymptomsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDatabase.setHeartRate(heartRate);
                myDatabase.setRespiratoryRate(respiratoryRate);
                myDatabase.setLocation(location);
                myDatabase.insertData(ratingList);
            }
        });

        // Upload to Server Button
        Button uploadToServerButton = findViewById(R.id.uploadToServer);
        uploadToServerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Uploading Database to Server!", Toast.LENGTH_LONG).show();
                RequestParams params = new RequestParams();
                try {
                    params.put("uploaded_file", new File("/data/data/com.example.covid_19symptomtrackerapp/databases/Roy.db"));
                    params.put("id", "test");
                    params.put("accept", "1");
                }
                catch(FileNotFoundException e) {}

                AsyncHttpClient client = new AsyncHttpClient();
                client.post("http://192.168.0.23/upload_file.php", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {
                        if(statusCode == 200) {
                            Toast.makeText(MainActivity2.this, "Success!", Toast.LENGTH_LONG).show();
                            mainActivity2.finish();
                        }
                        else {
                            Toast.makeText(MainActivity2.this, "Failed!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {

                    }

                    @Override
                    public void onProgress(long bytesWritten, long totalSize) {
                        super.onProgress(bytesWritten, totalSize);
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
            }
        });

        //Rating Bar
        final RatingBar ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 1)
                {
                    float ratingValues = ratingBar.getRating();
                    ratingList[symptomIndex] = (int) ratingValues;
                }
                return false;
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                symptomIndex = spinner.getSelectedItemPosition();
                ratingBar.setRating(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}