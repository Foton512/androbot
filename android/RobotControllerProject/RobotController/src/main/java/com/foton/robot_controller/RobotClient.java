package com.foton.robot_controller;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by foton on 06.10.13.
 */
public class RobotClient {
    private RobotService service;
    private BluetoothClient bluetoothClient;
    private boolean bounded = false;
    private ServiceConnection userConnection = null;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder binder) {
            service = ((RobotService.LocalBinder)binder).getService();
            service.setBluetoothClient(bluetoothClient);
            bounded = true;
            if (userConnection != null)
                userConnection.onServiceConnected(className, binder);
        }
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bounded = false;
            if (userConnection != null)
                userConnection.onServiceDisconnected(arg0);
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
        userConnection = null;
        bluetoothClient = bluetoothClient_;
        Intent intent = new Intent(context, RobotService.class);
        context.bindService(intent, connection, 0);
    }

    public void bindRobot(Context context, BluetoothClient bluetoothClient_, ServiceConnection userConnection_) {
        userConnection = userConnection_;
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

    public boolean sendCommand(ArrayList<Integer> command, ArrayList<Integer> result) {
        if (bounded)
            return service.sendCommand(command, result);
        return false;
    }

    public boolean startAutopilot() {
        if (bounded) {
            service.startAutopilot();
            return true;
        }
        return false;
    }

    public boolean stopAutopilot() {
        if (bounded) {
            service.stopAutopilot();
            return true;
        }
        return false;
    }

    public boolean repaint() {
        if (bounded) {
            service.repaint();
            return true;
        }
        return false;
    }

    public Field getField() {
        if (bounded)
            return service.getField();
        return null;
    }

    public boolean getAutopilotRunning() {
        return service.getAutopilotRunning();
    }
}
