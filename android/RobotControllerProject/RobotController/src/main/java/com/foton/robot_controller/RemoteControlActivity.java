package com.foton.robot_controller;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class RemoteControlActivity extends Activity implements View.OnTouchListener {
    ImageView forwardImage;
    ImageView backwardImage;
    ImageView leftImage;
    ImageView rightImage;
    ImageView turnLeftImage;
    ImageView turnRightImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_control);

        forwardImage = (ImageView) this.findViewById(R.id.forwardImage);
        forwardImage.setOnTouchListener(this);
        backwardImage = (ImageView) this.findViewById(R.id.backwardImage);
        backwardImage.setOnTouchListener(this);
        leftImage = (ImageView) this.findViewById(R.id.leftImage);
        leftImage.setOnTouchListener(this);
        rightImage = (ImageView) this.findViewById(R.id.rightImage);
        rightImage.setOnTouchListener(this);
        turnLeftImage = (ImageView) this.findViewById(R.id.turnLeftImage);
        turnLeftImage.setOnTouchListener(this);
        turnRightImage = (ImageView) this.findViewById(R.id.turnRightImage);
        turnRightImage.setOnTouchListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.remote_control, menu);
        return true;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        float x = 0, y = 0;
        if (view == turnRightImage) {
            int action = motionEvent.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    x = motionEvent.getX();
                    y = motionEvent.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    x = motionEvent.getX();
                    y = motionEvent.getY();
                    break;
                default:
                    break;
            }
        }
        Log.d("my", Float.toString(x) + " " + Float.toString(y));

        return true;
    }
}
