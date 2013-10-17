package com.foton.robot_controller;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by foton on 17.10.13.
 */
public class MoveStraight extends Macros {
    boolean moving = false;

    MoveStraight(RobotService robot, Stack macrosStack) {
        super(robot, macrosStack);
    }

    @Override
    protected void iteration() {
        ArrayList<Integer> distanceBytes = new ArrayList<Integer>();
        int distance = 1000;
        if (robot.sendCommand(commandCreator.GetInfraredDistanceCommand(), distanceBytes))
            distance = distanceBytes.get(0) * 256 + distanceBytes.get(1);
        if (!moving && distance <= 350) {
            robot.sendCommand(commandCreator.GetMoveCommand(new Movement(1, 0, false)), new ArrayList<Integer>());
            moving = true;
        }
        if (moving && distance > 350) {
            robot.sendCommand(commandCreator.GetMoveCommand(new Movement(0, 0, false)), new ArrayList<Integer>());
            moving = false;
            macrosStack.push(new Turn(robot, macrosStack));
        }
    }
}
