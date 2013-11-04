package com.foton.robot_controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.Menu;
import android.widget.TextView;

public class MapActivity extends Activity {
    private final double initCellSize = 10;
    private BluetoothClient bluetoothClient = new BluetoothClient();
    private RobotClient robotClient = new RobotClient();
    private Map map;
    private BroadcastReceiver repaintReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        Pair<Double, Double> centerCoords = new Pair<Double, Double>(Double.valueOf(metrics.widthPixels / 2), Double.valueOf(metrics.heightPixels / 2));
        map = new Map(this, centerCoords, initCellSize);
        setContentView(map);

        repaintReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                map.invalidate();
            }
        };
        IntentFilter intentFilter = new IntentFilter(RobotService.BROADCAST_REPAINT);
        registerReceiver(repaintReceiver, intentFilter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        bluetoothClient.bindBluetooth(this);
        robotClient.bindRobot(this, bluetoothClient);
        map.setRobotClient(robotClient);
    }

    @Override
    protected void onStop() {
        super.onStop();

        bluetoothClient.unbindBluetooth(this);
        robotClient.unbindRobot(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(repaintReceiver);
    }
}
