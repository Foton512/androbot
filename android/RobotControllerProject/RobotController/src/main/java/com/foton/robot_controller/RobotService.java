package com.foton.robot_controller;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.ArrayList;

/**
 * Created by foton on 06.10.13.
 */
public class RobotService extends Service {
    private final IBinder binder = new LocalBinder();
    private BluetoothClient bluetoothClient = new BluetoothClient();

    void setBluetoothClient(BluetoothClient bluetoothClient_) {
        bluetoothClient = bluetoothClient_;
    }

    public class LocalBinder extends Binder {
        RobotService getService() {
            return RobotService.this;
        }
    }

    @Override
    public void onCreate() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public boolean sendCommand(ArrayList<Integer> command) {
        return bluetoothClient.sendCommand(command);
    }
}
