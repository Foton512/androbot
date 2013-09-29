package com.foton.robot_controller;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MenuActivity extends Activity {
    BluetoothClient bluetoothClient = new BluetoothClient();

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
    }


    @Override
    protected void onStop() {
        super.onStop();
        bluetoothClient.unbindBluetooth(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public void onRemoteControlClick(View view) {
        Intent intent = new Intent(this, RemoteControlActivity.class);
        startActivity(intent);
    }
    
}
