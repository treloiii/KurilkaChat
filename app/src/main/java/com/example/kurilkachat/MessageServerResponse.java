package com.example.kurilkachat;

import com.google.gson.annotations.SerializedName;

public class MessageServerResponse {
    String id,message,time;

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }
}
