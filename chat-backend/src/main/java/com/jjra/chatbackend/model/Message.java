package com.jjra.chatbackend.model;

public class Message {

    private String id;
    private String text;

    public Message() {}

    public Message(String id, String text) {
        this.id = text;
        this.text = text;
    }

    public Message(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
