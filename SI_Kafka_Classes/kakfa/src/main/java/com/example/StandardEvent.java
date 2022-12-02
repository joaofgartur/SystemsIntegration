package com.example;

public class StandardEvent {
    private String location;
    private int temperature;

    public StandardEvent() {};

    public StandardEvent(String location, int temperature) {
        this.location = location;
        this.temperature = temperature;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getTemperature() {
        return temperature;
    }
}
