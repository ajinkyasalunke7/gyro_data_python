package com.example.gyroscope;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor gyroscopeSensor;
    private TextView gyroscopeTextView;

    private Button send;

    private EditText ip;
    private CheckBox cb;
    private OkHttpClient client = new OkHttpClient(); // Declare OkHttpClient as a class variable

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        send = findViewById(R.id.send);
        ip = findViewById(R.id.ip);
        cb = findViewById(R.id.cb);

        gyroscopeTextView = findViewById(R.id.gyroscopeTextView);

        // Initialize the SensorManager
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // Check if gyroscope sensor is available
        if (sensorManager != null) {
            gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        }

        // Register the gyroscope sensor listener
        if (gyroscopeSensor != null) {
            sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            // Gyroscope data
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            if(cb.isChecked()) sendDataToServer(x, y, z);
            // Display gyroscope data in your TextView or process it as needed
            gyroscopeTextView.setText("X: " + x + "\nY: " + y + "\nZ: " + z);
//            sendDataToServer(x, y, z);
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendDataToServer(x, y, z);
//                    Toast.makeText(MainActivity.this, "Data send", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Handle accuracy changes if needed
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the sensor listener when the activity is paused
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register the sensor listener when the activity is resumed
        if (gyroscopeSensor != null) {
            sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void sendDataToServer(float x, float y, float z) {
        // Replace "YOUR_SERVER_URL" with the actual URL of your server
        String ip_address = ip.getText().toString();
        String serverUrl = "http://"+ ip_address +":5000/gyroscope";

        // JSON payload with gyroscope data
        String jsonData = String.format("{\"x\": %f, \"y\": %f, \"z\": %f}", x, y, z);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(jsonData, JSON);

        // Create an AsyncTask to perform the network operation in the background
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                // Send a POST request to the server
                Request request = new Request.Builder()
                        .url(serverUrl)
                        .post(requestBody)
                        .build();

                try {
                    client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }
}
