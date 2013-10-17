package com.foton.robot_controller;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class AutopilotActivity extends Activity {
    BluetoothClient bluetoothClient = new BluetoothClient();
    RobotClient robotClient = new RobotClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autopilot);
    }

    @Override
    protected void onStart() {
        super.onStart();

        bluetoothClient.bindBluetooth(this);
        robotClient.bindRobot(this, bluetoothClient);
    }

    @Override
    protected void onStop() {
        super.onStop();

        bluetoothClient.unbindBluetooth(this);
        onStopClick(this.findViewById(android.R.id.content));
        robotClient.unbindRobot(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.autopilot, menu);
        return true;
    }

    public void onStartClick(View view) {
        while (!robotClient.startAutopilot()) {
            try {
                Thread.sleep(10);
            }
            catch (InterruptedException e) {
            }
        }
    }

    public void onStopClick(View view) {
        while (!robotClient.stopAutopilot()) {
            try {
                Thread.sleep(10);
            }
            catch (InterruptedException e) {
            }
        }
    }
    
}
