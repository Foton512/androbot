package com.foton.robot_controller;

import android.app.Service;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by foton on 06.10.13.
 */
public class RobotService extends Service {
    private final IBinder binder = new LocalBinder();
    private BluetoothClient bluetoothClient = new BluetoothClient();
    private AutopilotThread autopilotThread;
    private volatile boolean stopAutopilot = false;

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

    public boolean sendCommand(ArrayList<Integer> command, ArrayList<Integer> result) {
        return bluetoothClient.sendCommand(command, result);
    }

    public void startAutopilot() {
        if (autopilotThread != null && autopilotThread.isAlive())
            return;
        stopAutopilot = false;
        autopilotThread = new AutopilotThread();
        autopilotThread.start();
    }

    public void stopAutopilot() {
        stopAutopilot = true;
    }

    private class AutopilotThread extends Thread {
        Stack<Macros> macrosStack = new Stack<Macros>();

        AutopilotThread() {
            super();
            macrosStack.push(new MoveStraight(RobotService.this, macrosStack));
        }
        public void run() {
            while (!stopAutopilot) {
                Macros macros = macrosStack.peek();
                macros.run();

                try {
                    Thread.sleep(10);
                }
                catch (InterruptedException e) {
                }
            }
        }
    }
}
