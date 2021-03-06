package com.foton.robot_controller;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import java.util.ArrayList;

public class BluetoothClient {
    private BluetoothService service;
    private boolean bounded = false;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder binder) {
            service = ((BluetoothService.LocalBinder)binder).getService();
            bounded = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bounded = false;
        }
    };

    public void enableBluetooth(Activity activity) {
        activity.startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 0);
    }

    public void startBluetooth(Context context) {
        Intent intent = new Intent(context, BluetoothService.class);
        context.startService(intent);
    }

    public void stopBluetooth(Context context) {
        Intent intent = new Intent(context, BluetoothService.class);
        context.stopService(intent);
    }

    public void bindBluetooth(Context context) {
        Intent intent = new Intent(context, BluetoothService.class);
        context.bindService(intent, connection, 0);
    }

    public void unbindBluetooth(Context context) {
        if (bounded) {
            context.unbindService(connection);
            bounded = false;
        }
    }

    public void disconnect() {
        service.disconnect();
    }

    boolean sendCommand(ArrayList<Integer> command, ArrayList<Integer> result) {
        if (bounded)
            return service.sendCommand(command, result);
        return false;
    }
}