package com.foton.robot_controller;

import java.util.ArrayList;

/**
 * Created by foton on 06.10.13.
 */
public class CommandCreator {
    ArrayList<Integer> GetMoveCommand(Movement movement) {
        ArrayList<Integer> values = new ArrayList<Integer>();

        values.add(255);
        values.add(1);
        values.add((int) (Math.abs(movement.leftSpeed) * 254));
        values.add(movement.leftSpeed >= 0 ? 1 : 0);
        values.add((int) (Math.abs(movement.rightSpeed) * 254));
        values.add((movement.rightSpeed >= 0 ? 1 : 0));

        return values;
    }

    ArrayList<Integer> GetLightCommand(int intensity) {
        ArrayList<Integer> values = new ArrayList<Integer>();

        values.add(255);
        values.add(2);
        values.add(intensity);

        return values;
    }

    ArrayList<Integer> GetReadLightCommand() {
        ArrayList<Integer> values = new ArrayList<Integer>();

        values.add(255);
        values.add(3);
        values.add(0);

        return values;
    }

    ArrayList<Integer> GetInfraredDistanceCommand() {
        ArrayList<Integer> values = new ArrayList<Integer>();

        values.add(255);
        values.add(4);
        values.add(0);

        return values;
    }

    ArrayList<Integer> GetMoveServoCommand() {
        ArrayList<Integer> values = new ArrayList<Integer>();

        values.add(255);
        values.add(5);
        values.add(0);

        return values;
    }

    ArrayList<Integer> GetCompassAngleCommand() {
        ArrayList<Integer> values = new ArrayList<Integer>();

        values.add(255);
        values.add(6);
        values.add(0);

        return values;
    }
}

