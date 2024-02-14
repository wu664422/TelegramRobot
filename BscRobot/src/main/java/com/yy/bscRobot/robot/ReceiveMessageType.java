package com.yy.bscRobot.robot;

public enum ReceiveMessageType {

    TEXT(0, "text"),
    BUTTON(1, "button");

    ReceiveMessageType(int type, String description) {
        this.type = type;
        this.description = description;
    }
    private int type;
    private String description;
    public int getCode() {
        return type;
    }
    public String getDescription() {
        return description;
    }

}
