package com.foton.robot_controller;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class SimpleRemoteControlActivity extends Activity implements View.OnTouchListener {
    LinearLayout controlLayout;
    int[] controlLayoutLocation = new int[2];

    Rect leftForwardImageRect;
    Rect leftBackwardImageRect;
    Rect rightForwardImageRect;
    Rect rightBackwardImageRect;

    float leftSpeed = 0;
    float rightSpeed = 0;

    BluetoothClient bluetoothClient = new BluetoothClient();
    RobotClient robotClient = new RobotClient();
    CommandCreator commandCreator = new CommandCreator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_remote_control);

        controlLayout = (LinearLayout) this.findViewById(R.id.controlLayout);
        controlLayout.setOnTouchListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        bluetoothClient.bindBluetooth(this);
        robotClient.bindRobot(this, bluetoothClient);
    }

    @Override
    protected void onStop() {
        super.onStop();

        bluetoothClient.unbindBluetooth(this);
        robotClient.unbindRobot(this);
    }

    @Override
    public void onWindowFocusChanged (boolean hasFocus) {
        controlLayout.getLocationOnScreen(controlLayoutLocation);

        leftForwardImageRect = getViewRect(this.findViewById(R.id.leftForwardImage));
        leftBackwardImageRect = getViewRect(this.findViewById(R.id.leftBackwardImage));
        rightForwardImageRect = getViewRect(this.findViewById(R.id.rightForwardImage));
        rightBackwardImageRect = getViewRect(this.findViewById(R.id.rightBackwardImage));
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = motionEvent.getActionMasked();

        float newLeftSpeed = 0;
        float newRightSpeed = 0;

        if (action != motionEvent.ACTION_UP) {
            for (int i = 0; i < motionEvent.getPointerCount(); ++i) {
                int x = (int)(motionEvent.getX(i) + controlLayoutLocation[0]);
                int y = (int)(motionEvent.getY(i) + controlLayoutLocation[1]);

                if (leftForwardImageRect.contains(x, y))
                    newLeftSpeed = (float)(leftForwardImageRect.bottom - y) / leftForwardImageRect.height();
                if (leftBackwardImageRect.contains(x, y))
                    newLeftSpeed = (float)(leftBackwardImageRect.top - y) / leftBackwardImageRect.height();
                if (rightForwardImageRect.contains(x, y))
                    newRightSpeed = (float)(rightForwardImageRect.bottom - y) / rightForwardImageRect.height();
                if (rightBackwardImageRect.contains(x, y))
                    newRightSpeed = (float)(rightBackwardImageRect.top - y) / rightBackwardImageRect.height();
            }
        }

        if (newLeftSpeed != leftSpeed || newRightSpeed != rightSpeed) {
            leftSpeed = newLeftSpeed;
            rightSpeed = newRightSpeed;

            Movement movement = new Movement(leftSpeed, rightSpeed);
            robotClient.sendCommand(commandCreator.GetMoveCommand(movement), new ArrayList<Integer>());
        }

        return true;
    }

    private Rect getViewRect(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return new Rect(location[0], location[1], location[0] + view.getWidth(), location[1] + view.getHeight());
    }
}
