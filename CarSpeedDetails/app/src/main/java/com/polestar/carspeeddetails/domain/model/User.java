package com.polestar.carspeeddetails.domain.model;

public class User {
    private String id;
    private String name;
    private float maxSpeed;
    private String communicationChannel;

    public User(String id, String name, float maxSpeed, String communicationChannel) {
        this.id = id;
        this.name = name;
        this.maxSpeed = maxSpeed;
        this.communicationChannel = communicationChannel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public String getCommunicationChannel() {
        return communicationChannel;
    }

    public void setCommunicationChannel(String communicationChannel) {
        this.communicationChannel = communicationChannel;
    }
}
