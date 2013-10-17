package com.foton.robot_controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ToggleButton;

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
        /*bluetoothClient.bluetoothReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent i) {
                CheckBox connectedCheckBox = (CheckBox)findViewById(R.id.connectedCheckBox);
                connectedCheckBox.setChecked(true);
            }
        };
        registerReceiver(bluetoothClient.bluetoothReceiver, new IntentFilter("com.foton.intent.action.BLUETOOTH_CONNECTION"));*/

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

    public void onRemoteControlClick(View view) {
        Intent intent = new Intent(this, RemoteControlActivity.class);
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
