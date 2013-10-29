package com.foton.robot_controller;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;

public class MenuActivity extends Activity {
    BluetoothClient bluetoothClient = new BluetoothClient();
    RobotClient robotClient = new RobotClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    @Override
    protected void onStart() {
        super.onStart();

        bluetoothClient.enableBluetooth(this);
        bluetoothClient.startBluetooth(this);
        bluetoothClient.bindBluetooth(this);

        robotClient.startRobot(this);
        robotClient.bindRobot(this, bluetoothClient);
    }

    @Override
    protected void onStop() {
        super.onStop();
        bluetoothClient.unbindBluetooth(this);
        robotClient.unbindRobot(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onTraditionalRemoteControlClick(View view) {
        Intent intent = new Intent(this, TraditionalRemoteControlActivity.class);
        startActivity(intent);
    }

    public void onSimpleRemoteControlClick(View view) {
        Intent intent = new Intent(this, SimpleRemoteControlActivity.class);
        startActivity(intent);
    }

    public void onAutopilotClick(View view) {
        Intent intent = new Intent(this, AutopilotActivity.class);
        startActivity(intent);
    }

    public void onExitClick(View view) {
        bluetoothClient.disconnect();
        bluetoothClient.stopBluetooth(this);
        robotClient.stopRobot(this);
        finish();
    }
    
}
