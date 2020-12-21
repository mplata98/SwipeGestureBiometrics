package com.example.continuous_identyfication;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ModelData {
    private static final int DATA_MAX_SIZE = 500;
    private static final double ACCEPTATION_DISTANCE = 0.1;
    public boolean isLegible = false;
    public boolean firstTime = false;
    private ArrayList<Long> timeData;
    private ArrayList<Double> speedData;
    private double time_deviation = 0;
    private double speed_deviation = 0;

    public ModelData() {
        timeData = new ArrayList<>();
        speedData = new ArrayList<>();
    }

    public static ModelData readOldModel(Context context) {
        ModelData oldModel = new ModelData();
        File oldModelFile = new File(context.getExternalFilesDir(null), "oldModel.txt");
        try {
            oldModelFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<EventData> oldData = (ArrayList<EventData>) ReadFromFile.read(oldModelFile.getPath());
        if (oldData.size() < DATA_MAX_SIZE) {
            for (EventData eventData : oldData
            ) {
                oldModel.timeData.add(eventData.time);
                oldModel.speedData.add(Math.sqrt(Math.pow(eventData.positionsX.get(0) - eventData.positionsX.get(eventData.positionsX.size() - 1), 2) + Math.pow(eventData.positionsY.get(0) - eventData.positionsY.get(eventData.positionsY.size() - 1), 2)) / eventData.time);
            }
        } else {
            oldModel.firstTime = true;
        }
        oldModel.updateModel();
        return oldModel;
    }

    public void addNewData(EventData newData) {
        this.timeData.add(newData.time);
        this.speedData.add(Math.sqrt(Math.pow(newData.positionsX.get(0) - newData.positionsX.get(newData.positionsX.size() - 1), 2) + Math.pow(newData.positionsY.get(0) - newData.positionsY.get(newData.positionsY.size() - 1), 2)) / newData.time);
        if (timeData.size() > DATA_MAX_SIZE) {
            timeData.remove(0);
            speedData.remove(0);
            updateModel();
        }
    }

    private void updateModel() {
        this.isLegible = true;
        time_deviation = StandardDeviation.calculateFromLong(timeData);
        speed_deviation = StandardDeviation.calculateFromDouble(speedData);
    }

    public double compareWithModel(ModelData oldModel) {
        double acceptationScore = 0;
        acceptationScore += this.compareTime(oldModel.time_deviation);
        acceptationScore += this.compareSpeed(oldModel.speed_deviation);
        return acceptationScore;
    }

    private double compareSpeed(double oldModelDeviation) {
        if (Math.abs(oldModelDeviation - this.speed_deviation / oldModelDeviation) < ACCEPTATION_DISTANCE) {
            return 1;
        }
        return 0;
    }

    private double compareTime(double oldModelDeviation) {
        if (Math.abs(oldModelDeviation - this.time_deviation / oldModelDeviation) < ACCEPTATION_DISTANCE) {
            return 1;
        }
        return 0;
    }

    public ModelData removeNumberOfRecords(int numberToRemove) {
        for (int i = 0; i < numberToRemove; i++) {
            this.timeData.remove(0);
            this.speedData.remove(0);
        }
        this.isLegible = false;
        this.time_deviation = 0;
        this.speed_deviation = 0;
        return this;
    }
}
