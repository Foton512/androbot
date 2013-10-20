package com.foton.robot_controller;

import android.graphics.Rect;
import android.os.Bundle;
import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class TraditionalRemoteControlActivity extends Activity implements View.OnTouchListener {
    LinearLayout controlLayout;
    int[] controlLayoutLocation = new int[2];

    Rect forwardImageRect;
    Rect backwardImageRect;
    Rect leftImageRect;
    Rect rightImageRect;
    Rect turnLeftImageRect;
    Rect turnRightImageRect;

    float speed = 0;
    float turnSpeed = 0;
    boolean turnOnly = false;

    BluetoothClient bluetoothClient = new BluetoothClient();
    RobotClient robotClient = new RobotClient();
    CommandCreator commandCreator = new CommandCreator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traditional_remote_control);

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

        forwardImageRect = getViewRect(this.findViewById(R.id.forwardImage));
        backwardImageRect = getViewRect(this.findViewById(R.id.backwardImage));
        leftImageRect = getViewRect(this.findViewById(R.id.leftImage));
        rightImageRect = getViewRect(this.findViewById(R.id.rightImage));
        turnLeftImageRect = getViewRect(this.findViewById(R.id.turnLeftImage));
        turnRightImageRect = getViewRect(this.findViewById(R.id.turnRightImage));
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = motionEvent.getActionMasked();

        float forward = 0;
        float backward = 0;
        float left = 0;
        float right = 0;
        boolean turnLeft = false;
        boolean turnRight = false;

        if (action != motionEvent.ACTION_UP) {
            for (int i = 0; i < motionEvent.getPointerCount(); ++i) {
                int x = (int)(motionEvent.getX(i) + controlLayoutLocation[0]);
                int y = (int)(motionEvent.getY(i) + controlLayoutLocation[1]);

                if (forwardImageRect.contains(x, y))
                    forward = (float)(forwardImageRect.bottom - y) / forwardImageRect.height();
                if (backwardImageRect.contains(x, y))
                    backward = (float)(y - backwardImageRect.top) / backwardImageRect.height();
                if (leftImageRect.contains(x, y))
                    left = (float)(leftImageRect.right - x) / leftImageRect.width();
                if (rightImageRect.contains(x, y))
                    right = (float)(x - rightImageRect.left) / rightImageRect.width();
                if (turnLeftImageRect.contains(x, y))
                    turnLeft = true;
                if (turnRightImageRect.contains(x, y))
                    turnRight = true;
            }
        }

        float newSpeed = 0;
        float newTurnSpeed = 0;
        boolean newTurnOnly = false;

        if (turnLeft) {
            newTurnSpeed = -1;
            newTurnOnly = true;
        }
        else if (turnRight) {
            newTurnSpeed = 1;
            newTurnOnly = true;
        }
        else {
            newSpeed = forward - backward;
            newTurnSpeed = right - left;
            newTurnOnly = false;
        }

        if (newSpeed != speed || newTurnSpeed != turnSpeed || newTurnOnly != turnOnly) {
            speed = newSpeed;
              turnSpeed = newTurnSpeed;
            turnOnly = newTurnOnly;

            Movement movement = new Movement(speed, turnSpeed, turnOnly);
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
