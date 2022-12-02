package com.example;

public class AlertEvent {
    private String location, type;

    public AlertEvent() {};

    public AlertEvent(String location, String type) {
        this.location = location;
        this.type = type;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
}
