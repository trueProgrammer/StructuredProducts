package com.structuredproducts.data;

/**
 * Created by Vlad on 08.12.2015.
 */
public class Message {

    private final String message;

    public Message() {
        this("");
    }

    public Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
