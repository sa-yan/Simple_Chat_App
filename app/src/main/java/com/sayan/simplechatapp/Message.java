package com.sayan.simplechatapp;

public class Message {
    private String nickName, message;

    public Message() {
    }

    public Message(String nickName, String message) {
        this.nickName = nickName;
        this.message = message;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
