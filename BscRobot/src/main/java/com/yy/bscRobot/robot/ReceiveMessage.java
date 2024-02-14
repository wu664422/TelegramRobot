package com.yy.bscRobot.robot;

public class ReceiveMessage {

    private String id;
    private String chatId;
    private String userId;
    private String userName;
    private String content;
    private ReceiveMessageType messageType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ReceiveMessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(ReceiveMessageType messageType) {
        this.messageType = messageType;
    }


}
