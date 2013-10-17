package com.foton.robot_controller;

import java.util.HashMap;
import java.util.Stack;

/**
 * Created by foton on 17.10.13.
 */
public abstract class Macros {
    protected String state = "start"; // Begin with state "start". Set state "end" to end macros

    protected Stack macrosStack;
    protected RobotService robot;
    protected CommandCreator commandCreator = new CommandCreator();

    public Macros(RobotService robot_, Stack macrosStack_) {
        robot = robot_;
        macrosStack = macrosStack_;
    }

    public void run() {
        if (state == "end")
            macrosStack.pop();
        else
            iteration();
    }

    protected abstract void iteration(); // Process all stack states including "start". Do not process "end" state
}