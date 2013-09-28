package com.foton.robot_controller;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

public class BluetoothService extends Service {
    static final String deviceAddress = "20:13:06:03:10:30";
    private final IBinder binder = new LocalBinder();
    private ConnectionThread connectionThread;
    private volatile BluetoothDevice bluetoothDevice;
    private AtomicBoolean bluetoothConnected = new AtomicBoolean(false);

    // Client methods
    public boolean sendInt(int message) {
        return send(message);
    }

    // Public
    public class LocalBinder extends Binder {
        BluetoothService getService() {
            return BluetoothService.this;
        }
    }

    @Override
    public void onCreate() {
        BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
        bluetoothDevice = bluetooth.getRemoteDevice(deviceAddress);

        connect();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    // Private
    private class ConnectionThread extends Thread {
        public volatile BluetoothSocket socket;

        ConnectionThread() {
            super();
        }
        public void run() {
            while (true) {
                try{
                    Method m = bluetoothDevice.getClass().getMethod("createRfcommSocket",
                            new Class[] {int.class});
                    socket = (BluetoothSocket)m.invoke(bluetoothDevice, 1);
                    socket.connect();
                    bluetoothConnected.set(true);

                    return;
                }
                catch (Exception e) {
                }
                try {
                    wait(500);
                }
                catch (Exception e) {
                }
            }
        }
    }

    private boolean send(int message) {
        if (!bluetoothConnected.get())
            return false;
        try {
            OutputStream outputStream = connectionThread.socket.getOutputStream();
            InputStream inputStream = connectionThread.socket.getInputStream();
            outputStream.write(message);
            int res = inputStream.read();
            return true;
        }
        catch (Exception e) {
            bluetoothConnected.set(false);
            connect();
            return false;
        }
    }

    private void connect() {
        connectionThread = new ConnectionThread();
        connectionThread.start();
    }

}
