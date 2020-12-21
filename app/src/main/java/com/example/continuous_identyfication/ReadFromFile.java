package com.example.continuous_identyfication;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReadFromFile {
    static List<EventData> read(String path) {
        List<EventData> data = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(path));) {
            while (scanner.hasNextLine()) {
                data.add(getDataFromLine(scanner.nextLine()));
            }
        } catch (FileNotFoundException e) {
            System.err.println("Empty file");
            e.printStackTrace();
            return data;
        }
        return data;
    }

    private static EventData getDataFromLine(String nextLine) {
        String[] stringData = nextLine.split(";");
        int eventId = Integer.parseInt(stringData[0]);

        List<Float> positionsX = new ArrayList<>();
        for (String singleString : stringData[1].split(",")
        ) {
            positionsX.add(Float.valueOf(singleString));
        }

        List<Float> positionsY = new ArrayList<>();
        for (String singleString : stringData[2].split(",")
        ) {
            positionsY.add(Float.valueOf(singleString));
        }

        List<Float> pressure = new ArrayList<>();
        for (String singleString : stringData[3].split(",")
        ) {
            pressure.add(Float.valueOf(singleString));
        }

        List<Float> fingerSize = new ArrayList<>();
        for (String singleString : stringData[4].split(",")
        ) {
            fingerSize.add(Float.valueOf(singleString));
        }

        long time = Long.parseLong(stringData[5].trim());

        return new EventData(eventId, positionsX, positionsY, pressure, fingerSize, time);
    }
}
