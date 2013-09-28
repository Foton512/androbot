package com.foton.robot_controller;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.app.Activity;
import android.os.IBinder;
import android.view.Menu;
import android.view.View;
import android.widget.ToggleButton;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

public class MenuActivity extends Activity implements View.OnClickListener {
    ToggleButton runButton;
    BluetoothService bluetoothService;
    boolean bluetoothBounded = false;

    private ServiceConnection bluetoothConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            BluetoothService.LocalBinder binder = (BluetoothService.LocalBinder)service;
            bluetoothService = binder.getService();
            bluetoothBounded = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bluetoothBounded = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);
        runButton = (ToggleButton) findViewById(R.id.runButton);
        runButton.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        enableBluetooth();
        startBluetooth();
    }

    private void enableBluetooth() {
        startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 0);
    }

    private void startBluetooth() {
        Intent intent = new Intent(this, BluetoothService.class);
        startService(intent);
        bindService(intent, bluetoothConnection, 0);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (bluetoothBounded) {
            unbindService(bluetoothConnection);
            bluetoothBounded = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        if (bluetoothBounded) {
            bluetoothService.sendInt((runButton.isChecked() ? 1 : 0));
        }
    }
    
}
