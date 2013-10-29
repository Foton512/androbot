package com.foton.robot_controller;

import android.util.Pair;

/**
 * Created by foton on 29.10.13.
 */
public class FieldChunk {
    public int size;
    public int chunkX;
    public int chunkY;
    public int leftCellX;
    public int bottomCellY;

    FieldChunk(int size_, int chunkX_, int chunkY_) {
        size = size_;
        chunkX = chunkX_;
        chunkY = chunkY_;
        leftCellX = chunkX * size;
        bottomCellY = chunkY * size;
    }

    private Pair<Integer, Integer> getLocalCell(Pair<Integer, Integer> globalCell) {
        return new Pair<Integer, Integer>(globalCell.first - leftCellX, globalCell.second - bottomCellY);
    }
}
