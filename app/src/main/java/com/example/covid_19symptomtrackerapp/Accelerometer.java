package com.example.covid_19symptomtrackerapp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Accelerometer implements SensorEventListener {

    private Context context;
    private SensorManager sensorManager;
    private TextView respiratoryRateTextView;

    public float timestamp;
    public float timestamp1 = 0.0f;
    public int temp = 0;
    public int timeLimit = 45;
    public float respiratoryRate = 0;

    public ArrayList<Float> xValues = new ArrayList<>();
    public ArrayList<Float> yValues = new ArrayList<>();
    public ArrayList<Float> zValues = new ArrayList<>();

    public Accelerometer(Context context, float timestamp, TextView textView) {
        this.timestamp = timestamp;
        this.context = context;
        this.respiratoryRateTextView = textView;
        initializeSensor();
    }

    public void initializeSensor() {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unregisterSensor() {
        sensorManager.unregisterListener(this);
        Toast.makeText(context, "Sensor stopped after 45 seconds!", Toast.LENGTH_LONG).show();
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            if (temp == 0) {
                temp++;
                timestamp1 = sensorEvent.timestamp * 1.0f / 1000000000.0f;
            }
            int i = (int) (timestamp1 - (sensorEvent.timestamp * 1.0f / 1000000000.0f));
            if (-i <= timeLimit) {
                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];
                float z = sensorEvent.values[2];
                xValues.add(x);
                yValues.add(y);
                zValues.add(z);
            }
            if (-i == timeLimit) {
                this.unregisterSensor();
                getRespiratoryRate(zValues);
            }
        }
    }

    public void getRespiratoryRate(ArrayList<Float> zValues) {
        int windowSize = 30;
        int slidingSize = 5;
        int length = zValues.size();
        ArrayList<Float> zValuesSmooth = new ArrayList<>();
        for (int i = length % windowSize; windowSize + i< length; i += slidingSize) {
            int sum = 0;
            for (int j = i; j < windowSize + i; j++) {
                sum += zValues.get(j);
            }
            zValuesSmooth.add((float) sum / windowSize);
        }
        ArrayList<Float> zValuesZeroPoints = new ArrayList<>();
        for (int i = 1; i < zValuesSmooth.size(); i++) {
            zValuesZeroPoints.add(zValuesSmooth.get(i) - zValuesSmooth.get(i - 1));
        }
        for (int i = 1; i < zValuesZeroPoints.size(); i++) {
            if (zValuesZeroPoints.get(i) == 0 || (zValuesZeroPoints.get(i - 1) > 0 && zValuesZeroPoints.get(i) < 0) || (zValuesZeroPoints.get(i - 1) < 0 && zValuesZeroPoints.get(i) > 0)) {
                respiratoryRate++;
            }
        }
        respiratoryRate = respiratoryRate * 30 / timeLimit;
        respiratoryRateTextView.setText(respiratoryRate + "");
    }
}