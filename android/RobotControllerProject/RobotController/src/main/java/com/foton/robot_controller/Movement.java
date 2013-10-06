package com.foton.robot_controller;

import android.util.Log;

/**
 * Created by foton on 06.10.13.
 */
public class Movement {
    public float leftSpeed;
    public float rightSpeed;

    Movement(float leftSpeed_, float rightSpeed_) {
        leftSpeed = leftSpeed_;
        rightSpeed = rightSpeed_;
    }

    Movement(float speed, float turnSpeed, boolean turnOnly) {
        if (turnOnly) {
            if (turnSpeed >= 0) {
                leftSpeed = 1;
                rightSpeed = -1;
            }
            else {
                leftSpeed = -1;
                rightSpeed = 1;
            }
        }
        else {
            leftSpeed = rightSpeed = speed;
            if (speed != 0) {
                if (turnSpeed >= 0)
                    rightSpeed += (speed >= 0 ? -1 : 1) * turnSpeed;
                else
                    leftSpeed += (speed >= 0 ? 1 : -1) * turnSpeed;
            }
        }
    }
}
