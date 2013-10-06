package com.foton.robot_controller;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import java.util.ArrayList;

/**
 * Created by foton on 06.10.13.
 */
public class RobotClient {
    private RobotService service;
    private BluetoothClient bluetoothClient;
    private boolean bounded = false;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder binder) {
            service = ((RobotService.LocalBinder)binder).getService();
            service.setBluetoothClient(bluetoothClient);
            bounded = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bounded = false;
        }
    };

    public void startRobot(Context context) {
        Intent intent = new Intent(context, RobotService.class);
        context.startService(intent);
    }

    public void stopRobot(Context context) {
        Intent intent = new Intent(context, RobotService.class);
        context.stopService(intent);
    }

    public void bindRobot(Context context, BluetoothClient bluetoothClient_) {
        bluetoothClient = bluetoothClient_;
        Intent intent = new Intent(context, RobotService.class);
        context.bindService(intent, connection, 0);
    }

    public void unbindRobot(Context context) {
        if (bounded) {
            context.unbindService(connection);
            bounded = false;
        }
    }

    public boolean sendCommand(ArrayList<Integer> command) {
        if (bounded)
            return service.sendCommand(command);
        return false;
    }
}
