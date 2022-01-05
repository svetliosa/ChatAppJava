package com.example.chatappjava;

public class Messages {

    String message;
    String senderId;
    String recieverId;
    String shortTime;
    String longTime;

    public Messages() {

    }

    public Messages(String message, String senderId, String recieverId, String shortTime, String longTime) {
        this.message = message;
        this.senderId = senderId;
        this.recieverId = recieverId;
        this.shortTime = shortTime;
        this.longTime = longTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getRecieverId() {
        return recieverId;
    }

    public void setRecieverId(String recieverId) {
        this.recieverId = recieverId;
    }

    public String getShortTime() {
        return shortTime;
    }

    public void setShortTime(String shortTime) {
        this.shortTime = shortTime;
    }

    public String getLongTime() {
        return longTime;
    }

    public void setLongTime(String longTime) {
        this.longTime = longTime;
    }
}
