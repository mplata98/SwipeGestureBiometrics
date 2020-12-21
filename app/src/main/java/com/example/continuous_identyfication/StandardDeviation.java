package com.example.continuous_identyfication;

import java.util.ArrayList;

public class StandardDeviation {
    static double calculateFromLong(ArrayList<Long> input) {
        double sum = 0.0;
        for (long number : input
        ) {
            sum += number;
        }
        double average = sum / ((double) input.size());
        sum = 0.0;
        for (long number : input
        ) {
            sum += Math.pow(((double) number - average), 2);
        }
        average = sum / (double) input.size();
        return Math.sqrt(average);
    }

    static double calculateFromDouble(ArrayList<Double> input) {
        double sum = 0.0;
        for (double number : input
        ) {
            sum += number;
        }
        double average = sum / ((double) input.size());
        sum = 0.0;
        for (double number : input
        ) {
            sum += Math.pow(((double) number - average), 2);
        }
        average = sum / (double) input.size();
        return Math.sqrt(average);
    }
}
