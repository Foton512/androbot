package com.foton.robot_controller;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.app.Activity;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class AutopilotActivity extends Activity {
    private BluetoothClient bluetoothClient = new BluetoothClient();
    private RobotClient robotClient = new RobotClient();
    private Boolean robotConnected = false;
    private Boolean running = false;
    private TextView startStopTextView;
    private ServiceConnection robotConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder binder) {
            robotConnected = true;
            running = robotClient.getAutopilotRunning();
            setStartStopTextViewLooking();
        }
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            robotConnected = false;
            setStartStopTextViewLooking();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autopilot);
    }

    @Override
    protected void onStart() {
        super.onStart();

        bluetoothClient.bindBluetooth(this);
        robotClient.bindRobot(this, bluetoothClient, robotConnection);
        startStopTextView = (TextView)findViewById(R.id.startStopTextView);
        setStartStopTextViewLooking();
    }

    @Override
    protected void onStop() {
        super.onStop();

        bluetoothClient.unbindBluetooth(this);
        robotClient.unbindRobot(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.autopilot, menu);
        return true;
    }

    private void setStartStopTextViewLooking() {
        if (!robotConnected) {
            startStopTextView.setText("Getting state...");
            startStopTextView.setEnabled(false);
            startStopTextView.setBackgroundColor(getResources().getColor(R.color.grey));
            return;
        }
        startStopTextView.setEnabled(true);
        if (running) {
            startStopTextView.setText("Stop");
            startStopTextView.setBackgroundColor(getResources().getColor(R.color.darkRed));
        }
        else {
            startStopTextView.setText("Start");
            startStopTextView.setBackgroundColor(getResources().getColor(R.color.darkGreen));
        }
    }

    public void onStartStopClick(View view) {
        if (!running) {
            while (!robotClient.startAutopilot()) {
                try {
                    Thread.sleep(10);
                }
                catch (InterruptedException e) {
                }
            }
            running = true;
            setStartStopTextViewLooking();
        }
        else {
            while (!robotClient.stopAutopilot()) {
                try {
                    Thread.sleep(10);
                }
                catch (InterruptedException e) {
                }
            }
            running = false;
            setStartStopTextViewLooking();
        }
    }

    public void onViewMapClick(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }
    
}
