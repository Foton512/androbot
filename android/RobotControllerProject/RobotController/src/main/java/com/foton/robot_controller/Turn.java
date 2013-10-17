package com.foton.robot_controller;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by foton on 17.10.13.
 */
public class Turn extends Macros {
    int count = 0;

    Turn(RobotService robot, Stack macrosStack) {
        super(robot, macrosStack);
    }

    @Override
    protected void iteration() {
        if (state == "start") {
            robot.sendCommand(commandCreator.GetMoveCommand(new Movement(0, 0, true)), new ArrayList<Integer>());
            ++count;
            if (count >= 20)
                state = "end";
        }
    }
}
