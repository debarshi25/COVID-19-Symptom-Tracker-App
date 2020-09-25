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

public class MainActivity2 extends AppCompatActivity {

    Database myDatabase;

    String heartRate;
    String respiratoryRate;
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

        //Symptom Spinner
        final Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.symptoms_array));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Button uploadSymptomsButton = findViewById(R.id.uploadSymptoms);
        uploadSymptomsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDatabase.setHeartRate(heartRate);
                myDatabase.setRespiratoryRate(respiratoryRate);
                myDatabase.insertData(ratingList);
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