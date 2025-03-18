package com.example.theukuleleband.models;

public class Message {
    private String senderName;
    private String receiverName;
    private String messageBody;
    private String messageResponse;
    private int messageId;

    public Message(String senderName, String receiverName, String messageBody, String messageResponse) {
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.messageBody = messageBody;
        this.messageResponse = messageResponse;
    }


    public int getMessageId() {
        return messageId;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public String getMessageResponse() {
        return messageResponse;
    }

    public void setMessageResponse(String messageResponse) {
        this.messageResponse = messageResponse;
    }
}
