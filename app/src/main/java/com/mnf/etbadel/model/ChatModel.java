package com.mnf.etbadel.model;

public class ChatModel {
    private String chatId;
    private int user1Id;
    private int user2Id;
    private String user1Name;
    private String user1Profile;
    private String user2Name;
    private String user2Profile;
    private String lastMessage;
    private String lastMessageDateTime;

    private boolean isStarted;

    public boolean isStarted() {
        return isStarted;
    }

    public String getUser1Profile() {
        return user1Profile;
    }

    public void setUser1Profile(String user1Profile) {
        this.user1Profile = user1Profile;
    }

    public String getUser2Profile() {
        return user2Profile;
    }

    public void setUser2Profile(String user2Profile) {
        this.user2Profile = user2Profile;
    }

    public void setStarted(boolean started) {
        isStarted = started;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public int getUser1Id() {
        return user1Id;
    }

    public void setUser1Id(int user1Id) {
        this.user1Id = user1Id;
    }

    public int getUser2Id() {
        return user2Id;
    }

    public void setUser2Id(int user2Id) {
        this.user2Id = user2Id;
    }

    public String getUser1Name() {
        return user1Name;
    }

    public void setUser1Name(String user1Name) {
        this.user1Name = user1Name;
    }

    public String getUser2Name() {
        return user2Name;
    }

    public void setUser2Name(String user2Name) {
        this.user2Name = user2Name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastMessageDateTime() {
        return lastMessageDateTime;
    }

    public void setLastMessageDateTime(String lastMessageDateTime) {
        this.lastMessageDateTime = lastMessageDateTime;
    }
}
