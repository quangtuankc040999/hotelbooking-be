package com.example.demo.payload.request;

public class CommentRequest {
    public String messenger;

    public CommentRequest(String messenger) {
        this.messenger = messenger;
    }

    public String getMessenger() {
        return messenger;
    }

    public void setMessenger(String messenger) {
        this.messenger = messenger;
    }
}
