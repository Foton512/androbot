package com.foton.robot_controller;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by foton on 04.11.13.
 */
public class Map extends View {

    RobotClient robotClient;
    Pair<Double, Double> centerCoords;
    double cellSize;

    void setRobotClient(RobotClient robotClient_) {
        robotClient = robotClient_;
    }

    public Map(Context context, Pair<Double, Double> centerCoords_, double cellSize_) {
        super(context);
        centerCoords = centerCoords_;
        cellSize = cellSize_;
    }
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(R.color.orange));
        canvas.drawPaint(paint);

        Field field= robotClient.getField();
        if (field != null) {
            ArrayList<FieldChunk> fieldChunks = field.getFieldChunks();
            for (FieldChunk fieldChunk: fieldChunks) {
                for (int i = 0; i < fieldChunk.size; ++i) {
                    for (int j = 0; j < fieldChunk.size; ++j) {
                        boolean isWall = fieldChunk.cells[i][j].wall;
                        if (isWall)
                            paint.setColor(getResources().getColor(R.color.blue));
                        else
                            paint.setColor(getResources().getColor(R.color.green));
                        canvas.drawRect((float)(centerCoords.first + (fieldChunk.leftCellX + i) * cellSize),
                                        (float)(centerCoords.second - (fieldChunk.bottomCellY + j + 1) * cellSize + 1),
                                        (float)(centerCoords.first + (fieldChunk.leftCellX + i + 1) * cellSize - 1),
                                        (float)(centerCoords.second - (fieldChunk.bottomCellY + j) * cellSize),
                                        paint);
                    }
                }
            }

        }


    }
}
