package com.example.continuous_identyfication;

import java.util.ArrayList;
import java.util.List;

class EventData {
    public int eventId;
    public List<Float> positionsX;
    public List<Float> positionsY;
    public List<Float> pressure;
    public List<Float> fingerSize;
    public long time;

    public EventData(int idCounter, float x, float y, float pressure, float fingerSize, long time) {
        this.eventId = idCounter;
        this.positionsX = new ArrayList<Float>();
        this.positionsX.add(x);
        this.positionsY = new ArrayList<Float>();
        this.positionsY.add(y);
        this.pressure = new ArrayList<Float>();
        this.pressure.add(pressure);
        this.fingerSize = new ArrayList<Float>();
        this.fingerSize.add(fingerSize);
        this.time = time;
    }

    public EventData(int idCounter, List<Float> x, List<Float> y, List<Float> pressure, List<Float> fingerSize, long time) {
        this.eventId = idCounter;
        this.positionsX = x;
        this.positionsY = y;
        this.pressure = pressure;
        this.fingerSize = fingerSize;
        this.time = time;
    }

    public void appendData(float x, float y, float pressure, float fingerSize, long time) {
        this.positionsX.add(x);
        this.positionsY.add(y);
        this.pressure.add(pressure);
        this.fingerSize.add(fingerSize);
    }
}
