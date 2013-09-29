package com.foton.robot_controller;

import android.graphics.Rect;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class RemoteControlActivity extends Activity implements View.OnTouchListener {
    LinearLayout controlLayout;
    int[] controlLayoutLocation = new int[2];

    Rect forwardImageRect;
    Rect backwardImageRect;
    Rect leftImageRect;
    Rect rightImageRect;
    Rect turnLeftImageRect;
    Rect turnRightImageRect;

    float speed = 0;
    float turn = 0;
    boolean turnOnly = false;

    BluetoothClient bluetoothClient = new BluetoothClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_control);

        controlLayout = (LinearLayout) this.findViewById(R.id.controlLayout);
        controlLayout.setOnTouchListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bluetoothClient.bindBluetooth(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        bluetoothClient.unbindBluetooth(this);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.remote_control, menu);
        return true;
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
        float newTurn = 0;
        boolean newTurnOnly = false;

        if (turnLeft) {
            newTurn = -1;
            newTurnOnly = true;
        }
        else if (turnRight) {
            newTurn = 1;
            newTurnOnly = true;
        }
        else {
            newSpeed = forward - backward;
            newTurn = right - left;
            newTurnOnly = false;
        }

        if (newSpeed != speed || newTurn != turn || newTurnOnly != turnOnly) {
            speed = newSpeed;
            turn = newTurn;
            turnOnly = newTurnOnly;

            bluetoothClient.sendInt(
                    (int)(Math.abs(speed) * 200),
                    speed >= 0 ? 0 : 1,
                    (int)(Math.abs(turn) * 200),
                    turn >= 0 ? 0 : 1,
                    turnOnly ? 1 : 0
            );
        }

        return true;
    }

    private Rect getViewRect(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return new Rect(location[0], location[1], location[0] + view.getWidth(), location[1] + view.getHeight());
    }
}
